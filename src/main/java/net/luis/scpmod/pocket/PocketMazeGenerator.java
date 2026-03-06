package net.luis.scpmod.pocket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.*;

import static net.luis.scpmod.block.ModBlocks.*;

public class PocketMazeGenerator {

    record OpenConn(BlockPos worldPos, Direction facing, PocketConnectorBlock.ConnectorType type) {}
    record Frontier(OpenConn conn, int tries) {}

    static class PlacedPiece {
        final PocketPrefab prefab;
        final BlockPos origin;
        final Rotation rot;
        final List<OpenConn> opens = new ArrayList<>();
        PlacedPiece(PocketPrefab p, BlockPos o, Rotation r) { this.prefab = p; this.origin = o; this.rot = r; }
    }

    private final int maxPieces;
    private final float trapChance;     // e.g. 0.20
    private final float exitChance;     // e.g. 0.15
    private final int exitAfterCount;   // e.g. 10
    final int MAX_TRIES = 3;
    private static final int DOOR_HEIGHT = 3;
    private static final double OVERLAP_EPS = -0.001;

    private static final boolean DEBUG = true;

    private void dbg(String s) {
        if (DEBUG) System.out.println("[PocketMaze][DBG] " + s);
    }

    public PocketMazeGenerator(int maxPieces, float trapChance, float exitChance, int exitAfterCount) {
        this.maxPieces = maxPieces;
        this.trapChance = trapChance;
        this.exitChance = exitChance;
        this.exitAfterCount = exitAfterCount;
    }

    public void generate(ServerLevel level, BlockPos startAt, RandomSource rand) {
        // Ensure prefabs are loaded
        PocketPrefabLibrary.ensureLoaded(level.getServer());

        PocketPrefab startPrefab = PocketPrefabLibrary.getStart();
        boolean canPlaceExit = PocketPrefabLibrary.hasExits();
        boolean canCap = PocketPrefabLibrary.hasDeadEnds();

        var placed = new ArrayList<PlacedPiece>();
        var frontier = new ArrayDeque<Frontier>();

        // Place START
        PlacedPiece start = place(level, startPrefab, startAt, Rotation.NONE);
        placed.add(start);
        for (OpenConn oc : start.opens) {
            frontier.addLast(new Frontier(oc, 0));
        }

        boolean exitPlaced = false;

        while (!frontier.isEmpty() && placed.size() < maxPieces) {
            Frontier f = rand.nextFloat() < 0.75f ? frontier.pollLast() : frontier.pollFirst();
            OpenConn from = f.conn();

            PocketPrefab candidate = chooseNextPrefab(rand, from.type(), exitPlaced, placed.size(), canPlaceExit);

            if (candidate == null) {
                if (f.tries() < MAX_TRIES) {
                    frontier.addLast(new Frontier(from, f.tries() + 1));
                } else {
                    // No candidate exists at all -> just seal (or optionally cap)
                    sealOpening(level, from);
                }
                continue;
            }

            PlacedPiece np = tryAttachAndPlace(level, rand, placed, from, candidate);
            if (np == null) {
                if (f.tries() < MAX_TRIES) {
                    frontier.addLast(new Frontier(from, f.tries() + 1));
                } else {
                    // We tried enough times; attempt to cap with a dead end, else seal.
                    if (canCap) {
                        PocketPrefab cap = PocketPrefabLibrary.pickDeadEnd(rand);
                        PlacedPiece capped = tryAttachAndPlace(level, rand, placed, from, cap);
                        if (capped != null) {
                            placed.add(capped);
                            carveConnection(level, from);
                            continue;
                        }
                    }
                    sealOpening(level, from);
                }
                continue;
            }

            placed.add(np);
            carveConnection(level, from);
            exitPlaced |= candidate.isExit;

            for (OpenConn oc : np.opens) {
                BlockPos expectedBackMarker = from.worldPos().relative(from.facing());
                boolean isBackConn =
                        oc.worldPos().equals(expectedBackMarker) &&
                                oc.facing() == from.facing().getOpposite();

                if (!isBackConn) frontier.addLast(new Frontier(oc, 0));
            }

            if (candidate.isExit) {
                PocketDimensionData data = PocketDimensionData.get(level);

                if (data.exitPos == null) {
                    BlockPos exit = findExitTriggerInPiece(level, np);
                    if (exit != null) {
                        data.exitPos = exit;
                        data.setDirty();
                        System.out.println("[SCPMod] Saved pocket exitPos=" + exit);
                    } else {
                        System.out.println("[SCPMod] WARNING: exit prefab placed, but POCKET_EXIT_BLOCK not found inside it.");
                    }
                }

                break;
            }
        }
        // Cap remaining openings with dead ends (highly recommended)
        if (canCap) {
            capRemaining(level, rand, placed, frontier);
        }

        // Failsafe: If exit never got placed, you can do:
        // - convert a capped dead end into an exit
        // - OR place an exit at the last frontier point before capping
        // For now we leave it as-is.
    }

    private void carveConnection(ServerLevel level, OpenConn from) {
        BlockPos a = from.worldPos();
        Direction dir = from.facing();
        BlockPos b = a.relative(dir);

        carveNHigh(level, a, DOOR_HEIGHT);
        carveNHigh(level, b, DOOR_HEIGHT);
    }

    private void carveNHigh(ServerLevel level, BlockPos p, int h) {
        for (int i = 0; i < h; i++) {
            level.setBlock(p.above(i), Blocks.AIR.defaultBlockState(), 3);
        }
    }

    /** Picks a prefab category based on where we're expanding from. */
    private PocketPrefab chooseNextPrefab(RandomSource rand,
                                          PocketConnectorBlock.ConnectorType fromType,
                                          boolean exitPlaced,
                                          int placedCount,
                                          boolean canPlaceExit) {

        // Exit logic
        if (canPlaceExit && !exitPlaced && placedCount > exitAfterCount && rand.nextFloat() < exitChance) {
            return PocketPrefabLibrary.pickExit(rand);
        }

        // Trap logic
        if (rand.nextFloat() < trapChance) {
            PocketPrefab trap = PocketPrefabLibrary.pickTrapOrNull(rand);
            if (trap != null) return trap;
        }

        // Flow bias: if we're expanding from ROOM connector, prefer room; else prefer hall.
        boolean preferRoom = (fromType == PocketConnectorBlock.ConnectorType.ROOM);

        // If one category is empty, fall back automatically
        if (preferRoom && PocketPrefabLibrary.hasRooms()) {
            // 60/40 room vs hall feels good
            if (rand.nextFloat() < 0.60f) return PocketPrefabLibrary.pickRoom(rand);
            if (PocketPrefabLibrary.hasHalls()) return PocketPrefabLibrary.pickHall(rand);
            return PocketPrefabLibrary.pickRoom(rand);
        } else {
            if (PocketPrefabLibrary.hasHalls()) {
                if (rand.nextFloat() < 0.75f) return PocketPrefabLibrary.pickHall(rand);
                if (PocketPrefabLibrary.hasRooms()) return PocketPrefabLibrary.pickRoom(rand);
                return PocketPrefabLibrary.pickHall(rand);
            } else if (PocketPrefabLibrary.hasRooms()) {
                return PocketPrefabLibrary.pickRoom(rand);
            }
        }

        return null;
    }

    private PocketPrefab.Connector pickBestAttach(OpenConn from, PocketPrefab candidate, Rotation rot) {
        Direction needFacing = from.facing.getOpposite();
        BlockPos size = candidate.size();

        PocketPrefab.Connector best = null;
        int bestScore = Integer.MIN_VALUE;

        for (var c : candidate.connectors) {
            if (PocketMath.rotate(c.facing(), rot) != needFacing) continue;
            if (!canConnect(from.type, c.type())) continue;

            BlockPos lp = StructureTemplate.transform(c.local(), Mirror.NONE, rot, BlockPos.ZERO);

            // Score = how far this connector is toward the needed edge.
            int score = switch (needFacing) {
                case EAST  -> lp.getX();
                case WEST  -> -lp.getX();
                case SOUTH -> lp.getZ();
                case NORTH -> -lp.getZ();
                default    -> Integer.MIN_VALUE;
            };

            if (score > bestScore) {
                bestScore = score;
                best = c;
            }
        }

        return best;
    }

    private void placeWallColumn(ServerLevel level, BlockPos base) {
        level.setBlock(base, CORRODED_LOWER.get().defaultBlockState(), 3);
        level.setBlock(base.above(), CORRODED_MIDDLE.get().defaultBlockState(), 3);
        level.setBlock(base.above(2), CORRODED_UPPER.get().defaultBlockState(), 3);
    }

    private static BlockPos offset(BlockPos p, Direction dir, int n) {
        return p.offset(dir.getStepX() * n, dir.getStepY() * n, dir.getStepZ() * n);
    }

    private void sealOpening(ServerLevel level, OpenConn from) {
        BlockPos a = from.worldPos();
        Direction out = from.facing();

        Direction side = out.getClockWise();

        for (int i = -1; i <= 1; i++) {
            BlockPos p = offset(a, side, i);
            placeWallColumn(level, p);
        }
    }

    /** Attempt to attach candidate onto 'from'. Returns placed piece or null. */
    private PlacedPiece tryAttachAndPlace(ServerLevel level,
                                          RandomSource rand,
                                          List<PlacedPiece> placed,
                                          OpenConn from,
                                          PocketPrefab candidate) {

        // Try rotations in random order for variety
        List<Rotation> rots = new ArrayList<>(List.of(Rotation.values()));
        Collections.shuffle(rots, new Random(rand.nextLong()));
        int noAttach = 0, overlapFail = 0;

        for (Rotation rot : rots) {
            // Find an attach connector on candidate matching facing + type
            var attach = pickBestAttach(from, candidate, rot);
            if (attach == null) { noAttach++; continue; }

            // the block OUTSIDE the from doorway
            BlockPos targetMarkerWorld = from.worldPos().relative(from.facing());

            // put candidate’s attach marker there
            BlockPos markerLocalRot = StructureTemplate.transform(
                    attach.local(),
                    Mirror.NONE,
                    rot,
                    BlockPos.ZERO
            );

            BlockPos origin = targetMarkerWorld.subtract(markerLocalRot);


            BlockPos expectedMarkerWorld = from.worldPos().relative(from.facing()); // targetMarkerWorld
            BlockPos placedMarkerWorld = origin.offset(markerLocalRot);            // where our math says it will land
            BlockPos delta = placedMarkerWorld.subtract(expectedMarkerWorld);

            if (!delta.equals(BlockPos.ZERO)) {
                dbg("MARKER MISMATCH delta=" + delta
                        + " fromMarker=" + from.worldPos()
                        + " fromFacing=" + from.facing()
                        + " expectedMarker=" + expectedMarkerWorld
                        + " placedMarker=" + placedMarkerWorld
                        + " rot=" + rot
                        + " cand=" + candidate.id
                        + " attachLocal=" + attach.local()
                        + " markerLocalRot=" + markerLocalRot);
            }

            Direction needFacing = from.facing().getOpposite();
            Direction attachFacingRot = PocketMath.rotate(attach.facing(), rot);

            if (attachFacingRot != needFacing) {
                dbg("FACING MISMATCH need=" + needFacing + " got=" + attachFacingRot
                        + " fromFacing=" + from.facing()
                        + " rot=" + rot
                        + " cand=" + candidate.id
                        + " attachFacing=" + attach.facing());
            }

            if (overlaps(placed, origin, candidate.template, rot)) { overlapFail++; continue; }

            return place(level, candidate, origin, rot);
        }

        dbg("ATTACH FAIL cand=" + candidate.id
                + " from=" + from.worldPos() + " facing=" + from.facing() + " type=" + from.type()
                + " noAttach=" + noAttach + " overlapFail=" + overlapFail);
        return null;
    }

    /** Connector-type compatibility rules. Tune these as your design evolves. */
    private boolean canConnect(PocketConnectorBlock.ConnectorType a, PocketConnectorBlock.ConnectorType b) {
        if (a == PocketConnectorBlock.ConnectorType.ANY || b == PocketConnectorBlock.ConnectorType.ANY) return true;
        if (a == b) return true;

        // START can connect outward to halls/rooms
        if (a == PocketConnectorBlock.ConnectorType.START &&
                (b == PocketConnectorBlock.ConnectorType.HALL || b == PocketConnectorBlock.ConnectorType.ROOM)) return true;
        if (b == PocketConnectorBlock.ConnectorType.START &&
                (a == PocketConnectorBlock.ConnectorType.HALL || a == PocketConnectorBlock.ConnectorType.ROOM)) return true;

        // DEAD_END can cap a hall/room opening
        if (a == PocketConnectorBlock.ConnectorType.DEAD_END &&
                (b == PocketConnectorBlock.ConnectorType.HALL || b == PocketConnectorBlock.ConnectorType.ROOM)) return true;
        if (b == PocketConnectorBlock.ConnectorType.DEAD_END &&
                (a == PocketConnectorBlock.ConnectorType.HALL || a == PocketConnectorBlock.ConnectorType.ROOM)) return true;

        // EXIT can attach to hall/room
        if (a == PocketConnectorBlock.ConnectorType.EXIT &&
                (b == PocketConnectorBlock.ConnectorType.HALL || b == PocketConnectorBlock.ConnectorType.ROOM)) return true;
        if (b == PocketConnectorBlock.ConnectorType.EXIT &&
                (a == PocketConnectorBlock.ConnectorType.HALL || a == PocketConnectorBlock.ConnectorType.ROOM)) return true;

        // TRAP can attach to hall/room (optional)
        if (a == PocketConnectorBlock.ConnectorType.TRAP &&
                (b == PocketConnectorBlock.ConnectorType.HALL || b == PocketConnectorBlock.ConnectorType.ROOM)) return true;
        if (b == PocketConnectorBlock.ConnectorType.TRAP &&
                (a == PocketConnectorBlock.ConnectorType.HALL || a == PocketConnectorBlock.ConnectorType.ROOM)) return true;

        if ((a == PocketConnectorBlock.ConnectorType.HALL && b == PocketConnectorBlock.ConnectorType.ROOM) ||
                (a == PocketConnectorBlock.ConnectorType.ROOM && b == PocketConnectorBlock.ConnectorType.HALL)) {
            return true;
        }

        return false;
    }

    private void capRemaining(ServerLevel level,
                              RandomSource rand,
                              List<PlacedPiece> placed,
                              ArrayDeque<Frontier> frontier) {

        int caps = 0, sealed = 0, failed = 0;

        while (!frontier.isEmpty()) {
            OpenConn from = frontier.pollFirst().conn();

            PocketPrefab cap = PocketPrefabLibrary.pickDeadEnd(rand);
            PlacedPiece capped = tryAttachAndPlace(level, rand, placed, from, cap);

            if (capped != null) {
                placed.add(capped);
                caps++;
                carveConnection(level, from); // IMPORTANT so the door is actually opened into the cap
            } else {
                // couldn't place a dead end here (overlap, no attach, etc)
                failed++;
                sealOpening(level, from); // hard stop: no void openings
                sealed++;
            }
        }

        System.out.println("[SCPMod] PocketMazeGenerator: capped " + caps + " openings, sealed " + sealed + " (failed=" + failed + ")");
    }

    private AABB computePlacedAABB(StructureTemplate template, BlockPos origin, Rotation rot) {
        StructurePlaceSettings s = new StructurePlaceSettings()
                .setRotation(rot)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(true);

        // Mappings name may be getBoundingBox / getBoundingBox(s, origin)
        var bb = template.getBoundingBox(s, origin);

        // BoundingBox uses inclusive coords; make AABB cover the full block volume cleanly
        return new AABB(
                bb.minX(), bb.minY(), bb.minZ(),
                bb.maxX() + 1, bb.maxY() + 1, bb.maxZ() + 1
        );
    }

    private boolean overlaps(List<PlacedPiece> placed, BlockPos origin, StructureTemplate template, Rotation rot) {
        AABB box = computePlacedAABB(template, origin, rot).inflate(OVERLAP_EPS);

        for (PlacedPiece pp : placed) {
            AABB other = computePlacedAABB(pp.prefab.template, pp.origin, pp.rot).inflate(OVERLAP_EPS);
            if (box.intersects(other)) return true;
        }
        return false;
    }

    private List<OpenConn> scanExpected(ServerLevel level, List<OpenConn> expected) {
        List<OpenConn> found = new ArrayList<>();
        for (OpenConn oc : expected) {
            var state = level.getBlockState(oc.worldPos());
            if (state.getBlock() instanceof PocketConnectorBlock) {
                found.add(new OpenConn(
                        oc.worldPos(),
                        state.getValue(PocketConnectorBlock.FACING),
                        state.getValue(PocketConnectorBlock.TYPE)
                ));
            }
        }
        return found;
    }

    private static Direction rotateDir(Direction dir, Rotation rot) {
        return switch (rot) {
            case NONE -> dir;
            case CLOCKWISE_90 -> dir.getClockWise();
            case COUNTERCLOCKWISE_90 -> dir.getCounterClockWise();
            case CLOCKWISE_180 -> dir.getOpposite();
        };
    }

    private PlacedPiece place(ServerLevel level, PocketPrefab prefab, BlockPos origin, Rotation rot) {
        StructurePlaceSettings s = new StructurePlaceSettings()
                .setRotation(rot)
                .addProcessor(StripPocketConnectorProcessor.INSTANCE)
                .setIgnoreEntities(true);

        prefab.template.placeInWorld(level, origin, origin, s, level.random, 2);

        PlacedPiece pp = new PlacedPiece(prefab, origin, rot);

        for (var c : prefab.connectors) {
            BlockPos local = StructureTemplate.transform(c.local(), Mirror.NONE, rot, BlockPos.ZERO);
            BlockPos markerWorld = origin.offset(local);

            var state = level.getBlockState(markerWorld);
            if (state.getBlock() instanceof PocketConnectorBlock) {
                pp.opens.add(new OpenConn(
                        markerWorld,
                        state.getValue(PocketConnectorBlock.FACING),
                        state.getValue(PocketConnectorBlock.TYPE)
                ));
            } else {
                pp.opens.add(new OpenConn(markerWorld, rotateDir(c.facing(), rot), c.type()));
            }
        }

        if (DEBUG) {
            List<OpenConn> foundAtExpected = scanExpected(level, pp.opens);

            dbg("Placed " + prefab.id + " rot=" + rot + " origin=" + origin
                    + " expected=" + pp.opens.size()
                    + " foundAtExpected=" + foundAtExpected.size());

            // Show what we expected
            dbg("Expected (computed): " + pp.opens);

            // Show what actually exists at those expected positions
            dbg("Found@Expected (world): " + foundAtExpected);

            // Optional: report missing positions (super useful)
            if (foundAtExpected.size() != pp.opens.size()) {
                Set<BlockPos> foundPos = new HashSet<>();
                for (OpenConn oc : foundAtExpected) foundPos.add(oc.worldPos());

                List<OpenConn> missing = new ArrayList<>();
                for (OpenConn oc : pp.opens) {
                    if (!foundPos.contains(oc.worldPos())) missing.add(oc);
                }
                dbg("MISSING@Expected: " + missing);
            }
        }

        return pp;
    }

    private @Nullable BlockPos findExitTriggerInPiece(ServerLevel level, PlacedPiece pp) {
        AABB box = computePlacedAABB(pp.prefab.template, pp.origin, pp.rot);

        int minX = (int) Math.floor(box.minX);
        int minY = (int) Math.floor(box.minY);
        int minZ = (int) Math.floor(box.minZ);
        int maxX = (int) Math.ceil(box.maxX) - 1;
        int maxY = (int) Math.ceil(box.maxY) - 1;
        int maxZ = (int) Math.ceil(box.maxZ) - 1;

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos p = new BlockPos(x, y, z);
                    if (level.getBlockState(p).is(POCKET_EXIT_BLOCK.get())) {
                        return p;
                    }
                }
            }
        }
        return null;
    }


}

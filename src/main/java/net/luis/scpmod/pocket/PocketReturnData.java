package net.luis.scpmod.pocket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public final class PocketReturnData {
    public static final String ROOT = "scpmod_pocket_return";

    private static final String POCKET_LAST_XZ   = "scpmod_pocket_last_exit_xz";
    private static final String POCKET_LAST_POS  = "scpmod_pocket_last_pos";
    private static final String POCKET_HINT_CD   = "scpmod_pocket_hint_cd";
    private static final String POCKET_TREND     = "scpmod_pocket_trend";
    private static final String POCKET_LAST_BAND = "scpmod_pocket_last_band";
    private static final String POCKET_LAST_VERT = "scpmod_pocket_last_vert";

    public static void setReturn(ServerPlayer p, ResourceKey<Level> dim, BlockPos pos, float yaw, float pitch) {
        CompoundTag root = p.getPersistentData();
        CompoundTag tag = new CompoundTag();

        tag.putString("dim", dim.location().toString());
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        tag.putFloat("yaw", yaw);
        tag.putFloat("pitch", pitch);

        root.put(ROOT, tag);
    }

    public static boolean hasReturn(ServerPlayer p) {
        return p.getPersistentData().contains(ROOT, Tag.TAG_COMPOUND);
    }

    public static void clear(ServerPlayer p) {
        p.getPersistentData().remove(ROOT);
    }

    public static Return read(ServerPlayer p) {
        CompoundTag tag = p.getPersistentData().getCompound(ROOT);
        ResourceLocation rl = ResourceLocation.parse(tag.getString("dim"));
        ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, rl);
        BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        return new Return(dim, pos, tag.getFloat("yaw"), tag.getFloat("pitch"));
    }

    /** Call when a player ENTERS the pocket dimension */
    public static void beginPocketSession(ServerPlayer p) {
        CompoundTag root = p.getPersistentData();
        root.remove(POCKET_LAST_XZ);
        root.remove(POCKET_HINT_CD);
        root.remove(POCKET_LAST_POS);
        root.remove(POCKET_TREND);
        root.remove(POCKET_LAST_BAND);
        root.remove(POCKET_LAST_VERT);
    }

    public static void endPocketSession(ServerPlayer p) {
        CompoundTag root = p.getPersistentData();
        root.remove(POCKET_LAST_XZ);
        root.remove(POCKET_HINT_CD);
        root.remove(POCKET_LAST_POS);
        root.remove(POCKET_TREND);
        root.remove(POCKET_LAST_BAND);
        root.remove(POCKET_LAST_VERT);
    }

    public record Return(ResourceKey<Level> dim, BlockPos pos, float yaw, float pitch) {}
}


package net.luis.scpmod.research;

import net.luis.scpmod.network.ClientboundResearchSyncPacket;
import net.luis.scpmod.network.SCPModNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class ResearchManager extends SavedData {

    private static final String SAVE_NAME = "scp_research_progress";

    private final Map<UUID, Map<ResourceLocation, ResearchStage>> perPlayer = new HashMap<>();

    public static ResearchManager get(ServerPlayer player) {
        return player.server.overworld()
                .getDataStorage().computeIfAbsent(ResearchManager::fromNbt, ResearchManager::new, SAVE_NAME);
    }

    public static ResearchManager fromNbt(CompoundTag tag) {
        ResearchManager m = new ResearchManager();
        if (tag.contains("Players")) {
            CompoundTag players = tag.getCompound("Players");
            for (String uuidStr : players.getAllKeys()) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    CompoundTag researchTag = players.getCompound(uuidStr);
                    Map<ResourceLocation, ResearchStage> map = new HashMap<>();
                    for (String key : researchTag.getAllKeys()) {
                        map.put(ResourceLocation.parse(key), ResearchStage.valueOf(researchTag.getString(key)));
                    }
                    m.perPlayer.put(uuid, map);
                } catch (Exception ignored) {}
            }
        }
        return m;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag players = new CompoundTag();
        for (var e : perPlayer.entrySet()) {
            CompoundTag research = new CompoundTag();
            for (var r : e.getValue().entrySet()) {
                research.putString(r.getKey().toString(), r.getValue().name());
            }
            players.put(e.getKey().toString(), research);
        }
        tag.put("Players", players);
        return tag;
    }

    // ---- helpers ----
    private Map<ResourceLocation, ResearchStage> mapFor(UUID uuid) {
        return perPlayer.computeIfAbsent(uuid, __ -> new HashMap<>());
    }

    public ResearchStage getStage(ServerPlayer p, ResourceLocation id) {
        return mapFor(p.getUUID()).getOrDefault(id, ResearchStage.UNDISCOVERED);
    }

    public boolean setStage(ServerPlayer p, ResourceLocation id, ResearchStage stage) {
        var map = mapFor(p.getUUID());
        var cur = map.getOrDefault(id, ResearchStage.UNDISCOVERED);
        if (cur != stage) {
            map.put(id, stage);
            this.setDirty();
            sync(p);
            return true;
        }
        return false;
    }

    public boolean advanceStage(ServerPlayer p, ResourceLocation id) {
        return setStage(p, id, getStage(p, id).next());
    }

    public void sync(ServerPlayer p) {
        Map<ResourceLocation, ResearchStage> mine = Map.copyOf(mapFor(p.getUUID()));
        SCPModNetwork.sendToPlayer(new ClientboundResearchSyncPacket(mine), p);
    }
}


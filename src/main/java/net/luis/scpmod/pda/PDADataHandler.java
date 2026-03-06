package net.luis.scpmod.pda;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PDADataHandler {
    private static final String NBT_KEY = "scpmod_unlocked_tabs";
    private static final String NBT_ANOMALIES = "scpmod_discovered_anomalies";

    private static final String KEY_SCP_SORT_MODE = "scp_sort_mode";
    private static final String NBT_MISSION = "scpmod_mission";

    private static final String NBT_BLOCK_SCANS = "scpmod_block_scans";
    private static final String NBT_UNIQUE_BLOCK_SCANS = "scpmod_unique_block_scans";

    private static final String NBT_CLEARED_ZONES = "scpmod_cleared_anomaly_zones";
    private static final String NBT_RESEARCH_POINTS = "scpmod_research_points";
    private static final String NBT_RESEARCH_UNLOCKS = "scpmod_research_unlocks";

    public static void unlockTab(Player player, String tabId) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag tabData = persistentData.getCompound(NBT_KEY);

        tabData.putBoolean(tabId, true);
        persistentData.put(NBT_KEY, tabData);
    }

    public static boolean isTabUnlocked(Player player, String tabId) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag tabData = persistentData.getCompound(NBT_KEY);
        return tabData.getBoolean(tabId);
    }

    public static Set<String> getAllUnlockedTabs(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag tabData = persistentData.getCompound(NBT_KEY);

        Set<String> unlocked = new HashSet<>();

        unlocked.add("MAIN_MENU");
        unlocked.add("MISSIONS");
        unlocked.add("CAMERA");
//        unlocked.add("FACILITY");
//        unlocked.add("RESEARCH");
        unlocked.add("SCP");

        for (String key : tabData.getAllKeys()) {
            if (tabData.getBoolean(key)) {
                unlocked.add(key);
            }
        }
        return unlocked;
    }

    public static Set<String> getDiscoveredAnomalies(Player player) {
        CompoundTag data = player.getPersistentData().getCompound(NBT_ANOMALIES);
        Set<String> ids = new HashSet<>();
        for (String key : data.getAllKeys()) {
            if (data.getBoolean(key)) ids.add(key);
        }
        return ids;
    }

    public static void markAnomalyDiscovered(Player player, String id) {
        CompoundTag persistent = player.getPersistentData();
        CompoundTag data = persistent.getCompound(NBT_ANOMALIES);
        data.putBoolean(id, true);
        persistent.put(NBT_ANOMALIES, data);
    }

    private static CompoundTag getOrCreatePdaTag(Player player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains("scpmod_pda")) {
            CompoundTag tag = new CompoundTag();
            data.put("scpmod_pda", tag);
            return tag;
        }
        return data.getCompound("scpmod_pda");
    }

    public static CompoundTag getPdaTag(Player player) {
        return player.getPersistentData().getCompound("scpmod_pda");
    }


    public static void saveSCPSortMode(Player player, String sortModeName) {
        CompoundTag tag = getOrCreatePdaTag(player);
        tag.putString(KEY_SCP_SORT_MODE, sortModeName);
    }

    public static String loadSCPSortMode(Player player) {
        CompoundTag tag = getOrCreatePdaTag(player);
        return tag.getString(KEY_SCP_SORT_MODE);
    }

    public static Mission loadMission(Player p) {
        CompoundTag tag = p.getPersistentData().getCompound(NBT_MISSION);
        if (tag.isEmpty()) {
            return new Mission("find_anomalies",
                    Component.literal("Facility Authorization Pending"),
                    Component.literal("Locate and document 3 anomalies."),
                    3);
        }
        Mission m = new Mission(
                tag.getString("id"),
                Component.literal(tag.getString("title")),
                Component.literal(tag.getString("objective")),
                tag.getInt("req")
        );
        int cur = tag.getInt("cur");
        for (int i = 0; i < cur; i++) m.incrementProgress();
        return m;
    }

    public static void saveMission(Player p, Mission m) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", m.getId());
        tag.putString("title", m.getTitle().getString());
        tag.putString("objective", m.getObjective().getString());
        tag.putInt("cur", m.getCurrentProgress());
        tag.putInt("req", m.getRequiredProgress());
        p.getPersistentData().put(NBT_MISSION, tag);
    }

    public static void markBlockScanned(Player player, String blockId) {
        CompoundTag persistent = player.getPersistentData();
        CompoundTag scans = persistent.getCompound(NBT_BLOCK_SCANS);
        scans.putBoolean(blockId, true);
        persistent.put(NBT_BLOCK_SCANS, scans);
    }

    public static Set<String> getScannedBlocks(Player player) {
        CompoundTag scans = player.getPersistentData().getCompound(NBT_BLOCK_SCANS);
        Set<String> out = new HashSet<>();
        for (String key : scans.getAllKeys()) {
            if (scans.getBoolean(key)) out.add(key);
        }
        return out;
    }

    public static java.util.List<String> getScannedBlocksList(Player player) {
        java.util.List<String> list = new java.util.ArrayList<>(getScannedBlocks(player));
        list.sort(String::compareTo);
        return list;
    }

    public static boolean hasScannedBlock(Player player, String blockId) {
        CompoundTag scans = player.getPersistentData().getCompound(NBT_UNIQUE_BLOCK_SCANS);
        return scans.getBoolean(blockId);
    }

    public static boolean markBlockScannedUnique(Player player, String blockId) {
        CompoundTag persistent = player.getPersistentData();
        CompoundTag scans = persistent.getCompound(NBT_UNIQUE_BLOCK_SCANS);

        if (scans.getBoolean(blockId)) return false; // already known
        scans.putBoolean(blockId, true);
        persistent.put(NBT_UNIQUE_BLOCK_SCANS, scans);
        return true;
    }

    public static List<String> getUniqueScannedBlocksList(Player player) {
        CompoundTag scans = player.getPersistentData().getCompound(NBT_UNIQUE_BLOCK_SCANS);
        List<String> out = new ArrayList<>();
        for (String k : scans.getAllKeys()) if (scans.getBoolean(k)) out.add(k);
        out.sort(String::compareTo);
        return out;
    }

    public static boolean isZoneCleared(Player player, int zoneId) {
        CompoundTag tag = player.getPersistentData().getCompound(NBT_CLEARED_ZONES);
        return tag.getBoolean(String.valueOf(zoneId));
    }

    public static void clearZone(Player player, int zoneId) {
        CompoundTag data = player.getPersistentData();
        CompoundTag tag = data.getCompound(NBT_CLEARED_ZONES);
        tag.putBoolean(String.valueOf(zoneId), true);
        data.put(NBT_CLEARED_ZONES, tag);
    }

    public static int getResearchPoints(Player player) {
        return player.getPersistentData().getInt(NBT_RESEARCH_POINTS);
    }

    public static void setResearchPoints(Player player, int value) {
        player.getPersistentData().putInt(NBT_RESEARCH_POINTS, Math.max(0, value));
    }

    public static void addResearchPoints(Player player, int amount) {
        if (amount <= 0) return;
        int cur = getResearchPoints(player);
        setResearchPoints(player, cur + amount);
    }

    public static boolean isResearchUnlockBought(Player player, String id) {
        CompoundTag tag = player.getPersistentData().getCompound(NBT_RESEARCH_UNLOCKS);
        return tag.getBoolean(id);
    }

    public static void setResearchUnlockBought(Player player, String id, boolean value) {
        CompoundTag data = player.getPersistentData();
        CompoundTag tag = data.getCompound(NBT_RESEARCH_UNLOCKS);
        tag.putBoolean(id, value);
        data.put(NBT_RESEARCH_UNLOCKS, tag);
    }

    public static boolean trySpendResearch(Player player, int cost) {
        int cur = getResearchPoints(player);
        if (cur < cost) return false;
        setResearchPoints(player, cur - cost);
        return true;
    }

    public static Set<String> getBoughtResearchUnlocks(Player player) {
        CompoundTag tag = player.getPersistentData().getCompound(NBT_RESEARCH_UNLOCKS);
        Set<String> out = new HashSet<>();
        for (String k : tag.getAllKeys()) {
            if (tag.getBoolean(k)) out.add(k);
        }
        return out;
    }

}


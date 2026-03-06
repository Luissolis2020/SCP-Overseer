package net.luis.scpmod.pda;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class MissionManager {

    private static Mission currentMission;
    private static boolean missionJustCompleted = false;
    private static String lastCompletedMissionTitle = "";
    private static String lastCompletedMissionId = "";

    private static final Set<String> discoveredAnomalies = new HashSet<>();
    private static Player missionOwner;

    static {
        currentMission = new Mission(
                "find_anomalies",
                Component.literal("Facility Authorization Pending"),
                Component.literal("Locate and document 3 anomalies."),
                3
        );
    }

    public static Mission getCurrentMission() {
        return currentMission;
    }

    public static void init(Player player) {
        missionOwner = player;
        if (currentMission != null && currentMission.getId().equals("find_anomalies")) {
            discoveredAnomalies.clear();
            discoveredAnomalies.addAll(PDADataHandler.getDiscoveredAnomalies(player));
        }
    }

    public static void applyFromNetwork(Mission m) {
        currentMission = m;
//        missionJustCompleted = false;
//        lastCompletedMissionTitle = "";
//        lastCompletedMissionId = "";
    }

    public static void markCompletedFromServer(String id, Component title) {
        missionJustCompleted = true;
        lastCompletedMissionId = id;
        lastCompletedMissionTitle = title.getString();
    }

    public static boolean isMissionJustCompleted() {
        return missionJustCompleted;
    }

    public static String getLastCompletedMissionTitle() {
        return lastCompletedMissionTitle;
    }

    public static void acknowledgeMissionComplete() {
        missionJustCompleted = false;
        lastCompletedMissionTitle = "";
        lastCompletedMissionId = "";
    }

    public static String getLastCompletedMissionId() {
        return lastCompletedMissionId;
    }

}

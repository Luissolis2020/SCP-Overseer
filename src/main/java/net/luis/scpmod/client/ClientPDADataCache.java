package net.luis.scpmod.client;

import net.luis.scpmod.network.ClientboundFacilityRoomsPacket;
import net.luis.scpmod.network.ClientboundResearchCatalogPacket;
import net.minecraft.core.BlockPos;

import java.util.*;

public class ClientPDADataCache {

    private static final Set<String> unlockedTabs = new HashSet<>();
    private static String scpSortMode = "NUMBER";
    private static List<ClientboundFacilityRoomsPacket.FacilityRoomData> rooms = new ArrayList<>();
    private static final List<String> scannedBlocks = new ArrayList<>();
    private static int researchPoints = 0;
    private static final Set<String> boughtResearchUnlocks = new HashSet<>();
    private static final Map<String, ClientboundResearchCatalogPacket.Entry> researchCatalog = new HashMap<>();


    public static void setUnlockedTabs(Set<String> tabs) {
        unlockedTabs.clear();
        unlockedTabs.addAll(tabs);
    }

    public static boolean isTabUnlocked(String tab) {
        return unlockedTabs.contains(tab);
    }

    public static Set<String> getUnlockedTabs() {
        return Set.copyOf(unlockedTabs);
    }

    public static void setSCPSortMode(String mode) {
        scpSortMode = mode;
    }

    public static String getSCPSortMode() {
        return scpSortMode;
    }

    public static void setFacilityRooms(List<ClientboundFacilityRoomsPacket.FacilityRoomData> r) { rooms = r; }
    public static List<ClientboundFacilityRoomsPacket.FacilityRoomData> getFacilityRooms() { return rooms; }

    public static ClientboundFacilityRoomsPacket.FacilityRoomData getRoomAt(BlockPos origin) {
        for (ClientboundFacilityRoomsPacket.FacilityRoomData room : rooms) {
            if (room.origin().equals(origin)) {
                return room;
            }
        }
        return null;
    }

    public static void setScannedBlocks(List<String> ids) {
        scannedBlocks.clear();
        scannedBlocks.addAll(ids);
    }

    public static List<String> getScannedBlocks() {
        return List.copyOf(scannedBlocks);
    }

    public static void setResearchPoints(int value) {
        researchPoints = value;
    }

    public static int getResearchPoints() {
        return researchPoints;
    }

    public static void setBoughtResearchUnlocks(Set<String> ids) {
        boughtResearchUnlocks.clear();
        boughtResearchUnlocks.addAll(ids);
    }

    public static boolean isResearchUnlockBought(String id) {
        return boughtResearchUnlocks.contains(id);
    }


    public static void setResearchCatalog(List<ClientboundResearchCatalogPacket.Entry> entries) {
        researchCatalog.clear();
        for (var e : entries) {
            researchCatalog.put(e.id(), e);
        }
    }

    public static ClientboundResearchCatalogPacket.Entry getResearchEntry(String id) {
        return researchCatalog.get(id);
    }

    public static java.util.Collection<ClientboundResearchCatalogPacket.Entry> getResearchCatalog() {
        return java.util.List.copyOf(researchCatalog.values());
    }

    public static int getResearchCost(String id) {
        var e = researchCatalog.get(id);
        return e == null ? -1 : e.cost();
    }

    public static boolean hasResearchEntry(String id) {
        return researchCatalog.containsKey(id);
    }

    public static String getResearchNameOrId(String id) {
        var e = getResearchEntry(id);
        return e == null ? id : e.name();
    }

    public static String getMissingPrereqId(ClientboundResearchCatalogPacket.Entry entry) {
        for (String req : entry.requires()) {
            if (!isResearchUnlockBought(req)) return req;
        }
        return null;
    }

}

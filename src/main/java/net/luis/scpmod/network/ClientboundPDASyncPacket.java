package net.luis.scpmod.network;

import net.luis.scpmod.client.ClientPDADataCache;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ClientboundPDASyncPacket {

    public static record SCPStatusData(
            String state,
            String dimension,
            boolean hasLastKnownPos,
            long lastKnownPos,
            boolean hasContainedRoomOrigin,
            long containedRoomOrigin,
            long updatedGameTime
    ) {}

    private final Set<String> unlockedTabs;
    private final String scpSortMode;
    private final int researchPoints;
    private final Set<String> boughtUnlocks;
    private final Map<String, SCPStatusData> scpStatuses;

    public ClientboundPDASyncPacket(Set<String> unlockedTabs, String scpSortMode, int researchPoints,
                                    Set<String> boughtUnlocks, Map<String, SCPStatusData> scpStatuses) {
        this.unlockedTabs = unlockedTabs;
        this.scpSortMode = scpSortMode;
        this.researchPoints = researchPoints;
        this.boughtUnlocks = boughtUnlocks;
        this.scpStatuses = scpStatuses;
    }

    public ClientboundPDASyncPacket(FriendlyByteBuf buf) {
        int tabCount = buf.readVarInt();
        this.unlockedTabs = new HashSet<>();
        for (int i = 0; i < tabCount; i++) unlockedTabs.add(buf.readUtf());

        this.scpSortMode = buf.readUtf();
        this.researchPoints = buf.readVarInt();

        int boughtCount = buf.readVarInt();
        this.boughtUnlocks = new HashSet<>();
        for (int i = 0; i < boughtCount; i++) {
            boughtUnlocks.add(buf.readUtf(64));
        }

        int statusCount = buf.readVarInt();
        this.scpStatuses = new HashMap<>();
        for (int i = 0; i < statusCount; i++) {
            String scpId = buf.readUtf(64);
            String state = buf.readUtf(32);
            String dimension = buf.readUtf(128);
            boolean hasLastKnown = buf.readBoolean();
            long lastKnown = hasLastKnown ? buf.readLong() : BlockPos.ZERO.asLong();
            boolean hasContainedRoom = buf.readBoolean();
            long containedRoom = hasContainedRoom ? buf.readLong() : BlockPos.ZERO.asLong();
            long updatedGameTime = buf.readLong();

            scpStatuses.put(scpId, new SCPStatusData(
                    state,
                    dimension,
                    hasLastKnown,
                    lastKnown,
                    hasContainedRoom,
                    containedRoom,
                    updatedGameTime
            ));
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(unlockedTabs.size());
        for (String tab : unlockedTabs) buf.writeUtf(tab);

        buf.writeUtf(scpSortMode);
        buf.writeVarInt(researchPoints);

        buf.writeVarInt(boughtUnlocks.size());
        for (String id : boughtUnlocks) buf.writeUtf(id, 64);

        buf.writeVarInt(scpStatuses.size());
        for (Map.Entry<String, SCPStatusData> entry : scpStatuses.entrySet()) {
            SCPStatusData status = entry.getValue();
            buf.writeUtf(entry.getKey(), 64);
            buf.writeUtf(status.state(), 32);
            buf.writeUtf(status.dimension(), 128);
            buf.writeBoolean(status.hasLastKnownPos());
            if (status.hasLastKnownPos()) {
                buf.writeLong(status.lastKnownPos());
            }
            buf.writeBoolean(status.hasContainedRoomOrigin());
            if (status.hasContainedRoomOrigin()) {
                buf.writeLong(status.containedRoomOrigin());
            }
            buf.writeLong(status.updatedGameTime());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void handle(ClientboundPDASyncPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPDADataCache.setUnlockedTabs(pkt.unlockedTabs);
            ClientPDADataCache.setSCPSortMode(pkt.scpSortMode);
            ClientPDADataCache.setResearchPoints(pkt.researchPoints);
            ClientPDADataCache.setBoughtResearchUnlocks(pkt.boughtUnlocks);
            ClientPDADataCache.setSCPStatuses(pkt.scpStatuses);
        });
        ctx.get().setPacketHandled(true);
    }
}

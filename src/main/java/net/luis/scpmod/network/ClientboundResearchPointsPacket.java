package net.luis.scpmod.network;

import net.luis.scpmod.client.ClientPDADataCache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundResearchPointsPacket {

    private final int researchPoints;

    public ClientboundResearchPointsPacket(int researchPoints) {
        this.researchPoints = researchPoints;
    }

    public ClientboundResearchPointsPacket(FriendlyByteBuf buf) {
        this.researchPoints = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(researchPoints);
    }

    @OnlyIn(Dist.CLIENT)
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPDADataCache.setResearchPoints(researchPoints);
        });
        ctx.get().setPacketHandled(true);
    }
}

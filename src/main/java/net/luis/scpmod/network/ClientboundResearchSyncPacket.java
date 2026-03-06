package net.luis.scpmod.network;

import net.luis.scpmod.research.ClientResearchData;
import net.luis.scpmod.research.ResearchStage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ClientboundResearchSyncPacket {
    private final Map<ResourceLocation, ResearchStage> stages;

    public ClientboundResearchSyncPacket(Map<ResourceLocation, ResearchStage> stages) {
        this.stages = stages;
    }

    public ClientboundResearchSyncPacket(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        this.stages = new HashMap<>();
        for (int i = 0; i < size; i++) {
            ResourceLocation id = buf.readResourceLocation();
            ResearchStage stage = buf.readEnum(ResearchStage.class);
            stages.put(id, stage);
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(stages.size());
        for (Map.Entry<ResourceLocation, ResearchStage> entry : stages.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            buf.writeEnum(entry.getValue());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientResearchData.setAllStages(stages);
        });
        ctx.get().setPacketHandled(true);
    }
}


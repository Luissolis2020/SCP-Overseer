package net.luis.scpmod.network;

import net.luis.scpmod.client.ClientPreviewState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ActivateFacilityPreviewPacket {
    private final BlockPos pos;
    private final ResourceLocation structureId;

    public ActivateFacilityPreviewPacket(BlockPos pos, ResourceLocation structureId) {
        this.pos = pos;
        this.structureId = structureId;
    }

    public ActivateFacilityPreviewPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.structureId = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeResourceLocation(structureId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handle(this)));
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static final class ClientHandler {
        private static void handle(ActivateFacilityPreviewPacket packet) {
            var mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.level == null || mc.getSingleplayerServer() == null) return;

            var template = mc.getSingleplayerServer().getStructureManager().getOrCreate(packet.structureId);
            StructurePlaceSettings settings = new StructurePlaceSettings();
            ClientPreviewState.activate(packet.pos, template, settings, packet.structureId);
        }
    }
}

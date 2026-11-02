package net.luis.scpmod.network;

import net.luis.scpmod.client.ClientPreviewState;
import net.luis.scpmod.client.BuildPreviewState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ActivateFacilityPreviewPacket {
    private final BlockPos pos;
    private final ResourceLocation structureId;
    private final Rotation rotation;
    private final boolean socketLocked;

    public ActivateFacilityPreviewPacket(BlockPos pos, ResourceLocation structureId, Rotation rotation, boolean socketLocked) {
        this.pos = pos;
        this.structureId = structureId;
        this.rotation = rotation;
        this.socketLocked = socketLocked;
    }

    public ActivateFacilityPreviewPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.structureId = buf.readResourceLocation();
        this.rotation = buf.readEnum(Rotation.class);
        this.socketLocked = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeResourceLocation(structureId);
        buf.writeEnum(rotation);
        buf.writeBoolean(socketLocked);
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
            StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(packet.rotation);
            ClientPreviewState.activate(packet.pos, template, settings, packet.structureId, packet.socketLocked);
            ClientPreviewState.setPreviewState(BuildPreviewState.PLACEMENT_LOCKED);
        }
    }
}

package net.luis.scpmod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestPreviewOverlapPacket {
    private final BlockPos origin;
    private final ResourceLocation structureId;
    private final Rotation rotation;
    private final Mirror mirror;

    public RequestPreviewOverlapPacket(BlockPos origin, ResourceLocation structureId, Rotation rotation, Mirror mirror) {
        this.origin = origin;
        this.structureId = structureId;
        this.rotation = rotation;
        this.mirror = mirror;
    }

    public RequestPreviewOverlapPacket(FriendlyByteBuf buf) {
        this.origin = buf.readBlockPos();
        this.structureId = buf.readResourceLocation();
        this.rotation = buf.readEnum(Rotation.class);
        this.mirror = buf.readEnum(Mirror.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(origin);
        buf.writeResourceLocation(structureId);
        buf.writeEnum(rotation);
        buf.writeEnum(mirror);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            StructureTemplate template = player.server.getStructureManager().getOrCreate(structureId);

            StructurePlaceSettings settings = new StructurePlaceSettings()
                    .setRotation(rotation)
                    .setMirror(mirror);

            // compute overlaps server-side and respond
            SCPModNetwork.sendToPlayer(
                    ClientboundPreviewOverlapPacket.fromServer(level, origin, template, settings),
                    player
            );
        });
        ctx.get().setPacketHandled(true);
    }
}
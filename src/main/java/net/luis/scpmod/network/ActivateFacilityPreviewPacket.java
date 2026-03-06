package net.luis.scpmod.network;

import net.luis.scpmod.client.ClientPreviewState;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
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
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null || mc.getSingleplayerServer() == null) return;

            StructureTemplate template = mc.getSingleplayerServer()
                    .getStructureManager()
                    .getOrCreate(structureId);

            StructurePlaceSettings settings = new StructurePlaceSettings();
            ClientPreviewState.activate(pos, template, settings, structureId);
        }));
        ctx.get().setPacketHandled(true);
    }
}

package net.luis.scpmod.network;

import net.luis.scpmod.client.HighlightRenderData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RoomHighlightPacket {

    private final BlockPos origin;

    public RoomHighlightPacket(BlockPos origin) {
        this.origin = origin;
    }

    public RoomHighlightPacket(FriendlyByteBuf buf) {
        this.origin = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(origin);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            // Set the highlight origin and duration (e.g. 100 ticks = 5 seconds)
            HighlightRenderData.setHighlight(origin, 100);
        });
        context.get().setPacketHandled(true);
    }
}
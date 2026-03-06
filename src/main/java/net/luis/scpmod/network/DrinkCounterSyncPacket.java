package net.luis.scpmod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class DrinkCounterSyncPacket {
    private final int drinkCount;

    public DrinkCounterSyncPacket(int drinkCount) {
        this.drinkCount = drinkCount;
    }

    public static void encode(DrinkCounterSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.drinkCount);
    }

    public static DrinkCounterSyncPacket decode(FriendlyByteBuf buf) {
        return new DrinkCounterSyncPacket(buf.readInt());
    }

    public static void handle(DrinkCounterSyncPacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }
}

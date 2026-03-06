package net.luis.scpmod.network;

import net.luis.scpmod.pda.PDADataHandler;
import net.luis.scpmod.research.ResearchProjects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BuyResearchUnlockPacket {
    private final String unlockId;

    public BuyResearchUnlockPacket(String unlockId) {
        this.unlockId = unlockId;
    }

    public BuyResearchUnlockPacket(FriendlyByteBuf buf) {
        this.unlockId = buf.readUtf(64);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(unlockId, 64);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ResearchProjects.Project proj = ResearchProjects.get(unlockId);
            if (proj == null) return;

            if (PDADataHandler.isResearchUnlockBought(player, unlockId)) {
                player.displayClientMessage(Component.literal("Already purchased."), true);
                return;
            }

            for (String req : proj.requires()) {
                if (!PDADataHandler.isResearchUnlockBought(player, req)) {
                    player.displayClientMessage(Component.literal("Missing prerequisite: " + req), true);
                    SCPModNetwork.syncPDAStateTo(player, false);
                    return;
                }
            }

            int cost = proj.cost();
            if (cost > 0 && !PDADataHandler.trySpendResearch(player, cost)) {
                player.displayClientMessage(Component.literal("Insufficient Research Points."), true);
                SCPModNetwork.syncPDAStateTo(player, false); // keeps RP + bought list consistent
                return;
            }

            PDADataHandler.setResearchUnlockBought(player, unlockId, true);
            proj.effect().apply(player);

            SCPModNetwork.syncPDAStateTo(player, true);
        });
        ctx.get().setPacketHandled(true);
    }
}

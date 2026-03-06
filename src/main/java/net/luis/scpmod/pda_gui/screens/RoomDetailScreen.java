package net.luis.scpmod.pda_gui.screens;

import net.luis.scpmod.network.ClientboundFacilityRoomsPacket.FacilityRoomData;
import net.luis.scpmod.network.RoomHighlightRequestPacket;
import net.luis.scpmod.network.SCPModNetwork;
import net.luis.scpmod.network.TeleportToRoomPacket;
import net.luis.scpmod.network.ToggleRoomActivePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RoomDetailScreen extends Screen {

    private final FacilityRoomData room;
    private Button toggleActiveButton;

    public RoomDetailScreen(FacilityRoomData room) {
        super(Component.literal("Room Details"));
        this.room = room;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int baseY = this.height / 2 + 40;
        int offsetY = 0;

        // Highlight Room
        this.addRenderableWidget(Button.builder(Component.literal("Highlight Room"), (btn) -> {
            SCPModNetwork.sendToServer(new RoomHighlightRequestPacket(room.origin()));
        }).pos(centerX - 100, baseY + offsetY).size(200, 20).build());
        offsetY += 25;

        // Creative-only buttons
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative()) {
            this.addRenderableWidget(Button.builder(Component.literal("Teleport to Room"), (btn) -> {
                SCPModNetwork.sendToServer(new TeleportToRoomPacket(room.origin(), room.width(), room.depth()));
            }).pos(centerX - 100, baseY + offsetY).size(200, 20).build());
            offsetY += 25;

            String state = room.state().toUpperCase();
            boolean built = state.equals("BUILT") || state.equals("ACTIVE");

            String toggleLabel = (built && room.isActive()) ? "Deactivate Room" : "Activate Room";


            toggleActiveButton = Button.builder(Component.literal(toggleLabel), (btn) -> {
                SCPModNetwork.sendToServer(new ToggleRoomActivePacket(room.origin()));
            }).pos(centerX - 100, baseY + offsetY).size(200, 20).build();

            this.addRenderableWidget(toggleActiveButton);
            offsetY += 25;
        }

        // Back button (always added at bottom)
        this.addRenderableWidget(Button.builder(Component.literal("Back"), (btn) -> {
            Minecraft.getInstance().setScreen(new PDAMenuScreen("FACILITY"));
        }).pos(centerX - 100, baseY + offsetY).size(200, 20).build());

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        int startX = this.width / 2 - 100;
        int startY = this.height / 2 - 80;

        guiGraphics.drawCenteredString(this.font, "Room Detail", this.width / 2, startY, 0xFFFFFF);
        startY += 15;

        guiGraphics.drawString(this.font, "Name: " + room.displayName(), startX, startY, 0xCCCCCC);
        guiGraphics.drawString(this.font, "State: " + room.state(), startX, startY + 15, 0xCCCCCC);
        guiGraphics.drawString(this.font, "Origin: " + room.origin().toShortString(), startX, startY + 30, 0xCCCCCC);
        guiGraphics.drawString(this.font, "Size: " + room.width() + " x " + room.height() + " x " + room.depth(), startX, startY + 45, 0xCCCCCC);
        guiGraphics.drawString(this.font, "Blueprint ID: " + room.blueprintId(), startX, startY + 60, 0xAAAAAA);

        String state = room.state().toUpperCase();
        if ((state.equals("PLANNED") || state.equals("UNDER_CONSTRUCTION")) && room.progressPercent() > 0) {
            guiGraphics.drawString(this.font, "Progress: " + room.progressPercent() + "%", startX, startY + 75, 0x88FF88);

            int barX = startX;
            int barY = startY + 90;
            int barWidth = 200;
            int filled = (int) (barWidth * (room.progressPercent() / 100.0f));
            guiGraphics.fill(barX, barY, barX + barWidth, barY + 10, 0xFF444444); // background
            guiGraphics.fill(barX, barY, barX + filled, barY + 10, 0xFF22FF22);   // foreground
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}

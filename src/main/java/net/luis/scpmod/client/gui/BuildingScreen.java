package net.luis.scpmod.client.gui;

import net.luis.scpmod.client.BuildPreviewState;
import net.luis.scpmod.client.ClientPreviewState;
import net.luis.scpmod.network.ConfirmFacilityPlacementPacket;
import net.luis.scpmod.network.SCPModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class BuildingScreen extends Screen {

    private final List<Button> buttons = new ArrayList<>();
    private Button confirmButton;

    public BuildingScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        buttons.clear();
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int buttonSize = 20;
        int gap = 4;

        // Arrow buttons
        buttons.add(Button.builder(Component.literal("⬆"), b -> nudge(0, 0, -1)).pos(centerX - buttonSize / 2, centerY - buttonSize - gap).size(buttonSize, buttonSize).build());
        buttons.add(Button.builder(Component.literal("⬇"), b -> nudge(0, 0, 1)).pos(centerX - buttonSize / 2, centerY + buttonSize + gap).size(buttonSize, buttonSize).build());
        buttons.add(Button.builder(Component.literal("⬅"), b -> nudge(-1, 0, 0)).pos(centerX - buttonSize - gap, centerY).size(buttonSize, buttonSize).build());
        buttons.add(Button.builder(Component.literal("➡"), b -> nudge(1, 0, 0)).pos(centerX + gap, centerY).size(buttonSize, buttonSize).build());

        // Y axis
        buttons.add(Button.builder(Component.literal("Up"), b -> nudge(0, 1, 0)).pos(centerX + 40, centerY - buttonSize - gap).size(40, buttonSize).build());
        buttons.add(Button.builder(Component.literal("Down"), b -> nudge(0, -1, 0)).pos(centerX + 40, centerY + buttonSize + gap).size(40, buttonSize).build());

        // Rotate
        buttons.add(Button.builder(Component.literal("Rotate"), b -> ClientPreviewState.rotateClockwise()).pos(centerX - 50, centerY + 50 + 2 * gap).size(100, 20).build());

        // Confirm
        confirmButton = Button.builder(Component.literal("Confirm"), b -> {
            SCPModNetwork.sendToServer(new ConfirmFacilityPlacementPacket(
                    ClientPreviewState.getPreviewPos(),
                    ClientPreviewState.getStructureId(),
                    ClientPreviewState.getSettings().getRotation()
            ));
            ClientPreviewState.deactivate();
            Minecraft.getInstance().setScreen(null);
        }).pos(centerX - 105, centerY + 80 + 2 * gap).size(100, 20).build();

        buttons.add(confirmButton);

        // Cancel
        buttons.add(Button.builder(Component.literal("Cancel"), b -> {
            ClientPreviewState.deactivate();
            Minecraft.getInstance().setScreen(null);
        }).pos(centerX + 5, centerY + 80 + 2 * gap).size(100, 20).build());

        for (Button button : buttons) {
            this.addRenderableWidget(button);
        }
    }

    private void nudge(int dx, int dy, int dz) {
        ClientPreviewState.movePreview(dx, dy, dz);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        super.onClose();

        ClientPreviewState.setPreviewState(BuildPreviewState.FREE_VIEW);

        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(null);

        mc.execute(() -> {
            if (mc.screen == null && mc.isWindowActive()) {
                mc.mouseHandler.grabMouse();
            }
        });
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Transparent overlay — could render hints here if needed
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        if (confirmButton != null) {
            confirmButton.active = !ClientPreviewState.isServerSaysOverlapping();
        }
    }

}
package net.luis.scpmod.pda_gui.screens;

import net.luis.scpmod.client.ScreenshotTextureLoader;
import net.luis.scpmod.research.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SCPProfileScreen extends Screen {

    private final ResourceLocation scpId;
    private ResearchEntry entry;
    private ResearchStage stage;

    private Button backButton;

    public SCPProfileScreen(ResourceLocation scpId) {
        super(Component.literal("SCP Profile"));
        this.scpId = scpId;
    }

    @Override
    protected void init() {
        entry = ModResearchEntries.get(scpId);
        stage = ClientResearchData.getStage(scpId);

        int buttonWidth = 100;
        int buttonHeight = 20;
        int x = (this.width - buttonWidth) / 2;
        int y = this.height - 40;

        backButton = Button.builder(Component.literal("Back"), (btn) -> {
            Minecraft.getInstance().setScreen(new PDAMenuScreen("SCP"));
        }).pos(x, y).size(buttonWidth, buttonHeight).build();

        this.addRenderableWidget(backButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);

        Minecraft mc = Minecraft.getInstance();
        int centerX = width / 2;
        int y = 40;

        guiGraphics.drawCenteredString(mc.font, entry.getDisplayName(stage), centerX, y, 0x00FF00);
        y += 20;

        guiGraphics.drawCenteredString(mc.font, "Stage: " + stage.name(), centerX, y, 0xAAAAAA);
        y += 20;

        // Centered description
        String desc = entry.getDescription(stage);
        guiGraphics.drawCenteredString(mc.font, desc, centerX, y, 0xFFFFFF);
        y += 40;

        Classification classification = entry.getClassification();
        String classText = "Classification: " + classification.getDisplayName();
        int color = classification.getColor();

        guiGraphics.drawCenteredString(mc.font, classText, centerX, y, color);
        y += 20;

        guiGraphics.drawCenteredString(mc.font, "Type: " + entry.getType().name(), centerX, y, 0xAAAAAA);
        y += 20;

        // Centered screenshot
        ResourceLocation tex = ScreenshotTextureLoader.getTexture(scpId);
        if (tex != null) {
            guiGraphics.blit(tex, centerX - 32, y, 0, 0, 64, 64, 64, 64);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
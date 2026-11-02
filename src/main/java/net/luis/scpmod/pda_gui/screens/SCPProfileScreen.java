package net.luis.scpmod.pda_gui.screens;

import net.luis.scpmod.client.ClientPDADataCache;
import net.luis.scpmod.client.ScreenshotTextureLoader;
import net.luis.scpmod.network.ClientboundPDASyncPacket;
import net.luis.scpmod.research.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FormattedCharSequence;
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
    private int scrollOffset = 0;
    private int maxScroll = 0;

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
        int contentTop = 36;
        int contentBottom = this.height - 52; // keep clear of back button
        int y = 40 - scrollOffset;

        y = drawCenteredLine(guiGraphics, mc, entry.getDisplayName(stage), centerX, y, 0x00FF00, contentTop, contentBottom);
        y += 20;

        y = drawCenteredLine(guiGraphics, mc, "Stage: " + stage.name(), centerX, y, 0xAAAAAA, contentTop, contentBottom);
        y += 20;

        String desc = entry.getDescription(stage);
        for (FormattedCharSequence line : mc.font.split(Component.literal(desc), Math.min(420, this.width - 40))) {
            if (y >= contentTop - 10 && y <= contentBottom + 10) {
                guiGraphics.drawCenteredString(mc.font, line, centerX, y, 0xFFFFFF);
            }
            y += 12;
        }
        y += 16;

        Classification classification = entry.getClassification();
        String classText = "Classification: " + classification.getDisplayName();
        int color = classification.getColor();

        y = drawCenteredLine(guiGraphics, mc, classText, centerX, y, color, contentTop, contentBottom);
        y += 20;

        y = drawCenteredLine(guiGraphics, mc, "Type: " + entry.getType().name(), centerX, y, 0xAAAAAA, contentTop, contentBottom);
        y += 20;

        ClientboundPDASyncPacket.SCPStatusData status = ClientPDADataCache.getSCPStatus(scpId);
        if (status != null) {
            y = drawCenteredLine(guiGraphics, mc, "World Status: " + prettyState(status.state()), centerX, y, stateColor(status.state()), contentTop, contentBottom);
            y += 12;

            if (!status.dimension().isEmpty()) {
                y = drawCenteredLine(guiGraphics, mc, "Dimension: " + status.dimension(), centerX, y, 0xAAAAAA, contentTop, contentBottom);
                y += 12;
            }

            if (status.hasContainedRoomOrigin()) {
                y = drawCenteredLine(guiGraphics, mc, "Contained Room: " + formatPos(status.containedRoomOrigin()), centerX, y, 0x88FF88, contentTop, contentBottom);
                y += 12;
            }

            if (status.hasLastKnownPos()) {
                y = drawCenteredLine(guiGraphics, mc, "Last Known Position: " + formatPos(status.lastKnownPos()), centerX, y, 0xAAAAAA, contentTop, contentBottom);
                y += 12;
            }
        } else {
            y = drawCenteredLine(guiGraphics, mc, "World Status: No record", centerX, y, 0xAAAAAA, contentTop, contentBottom);
            y += 12;
        }

        ResourceLocation tex = ScreenshotTextureLoader.getTexture(scpId);
        if (tex != null) {
            if (y <= contentBottom && y + 64 >= contentTop) {
                guiGraphics.blit(tex, centerX - 32, y, 0, 0, 64, 64, 64, 64);
            }
            y += 72;
        }

        maxScroll = Math.max(0, y - contentBottom);
        if (maxScroll > 0) {
            if (scrollOffset > maxScroll) {
                scrollOffset = maxScroll;
            }
            renderScrollbar(guiGraphics, contentTop, contentBottom);
            guiGraphics.drawCenteredString(mc.font, "Mouse wheel to scroll", centerX, contentBottom + 4, 0x777777);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static String prettyState(String raw) {
        return switch (raw) {
            case "NOT_SPAWNED" -> "Not Spawned";
            case "WILD" -> "Wild";
            case "CONTAINED" -> "Contained";
            case "MISSING" -> "Missing";
            case "NEUTRALIZED" -> "Neutralized";
            default -> raw;
        };
    }

    private static int stateColor(String raw) {
        return switch (raw) {
            case "CONTAINED" -> 0x55FF55;
            case "MISSING" -> 0xFF5555;
            case "WILD" -> 0xFFAA00;
            case "NEUTRALIZED" -> 0xAAAAAA;
            default -> 0xFFFFFF;
        };
    }

    private static String formatPos(long packed) {
        BlockPos pos = BlockPos.of(packed);
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    private int drawCenteredLine(GuiGraphics guiGraphics, Minecraft mc, String text, int centerX, int y, int color, int contentTop, int contentBottom) {
        if (y >= contentTop - 10 && y <= contentBottom + 10) {
            guiGraphics.drawCenteredString(mc.font, text, centerX, y, color);
        }
        return y;
    }

    private void renderScrollbar(GuiGraphics guiGraphics, int top, int bottom) {
        int trackX = this.width - 14;
        int trackW = 4;
        int trackH = Math.max(1, bottom - top);

        guiGraphics.fill(trackX, top, trackX + trackW, bottom, 0x55333333);

        int thumbH = Math.max(14, (int) (trackH * (trackH / (float) (trackH + maxScroll))));
        int thumbRange = Math.max(0, trackH - thumbH);
        int thumbY = top + (maxScroll <= 0 ? 0 : (int) ((scrollOffset / (float) maxScroll) * thumbRange));

        guiGraphics.fill(trackX, thumbY, trackX + trackW, thumbY + thumbH, 0xFFAAAAAA);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (maxScroll <= 0) {
            return super.mouseScrolled(mouseX, mouseY, delta);
        }
        int step = 16;
        scrollOffset -= (int) Math.signum(delta) * step;
        if (scrollOffset < 0) scrollOffset = 0;
        if (scrollOffset > maxScroll) scrollOffset = maxScroll;
        return true;
    }
}

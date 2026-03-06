package net.luis.scpmod.pda_gui.tabs;

import net.luis.scpmod.pda.MissionManager;
import net.luis.scpmod.pda.Mission;
import net.luis.scpmod.pda.PDADataHandler;
import net.luis.scpmod.pda_gui.screens.PDAMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;

import java.util.List;

public class MissionsTab implements PDATab {

    private final PDAMenuScreen parent;

    public MissionsTab(PDAMenuScreen parent) {
        this.parent = parent;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        int x = screenWidth / 2 - 100;
        int y = 80;

        Mission mission = MissionManager.getCurrentMission();

        if (mission == null) {
            guiGraphics.drawString(Minecraft.getInstance().font, "No active mission.", x, y, 0xAAAAAA);
            return;
        }

        guiGraphics.drawString(Minecraft.getInstance().font, "Current Mission:", x, y, 0xFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, mission.getTitle(), x, y + 15, 0xAAAAFF);

        guiGraphics.drawString(Minecraft.getInstance().font, "Objective:", x, y + 40, 0xFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, mission.getObjective(), x, y + 55, 0xAAAAFF);

        guiGraphics.drawString(Minecraft.getInstance().font, "Progress: " + mission.getCurrentProgress() + "/" + mission.getRequiredProgress(), x, y + 80, 0xFFFFFF);

        guiGraphics.drawString(Minecraft.getInstance().font, "Status: " + (mission.isComplete() ? "Complete!" : "In Progress..."), x, y + 105, 0xFFFFFF);

        if (MissionManager.isMissionJustCompleted()) {
            parent.showPopup("Mission Complete!\n" + MissionManager.getLastCompletedMissionTitle(), MissionManager::acknowledgeMissionComplete);
        }

    }

    @Override
    public List<Button> getButtons() {
        return List.of();
    }
}

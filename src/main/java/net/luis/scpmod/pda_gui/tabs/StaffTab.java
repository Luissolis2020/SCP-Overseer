package net.luis.scpmod.pda_gui.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;

import java.util.List;

public class StaffTab implements PDATab {

    @Override
    public void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        int x = screenWidth / 2 - 100;
        int y = 80;

        guiGraphics.drawString(Minecraft.getInstance().font, "Staff Tab Content Here...", x, y, 0xAAAAAA);
    }

    @Override
    public List<Button> getButtons() {
        return List.of();
    }
}

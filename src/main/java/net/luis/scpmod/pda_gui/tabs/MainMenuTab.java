package net.luis.scpmod.pda_gui.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import java.util.List;

public class MainMenuTab implements PDATab {

    @Override
    public void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        int x = screenWidth / 2 - 100;
        int y = 100;

        guiGraphics.drawString(Minecraft.getInstance().font, "Welcome to the Foundation PDA.", x, y, 0xFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, "Select a section above to begin.", x, y + 20, 0xAAAAAA);
    }

    @Override
    public List<net.minecraft.client.gui.components.Button> getButtons() {
        return List.of(); // No buttons yet
    }
}

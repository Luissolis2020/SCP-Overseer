package net.luis.scpmod.pda_gui.tabs;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;

import java.util.List;


public interface PDATab {

    void render(GuiGraphics g, int screenWidth, int screenHeight, int mouseX, int mouseY);

    List<Button> getButtons();

    default boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }
}

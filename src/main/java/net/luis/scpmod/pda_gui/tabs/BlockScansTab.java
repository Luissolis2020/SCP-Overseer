package net.luis.scpmod.pda_gui.tabs;

import net.luis.scpmod.client.ClientPDADataCache;
import net.luis.scpmod.pda_gui.screens.PDAMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BlockScansTab implements PDATab {

    private final PDAMenuScreen parent;
    private final List<Button> buttons = new ArrayList<>();
    private ResourceLocation selected = null;

    private int scrollOffset = 0;

    public BlockScansTab(PDAMenuScreen parent) {
        this.parent = parent;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        int x = screenWidth / 2 - 140;
        int y = 70;

        guiGraphics.drawString(Minecraft.getInstance().font, "Block Scans", x, y, 0xFFFFFF);
        y += 18;

        if (selected != null) {
            guiGraphics.drawString(Minecraft.getInstance().font,
                    "Selected: " + selected.toString(),
                    x, y, 0xAAAAFF);
        } else {
            guiGraphics.drawString(Minecraft.getInstance().font,
                    "Selected: (none)",
                    x, y, 0xAAAAAA);
        }

        y += 20;

        // draw list
        List<String> raw = ClientPDADataCache.getScannedBlocks();
        List<ResourceLocation> scans = new ArrayList<>(raw.size());
        for (String s : raw) {
            try { scans.add(ResourceLocation.parse(s)); }
            catch (Exception ignored) {}
        }

        if (scans.isEmpty()) {
            guiGraphics.drawString(Minecraft.getInstance().font, "No scans yet.", x, y, 0xAAAAAA);
            return;
        }

        int maxVisible = 10;
        int start = Math.max(0, Math.min(scrollOffset, Math.max(0, scans.size() - maxVisible)));
        int end = Math.min(scans.size(), start + maxVisible);

        int lineY = y;
        for (int i = start; i < end; i++) {
            ResourceLocation id = scans.get(i);
            int color = (id.equals(selected)) ? 0x00FF00 : 0xFFFFFF;
            guiGraphics.drawString(Minecraft.getInstance().font, "- " + id.toString(), x, lineY, color);
            lineY += 12;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int dir = (delta > 0) ? -1 : 1;
        scrollOffset = Math.max(0, scrollOffset + dir);
        return true;
    }

    @Override
    public List<Button> getButtons() {
        // no buttons needed for POC (you can add scroll buttons later)
        return buttons;
    }
}

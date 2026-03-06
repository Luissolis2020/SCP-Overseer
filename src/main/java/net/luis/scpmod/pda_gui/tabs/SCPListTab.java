package net.luis.scpmod.pda_gui.tabs;

import net.luis.scpmod.pda.PDADataHandler;
import net.luis.scpmod.pda_gui.screens.PDAMenuScreen;
import net.luis.scpmod.pda_gui.screens.SCPProfileScreen;
import net.luis.scpmod.research.ClientResearchData;
import net.luis.scpmod.research.ModResearchEntries;
import net.luis.scpmod.research.ResearchEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SCPListTab implements PDATab {

    private final PDAMenuScreen parent;
    private final Minecraft mc = Minecraft.getInstance();
    private int scrollOffset = 0;
    private static final int ENTRY_HEIGHT = 20;
    private static final int MAX_SCROLL = 2000;

    private SortMode currentSort = SortMode.NUMBER;
    private Button sortButton;

    public SCPListTab(PDAMenuScreen parent) {
        this.parent = parent;

        String saved = PDADataHandler.loadSCPSortMode(Minecraft.getInstance().player);
        try {
            currentSort = SortMode.valueOf(saved);
        } catch (IllegalArgumentException ignored) {
            currentSort = SortMode.NUMBER;
        }

    }

    public enum SortMode {
        NUMBER,
        TYPE,
        CLASSIFICATION
    }

    @Override
    public void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        String title = "SCP Archives";
        int titleWidth = Minecraft.getInstance().font.width(title);
        guiGraphics.drawString(Minecraft.getInstance().font, title, (screenWidth - titleWidth) / 2, 30, 0xFFFFFF);
    }

    @Override
    public List<Button> getButtons() {
        if (parent.isPopupActive()) {
            return List.of();
        }

        List<Button> buttons = new ArrayList<>();
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();

        int columns = 3;
        int buttonWidth = 100;
        int buttonHeight = ENTRY_HEIGHT - 4;
        int spacing = 10;

        int xStart = (screenWidth - (columns * buttonWidth + (columns - 1) * spacing)) / 2;
        int yBase = 60;

        List<ResearchEntry> entries = new ArrayList<>(ModResearchEntries.getAllEntries());
        switch (currentSort) {
            case NUMBER -> {
                entries.sort(Comparator.comparing((ResearchEntry entry) -> {
                    String path = entry.getId().getPath(); // e.g. "scp_427_1"
                    List<Integer> parts = new ArrayList<>();
                    for (String part : path.replace("scp_", "").split("_")) {
                        try {
                            parts.add(Integer.parseInt(part));
                        } catch (NumberFormatException ignored) {
                            parts.add(Integer.MAX_VALUE);
                        }
                    }
                    while (parts.size() < 2) parts.add(0);
                    return parts;
                }, Comparator.<List<Integer>>comparingInt(list -> list.get(0))
                        .thenComparingInt(list -> list.get(1))));
            }

            case TYPE -> {
                entries.sort(Comparator.comparing(ResearchEntry::getType));
            }

            case CLASSIFICATION -> {
                entries.sort(Comparator.comparing(entry -> entry.getClassification().ordinal()));
            }
        }

        for (int i = 0; i < entries.size(); i++) {
            ResearchEntry entry = entries.get(i);
            boolean discovered = ClientResearchData.isDiscovered(entry.getId());
            String label = discovered ? entry.getDisplayName(ClientResearchData.getStage(entry.getId())) : "???";

            int row = i / columns;
            int col = i % columns;

            int x = xStart + col * (buttonWidth + spacing);
            int y = yBase + row * ENTRY_HEIGHT - scrollOffset;

            Button button = Button.builder(Component.literal(label), (btn) -> {
                if (discovered) {
                    Minecraft.getInstance().setScreen(new SCPProfileScreen(entry.getId()));
                } else {
                    parent.showPopup("Access Denied", () -> {});
                }
            }).pos(x, y).size(buttonWidth, buttonHeight).build();

            buttons.add(button);
        }

        return buttons;
    }

    public SortMode getCurrentSort() {
        return currentSort;
    }

    public void cycleSortMode() {
        currentSort = switch (currentSort) {
            case NUMBER -> SortMode.TYPE;
            case TYPE -> SortMode.CLASSIFICATION;
            case CLASSIFICATION -> SortMode.NUMBER;
        };

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            PDADataHandler.saveSCPSortMode(player, currentSort.name());
        }
    }



    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int scrollAmount = (int) (delta * 15);
        scrollOffset -= scrollAmount;
        scrollOffset = Math.max(0, Math.min(scrollOffset, MAX_SCROLL));
        return true;
    }
}

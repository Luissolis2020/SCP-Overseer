package net.luis.scpmod.pda_gui.tabs;

import net.luis.scpmod.client.ClientPDADataCache;
import net.luis.scpmod.network.BuyResearchUnlockPacket;
import net.luis.scpmod.network.ClientboundResearchCatalogPacket;
import net.luis.scpmod.network.SCPModNetwork;
import net.luis.scpmod.pda_gui.screens.PDAMenuScreen;
import net.luis.scpmod.research.ResearchCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ResearchTab implements PDATab {

    private final PDAMenuScreen parent;
    private final List<Button> buttons = new ArrayList<>();
    private ResearchCategory selectedCategory = null;

    private final Map<Button, String> projectButtonIds = new HashMap<>();
    private record ProjectState(boolean bought, boolean prereqsMet, boolean canAfford) {}

    private String hoveredProjectId = null;
    private String selectedProjectId = null;

    public ResearchTab(PDAMenuScreen parent) {
        this.parent = parent;
    }

    @Override
    public void render(GuiGraphics g, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        var font = Minecraft.getInstance().font;

        int x = screenWidth / 2 - 140;
        int y = 70;

        g.drawString(font, "Research", x, y, 0xFFFFFF);
        y += 16;

        if (selectedCategory == null) {
            g.drawString(font, "Choose a division to expand:", x, y, 0xAAAAAA);
            return;
        }

        g.drawString(font, selectedCategory.title, x, y, 0x00FF00);
        y += 14;
        g.drawString(font, "Purchase upgrades using Research Points.", x, y, 0xAAAAAA);

        // Hover tooltip
        hoveredProjectId = null;
        for (var e : projectButtonIds.entrySet()) {
            Button btn = e.getKey();
            if (!btn.visible) continue;

            if (mouseX >= btn.getX() && mouseX < btn.getX() + btn.getWidth()
                    && mouseY >= btn.getY() && mouseY < btn.getY() + btn.getHeight()) {
                hoveredProjectId = e.getValue();
                break;
            }
        }

        if (hoveredProjectId != null) {
            var entry = ClientPDADataCache.getResearchEntry(hoveredProjectId);
            if (entry != null) {
                drawProjectTooltip(g, entry, mouseX, mouseY, screenWidth, screenHeight);
            }
        }
    }

    @Override
    public List<Button> getButtons() {
        buttons.clear();
        hoveredProjectId = null;
        projectButtonIds.clear();

        int sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int xLeft = sw / 2 - 140;

        // ===== Category hub =====
        if (selectedCategory == null) {
            int y = 110;

            buttons.add(categoryButton(ResearchCategory.FACILITY, xLeft, y));
            buttons.add(categoryButton(ResearchCategory.PERSONNEL, xLeft + 145, y));

            y += 55;
            buttons.add(categoryButton(ResearchCategory.CONTAINMENT, xLeft, y));
            buttons.add(categoryButton(ResearchCategory.EQUIPMENT, xLeft + 145, y));

            return buttons;
        }

        // ===== Category view =====
        int y = 110;

        // Back button
        buttons.add(Button.builder(Component.literal("< Back"), b -> {
                    selectedCategory = null;
                    selectedProjectId = null;
                    parent.refreshCurrentTab();
                })
                .pos(xLeft, y)
                .size(70, 18)
                .build());

        y += 25;

        // Sort projects: available first, then locked, then purchased last.
        List<ClientboundResearchCatalogPacket.Entry> list = ClientPDADataCache.getResearchCatalog().stream()
                .filter(e -> e.category() == selectedCategory)
                .sorted(Comparator
                        .comparingInt((ClientboundResearchCatalogPacket.Entry e) -> sortBucket(e))
                        .thenComparingInt(ClientboundResearchCatalogPacket.Entry::cost)
                        .thenComparing(ClientboundResearchCatalogPacket.Entry::name))
                .toList();

        for (var e : list) {
            boolean bought = ClientPDADataCache.isResearchUnlockBought(e.id());
            boolean prereqsMet = hasAllPrereqs(e);

            int cost = e.cost();
            int rp = ClientPDADataCache.getResearchPoints();
            boolean canAfford = (cost <= 0) || (rp >= cost);

            String label;
            if (bought) label = e.name() + " (Purchased)";
            else if (!prereqsMet) label = e.name() + " (Locked)";
            else if (!canAfford) label = e.name() + " (Need " + cost + " RP)";
            else label = e.name() + " (" + cost + " RP)";

            Button btn = Button.builder(Component.literal(label), b -> {
                        selectedProjectId = e.id();

                        // Only attempt purchase if it is actually purchasable.
                        var now = ClientPDADataCache.getResearchEntry(e.id());
                        if (now == null) return;

                        if (ClientPDADataCache.isResearchUnlockBought(now.id())) return;
                        if (!hasAllPrereqs(now)) return;

                        int nowCost = now.cost();
                        int nowRp = ClientPDADataCache.getResearchPoints();
                        if (nowCost > 0 && nowRp < nowCost) return;

                        SCPModNetwork.sendToServer(new BuyResearchUnlockPacket(now.id()));
                    })
                    .pos(xLeft, y)
                    .size(280, 20)
                    .build();

            // UX: disable button when not purchasable (still shows tooltip on hover)
            btn.active = !bought && prereqsMet && canAfford;

            buttons.add(btn);
            projectButtonIds.put(btn, e.id());
            y += 23;
        }

        return buttons;
    }

    private int sortBucket(ClientboundResearchCatalogPacket.Entry e) {
        boolean bought = ClientPDADataCache.isResearchUnlockBought(e.id());
        if (bought) return 2;

        boolean prereqsMet = hasAllPrereqs(e);
        if (!prereqsMet) return 1;

        int cost = e.cost();
        int rp = ClientPDADataCache.getResearchPoints();
        boolean canAfford = (cost <= 0) || (rp >= cost);

        // purchasable first, then can't afford (still prereqs met)
        return canAfford ? 0 : 1;
    }

    private Button categoryButton(ResearchCategory cat, int x, int y) {
        return Button.builder(Component.literal(cat.title), b -> {
                    selectedCategory = cat;
                    selectedProjectId = null;
                    parent.refreshCurrentTab();
                })
                .pos(x, y)
                .size(140, 45)
                .build();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

    private void drawProjectTooltip(GuiGraphics g, ClientboundResearchCatalogPacket.Entry proj,
                                    int mouseX, int mouseY, int sw, int sh) {
        var font = Minecraft.getInstance().font;

        int pad = 7;
        int margin = 8;
        int maxWidth = 210;
        int maxDescLines = 4;

        boolean bought = ClientPDADataCache.isResearchUnlockBought(proj.id());
        int cost = proj.cost();
        int rp = ClientPDADataCache.getResearchPoints();

        boolean prereqsMet = hasAllPrereqs(proj);
        boolean canAfford = (cost <= 0) || (rp >= cost);

        // Build structured info lines
        List<Component> infoLines = new ArrayList<>();
        List<Integer> infoColors = new ArrayList<>();

        if (bought) {
            infoLines.add(Component.literal("Status: Purchased"));
            infoColors.add(0x00FF00);
        } else if (!prereqsMet) {
            infoLines.add(Component.literal("Status: Locked"));
            infoColors.add(0xFF5555);
            infoLines.add(Component.literal("Cost: " + cost + " RP"));
            infoColors.add(0xAAAAAA);
        } else if (!canAfford) {
            infoLines.add(Component.literal("Need " + cost + " RP (Have " + rp + ")"));
            infoColors.add(0xFFAA00); // amber
        } else {
            infoLines.add(Component.literal("Cost: " + cost + " RP"));
            infoColors.add(0xAAAAAA);
        }

        // Wrap title + desc
        var titleLines = font.split(Component.literal(proj.name()), maxWidth);
        var descLines = font.split(Component.literal(proj.desc()), maxWidth);
        boolean descTrimmed = descLines.size() > maxDescLines;
        if (descTrimmed) descLines = descLines.subList(0, maxDescLines);

        // Prereq lines (friendly, colored)
        List<Component> reqLines = new ArrayList<>();
        List<Integer> reqColors = new ArrayList<>();
        if (!proj.requires().isEmpty()) {
            reqLines.add(Component.literal("Requires:"));
            reqColors.add(0xFFFFFF);

            for (String reqId : proj.requires()) {
                String nm = ClientPDADataCache.getResearchNameOrId(reqId);
                boolean have = ClientPDADataCache.isResearchUnlockBought(reqId);

                reqLines.add(Component.literal("- " + nm));
                reqColors.add(have ? 0x55FF55 : 0xFF7777);
            }
        }

        // Measure width
        int measured = 0;
        for (var line : titleLines) measured = Math.max(measured, font.width(line));
        for (var line : infoLines) measured = Math.max(measured, font.width(line));
        for (var line : descLines) measured = Math.max(measured, font.width(line));
        for (var line : reqLines) measured = Math.max(measured, font.width(line));
        if (descTrimmed) measured = Math.max(measured, font.width(Component.literal("...")));

        int textW = Math.min(maxWidth, measured);
        int lineH = font.lineHeight;

        int dividerTopPad = 3;
        int dividerBottomPad = 3;

        int contentH =
                titleLines.size() * lineH +
                        dividerTopPad + 1 + dividerBottomPad +
                        (infoLines.size() * lineH) +
                        2 +
                        (descLines.size() * lineH) +
                        (descTrimmed ? lineH : 0) +
                        (reqLines.isEmpty() ? 0 : (3 + reqLines.size() * lineH));

        int boxW = textW + pad * 2;
        int boxH = contentH + pad * 2;

        // Position near mouse
        int x = mouseX + 14;
        int y = mouseY + 10;

        // Clamp to screen
        if (x + boxW + margin > sw) x = sw - boxW - margin;
        if (y + boxH + margin > sh) y = sh - boxH - margin;
        if (x < margin) x = margin;
        if (y < margin) y = margin;

        int left = x;
        int top = y;
        int right = x + boxW;
        int bottom = y + boxH;

        // Border + background
        g.fill(left - 1, top - 1, right + 1, bottom + 1, 0xFF1A1A1A);
        g.fill(left, top, right, bottom, 0xDD000000);
        g.fill(left, top, right, top + 1, 0x33FFFFFF);

        int tx = left + pad;
        int ty = top + pad;

        // Title
        for (var line : titleLines) {
            g.drawString(font, line, tx, ty, 0xFFFFFF);
            ty += lineH;
        }

        // Divider
        ty += dividerTopPad;
        g.fill(tx, ty, tx + textW, ty + 1, 0x33FFFFFF);
        ty += 1 + dividerBottomPad;

        // Info lines (status/cost/locked)
        for (int i = 0; i < infoLines.size(); i++) {
            g.drawString(font, infoLines.get(i), tx, ty, infoColors.get(i));
            ty += lineH;
        }
        ty += 2;

        // Description
        for (var line : descLines) {
            g.drawString(font, line, tx, ty, 0xAAAAAA);
            ty += lineH;
        }
        if (descTrimmed) {
            g.drawString(font, Component.literal("..."), tx, ty, 0xAAAAAA);
            ty += lineH;
        }

        // Prereqs
        if (!reqLines.isEmpty()) {
            ty += 3;
            for (int i = 0; i < reqLines.size(); i++) {
                g.drawString(font, reqLines.get(i), tx, ty, reqColors.get(i));
                ty += lineH;
            }
        }
    }

    private boolean hasAllPrereqs(ClientboundResearchCatalogPacket.Entry e) {
        for (String req : e.requires()) {
            if (!ClientPDADataCache.isResearchUnlockBought(req)) return false;
        }
        return true;
    }

    private ProjectState state(ClientboundResearchCatalogPacket.Entry e) {
        boolean bought = ClientPDADataCache.isResearchUnlockBought(e.id());
        boolean prereqsMet = hasAllPrereqs(e);
        boolean canAfford = e.cost() <= 0 || ClientPDADataCache.getResearchPoints() >= e.cost();
        return new ProjectState(bought, prereqsMet, canAfford);
    }

}

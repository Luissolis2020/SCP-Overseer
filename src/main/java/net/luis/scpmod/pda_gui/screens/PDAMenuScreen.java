package net.luis.scpmod.pda_gui.screens;

import net.luis.scpmod.client.ClientPDADataCache;
import net.luis.scpmod.pda.MissionManager;
import net.luis.scpmod.pda.PDADataHandler;
import net.luis.scpmod.pda_gui.tabs.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class PDAMenuScreen extends Screen {

    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("scpmod", "textures/gui/pda_background.png");

    private final List<Button> tabButtons = new ArrayList<>();
    private final Map<String, PDATab> tabs = new HashMap<>();
    private final List<Button> currentTabButtons = new ArrayList<>();
    private String currentTab = "MAIN_MENU";
    private boolean tabSwitchBlockedByPopup = false;
    private String pendingTab = null;

    private boolean showingPopup = false;
    private String popupMessage = "";
    private Runnable popupAction = null;
    private Button popupAcknowledgeButton = null;

    public PDAMenuScreen() {
        this("MAIN_MENU");
    }

    public PDAMenuScreen(String startTab) {
        super(Component.literal("PDA Menu"));
        this.currentTab = startTab;

        tabs.put("MAIN_MENU", new MainMenuTab());
        tabs.put("MISSIONS", new MissionsTab(this));
        tabs.put("CAMERA", new CameraTab());
        tabs.put("FACILITY", new FacilityTab());
        tabs.put("STAFF", new StaffTab());
        tabs.put("RESEARCH", new ResearchTab(this));
        tabs.put("SCP", new SCPListTab(this));
        tabs.put("BLOCKS", new BlockScansTab(this));

        if (Minecraft.getInstance().player != null) {
            MissionManager.init(Minecraft.getInstance().player);
        }
    }

    @Override
    protected void init() {
        int buttonWidth = 65;
        int buttonHeight = 20;
        int gap = 5;
        int buttonCount = 7;
        int totalWidth = buttonCount * buttonWidth + (buttonCount - 1) * gap;
        int startX = (this.width - totalWidth) / 2;
        int y = 10;

        String[] labels = {"Missions", "Camera", "Facility", "Staff", "Research", "SCPs", "Blocks"};
        String[] tabIds  = {"MISSIONS", "CAMERA", "FACILITY", "STAFF", "RESEARCH", "SCP",  "BLOCKS"};

        for (int i = 0; i < buttonCount; i++) {
            final String tab = tabIds[i];
            Button button = Button.builder(Component.literal(labels[i]), b -> switchTab(tab))
                    .pos(startX + i * (buttonWidth + gap), y)
                    .size(buttonWidth, buttonHeight)
                    .build();
            tabButtons.add(addRenderableWidget(button));
        }

        switchTab(currentTab);
    }


    private void switchTab(String tab) {
        // Remove existing buttons
        this.renderables.removeIf(w -> w instanceof Button && !tabButtons.contains(w));
        this.children().removeIf(w -> w instanceof Button && !tabButtons.contains(w));
        currentTabButtons.clear();

        // Close popup if active
        if (this.popupAcknowledgeButton != null) {
            this.renderables.remove(this.popupAcknowledgeButton);
            this.children().remove(this.popupAcknowledgeButton);
            this.popupAcknowledgeButton = null;
            this.showingPopup = false;
        }

        // Access denied logic
        if (!"MAIN_MENU".equals(tab) && !ClientPDADataCache.isTabUnlocked(tab)) {
            this.tabSwitchBlockedByPopup = true;
            this.pendingTab = tab;
            this.showPopup("Access Denied\nThis section is currently locked.", this::handleDeniedTabReturn);
            return;
        }

        this.tabSwitchBlockedByPopup = false;
        this.currentTab = tab;

        PDATab activeTab = tabs.get(tab);
        if (activeTab != null) {
            for (Button button : activeTab.getButtons()) {
                addRenderableWidget(button);
                currentTabButtons.add(button);
            }

        }

    }

    public boolean isPopupActive() {
        return showingPopup;
    }

    private void handleDeniedTabReturn() {
        this.tabSwitchBlockedByPopup = false;

        if (this.pendingTab != null) {
            switchTab(this.currentTab);
            this.pendingTab = null;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        int rp = ClientPDADataCache.getResearchPoints();
        guiGraphics.drawString(this.font, "Research: " + rp, 10, 35, 0x00FF00);

        if (showingPopup) {
            renderPopup(guiGraphics);
        } else {
            PDATab activeTab = tabs.get(currentTab);
            if (activeTab != null) {
                activeTab.render(guiGraphics, this.width, this.height, mouseX, mouseY);
            }
            if (Objects.equals(currentTab, "MAIN_MENU")){
                guiGraphics.drawCenteredString(this.font, Component.literal("PDA Menu"), this.width / 2, 40, 0xFFFFFF);
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void showPopup(String message, Runnable onAcknowledge) {
        // Clear tab buttons so they don’t render behind popup
        for (Button b : currentTabButtons) {
            this.renderables.remove(b);
            this.children().remove(b);
        }
        currentTabButtons.clear();

        this.showingPopup = true;
        this.popupMessage = message;
        this.popupAction = onAcknowledge;

        int buttonWidth = 100;
        int buttonHeight = 20;
        int x = (this.width - buttonWidth) / 2;
        int y = (this.height / 2) + 20;

        this.popupAcknowledgeButton = Button.builder(Component.literal("Acknowledge"), (b) -> {
            if (this.popupAction != null) {
                this.popupAction.run();
            }
            this.closePopup();
        }).pos(x, y).size(buttonWidth, buttonHeight).build();

        this.addRenderableWidget(this.popupAcknowledgeButton);
    }


    public void closePopup() {
        this.showingPopup = false;
        this.popupMessage = "";
        this.popupAction = null;

        if (this.popupAcknowledgeButton != null) {
            this.renderables.remove(this.popupAcknowledgeButton);
            this.children().remove(this.popupAcknowledgeButton);
            this.popupAcknowledgeButton = null;
        }

        if (!this.tabSwitchBlockedByPopup) {
            switchTab(this.currentTab);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!showingPopup && MissionManager.isMissionJustCompleted()) {
            showPopup(
                    "Mission Complete!\n" + MissionManager.getLastCompletedMissionTitle(),
                    MissionManager::acknowledgeMissionComplete
            );
        }
    }

    @Override
    public void resize(Minecraft mc, int width, int height) {
        super.resize(mc, width, height);
        switchTab(this.currentTab);
    }

    private void renderPopup(GuiGraphics guiGraphics) {
        int popupWidth = 300; // Bigger
        int popupHeight = 150; // Bigger
        int x = (this.width - popupWidth) / 2;
        int y = (this.height - popupHeight) / 2;

        guiGraphics.fill(x, y, x + popupWidth, y + popupHeight, 0xAA000000); // semi-transparent black

        String[] lines = popupMessage.split("\n");
        if (lines.length >= 1) {
            guiGraphics.drawCenteredString(this.font, lines[0], this.width / 2, y + 40, 0x00FF00);
        }
        if (lines.length >= 2) {
            guiGraphics.drawCenteredString(this.font, lines[1], this.width / 2, y + 60, 0xAAAAAA);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        PDATab tab = tabs.get(currentTab);
        if (tab != null && tab.mouseScrolled(mouseX, mouseY, delta)) {
            refreshCurrentTab();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        PDATab tab = tabs.get(currentTab);
        if (tab != null) {
            double delta = 0.0;

            if (keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_W) {
                delta = 1.0;
            } else if (keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_S) {
                delta = -1.0;
            } else if (keyCode == GLFW.GLFW_KEY_PAGE_UP) {
                delta = 4.0;
            } else if (keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
                delta = -4.0;
            }

            if (delta != 0.0 && tab.mouseScrolled(0, 0, delta)) {
                refreshCurrentTab();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void refreshCurrentTab() {
        switchTab(this.currentTab);
    }
}

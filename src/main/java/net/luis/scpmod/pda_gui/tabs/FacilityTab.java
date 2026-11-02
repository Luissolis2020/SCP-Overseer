package net.luis.scpmod.pda_gui.tabs;

import net.luis.scpmod.client.BuildPreviewState;
import net.luis.scpmod.client.ClientPreviewState;
import net.luis.scpmod.facility.building.FacilityBlueprint;
import net.luis.scpmod.facility.building.FacilityBlueprintLoader;
import net.luis.scpmod.client.ClientPDADataCache;
import net.luis.scpmod.network.ClientboundFacilityRoomsPacket;
import net.luis.scpmod.pda_gui.screens.RoomDetailScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.ArrayList;
import java.util.List;

public class FacilityTab implements PDATab {

    private final List<Button> buttons = new ArrayList<>();
    private int scrollOffset = 0;
    private int maxScroll = 0;

    private static final int START_Y = 50;
    private static final int ROW_SPACING = 25;

    private void updateButtons() {
        buttons.clear();

        int startX = 0;
        int width = 200;
        int spacing = 25;
        int buildY = 50;

        // Section: Construct New Room
        buttons.add(Button.builder(Component.literal("--- Build New Room ---"), b -> {}).pos(startX, buildY).size(width, 20).build());
        buildY += spacing;

        for (FacilityBlueprint blueprint : FacilityBlueprintLoader.BLUEPRINTS.values()) {
            Button buildButton = Button.builder(Component.literal(blueprint.displayName), (b) -> {
                startFacilityPreview(blueprint.id);
            }).pos(startX, buildY).size(width, 20).build();

            buttons.add(buildButton);
            buildY += spacing;
        }

        // Section: Existing Rooms
        int viewY = buildY + 15;
        buttons.add(Button.builder(Component.literal("--- Existing Rooms ---"), b -> {}).pos(startX, viewY).size(width, 20).build());
        viewY += spacing;

        for (ClientboundFacilityRoomsPacket.FacilityRoomData room : ClientPDADataCache.getFacilityRooms()) {
            Button roomButton = Button.builder(
                    Component.literal(room.displayName() + " [" + room.state() + "]"),
                    (b) -> {
                        Minecraft.getInstance().setScreen(new RoomDetailScreen(room));
                    }).pos(startX, viewY).size(width, 20).build();

            buttons.add(roomButton);
            viewY += spacing;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        int startX = screenWidth / 2 - 100;

        int contentHeight = START_Y + buttons.size() * ROW_SPACING;
        int viewportHeight = Math.max(1, screenHeight - 46);
        maxScroll = Math.max(0, contentHeight - viewportHeight);
        if (scrollOffset > maxScroll) {
            scrollOffset = maxScroll;
        }

        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            int y = START_Y + i * ROW_SPACING - scrollOffset;
            button.setPosition(startX, y);
            boolean visible = y >= 46 && y + button.getHeight() <= screenHeight - 6;
            button.visible = visible;
            button.active = visible;
        }
    }

    @Override
    public List<Button> getButtons() {
        updateButtons();
        return buttons;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (maxScroll <= 0) return false;

        int old = scrollOffset;
        int step = 18;
        scrollOffset -= (int) Math.signum(delta) * step;
        if (scrollOffset < 0) scrollOffset = 0;
        if (scrollOffset > maxScroll) scrollOffset = maxScroll;

        return old != scrollOffset;
    }

    private void startFacilityPreview(ResourceLocation facilityId) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        BlockPos pos = mc.player.blockPosition().above(1);

        if (mc.getSingleplayerServer() != null) {
            ClientPreviewState.activateDummy(pos, facilityId);  // renders ghost blocks
        } else {
            ClientPreviewState.activate(pos, null, new StructurePlaceSettings(), facilityId); // bbox-only preview
        }

        mc.player.closeContainer();
        ClientPreviewState.setPreviewState(BuildPreviewState.PLACEMENT_LOCKED);
    }

}



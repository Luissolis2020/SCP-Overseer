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

    private List<Button> buttons = new ArrayList<>();

    public FacilityTab() {
        updateButtons();
    }

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
        updateButtons(); // <- dynamically refresh buttons
        int startX = screenWidth / 2 - 100;

        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            button.setPosition(startX, 50 + i * 25);
        }
    }

    @Override
    public List<Button> getButtons() {
        return buttons;
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



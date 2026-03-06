package net.luis.scpmod.pda_gui.tabs;

import net.luis.scpmod.client.CameraScreenshotScheduler;
import net.luis.scpmod.entity.AnomalyEntity;
import net.luis.scpmod.network.SCPModNetwork;
import net.luis.scpmod.network.TakePhotoPacket;
import net.luis.scpmod.pda.MissionManager;
import net.luis.scpmod.research.BlockResearchRegistry;
import net.luis.scpmod.research.ItemResearchRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;
import java.util.*;

public class CameraTab implements PDATab {

    private final List<Button> buttons = new ArrayList<>();
    private final Button cameraButton;

    private final Set<ResourceLocation> photographed = new HashSet<>();

    public class CameraTabState {
        public static String lastStatus = "Ready";
        public static String lastShot = "No photo taken.";
    }

    public CameraTab() {
        cameraButton = Button.builder(Component.literal("Take Photo"), (button) -> {
            beginPhotoSequence();
        }).pos(0, 0).size(120, 20).build();

        buttons.add(cameraButton);
    }

    private void beginPhotoSequence() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 reach = eye.add(look.scale(20.0));

        ResourceLocation id = null;
        String displayName = null;

        EntityHitResult entityHit = getEntityLookedAt(player, eye, reach, 20.0);
        if (entityHit != null) {
            Entity entity = entityHit.getEntity();

            if (entity instanceof AnomalyEntity anomaly) {
                id = anomaly.getResearchId();
                displayName = anomaly.getAnomalyDisplayName();
            }

            else if (entity instanceof ItemEntity itemEntity) {
                var itemStack = itemEntity.getItem();
                var item = itemStack.getItem();

                if (ItemResearchRegistry.isResearchable(item)) {
                    id = ItemResearchRegistry.getResearchId(item);
                    displayName = id.getPath().replace('_', '-').toUpperCase();
                }
            }
        }

        if (id == null) {
            BlockHitResult blockHit = mc.level.clip(new ClipContext(
                    eye, reach,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.NONE,
                    player
            ));

            if (blockHit.getType() == HitResult.Type.BLOCK) {
                var pos = blockHit.getBlockPos();
                var block = mc.level.getBlockState(pos).getBlock();

                if (BlockResearchRegistry.isResearchable(block)) {
                    id = BlockResearchRegistry.getResearchId(block);
                    displayName = id.getPath().replace('_', '-').toUpperCase();
                }
            }
        }

        if (id != null) {
            if (!photographed.contains(id)) {
                photographed.add(id);
                SCPModNetwork.sendToServer(new TakePhotoPacket(id));
                CameraTabState.lastStatus = "Captured: " + displayName;
                CameraTabState.lastShot = "Logged to Research tab.";

                CameraScreenshotScheduler.schedule(id, mc.options.hideGui);
                mc.options.hideGui = true;
                mc.setScreen(null);
            } else {
                CameraTabState.lastStatus = "Already Photographed";
                CameraTabState.lastShot = displayName + " is already in the database.";
            }
        } else {
            CameraTabState.lastStatus = "No SCP Found";
            CameraTabState.lastShot = "No anomaly, item, or SCP block detected.";
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        int x = screenWidth / 2 - 100;
        int y = 80;

        guiGraphics.drawString(Minecraft.getInstance().font, "Research Camera", x, y, 0xFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, "Status: " + CameraTabState.lastStatus, x, y + 20, 0xAAAAAA);
        guiGraphics.drawString(Minecraft.getInstance().font, "Last Photo: " + CameraTabState.lastShot, x, y + 50, 0xAAAAAA);

        updateButtonPosition(screenWidth, screenHeight);
    }

    private void updateButtonPosition(int screenWidth, int screenHeight) {
        int buttonX = (screenWidth - cameraButton.getWidth()) / 2;
        int buttonY = (int) (screenHeight * 0.75);
        cameraButton.setPosition(buttonX, buttonY);
    }

    @Override
    public List<Button> getButtons() {
        updateButtonPosition(Minecraft.getInstance().getWindow().getGuiScaledWidth(),
                Minecraft.getInstance().getWindow().getGuiScaledHeight());
        return buttons;
    }

    @Nullable
    private EntityHitResult getEntityLookedAt(Player player, Vec3 from, Vec3 to, double maxDistance) {
        EntityHitResult closest = null;
        double closestDistance = maxDistance * maxDistance;

        for (Entity entity : player.level().getEntities(player, player.getBoundingBox().expandTowards(to.subtract(from)).inflate(1.0))) {
            if (!(entity instanceof AnomalyEntity || entity instanceof ItemEntity)) continue;

            AABB box = entity.getBoundingBox().inflate(0.3);
            Optional<Vec3> hit = box.clip(from, to);
            if (hit.isPresent()) {
                double dist = from.distanceToSqr(hit.get());
                if (dist < closestDistance) {
                    closestDistance = dist;
                    closest = new EntityHitResult(entity, hit.get());
                }
            }
        }

        return closest;
    }

}


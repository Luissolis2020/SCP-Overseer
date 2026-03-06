// CameraScreenshotScheduler.java
package net.luis.scpmod.client;

import net.luis.scpmod.pda_gui.screens.PDAMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraScreenshotScheduler {

    private static int delayTicks = -1;
    private static ResourceLocation pendingId = null;
    private static boolean restoreHud = false;

    public static void schedule(ResourceLocation id, boolean previousHudState) {
        delayTicks = 2;
        pendingId = id;
        restoreHud = previousHudState;
    }

    public static void tick() {
        if (delayTicks >= 0) {
            delayTicks--;
            if (delayTicks == 0 && pendingId != null) {
                takeScreenshot(pendingId);
                Minecraft mc = Minecraft.getInstance();
                mc.options.hideGui = restoreHud;
                mc.setScreen(new PDAMenuScreen("CAMERA"));
                pendingId = null;
            }
        }
    }

    private static void takeScreenshot(ResourceLocation id) {
        Minecraft mc = Minecraft.getInstance();
        String fileName = id.getPath() + ".png";
        File targetDir = new File(mc.gameDirectory, "screenshots/scpmod/");
        File targetFile = new File(targetDir, fileName);

        Screenshot.grab(mc.gameDirectory, mc.getMainRenderTarget(), (msg) -> {
            Matcher matcher = Pattern.compile("([0-9\\-]{10}_[0-9\\.]{8}\\.png)").matcher(msg.getString());
            if (!matcher.find()) {
                System.err.println("[Camera] Could not extract filename");
                return;
            }

            File latest = new File(mc.gameDirectory, "screenshots/" + matcher.group(1));
            if (!latest.exists()) {
                System.err.println("[Camera] Screenshot file not found: " + latest.getAbsolutePath());
                return;
            }

            if (!targetDir.exists()) targetDir.mkdirs();
            if (targetFile.exists()) targetFile.delete();
            if (latest.renameTo(targetFile)) {
                System.out.println("[Camera] Saved SCP image as: " + targetFile.getAbsolutePath());
            } else {
                System.err.println("[Camera] Failed to move screenshot");
            }
        });
    }
}

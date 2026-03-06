package net.luis.scpmod.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScreenshotTextureLoader {

    private static final Map<ResourceLocation, ResourceLocation> CACHE = new HashMap<>();

    public static ResourceLocation getTexture(ResourceLocation researchId) {
        if (CACHE.containsKey(researchId)) {
            return CACHE.get(researchId);
        }

        Minecraft mc = Minecraft.getInstance();
        File file = new File(mc.gameDirectory, "screenshots/scpmod/" + researchId.getPath() + ".png");

        if (!file.exists()) return null;

        try {
            BufferedImage image = ImageIO.read(file);
            NativeImage nativeImage = convertToNativeImage(image);
            DynamicTexture texture = new DynamicTexture(nativeImage);
            ResourceLocation textureId = ResourceLocation.fromNamespaceAndPath("scpmod_screenshots", researchId.getPath());
            mc.getTextureManager().register(textureId, texture);
            CACHE.put(researchId, textureId);
            return textureId;
        } catch (IOException e) {
            System.err.println("[ResearchTab] Failed to load screenshot for " + researchId);
            e.printStackTrace();
            return null;
        }
    }

    private static NativeImage convertToNativeImage(BufferedImage image) {
        NativeImage nativeImage = new NativeImage(image.getWidth(), image.getHeight(), true);
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                int rgba = image.getRGB(x, y);
                int a = (rgba >> 24) & 0xFF;
                int r = (rgba >> 16) & 0xFF;
                int g = (rgba >> 8) & 0xFF;
                int b = rgba & 0xFF;

                int packed = (a << 24) | (b << 16) | (g << 8) | r;

                nativeImage.setPixelRGBA(x, y, packed);
            }
        }
        return nativeImage;
    }

}

package net.luis.scpmod.research;

import net.minecraft.network.chat.Component;

public enum Classification {
    SAFE("Safe", 0x00FF00),
    EUCLID("Euclid", 0xFFFF00),
    KETER("Keter", 0xFF0000),
    THAUMIEL("Thaumiel", 0x00FFFF),
    NEUTRALIZED("Neutralized", 0xAAAAAA);

    private final String name;
    private final int color;

    Classification(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public Component getFormattedName() {
        return Component.literal(name).withStyle(style -> style.withColor(color));
    }

    public String getDisplayName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}


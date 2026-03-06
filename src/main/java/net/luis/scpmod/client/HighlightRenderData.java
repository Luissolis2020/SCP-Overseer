package net.luis.scpmod.client;

import net.minecraft.core.BlockPos;

public class HighlightRenderData {

    private static BlockPos highlightPos = null;
    private static int ticksRemaining = 0;

    public static void setHighlight(BlockPos pos, int durationTicks) {
        highlightPos = pos;
        ticksRemaining = durationTicks;
    }

    public static void tick() {
        if (ticksRemaining > 0) {
            ticksRemaining--;
        } else {
            highlightPos = null;
        }
    }

    public static BlockPos getHighlightPos() {
        return highlightPos;
    }

    public static boolean isHighlightActive() {
        return highlightPos != null && ticksRemaining > 0;
    }
}
package net.luis.scpmod.pocket;

import javax.annotation.Nullable;

public final class PocketPrefabRegistry {
    private static PocketPrefabConfig CONFIG;

    public static void setConfig(PocketPrefabConfig cfg) {
        CONFIG = cfg;
    }

    public static @Nullable PocketPrefabConfig getConfig() {
        return CONFIG;
    }

    private PocketPrefabRegistry() {}
}

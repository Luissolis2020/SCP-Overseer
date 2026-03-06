package net.luis.scpmod.pocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.luis.scpmod.SCPMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class PocketPrefabConfigLoader implements PreparableReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private static final ResourceLocation PREFABS_JSON =
            ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "structures/pocket/prefabs.json");

    private PocketPrefabConfig prepared;

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier barrier,
                                          ResourceManager resourceManager,
                                          ProfilerFiller prepProfiler,
                                          ProfilerFiller applyProfiler,
                                          Executor backgroundExecutor,
                                          Executor gameExecutor) {

        return CompletableFuture.supplyAsync(() -> {
            prepProfiler.push("scpmod_pocket_prefabs_prepare");

            prepared = null;

            Optional<Resource> resOpt = resourceManager.getResource(PREFABS_JSON);
            if (resOpt.isEmpty()) {
                System.out.println("[SCPMod] Missing " + PREFABS_JSON);
                return null;
            }

            try {
                Resource res = resOpt.get();
                try (InputStreamReader reader =
                             new InputStreamReader(res.open(), StandardCharsets.UTF_8)) {

                    JsonElement json = JsonParser.parseReader(reader);
                    prepared = GSON.fromJson(json, PocketPrefabConfig.class);
                    System.out.println("[SCPMod] Loaded pocket prefabs from " + PREFABS_JSON);
                }
            } catch (IOException e) {
                System.err.println("[SCPMod] Failed to read " + PREFABS_JSON + ": " + e);
                e.printStackTrace();
            }

            prepProfiler.pop();
            return null;

        }, backgroundExecutor).thenCompose(barrier::wait).thenRunAsync(() -> {
            applyProfiler.push("scpmod_pocket_prefabs_apply");

            if (prepared != null) {
                PocketPrefabRegistry.setConfig(prepared);

                PocketPrefabLibrary.invalidate();

                System.out.println("[SCPMod] Pocket prefabs applied (start=" + (prepared.start != null ? prepared.start.id : "null") + ")");
            }

            applyProfiler.pop();
        }, gameExecutor);
    }
}

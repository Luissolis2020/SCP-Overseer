package net.luis.scpmod.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import net.luis.scpmod.facility.connector.FacilityConnectorBlock;
import net.luis.scpmod.mixin.StructureTemplateAccessor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateBlueprintCommand {
    private static final int DEFAULT_CONNECTOR_WIDTH = 3;
    private static final int DEFAULT_CONNECTOR_HEIGHT = 3;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("generate_blueprint")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("structure", ResourceLocationArgument.id())
                                .executes(ctx -> {
                                    CommandSourceStack source = ctx.getSource();
                                    ServerLevel level = source.getLevel();
                                    ResourceLocation structureId = ResourceLocationArgument.getId(ctx, "structure");

                                    StructureTemplate template = level.getServer().getStructureManager().getOrCreate(structureId);
                                    if (template == null) {
                                        source.sendFailure(Component.literal("Structure not found: " + structureId));
                                        return 0;
                                    }

                                    Map<String, Integer> materialCounts = new HashMap<>();
                                    List<StructureTemplate.Palette> palettes = ((StructureTemplateAccessor) template).getPalettes();

                                    if (palettes.isEmpty()) {
                                        source.sendFailure(Component.literal("No palette data found in structure: " + structureId));
                                        return 0;
                                    }

                                    Map<BlockPos, ConnectorExport> connectorsByPos = new HashMap<>();

                                    for (StructureTemplate.Palette palette : palettes) {
                                        for (StructureTemplate.StructureBlockInfo info : palette.blocks()) {
                                            Block block = info.state().getBlock();
                                            if (block.defaultBlockState().isAir()) continue;

                                            if (block instanceof FacilityConnectorBlock connectorBlock) {
                                                connectorsByPos.computeIfAbsent(info.pos(), ignored -> {
                                                    var state = info.state();
                                                    var facing = state.getValue(FacilityConnectorBlock.FACING);
                                                    var type = state.getValue(FacilityConnectorBlock.TYPE);
                                                    return new ConnectorExport(
                                                            info.pos().immutable(),
                                                            facing.getSerializedName().toUpperCase(Locale.ROOT),
                                                            type.getSerializedName()
                                                    );
                                                });
                                                continue;
                                            }

                                            Item item = block.asItem();
                                            if (item == Items.AIR) continue;

                                            ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(item);
                                            if (itemKey == null) continue;

                                            materialCounts.merge(itemKey.toString(), 1, Integer::sum);
                                        }
                                    }

                                    var size = template.getSize(); // Use Vec3i
                                    JsonObject json = new JsonObject();
                                    json.addProperty("displayName", formatDisplayName(structureId));
                                    json.addProperty("width", size.getX());
                                    json.addProperty("height", size.getY());
                                    json.addProperty("depth", size.getZ());
                                    json.addProperty("structureFile", structureId.toString());

                                    JsonObject materials = new JsonObject();
                                    for (Map.Entry<String, Integer> entry : new TreeMap<>(materialCounts).entrySet()) {
                                        materials.addProperty(entry.getKey(), entry.getValue());
                                    }
                                    json.add("requiredMaterials", materials);

                                    List<ConnectorExport> sortedConnectors = new ArrayList<>(connectorsByPos.values());
                                    sortedConnectors.sort(Comparator
                                            .comparing((ConnectorExport c) -> c.pos.getX())
                                            .thenComparing(c -> c.pos.getY())
                                            .thenComparing(c -> c.pos.getZ()));

                                    JsonArray connectorsJson = new JsonArray();
                                    int index = 1;
                                    for (ConnectorExport connector : sortedConnectors) {
                                        JsonObject c = new JsonObject();
                                        c.addProperty("name", connector.type + "_" + String.format(Locale.ROOT, "%02d", index++));

                                        JsonArray pos = new JsonArray();
                                        pos.add(connector.pos.getX());
                                        pos.add(connector.pos.getY());
                                        pos.add(connector.pos.getZ());
                                        c.add("pos", pos);

                                        c.addProperty("dir", connector.dir);
                                        c.addProperty("width", DEFAULT_CONNECTOR_WIDTH);
                                        c.addProperty("height", DEFAULT_CONNECTOR_HEIGHT);
                                        c.addProperty("ctype", connector.type); // Optional metadata for future filtering.
                                        connectorsJson.add(c);
                                    }
                                    if (!sortedConnectors.isEmpty()) {
                                        json.add("connectors", connectorsJson);
                                    }

                                    File output = new File("generated/blueprints/" + structureId.getPath() + ".json");
                                    output.getParentFile().mkdirs();

                                    try (FileWriter writer = new FileWriter(output)) {
                                        GSON.toJson(json, writer);
                                        int connectorCount = sortedConnectors.size();
                                        int materialCount = materialCounts.size();
                                        source.sendSuccess(() -> Component.literal(
                                                "Generated blueprint to: " + output.getAbsolutePath()
                                                        + " (materials=" + materialCount
                                                        + ", connectors=" + connectorCount + ")"
                                        ), false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        source.sendFailure(Component.literal("Error: " + e.getMessage()));
                                        return 0;
                                    }

                                    return 1;
                                }))
        );
    }

    private static String formatDisplayName(ResourceLocation id) {
        return Arrays.stream(id.getPath().split("_"))
                .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                .collect(Collectors.joining(" "));
    }

    private record ConnectorExport(BlockPos pos, String dir, String type) {
    }
}

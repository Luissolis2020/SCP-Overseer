package net.luis.scpmod.command;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import net.luis.scpmod.mixin.StructureTemplateAccessor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateBlueprintCommand {

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
                                    StructurePlaceSettings settings = new StructurePlaceSettings();

                                    List<StructureTemplate.Palette> palettes = ((StructureTemplateAccessor) template).getPalettes();

                                    if (palettes.isEmpty()) {
                                        source.sendFailure(Component.literal("No palette data found in structure: " + structureId));
                                        return 0;
                                    }

                                    List<StructureTemplate.StructureBlockInfo> blocks = palettes.get(0).blocks();
                                    for (StructureTemplate.StructureBlockInfo info : blocks) {
                                        Block block = info.state().getBlock();
                                        if (!block.defaultBlockState().isAir()) {
                                            Item item = block.asItem();
                                            if (item != Items.AIR) {
                                                String itemId = ForgeRegistries.ITEMS.getKey(item).toString();
                                                materialCounts.merge(itemId, 1, Integer::sum);
                                            }
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
                                    for (Map.Entry<String, Integer> entry : materialCounts.entrySet()) {
                                        materials.addProperty(entry.getKey(), entry.getValue());
                                    }
                                    json.add("requiredMaterials", materials);

                                    File output = new File("generated/blueprints/" + structureId.getPath() + ".json");
                                    output.getParentFile().mkdirs();

                                    try (FileWriter writer = new FileWriter(output)) {
                                        new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
                                        source.sendSuccess(() -> Component.literal("Generated blueprint to: " + output.getAbsolutePath()), false);
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
}

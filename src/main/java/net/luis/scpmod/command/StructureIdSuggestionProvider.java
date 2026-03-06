package net.luis.scpmod.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.luis.scpmod.facility.building.FacilityBlueprintLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class StructureIdSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        for (ResourceLocation id : FacilityBlueprintLoader.BLUEPRINTS.keySet()) {
            builder.suggest(id.toString());
        }
        return builder.buildFuture();
    }

    public static final StructureIdSuggestionProvider INSTANCE = new StructureIdSuggestionProvider();
}

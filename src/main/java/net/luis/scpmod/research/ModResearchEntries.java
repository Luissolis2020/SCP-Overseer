package net.luis.scpmod.research;

import net.minecraft.resources.ResourceLocation;
import java.util.*;
import java.util.function.Consumer;

public class ModResearchEntries {
    private static final Map<ResourceLocation, ResearchEntry> ENTRIES = new HashMap<>();

    public static final SCPResearchEntry SCP_096 = registerEntity("scp_096", true, Classification.EUCLID, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A humanoid creature that reacts violently to being viewed.");
        entry.setDescription(ResearchStage.CONTAINED, "Subject successfully contained in a specialized chamber.");
        entry.setDescription(ResearchStage.TESTED, "Field tests confirm extreme aggression when viewed.");
        entry.setDescription(ResearchStage.MISSING, "Escaped after breach. Status unknown.");
    });

    public static final SCPResearchEntry SCP_049 = registerEntity("scp_049", true, Classification.EUCLID, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "An entity resembling a plague doctor.");
        entry.setDescription(ResearchStage.CONTAINED, "SCP-049 is held in medical containment.");
        entry.setDescription(ResearchStage.TESTED, "Known to perform 'cures' by killing and reanimating victims.");
    });

    public static final SCPResearchEntry SCP_1048 = registerEntity("scp_1048", true, Classification.KETER, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A small teddy bear capable of creating copies from organic material.");
        entry.setDescription(ResearchStage.CONTAINED, "SCP-1048 held in Secure Storage.");
        entry.setDescription(ResearchStage.TESTED, "Multiple variants observed, some highly dangerous.");
    });

    public static final SCPResearchEntry SCP_173 = registerEntity("scp_173", true, Classification.EUCLID, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A statue that moves when unobserved.");
        entry.setDescription(ResearchStage.CONTAINED, "Subject requires 3 observers at all times.");
        entry.setDescription(ResearchStage.TESTED, "Snaps necks of nearby targets instantly.");
    });

    public static final SCPResearchEntry SCP_106 = registerEntity("scp_106", true, Classification.KETER, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "An old humanoid capable of phase traversal.");
        entry.setDescription(ResearchStage.CONTAINED, "Requires heavy electromagnetic containment.");
        entry.setDescription(ResearchStage.TESTED, "Victims dragged to unknown pocket dimension.");
    });

    public static final SCPResearchEntry SCP_513_1 = registerEntity("scp_513_1", true, Classification.EUCLID, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A figure seen after hearing a certain bell.");
        entry.setDescription(ResearchStage.TESTED, "Induces paranoia, hallucinations, eventual madness.");
    });

    public static final SCPResearchEntry SCP_2295 = registerEntity("scp_2295", true, Classification.SAFE, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "Patchwork bear that attempts emergency surgery.");
        entry.setDescription(ResearchStage.TESTED, "Replaces failing organs with plush material.");
    });

    public static final SCPResearchEntry SCP_427_1 = registerEntity("scp_427_1", true, Classification.EUCLID, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "Anomalous pendant with regenerative effects.");
        entry.setDescription(ResearchStage.TESTED, "Causes extreme mutations with prolonged use.");
    });

    public static final ResearchEntry SCP_500 = registerItem("scp_500", true, Classification.SAFE, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A red pill capable of curing most known diseases.");
        entry.setDescription(ResearchStage.TESTED, "Overuse may cause diminishing returns or side effects.");
    });

    public static final ResearchEntry SCP_427 = registerItem("scp_427", true, Classification.SAFE, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A pendant that causes regeneration.");
        entry.setDescription(ResearchStage.TESTED, "Extended use causes mutation.");
    });

    public static final ResearchEntry SCP_124 = registerBlock("scp_124", true, Classification.EUCLID, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "Soil that causes accelerated plant growth.");
        entry.setDescription(ResearchStage.TESTED, "Produces full crop yield within seconds.");
    });

    public static final ResearchEntry SCP_458 = registerBlock("scp_458", true, Classification.SAFE, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A pizza box");
        entry.setDescription(ResearchStage.TESTED, "Produces a unlimited amount of pizza with a short cooldown.");
    });

    public static final ResearchEntry SCP_109 = registerItem("scp_109", true, Classification.EUCLID, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A old military canteen.");
        entry.setDescription(ResearchStage.TESTED, "Has a unending supply of water inside.");
    });

    public static final ResearchEntry SCP_207 = registerItem("scp_207", true, Classification.SAFE, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A bottle of cola.");
        entry.setDescription(ResearchStage.TESTED, "Massive sugar levels cause user to have increased speed with chance of heart attack.");
    });

    public static final ResearchEntry SCP_513 = registerItem("scp_513", true, Classification.EUCLID, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A rusted cowbell");
        entry.setDescription(ResearchStage.TESTED, "Hearing the bell ring causes user to hallucinate until death occurs.");
    });

    public static final ResearchEntry SCP_668 = registerItem("scp_668", true, Classification.EUCLID, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A large knife.");
        entry.setDescription(ResearchStage.TESTED, "Wielder of knife experiences murder like tendencies.");
    });

    public static final ResearchEntry SCP_1025 = registerItem("scp_1025", true, Classification.EUCLID, entry -> {
        entry.setDescription(ResearchStage.UNDISCOVERED, "This SCP has not been discovered.");
        entry.setDescription(ResearchStage.DISCOVERED, "A weird blue book.");
        entry.setDescription(ResearchStage.TESTED, "The book contains entries of diseases, reading one of the diseases will cause the user to gain that disease.");
    });

    private static SCPResearchEntry registerEntity(String id, boolean visibleByDefault, Classification classification, Consumer<SCPResearchEntry> setup) {
        ResourceLocation resId = ResourceLocation.fromNamespaceAndPath("scpmod", id);
        SCPResearchEntry entry = new SCPResearchEntry(resId, visibleByDefault, classification);
        setup.accept(entry);
        ENTRIES.put(resId, entry);
        return entry;
    }

    private static ResearchEntry registerBlock(String id, boolean visibleByDefault, Classification classification, Consumer<SCPBlockEntry> setup) {
        ResourceLocation resId = ResourceLocation.fromNamespaceAndPath("scpmod", id);
        SCPBlockEntry entry = new SCPBlockEntry(resId, visibleByDefault, classification);
        setup.accept(entry);
        ENTRIES.put(resId, entry);
        return entry;
    }

    private static ResearchEntry registerItem(String id, boolean visibleByDefault, Classification classification, Consumer<SCPItemEntry> setup) {
        ResourceLocation resId = ResourceLocation.fromNamespaceAndPath("scpmod", id);
        SCPItemEntry entry = new SCPItemEntry(resId, visibleByDefault, classification);
        setup.accept(entry);
        ENTRIES.put(resId, entry);
        return entry;
    }

    public static ResearchEntry get(ResourceLocation id) {
        return ENTRIES.get(id);
    }

    public static Collection<ResearchEntry> getAllEntries() {
        return ENTRIES.values();
    }

}

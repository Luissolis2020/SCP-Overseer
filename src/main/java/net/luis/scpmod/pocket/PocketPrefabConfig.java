package net.luis.scpmod.pocket;

import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class PocketPrefabConfig {

    public Entry start;
    public List<Entry> exit;
    public List<Entry> halls;
    public List<Entry> rooms;

    @SerializedName("dead_ends")
    public List<Entry> deadEnds;

    public List<Entry> traps;

    public static final class Entry {
        public String id;
        public int weight = 1;

        public ResourceLocation rl() {
            return ResourceLocation.parse(id);
        }

        @Override
        public String toString() {
            return "Entry{id=" + id + ", weight=" + weight + "}";
        }
    }
}
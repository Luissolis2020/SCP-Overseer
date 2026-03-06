package net.luis.scpmod.research;

public enum ResearchCategory {
    FACILITY("Facility Infrastructure"),
    PERSONNEL("Personnel Management"),
    CONTAINMENT("Containment Tech"),
    EQUIPMENT("Equipment & Tools");

    public final String title;

    ResearchCategory(String title) {
        this.title = title;
    }
}

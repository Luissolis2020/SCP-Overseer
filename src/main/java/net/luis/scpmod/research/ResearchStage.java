package net.luis.scpmod.research;

public enum ResearchStage {
    UNDISCOVERED,
    DISCOVERED,
    CONTAINED,
    TESTED,
    NEUTRALIZED,
    MISSING;

    public ResearchStage next() {
        return switch (this) {
            case UNDISCOVERED -> DISCOVERED;
            case DISCOVERED -> CONTAINED;
            case CONTAINED -> TESTED;
            case TESTED -> TESTED; // terminal stage
            case NEUTRALIZED, MISSING -> this; // terminal alternate states
        };
    }

    public boolean isFinal() {
        return this == TESTED || this == NEUTRALIZED || this == MISSING;
    }
}



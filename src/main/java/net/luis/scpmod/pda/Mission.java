package net.luis.scpmod.pda;

import net.minecraft.network.chat.Component;

public class Mission {

    private final String id;
    private final Component title;
    private final Component objective;
    private int currentProgress;
    private int requiredProgress;

    public Mission(String id, Component title, Component objective, int requiredProgress) {
        this.id = id;
        this.title = title;
        this.objective = objective;
        this.requiredProgress = requiredProgress;
        this.currentProgress = 0;
    }

    public Mission(String id, Component title, Component objective, int requiredProgress, int currentProgress) {
        this.id = id;
        this.title = title;
        this.objective = objective;
        this.requiredProgress = Math.max(0, requiredProgress);
        this.currentProgress = Math.max(0, currentProgress);
    }

    public String getId() {
        return id;
    }

    public Component getTitle() {
        return title;
    }

    public Component getObjective() {
        return objective;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getRequiredProgress() {
        return requiredProgress;
    }

    public boolean isComplete() {
        return currentProgress >= requiredProgress;
    }

    public void incrementProgress() {
        currentProgress = Math.min(currentProgress + 1, requiredProgress);
    }
}

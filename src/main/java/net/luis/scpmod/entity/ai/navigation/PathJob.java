package net.luis.scpmod.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public abstract class PathJob {
    protected final Mob mob;
    protected final Level level;
    protected boolean started = false;
    protected boolean completed = false;
    protected boolean failed = false;
    protected int ticksRunning = 0;
    protected int retryCooldown = 0;

    public PathJob(Mob mob) {
        this.mob = mob;
        this.level = mob.level();
    }

    /** Called once when the job is first executed */
    public void start(ServerLevel level) {
        started = true;
    }

    /** Called every tick */
    public void tick(ServerLevel level) {
        if (!started || completed || failed) return;
        ticksRunning++;
        if (retryCooldown > 0) retryCooldown--;
        onTick(level);
    }

    protected abstract void onTick(ServerLevel level);

    public boolean isComplete() {
        return completed;
    }

    public boolean hasFailed() {
        return failed;
    }

    public boolean shouldAbort(ServerLevel level) {
        return false;
    }

    public void onAbort(ServerLevel level) {}

    /** Called if the job is canceled */
    public void cancel() {
        failed = true;
    }

    /** Called before re-enqueueing the same job instance after failure */
    public void resetForRetry() {
        // default: nothing; subclasses can override
        this.started = false;
        this.completed = false;
        this.failed = false;
        this.ticksRunning = 0;
        this.retryCooldown = 0;
    }

    public abstract String getName();
}

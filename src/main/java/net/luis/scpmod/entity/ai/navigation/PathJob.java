package net.luis.scpmod.entity.ai.navigation;

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

    /**
     * Stable key used for retry accounting and queue de-duplication.
     * Defaults to the display name.
     */
    public String getRetryKey() {
        return getName();
    }

    /** Number of attempts before the queue treats this job as permanently failed. */
    public int getMaxFailureRetries() {
        return 3;
    }

    /** Queue-level cooldown before retrying this job after a failure. */
    public int getRetryDelayTicks() {
        return 20;
    }

    /** Whether enqueue should de-duplicate jobs with the same retry key. */
    public boolean shouldDeduplicate() {
        return true;
    }

    public abstract String getName();
}

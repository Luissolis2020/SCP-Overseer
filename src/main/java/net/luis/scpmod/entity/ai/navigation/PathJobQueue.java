package net.luis.scpmod.entity.ai.navigation;

import net.luis.scpmod.entity.custom.BuilderNPC;
import net.minecraft.server.level.ServerLevel;

import java.util.LinkedList;
import java.util.Queue;

public class PathJobQueue {

    private final BuilderNPC builder;
    private final Queue<PathJob> jobs = new LinkedList<>();
    private PathJob currentJob;
    private int retryCooldown = 0;
    private static final int RETRY_TICKS = 20;

    public PathJobQueue(BuilderNPC builder) {
        this.builder = builder;
    }

    public void tick(ServerLevel level) {
        if (retryCooldown > 0) {
            retryCooldown--;
            return;
        }

        // pick/start next job
        if (currentJob == null || currentJob.isComplete()) {
            currentJob = jobs.poll();
            if (currentJob != null) {
                // abort instantly if it shouldn't run anymore
                if (currentJob.shouldAbort(level)) {
                    System.out.println("[PathJobQueue] Job aborted before start: " + currentJob.getName());
                    currentJob.onAbort(level);
                    currentJob = null;
                    return;
                }
                currentJob.start(level);
            }
        }

        if (currentJob == null) return;

        PathJob job = currentJob;

        if (job.shouldAbort(level)) {
            System.out.println("[PathJobQueue] Job aborted: " + job.getName());
            job.onAbort(level);
            currentJob = null;
            retryCooldown = 0;
            return;
        }

        job.tick(level);

        if (currentJob == job && job.isComplete()) {
            System.out.println("[PathJobQueue] Job completed: " + job.getName());
            currentJob = null;
            retryCooldown = 0;
            return;
        }

        if (currentJob == job && job.shouldAbort(level)) {
            System.out.println("[PathJobQueue] Job aborted after tick: " + job.getName());
            job.onAbort(level);
            currentJob = null;
            retryCooldown = 0;
            return;
        }

        if (job.hasFailed()) {
            retryCooldown = RETRY_TICKS;
            job.resetForRetry();
            jobs.add(job);
            System.out.println("[PathJobQueue] Job failed, retrying in " + RETRY_TICKS + " ticks.");
            currentJob = null;
        }

    }

    public void enqueue(PathJob job) {
        String name = job.getName();

        if (name.equals("MoveToConstructionSite")) {
            if (currentJob != null && currentJob.getName().equals("MoveToConstructionSite")) return;

            PathJob last = null;
            for (PathJob j : jobs) last = j;
            if (last != null && last.getName().equals("MoveToConstructionSite")) return;
        }

        if (name.startsWith("MoveToPos")) {
            if (currentJob != null && currentJob.getName().startsWith("MoveToPos")) return;

            PathJob last = null;
            for (PathJob j : jobs) last = j;
            if (last != null && last.getName().startsWith("MoveToPos")) return;
        }

        jobs.add(job);
    }

    public boolean isBusy() {
        return currentJob != null || !jobs.isEmpty();
    }

    public void clear() {
        cancelCurrentJob();
        jobs.clear();
    }

    public PathJob getCurrentJob() {
        return currentJob;
    }

    public void cancelCurrentJob() {
        if (currentJob != null) {
            currentJob.cancel();
            currentJob = null;
        }
    }

}

package net.luis.scpmod.entity.ai.navigation;

import net.luis.scpmod.entity.custom.BuilderNPC;
import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class PathJobQueue {

    private final BuilderNPC builder;
    private final Queue<PathJob> jobs = new LinkedList<>();
    private final Map<String, Integer> failureRetries = new HashMap<>();
    private PathJob currentJob;
    private int retryCooldown = 0;

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
                    clearFailureState(currentJob);
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
            clearFailureState(job);
            currentJob = null;
            retryCooldown = 0;
            return;
        }

        job.tick(level);

        // Job may clear/cancel itself indirectly (e.g., site finalized clears builder queue).
        // In that case, don't treat it as a retryable failure here.
        if (currentJob != job) {
            retryCooldown = 0;
            return;
        }

        if (job.isComplete()) {
            System.out.println("[PathJobQueue] Job completed: " + job.getName());
            clearFailureState(job);
            currentJob = null;
            retryCooldown = 0;
            return;
        }

        if (job.shouldAbort(level)) {
            System.out.println("[PathJobQueue] Job aborted after tick: " + job.getName());
            job.onAbort(level);
            clearFailureState(job);
            currentJob = null;
            retryCooldown = 0;
            return;
        }

        if (job.hasFailed()) {
            String key = keyFor(job);
            int attempts = failureRetries.getOrDefault(key, 0) + 1;
            failureRetries.put(key, attempts);
            int maxRetries = Math.max(0, job.getMaxFailureRetries());
            int retryDelay = Math.max(0, job.getRetryDelayTicks());

            if (attempts < maxRetries) {
                retryCooldown = retryDelay;
                job.resetForRetry();
                jobs.add(job);
                System.out.println("[PathJobQueue] Job failed (" + attempts + "/" + maxRetries
                        + "), retrying in " + retryDelay + " ticks: " + job.getName());
            } else {
                System.out.println("[PathJobQueue] Job failed permanently after " + attempts
                        + " attempts: " + job.getName());
                job.onAbort(level);
                clearFailureState(job);
                retryCooldown = 0;
            }
            currentJob = null;
        }

    }

    public void enqueue(PathJob job) {
        if (job == null) return;
        if (job.shouldDeduplicate() && isDuplicate(job)) return;
        jobs.add(job);
    }

    public boolean isBusy() {
        return currentJob != null || !jobs.isEmpty();
    }

    public void clear() {
        cancelCurrentJob();
        jobs.clear();
        failureRetries.clear();
        retryCooldown = 0;
    }

    public PathJob getCurrentJob() {
        return currentJob;
    }

    public void cancelCurrentJob() {
        if (currentJob != null) {
            clearFailureState(currentJob);
            currentJob.cancel();
            currentJob = null;
        }
        builder.getNavigation().stop();
    }

    private boolean isDuplicate(PathJob job) {
        String key = keyFor(job);
        if (currentJob != null && key.equals(keyFor(currentJob))) return true;

        for (PathJob queued : jobs) {
            if (key.equals(keyFor(queued))) return true;
        }
        return false;
    }

    private void clearFailureState(PathJob job) {
        failureRetries.remove(keyFor(job));
    }

    private String keyFor(PathJob job) {
        String key = job.getRetryKey();
        if (key == null || key.isBlank()) return job.getName();
        return key;
    }

}

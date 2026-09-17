package engine;

import models.Task;

/**
 * Strategy interface for spaced-repetition scheduling algorithms.
 * Decoupling the scheduling policy from the SessionRunner (client code)
 * means alternative algorithms (e.g. Leitner system, SM-18, a fixed-interval
 * scheme) could be substituted in the future without touching the console UI.
 */
public interface SchedulingAlgorithm {

    /**
     * Given a Task and a user-supplied quality rating (1 = poor recall,
     * 5 = perfect recall), updates the Task's easiness factor, repetition
     * count, interval, and next review date, and appends a ReviewRecord
     * to its history.
     */
    void schedule(Task task, int qualityRating);
}

package engine;

import models.HabitTask;
import models.ReviewRecord;
import models.Task;

import java.time.LocalDate;

/**
 * Concrete implementation of the SuperMemo-2 (SM-2) spaced repetition
 * algorithm, adapted to accept a 1-5 quality rating instead of SM-2's
 * traditional 0-5 scale (1-2 = failed recall, 3-5 = successful recall of
 * increasing quality).
 *
 * Reference formula (original SM-2, q on a 0-5 scale):
 *   EF' = EF + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
 *   EF' is clamped to a minimum of 1.3.
 *
 * Interval progression on success:
 *   n = 1  -> interval = 1 day
 *   n = 2  -> interval = 6 days
 *   n > 2  -> interval = round(previous interval * EF)
 *
 * On failure (q < 3), the repetition counter and interval both reset,
 * mirroring SM-2's "forgetting" behaviour.
 */
public class SM2SchedulingAlgorithm implements SchedulingAlgorithm {

    private static final double MIN_EASINESS_FACTOR = 1.3;
    private static final int SECOND_REPETITION_INTERVAL = 6;
    private static final int FIRST_REPETITION_INTERVAL = 1;

    @Override
    public void schedule(Task task, int qualityRating) {
        if (qualityRating < 1 || qualityRating > 5) {
            throw new IllegalArgumentException("Quality rating must be between 1 and 5 inclusive.");
        }

        double easinessFactor = task.getEasinessFactor();
        int repetitions = task.getRepetitionCount();
        int newInterval;

        if (qualityRating < 3) {
            // Failed recall: restart the repetition sequence.
            repetitions = 0;
            newInterval = FIRST_REPETITION_INTERVAL;
            if (task instanceof HabitTask) {
                ((HabitTask) task).resetStreak();
            }
        } else {
            repetitions += 1;
            if (repetitions == 1) {
                newInterval = FIRST_REPETITION_INTERVAL;
            } else if (repetitions == 2) {
                newInterval = SECOND_REPETITION_INTERVAL;
            } else {
                newInterval = (int) Math.round(task.getCurrentInterval() * easinessFactor);
            }
            if (task instanceof HabitTask) {
                ((HabitTask) task).incrementStreak();
            }
        }

        double updatedEf = easinessFactor
                + (0.1 - (5 - qualityRating) * (0.08 + (5 - qualityRating) * 0.02));
        if (updatedEf < MIN_EASINESS_FACTOR) {
            updatedEf = MIN_EASINESS_FACTOR;
        }

        task.setEasinessFactor(updatedEf);
        task.setRepetitionCount(repetitions);
        task.setCurrentInterval(newInterval);
        task.setNextReviewDate(LocalDate.now().plusDays(newInterval));
        task.addReviewRecord(new ReviewRecord(LocalDate.now(), qualityRating, newInterval));
    }
}

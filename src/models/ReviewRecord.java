package models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * An immutable log entry representing a single review event for a Task.
 * A List<ReviewRecord> forms the historical audit trail of a Task,
 * which is later used for CSV export and for testing the scheduling engine.
 */
public class ReviewRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private final LocalDate reviewDate;
    private final int qualityRating;
    private final int resultingIntervalDays;

    public ReviewRecord(LocalDate reviewDate, int qualityRating, int resultingIntervalDays) {
        this.reviewDate = reviewDate;
        this.qualityRating = qualityRating;
        this.resultingIntervalDays = resultingIntervalDays;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public int getQualityRating() {
        return qualityRating;
    }

    public int getResultingIntervalDays() {
        return resultingIntervalDays;
    }

    @Override
    public String toString() {
        return String.format("%s -> Rating: %d, Next Interval: %d day(s)",
                reviewDate, qualityRating, resultingIntervalDays);
    }
}

package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class representing any trackable item (a Skill or a Habit)
 * that is subject to spaced-repetition scheduling.
 *
 * Demonstrates:
 *  - Encapsulation: all fields are private with controlled accessors.
 *  - Abstraction: getTaskType() / getProgressSummary() are implemented
 *    differently by each concrete subclass (polymorphism).
 *  - Natural ordering via Comparable, so Task objects can be placed
 *    directly into a PriorityQueue ordered by nextReviewDate.
 */
public abstract class Task implements Serializable, Comparable<Task> {

    private static final long serialVersionUID = 1L;

    /** Class-level counter used to hand out unique, human-readable IDs. */
    private static int idCounter = 1;

    private final int id;
    private String name;
    private String description;
    private Category category;
    private final LocalDate creationDate;
    private LocalDate nextReviewDate;
    private int repetitionCount;
    private double easinessFactor;
    private int currentInterval;
    private final List<ReviewRecord> history;

    protected Task(String name, String description, Category category) {
        this.id = idCounter++;
        this.name = name;
        this.description = description;
        this.category = category;
        this.creationDate = LocalDate.now();
        this.nextReviewDate = LocalDate.now();
        this.repetitionCount = 0;
        this.easinessFactor = 2.5; // Standard SuperMemo-2 starting easiness factor.
        this.currentInterval = 0;
        this.history = new ArrayList<>();
    }

    /**
     * Ensures the static ID counter never issues a duplicate ID after a
     * DeckManager has been restored from disk (since static fields are
     * not part of the serialized object graph).
     */
    public static void ensureIdCounterAbove(int usedId) {
        if (usedId >= idCounter) {
            idCounter = usedId + 1;
        }
    }

    // ---- Abstract contract implemented polymorphically by subclasses ----
    public abstract String getTaskType();
    public abstract String getProgressSummary();

    // ---- Encapsulated accessors ----
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(LocalDate nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public int getRepetitionCount() {
        return repetitionCount;
    }

    public void setRepetitionCount(int repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    public double getEasinessFactor() {
        return easinessFactor;
    }

    public void setEasinessFactor(double easinessFactor) {
        this.easinessFactor = easinessFactor;
    }

    public int getCurrentInterval() {
        return currentInterval;
    }

    public void setCurrentInterval(int currentInterval) {
        this.currentInterval = currentInterval;
    }

    public List<ReviewRecord> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public void addReviewRecord(ReviewRecord record) {
        this.history.add(record);
    }

    /** Natural ordering: earliest due date first, enabling PriorityQueue usage. */
    @Override
    public int compareTo(Task other) {
        return this.nextReviewDate.compareTo(other.nextReviewDate);
    }

    @Override
    public String toString() {
        return String.format("[#%d] %-38s | %-16s | Next Review: %s | Reps: %d | EF: %.2f",
                id, name, category, nextReviewDate, repetitionCount, easinessFactor);
    }
}

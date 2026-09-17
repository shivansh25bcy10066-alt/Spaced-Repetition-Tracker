package models;

/**
 * Concrete Task subclass representing a recurring "Habit" (e.g. a
 * Calisthenics routine) whose progress is best expressed as a streak
 * rather than a proficiency label. Demonstrates that subclasses of the
 * same abstract Task can carry entirely different internal state while
 * still being interchangeable wherever a Task is expected (polymorphism).
 */
public class HabitTask extends Task {

    private static final long serialVersionUID = 1L;

    private int currentStreak;
    private int longestStreak;

    public HabitTask(String name, String description, Category category) {
        super(name, description, category);
        this.currentStreak = 0;
        this.longestStreak = 0;
    }

    public void incrementStreak() {
        currentStreak++;
        if (currentStreak > longestStreak) {
            longestStreak = currentStreak;
        }
    }

    public void resetStreak() {
        currentStreak = 0;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    @Override
    public String getTaskType() {
        return "Habit";
    }

    @Override
    public String getProgressSummary() {
        return "Current Streak: " + currentStreak + " session(s) | Longest Streak: " + longestStreak + " session(s)";
    }
}

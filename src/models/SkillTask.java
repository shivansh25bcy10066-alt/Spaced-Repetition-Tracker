package models;

/**
 * Concrete Task subclass representing a "Skill" that is being acquired
 * or refined through deliberate, spaced practice (e.g. Java DSA, Harmonica,
 * Cursive Handwriting). Skills are tracked by a self-declared proficiency
 * label rather than a day-to-day streak.
 */
public class SkillTask extends Task {

    private static final long serialVersionUID = 1L;

    private String proficiencyLevel;

    public SkillTask(String name, String description, Category category, String proficiencyLevel) {
        super(name, description, category);
        this.proficiencyLevel = proficiencyLevel;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    @Override
    public String getTaskType() {
        return "Skill";
    }

    @Override
    public String getProgressSummary() {
        return "Proficiency Level: " + proficiencyLevel
                + " | Successful repetitions logged: " + getRepetitionCount();
    }
}

package utils;

import engine.DeckManager;
import models.Category;
import models.HabitTask;
import models.SkillTask;

/**
 * Pre-populates a fresh DeckManager with the four required seed entries
 * so the application is immediately demonstrable on first run.
 */
public final class SeedDataInitializer {

    private SeedDataInitializer() {
        // Utility class; no instances.
    }

    public static void populate(DeckManager manager) {
        SkillTask dsaSkill = new SkillTask(
                "Java DSA Problem Solving",
                "Daily practice of Data Structures & Algorithms problems in Java "
                        + "to build competitive programming and interview readiness.",
                Category.TECHNICAL_SKILL,
                "Intermediate"
        );

        SkillTask harmonicaSkill = new SkillTask(
                "Harmonica Practice",
                "Practicing scales, note bending, and simple melodies on a "
                        + "diatonic harmonica.",
                Category.MUSICAL_SKILL,
                "Beginner"
        );

        SkillTask cursiveSkill = new SkillTask(
                "Continuous Cursive Handwriting",
                "Practicing continuous, joined-up cursive handwriting for "
                        + "improved speed and legibility.",
                Category.HANDWRITING,
                "Beginner"
        );

        HabitTask calisthenicsHabit = new HabitTask(
                "Calisthenics (Pull-up progression)",
                "Progressive bodyweight training routine focused on building "
                        + "toward a strict, unassisted pull-up.",
                Category.PHYSICAL_FITNESS
        );

        manager.addTask(dsaSkill);
        manager.addTask(harmonicaSkill);
        manager.addTask(cursiveSkill);
        manager.addTask(calisthenicsHabit);
    }
}

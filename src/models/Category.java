package models;

/**
 * Represents the broad domain a Task (Skill or Habit) belongs to.
 * Used as the key basis for the Map<String, List<Task>> categorization
 * structure maintained by the DeckManager.
 */
public enum Category {
    TECHNICAL_SKILL,
    MUSICAL_SKILL,
    HANDWRITING,
    PHYSICAL_FITNESS
}

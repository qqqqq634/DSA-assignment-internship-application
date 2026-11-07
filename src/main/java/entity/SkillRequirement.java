package entity;

import java.io.Serializable;
public class SkillRequirement implements Serializable {
    private String skillName;
    private int importance;

    /**
     * Creates a skill requirement with the supplied name and importance.
     * @param skillName the skillName value
     * @param importance the importance value
     */
    public SkillRequirement(String skillName, int importance) {
        this.skillName = skillName;
        this.importance = importance;
    }

    /**
     * Copies the skill name and importance value.
     * @param skillRequirement the skillRequirement value
     */
    public SkillRequirement(SkillRequirement skillRequirement) {
        this.skillName = skillRequirement.getSkillName();
        this.importance = skillRequirement.getImportance();
    }

    /**
     * Returns the skill name.
     * @return returns the skill name.
     */
    public String getSkillName() {
        return skillName;
    }

    /**
     * Returns the importance.
     * @return returns the importance.
     */
    public int getImportance() {
        return importance;
    }

        /**
     * Formats this entity and its relevant details for display.
     * @return formats this entity and its relevant details for display.
     */

    @Override
    public String toString() {
        return String.format("| %-15s | %-10d |%n", skillName, importance);
    }
}

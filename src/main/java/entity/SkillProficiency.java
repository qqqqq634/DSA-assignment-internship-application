package entity;
import java.io.Serializable;
public class SkillProficiency implements Serializable {
    private String skillName;
    private int proficiency;

    /**
     * Creates a skill proficiency with the supplied name and value.
     * @param skillName the skillName value
     * @param proficiency the proficiency value
     */
    public SkillProficiency(String skillName, int proficiency) {
        this.skillName = skillName;
        this.proficiency = proficiency;
    }

    /**
     * Copies the skill name and proficiency value.
     * @param skillProficiency the skillProficiency value
     */
    public SkillProficiency(SkillProficiency skillProficiency) {
        this.skillName = skillProficiency.getSkillName();
        this.proficiency = skillProficiency.getProficiency();
    }

    // Getters
    /**
     * Returns the skill name.
     * @return returns the skill name.
     */
    public String getSkillName() {
        return skillName;
    }

    /**
     * Returns the proficiency.
     * @return returns the proficiency.
     */
    public int getProficiency() {
        return proficiency;
    }

    // Setters
    /**
     * Sets the skill name.
     * @param skillName the skillName value
     */
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    /**
     * Sets the proficiency.
     * @param proficiency the proficiency value
     */
    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }

        /**
     * Formats this entity and its relevant details for display.
     * @return formats this entity and its relevant details for display.
     */

    @Override
    public String toString() {
        String result = "";
        result += String.format("|%-15s| %12d|\n", skillName, proficiency);
        result += "\t\t+-----+---------------+-------------+\n";
        return result;
    }
}
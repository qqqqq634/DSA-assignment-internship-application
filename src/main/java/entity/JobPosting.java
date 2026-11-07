package entity;

import adt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class JobPosting implements Serializable {
    /** Keeps the serialized class form compatible across application versions. */
    private static final long serialVersionUID = -4943339634603753560L;
    private String id;
    private String title;
    private String description;
    private ListInterface<SkillRequirement> requiredSkills = new ArrayList<>();
    private String location;
    private double[] salaryRange;
    private int experienceRequired;
    // score for match
    private int locationImprotance;
    private int experienceImportance;
    private int skillImportance;
    private static int COUNTER = 0;
    /**
     * Reads serialized fields and restores the ID allocator from the stored identifier.
     * @param input the input value
     */
    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        restoreCounter(id, "J");
    }

    /**
     * Advances the ID allocator when a stored identifier has a valid higher numeric suffix.
     * @param storedId the storedId value
     * @param prefix the prefix value
     */
    private static synchronized void restoreCounter(String storedId, String prefix) {
        if (storedId == null || !storedId.startsWith(prefix) || storedId.length() == prefix.length()) return;
        try {
            int suffix = Integer.parseInt(storedId.substring(prefix.length()));
            if (suffix > COUNTER) COUNTER = suffix;
        } catch (NumberFormatException ignored) {
            // Ignore legacy or malformed IDs and keep the current allocator state.
        }
    }


    /**
     * Copies the job ID and fields without allocating an ID; the skill list and salary array are shared.
     * @param jobPosting the jobPosting value
     */
    public JobPosting(JobPosting jobPosting) {
        this.id = jobPosting.getId();
        this.title = jobPosting.getTitle();
        this.description = jobPosting.getDescription();
        this.requiredSkills = jobPosting.getRequiredSkills();
        this.location = jobPosting.getLocation();
        this.salaryRange = jobPosting.getSalaryRange();
        this.experienceRequired = jobPosting.getExperienceRequired();
        this.locationImprotance = jobPosting.getLocationImprotance();
        this.experienceImportance = jobPosting.getExperienceImportance();
        this.skillImportance = jobPosting.getSkillImportance();
    }

    // constructor
    /**
     * Allocates a new job ID and retains the supplied skill list and salary array by reference.
     * @param title the title value
     * @param description the description value
     * @param requiredSkills the requiredSkills value
     * @param location the location value
     * @param salaryRange the salaryRange value
     * @param experienceRequired the experienceRequired value
     * @param locationImprotance the locationImprotance value
     * @param experienceImportance the experienceImportance value
     * @param skillImportance the skillImportance value
     */
    public JobPosting(String title, String description, ListInterface<SkillRequirement> requiredSkills, String location,
            double[] salaryRange, int experienceRequired, int locationImprotance, int experienceImportance,
            int skillImportance) {
        COUNTER++;
        this.id = "J" + COUNTER;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.location = location;
        this.salaryRange = salaryRange;
        this.experienceRequired = experienceRequired;
        this.locationImprotance = locationImprotance;
        this.experienceImportance = experienceImportance;
        this.skillImportance = skillImportance;
    }

    // Getters
    /**
     * Returns the location improtance.
     * @return returns the location improtance.
     */
    public int getLocationImprotance() {
        return locationImprotance;
    }

    /**
     * Returns the experience importance.
     * @return returns the experience importance.
     */
    public int getExperienceImportance() {
        return experienceImportance;
    }

    /**
     * Returns the skill importance.
     * @return returns the skill importance.
     */
    public int getSkillImportance() {
        return skillImportance;
    }

    /**
     * Returns the id.
     * @return returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the title.
     * @return returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description.
     * @return returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the live required-skills list.
     * @return returns the required skills.
     */
    public ListInterface<SkillRequirement> getRequiredSkills() {
        return requiredSkills;
    }

    /**
     * Returns the location.
     * @return returns the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the stored salary array by reference.
     * @return returns the salary range.
     */
    public double[] getSalaryRange() {
        return salaryRange;
    }

    /**
     * Returns the experience required.
     * @return returns the experience required.
     */
    public int getExperienceRequired() {
        return experienceRequired;
    }

    // Setters
    /**
     * Sets the stored ID directly; it does not advance the ID allocator.
     * @param id the id value
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the title.
     * @param title the title value
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the description.
     * @param description the description value
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Replaces the required-skills list with the supplied reference.
     * @param requiredSkills the requiredSkills value
     */
    public void setRequiredSkills(ListInterface<SkillRequirement> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    /**
     * Sets the location.
     * @param location the location value
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Replaces the salary array with the supplied reference.
     * @param salaryRange the salaryRange value
     */
    public void setSalaryRange(double[] salaryRange) {
        this.salaryRange = salaryRange;
    }

    /**
     * Sets the experience required.
     * @param experienceRequired the experienceRequired value
     */
    public void setExperienceRequired(int experienceRequired) {
        this.experienceRequired = experienceRequired;
    }

    /**
     * Sets the location improtance.
     * @param locationImprotance the locationImprotance value
     */
    public void setLocationImprotance(int locationImprotance) {
        this.locationImprotance = locationImprotance;
    }

    /**
     * Sets the experience importance.
     * @param experienceImportance the experienceImportance value
     */
    public void setExperienceImportance(int experienceImportance) {
        this.experienceImportance = experienceImportance;
    }

    /**
     * Sets the skill importance.
     * @param skillImportance the skillImportance value
     */
    public void setSkillImportance(int skillImportance) {
        this.skillImportance = skillImportance;
    }

        /**
     * Formats this entity and its relevant details for display.
     * @return formats this entity and its relevant details for display.
     */

    @Override
    public String toString() {
        String result = "";
        result += "\n\n\t--------------------------------------------\n";
        result += "\tID              : " + id + "\n";
        result += "\tTitle           : " + title + "\n";
        result += "\tDescription     : " + description + "\n";
        result += "\tLocation        : " + location + "\n";
        result += "\tExperience Req  : " + experienceRequired + " years\n";
        result += String.format("\tSalary Range    : RM %.2f ~ RM %.2f\n", salaryRange[0], salaryRange[1]);
        result += "\tExperience Imp. : " + experienceImportance + "\n";
        result += "\tLocation Imp.   : " + locationImprotance + "\n";
        result += "\tSkill Imp.      : " + skillImportance + "\n";
        result += "\t--------------------------------------------\n";
        result += getStringSkillRequirement();
        return result;
    }


    /**
     * Formats required skills and their importance values as a table.
     * @return formats required skills and their importance values as a table.
     */
    public String getStringSkillRequirement() {
        String result = "\tSkill Requirements:\n";
        result += "\t+--------------------------------+------------+\n";
        result += "\t| Skill Name                     | Importance |\n";
        result += "\t+--------------------------------+------------+\n";
        for (int i = 0; i < requiredSkills.size(); i++) {
            result += String.format("\t| %-30s | %-10d |\n",
                    requiredSkills.get(i).getSkillName(),
                    requiredSkills.get(i).getImportance());
        }
        result += "\t+--------------------------------+------------+\n";
        return result;
    }


}

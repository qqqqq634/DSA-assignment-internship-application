package entity;

import adt.ArrayList;
import adt.ListInterface;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Student implements Serializable {
    /** Keeps the serialized class form compatible across application versions. */
    private static final long serialVersionUID = -6888026252506509874L;
    private String id;
    private String name;
    private ListInterface<SkillProficiency> skills = new ArrayList<>();
    private String location;
    private int experience;
    private static int COUNTER;
    /**
     * Reads serialized fields and restores the ID allocator from the stored identifier.
     * @param input the input value
     */
    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        restoreCounter(id, "S");
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


    // constructor
    /**
     * Allocates a new student ID and retains the supplied skills list by reference.
     * @param name the name value
     * @param skills the skills value
     * @param location the location value
     * @param experience the experience value
     */
    public Student(String name, ListInterface<SkillProficiency> skills, String location, int experience) {
        COUNTER++;
        this.id = "S" + COUNTER;
        this.name = name;
        this.skills = skills;
        this.location = location;
        this.experience = experience;
    }

    /**
     * Copies the student ID and fields without allocating an ID; the skills list is shared.
     * @param student the student value
     */
    public Student(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.skills = student.getSkillProficiencies();
        this.location = student.getLocation();
        this.experience = student.getExperience();
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
     * Sets the name.
     * @param name the name value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Replaces the skill list with the supplied reference.
     * @param skills the skills value
     */
    public void setSkills(ListInterface<SkillProficiency> skills) {
        this.skills = skills;
    }

    /**
     * Sets the location.
     * @param location the location value
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the experience.
     * @param experience the experience value
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    // Getters
    /**
     * Returns the id.
     * @return returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name.
     * @return returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the live skill list; changes to it affect this student.
     * @return returns the skill proficiencies.
     */
    public ListInterface<SkillProficiency> getSkillProficiencies() {
        return skills;
    }

    /**
     * Returns the location.
     * @return returns the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the experience.
     * @return returns the experience.
     */
    public int getExperience() {
        return experience;
    }

        /**
     * Formats this entity and its relevant details for display.
     * @return formats this entity and its relevant details for display.
     */

    @Override
    public String toString() {
        String studentInfo = "";

        // Basic profile details
        studentInfo += "ID        : " + id + "\n";
        studentInfo += "Name      : " + name + "\n";
        studentInfo += "Location  : " + location + "\n";
        studentInfo += "Experience: " + experience + " years\n";

        // Skill Section
        studentInfo += "\nSkills:\n";

        if (skills == null || skills.isEmpty()) {
            studentInfo += "  No Skill Proficiency\n";
        } else {
            studentInfo += "+-----+-------------------+-------------+\n";
            studentInfo += "| No. | Skill             | Proficiency |\n";
            studentInfo += "+-----+-------------------+-------------+\n";
            for (int i = 0; i < skills.size(); i++) {
                SkillProficiency sp = skills.get(i);
                studentInfo += String.format("| %-3d | %-17s | %-11d |\n",
                        i + 1, sp.getSkillName(), sp.getProficiency());
            }
            studentInfo += "+-----+-------------------+-------------+\n";
        }

        return studentInfo;
    }

}

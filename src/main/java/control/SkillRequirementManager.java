package control;

import adt.*;
import boundary.SkillRequirementUI;
import entity.SkillRequirement;
import utility.MessageUI;
import utility.EntitySorting;
import java.io.Serializable;

public class SkillRequirementManager implements Serializable {
    private ListInterface<SkillRequirement> skillRequirements = new ArrayList<>();
    private SkillRequirementUI skillRequirementUI = new SkillRequirementUI();

    private boolean sortAscending = false;

    /** Uses the supplied list as the current skill requirements. */
    public SkillRequirementManager(ListInterface<SkillRequirement> skillRequirements) {
        this.skillRequirements = skillRequirements;
    }

    /** Runs the requirements menu until choice 5; choice 6 changes this manager’s display sort direction. The overload first adopts the supplied list. */
    public void runSkillRequirement() {
        int choice = 0;
        do {
            choice = skillRequirementUI.getMenuChoice();
            switch (choice) {
                case 1:
                    addNewSkillRequirement();
                    skillRequirementUI.listAllSkillRequirements(getAllSkillRequirements());
                    break;
                case 2:
                    displaySkillRequirement();
                    break;
                case 3:
                    updateSkillRequirement();
                    displaySkillRequirement();
                    break;
                case 4:
                    deleteSkillRequirement();
                    displaySkillRequirement();
                    break;
                case 5:
                    MessageUI.displayExitMessageSkillRequirement();
                    break;
                case 6:
                    sortAscending = skillRequirementUI.inputSortAscending();
                    displaySkillRequirement();
                    break;
                default:
                    MessageUI.displayInvalidChoiceMessage();
            }
        } while (choice != 5);
    }

    /** Runs the requirements menu until choice 5; choice 6 changes this manager’s display sort direction. The overload first adopts the supplied list. */
    protected void runSkillRequirement(ListInterface<SkillRequirement> skillRequirements) {
        this.skillRequirements = skillRequirements;
        int choice = 0;
        do {
            choice = skillRequirementUI.getMenuChoice();
            switch (choice) {
                case 1:
                    addNewSkillRequirement();
                    skillRequirementUI.listAllSkillRequirements(getAllSkillRequirements());
                    break;
                case 2:
                    displaySkillRequirement();
                    break;
                case 3:
                    updateSkillRequirement();
                    displaySkillRequirement();
                    break;
                case 4:
                    deleteSkillRequirement();
                    displaySkillRequirement();
                    break;
                case 5:
                    MessageUI.displayExitMessageSkillRequirement();
                    break;
                case 6:
                    sortAscending = skillRequirementUI.inputSortAscending();
                    displaySkillRequirement();
                    break;
                default:
                    MessageUI.displayInvalidChoiceMessage();
            }
        } while (choice != 5);
    }

    /** Collects and adds a required skill and importance. */
    public void addNewSkillRequirement() {
        String name = skillRequirementUI.inputSkillRequirementName();
        int importance = skillRequirementUI.inputSkillRequirementImportance();
        SkillRequirement skillRequirement = new SkillRequirement(name, importance);
        skillRequirements.add(skillRequirement);
    }

    /** Replaces a valid one-based selection using zero-based set. */
    public void updateSkillRequirement() {
        int index = skillRequirementUI.inputSkillRequirementIndex();
        if (index > 0 && index <= skillRequirements.size()) {
            String name = skillRequirementUI.inputSkillRequirementName();
            int importance = skillRequirementUI.inputSkillRequirementImportance();
            SkillRequirement skillRequirement = new SkillRequirement(name, importance);
            skillRequirements.set(index - 1, skillRequirement);
        } else {
            MessageUI.displayInvalidChoiceMessage();
        }
    }

    /** Sorts the live list in the selected direction and formats numbered requirements. */
    public String getAllSkillRequirements() {
        EntitySorting.sortRequirements(skillRequirements, sortAscending);
        String allSkillRequirements = "";
        for (int i = 0; i < skillRequirements.size(); i++) {
            allSkillRequirements += "\t\t" + (i + 1) + ". " + skillRequirements.get(i).toString();
        }
        return allSkillRequirements;
    }

    /** Displays the sorted requirements. */
    public void displaySkillRequirement() {
        skillRequirementUI.listAllSkillRequirements(getAllSkillRequirements());
    }

    /** Removes a valid selection using the list’s one-based remove operation. */
    public void deleteSkillRequirement() {
        int index = skillRequirementUI.inputSkillRequirementIndex();
        if (index > 0 && index <= skillRequirements.size()) {
            skillRequirements.remove(index);
        } else {
            MessageUI.displayInvalidChoiceMessage();
        }
    }

    /** Copies references into a new list, clears the current list, and returns the copy. */
    protected ListInterface<SkillRequirement> getSkillRequirements() {
        ListInterface<SkillRequirement> skillRequirements = new ArrayList<>();
        for (int i = 0; i < this.skillRequirements.size(); i++) {
            skillRequirements.add(this.skillRequirements.get(i));
        }
        this.skillRequirements.clear();
        return skillRequirements;
    }
    // public static void main(String[] args) {
    // SkillRequirementManager skillRequirementManager = new
    // SkillRequirementManager();
    // skillRequirementManager.runSkillRequirement();
    // }
}

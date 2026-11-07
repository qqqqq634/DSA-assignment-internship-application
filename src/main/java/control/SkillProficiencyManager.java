package control;

import adt.*;
import boundary.SkillProficiencyUI;
import entity.SkillProficiency;
import utility.MessageUI;
import utility.EntitySorting;

public class SkillProficiencyManager {
        private ListInterface<SkillProficiency> skillProficiencys = new ArrayList<>();
    private SkillProficiencyUI skillProficiencyUI = new SkillProficiencyUI();

    private boolean sortAscending = false;

    /** Uses the supplied list as the current skill proficiencies. */
    public SkillProficiencyManager(ListInterface<SkillProficiency> skillProficiencys) {
        this.skillProficiencys = skillProficiencys;
    }

    // // remove this after testing
    // public SkillProficiencyManager() {

    // }

    /** Runs the skills menu until choice 5; choice 6 changes this manager’s display sort direction. The overload first adopts the supplied list. */
    public void runSkillProficiency() {
        int choice = 0;
        do {
            choice = skillProficiencyUI.getMenuChoice();
            switch (choice) {
                case 1:
                    addNewSkillProficiency();
                    skillProficiencyUI.listAllSkillProficiencys(getAllSkillProficiencys());
                    break;
                case 2:
                    displaySkillProficiency();
                    break;
                case 3:
                    updateSkillProficiency();
                    displaySkillProficiency();
                    break;
                case 4:
                    deleteSkillProficiency();
                    displaySkillProficiency();
                    break;
                case 5:
                    MessageUI.displayExitMessageSkillProficiency();
                    break;
                case 6:
                    sortAscending = skillProficiencyUI.inputSortAscending();
                    displaySkillProficiency();
                    break;
                default:
                    MessageUI.displayInvalidChoiceMessage();
            }
        } while (choice != 5);
    }

    /** Runs the skills menu until choice 5; choice 6 changes this manager’s display sort direction. The overload first adopts the supplied list. */
    protected void runSkillProficiency(ListInterface<SkillProficiency> skillProficiencys) {
        this.skillProficiencys = skillProficiencys;
        int choice = 0;
        do {
            choice = skillProficiencyUI.getMenuChoice();
            switch (choice) {
                case 1:
                    addNewSkillProficiency();
                    skillProficiencyUI.listAllSkillProficiencys(getAllSkillProficiencys());
                    break;
                case 2:
                    displaySkillProficiency();
                    break;
                case 3:
                    updateSkillProficiency();
                    displaySkillProficiency();
                    break;
                case 4:
                    deleteSkillProficiency();
                    displaySkillProficiency();
                    break;
                case 5:
                    MessageUI.displayExitMessageSkillProficiency();
                    break;
                case 6:
                    sortAscending = skillProficiencyUI.inputSortAscending();
                    displaySkillProficiency();
                    break;
                default:
                    MessageUI.displayInvalidChoiceMessage();
            }
        } while (choice != 5);
    }

    /** Collects and adds a skill and proficiency. */
    public void addNewSkillProficiency() {
        String name = skillProficiencyUI.inputSkillProficiencyName();
        int importance = skillProficiencyUI.inputSkillProficiencyImportance();
        SkillProficiency skillProficiency = new SkillProficiency(name, importance);
        skillProficiencys.add(skillProficiency);
    }

    /** Replaces a valid one-based selection using zero-based set. */
    public void updateSkillProficiency() {
        int index = skillProficiencyUI.inputSkillProficiencyIndex();
        if (index > 0 && index <= skillProficiencys.size()) {
            String name = skillProficiencyUI.inputSkillProficiencyName();
            int importance = skillProficiencyUI.inputSkillProficiencyImportance();
            SkillProficiency skillProficiency = new SkillProficiency(name, importance);
            skillProficiencys.set(index - 1, skillProficiency);
        } else {
            MessageUI.displayInvalidChoiceMessage();
        }
    }

    /** Sorts the live list in the selected direction and formats a numbered table. */
    public String getAllSkillProficiencys() {
        EntitySorting.sortProficiencies(skillProficiencys, sortAscending);
        String allSkillProficiencys = "";

        if (skillProficiencys.isEmpty()) {
            allSkillProficiencys += "\t\tNo Skill Proficiency\n";
        } else {
            allSkillProficiencys += "\t\t+-----+---------------+-------------+\n";
            allSkillProficiencys += "\t\t| No. |     Skill     | Proficiency |\n";
            allSkillProficiencys += "\t\t+-----+---------------+-------------+\n";
            for (int i = 0; i < skillProficiencys.size(); i++) {
                allSkillProficiencys += String.format("\t\t|%-4d %s\n", i + 1, skillProficiencys.get(i));
            }
        }
        return allSkillProficiencys;
    }

    /** Displays the sorted proficiency table. */
    public void displaySkillProficiency() {
        skillProficiencyUI.listAllSkillProficiencys(getAllSkillProficiencys());
    }

    /** Removes a valid selection using the list’s one-based remove operation. */
    public void deleteSkillProficiency() {
        int index = skillProficiencyUI.inputSkillProficiencyIndex();
        if (index > 0 && index <= skillProficiencys.size()) {
            skillProficiencys.remove(index);
        } else {
            MessageUI.displayInvalidChoiceMessage();
        }
    }

    /** Copies references into a new list, clears the current list, and returns the copy. */
    protected ListInterface<SkillProficiency> getSkillProficiencys() {
        ListInterface<SkillProficiency> skillProficiencys = new ArrayList<>();
        for (int i = 0; i < this.skillProficiencys.size(); i++) {
            skillProficiencys.add(this.skillProficiencys.get(i));
        }
        this.skillProficiencys.clear();
        return skillProficiencys;
    }

    /** Starts a standalone proficiency menu with an empty list. */
    public static void main(String[] args) {
        SkillProficiencyManager skillProficiencyManager = new SkillProficiencyManager(new ArrayList<>());
        skillProficiencyManager.runSkillProficiency();
    }

}

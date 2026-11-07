package boundary;

import java.util.InputMismatchException;
import java.util.Scanner;

import entity.SkillProficiency;

public class SkillProficiencyUI {
    private Scanner sc = new Scanner(System.in);

    /** Prints the skill proficiency actions. */
    private void skillProficiencyMenu() {
        System.out.println();
        System.out.println();
        System.out.println("---------------------");
        System.out.println("Skill Proficiency Menu");
        System.out.println("---------------------");
        System.out.println("1. Add Skill Proficiency");
        System.out.println("2. View Skill Proficiencys");
        System.out.println("3. Update Skill Proficiency");
        System.out.println("4. Delete Skill Proficiency");
        System.out.println("5. Return To Student Menu");
        System.out.println("6. Change sort order");
    }

    /** Prompts for sort direction and returns true for 1 (ascending) or false for 2 (descending). */
    public boolean inputSortAscending() {
        return SortUI.inputAscending(sc);
    }

    /** Displays the proficiency menu and returns a choice from 1 to 6; consumes the rest of the line. */
    public int getMenuChoice() {
        skillProficiencyMenu();
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                if (choice < 1 || choice > 6) {
                    System.out.println("Please enter a valid choice between 1 and 6.");
                } else {
                    flag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        sc.nextLine();
        return choice;
    }

    /** Reads a skill name and proficiency from 1 to 10, then returns a new SkillProficiency; leaves the numeric line terminator pending. */
    public SkillProficiency addSkillProficiency() {
        System.out.print("Enter Skill Proficiency Name:");
        String skillProficiencyName = sc.nextLine();
        int skillProficiencyImportance = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter Skill Proficiency:");
                skillProficiencyImportance = sc.nextInt();
                if (skillProficiencyImportance > 0 && skillProficiencyImportance <= 10) {
                    flag = false;
                } else {
                    System.out.println("Please enter a number between 0 and 10.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        return new SkillProficiency(skillProficiencyName, skillProficiencyImportance);
    }

    /** Reads and returns a proficiency from 1 to 10; leaves the numeric line terminator pending. */
    public int inputSkillProficiencyImportance() {
        int skillProficiencyImportance = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter Skill Proficiency :");
                skillProficiencyImportance = sc.nextInt();
                if (skillProficiencyImportance > 0 && skillProficiencyImportance <= 10) {
                    flag = false;
                } else {
                    System.out.println("Please enter a number between 0 and 10.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        return skillProficiencyImportance;
    }

    /** Prompts for and returns the skill name from the next line. */
    public String inputSkillProficiencyName() {
        System.out.print("Enter Skill Proficiency Name:");
        String skillProficiencyName = sc.nextLine();
        return skillProficiencyName;
    }

    /** Returns a positive displayed row number as a one-based selection and consumes the rest of the line. */
    public int inputSkillProficiencyIndex() {
        int index = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter the number of skill Proficiency to update:");
                index = sc.nextInt();
                if (index > 0) {
                    flag = false;
                } else {
                    System.out.println("Please enter a valid number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        sc.nextLine();
        return index;
    }

    /** Prints the proficiency list heading and supplied rows. */
    public void listAllSkillProficiencys(String allSkillProficiencys) {
        System.out.println("\t\t---------------------");
        System.out.println("\t\tSkill Proficiency List");
        System.out.println("\t\t---------------------");
        System.out.println(allSkillProficiencys);
    }

}

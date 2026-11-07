package boundary;

import java.util.InputMismatchException;
import java.util.Scanner;
import entity.SkillRequirement;

public class SkillRequirementUI {
    private Scanner sc = new Scanner(System.in);

    /** Prints the skill requirement actions. */
    private void skillRequirementMenu() {
        System.out.println();
        System.out.println();
        System.out.println("---------------------");
        System.out.println("Skill Requirement Menu");
        System.out.println("---------------------");
        System.out.println("1. Add Skill Requirement");
        System.out.println("2. View Skill Requirements");
        System.out.println("3. Update Skill Requirement");
        System.out.println("4. Delete Skill Requirement");
        System.out.println("5. Return To Job Posting Menu");
        System.out.println("6. Change sort order");
    }

    /** Prompts for sort direction and returns true for 1 (ascending) or false for 2 (descending). */
    public boolean inputSortAscending() {
        return SortUI.inputAscending(sc);
    }

    /** Displays the requirement menu and returns a choice from 1 to 6; consumes the rest of the line. */
    public int getMenuChoice(){
        skillRequirementMenu();
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

    /** Reads a requirement name and importance from 1 to 10, then returns a new SkillRequirement; leaves the numeric line terminator pending. */
    public SkillRequirement addSkillRequirement() {
        System.out.print("Enter Skill Requirement Name:");
        String skillRequirementName = sc.nextLine();
        int skillRequirementImportance =0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter Skill Requirement Importance:");
                skillRequirementImportance = sc.nextInt();
                if (skillRequirementImportance > 0 && skillRequirementImportance <= 10) {
                    flag = false;
                } else {
                    System.out.println("Please enter a number between 0 and 10.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        return new SkillRequirement(skillRequirementName, skillRequirementImportance);
    }

    /** Reads and returns an importance from 1 to 10; leaves the numeric line terminator pending. */
    public int inputSkillRequirementImportance() {
        int skillRequirementImportance =0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter Skill Requirement Importance:");
                skillRequirementImportance = sc.nextInt();
                if (skillRequirementImportance > 0 && skillRequirementImportance <= 10) {
                    flag = false;
                } else {
                    System.out.println("Please enter a number between 0 and 10.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        return skillRequirementImportance;
    }

    /** Prompts for and returns the requirement name from the next line. */
    public String inputSkillRequirementName() {
        System.out.print("Enter Skill Requirement Name:");
        String skillRequirementName = sc.nextLine();
        return skillRequirementName;
    }

    /** Returns a positive displayed row number as a one-based selection and consumes the rest of the line. */
    public int inputSkillRequirementIndex(){
        int index = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter the number of skill requirement to update:");
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

    /** Prints the requirement list heading and supplied rows. */
    public void listAllSkillRequirements(String allSkillRequirements){
        System.out.println("\t\t---------------------");
        System.out.println("\t\tSkill Requirement List");
        System.out.println("\t\t---------------------");
        System.out.println(allSkillRequirements);
    }




}
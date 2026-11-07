package boundary;

import java.util.InputMismatchException;
import java.util.Scanner;


public class MatchUI {
    private Scanner sc = new Scanner(System.in);

    /** Prints the job search and match actions. */
    private void matchMenu() {
        System.out.println();
        System.out.println();
        System.out.println("Find Job Menu");
        System.out.println("---------------------");
        System.out.println("1. Search For Jobs");
        System.out.println("2. View All Jobs");
        System.out.println("3. Return To Applicant Profile Menu");
        System.out.println("4. Change match score order");
        System.out.println("5. Change company/job display order");
    }

    /** Displays the find-job menu and returns a choice from 1 to 5; consumes the rest of the line. */
    public int matchMenuInput() {
        matchMenu();
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                if (choice < 1 || choice > 5) {
                    System.out.println("Please enter a valid number.");
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

    /** Prompts for sort direction and returns true for 1 (ascending) or false for 2 (descending). */
    public boolean inputSortAscending() {
        return SortUI.inputAscending(sc);
    }

    /** Prints the supplied match text. */
    public void displayMatch(String info){
        System.out.println(info);
    }

    /** Prompts for and returns the job title from the next line. */
    public String inputJobTitle(){
        System.out.print("Enter Job Title: ");
        String jobTitle = sc.nextLine();
        return jobTitle;
    }

    /** Returns the entered match number; -1 is shown as exit and -2 as change score order, and the rest of the line is consumed. */
    public int inputMatchIndex(){
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("Enter -1  to exit apply job.");
                System.out.println("Enter -2 to change score sort order.");
                System.out.print("Enter Match No To apply Job: ");
                choice = sc.nextInt();
                sc.nextLine();
                flag = false;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine(); // clear the invalid input
            }
        }
        return choice;
    }

    /** Prompts for Enter and consumes the next input line. */
    public void pressEnterToContinueMessage(){
        System.out.print("Press enter to continue.");
        sc.nextLine();
    }

}

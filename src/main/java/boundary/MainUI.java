package boundary;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainUI {
    
    private Scanner sc = new Scanner(System.in);

    /** Prints the application main menu. */
    private void mainMenu() {
        System.out.println("");
        System.out.println("");
        System.out.println("Internship Application System");
        System.out.println("1. Applicant");
        System.out.println("2. Company");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    /** Displays the main menu and returns the next integer without range validation; consumes the rest of the line. */
    public int mainMenuInput() {
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                mainMenu();
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
}
package boundary;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CompanyUI {

    private Scanner sc = new Scanner(System.in);

    /** Prints the company sign-in, sign-up, and return options. */
    private void companyMenuLogin() {
        System.out.println();
        System.out.println();
        System.out.println("---------------------");
        System.out.println("Company Menu");
        System.out.println("---------------------");
        System.out.println("1. Log In");
        System.out.println("2. Sign Up");
        System.out.println("3. Return To Main Menu");
    }

    /** Displays the login menu and returns the next integer without checking its range; consumes the rest of its line. */
    public int getMenuChoiceLogin() {
        companyMenuLogin();
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                flag = false;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
        sc.nextLine();
        return choice;
    }

    /** Prints the company profile actions. */
    private void companyMenu() {
        System.out.println();
        System.out.println();
        System.out.println("---------------------");
        System.out.println("Company Menu");
        System.out.println("---------------------");
        System.out.println("1. View Profile");
        System.out.println("2. Update Profile");
        System.out.println("3. Delete Profile");
        System.out.println("4. Manage Job Postings");
        System.out.println("5. Manage Interviews");
        System.out.println("6. Log out");

    }

    /** Prints the company profile update actions. */
    private void updateMenu() {
        System.out.println();
        System.out.println();
        System.out.println("---------------------");
        System.out.println("Update Menu");
        System.out.println("---------------------");
        System.out.println("1. Update Name");
        System.out.println("2. Update Location");
        System.out.println("3. Return To Company Profile Menu");
    }

    /** Displays the company menu and returns the next integer without range validation; consumes the rest of its line. */
    public int getMenuChoice() {
        companyMenu();
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                flag = false;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
        sc.nextLine();
        return choice;
    }

    /** Displays the update menu and returns the next integer without range validation; consumes the rest of its line. */
    public int getUpdateMenuChoice() {
        updateMenu();
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                flag = false;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
        sc.nextLine();
        return choice;
    }

    /** Prompts for company deletion confirmation and returns the entered line without interpreting it. */
    public String comfimationDelete() {
        System.out.print("Are you sure you want to delete this company profile? (y/n):");
        String choice = sc.nextLine();
        return choice;
    }

    /** Prompts for and returns the company name from the next line. */
    public String inputCompanyName() {
        System.out.print("Enter company name: ");
        String name = sc.nextLine();
        return name;
    }

    /** Prompts for and returns the company location from the next line. */
    public String inputCompanyLocation() {
        System.out.print("Enter company location: ");
        String location = sc.nextLine();
        return location;
    }

    /** Prompts for and returns the company ID from the next line. */
    public String inputCompanyId() {
        System.out.print("Enter company id: ");
        String id = sc.nextLine();
        return id;
    }

    /** Prompts for Enter and consumes the next input line. */
    public void displayEnterToContinueMessage() {
        System.out.println("Press enter to continue.");
        sc.nextLine();
    }

    /** Prints company table headings and the supplied rows. */
    public void listCompanyInfo(String info) {
        String header = "";
        header = "+----+----------+----------+-----------------------+\n" +
                "| ID | Name     | Location | Job Postings          |\n" +
                "+----+----------+----------+-----------------------+";
        System.out.println(header);
        System.out.println(info);
    }

}

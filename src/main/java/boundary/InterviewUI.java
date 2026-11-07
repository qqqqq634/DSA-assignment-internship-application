package boundary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 *
 * @author Teoh Yu Xiang
 */

public class InterviewUI {

    private Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /** Prints the company interview actions. */
    private void displayInterviewMenu() {
        System.out.println("Interview Menu");
        System.out.println("---------------");
        System.out.println("1. Shedule Interview for a Job Posting");
        System.out.println("2. Return To Company Menu");
        System.out.println("3. Change interview date order");
    }

    /** Displays the company interview menu and returns a choice from 1 to 3; invalid tokens are discarded by line. */
    public int displayInterviewMenuOptions() {
        displayInterviewMenu();
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Please enter a valid choice between 1 and 3.");
                } else {
                    flag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // clear the invalid input
            }
        }
        return choice;
    }

    /** Prints the student interview actions. */
    private void displayInterviewMenuStudent(){
        System.out.println("");
        System.out.println("");
        System.out.println("Interview Menu");
        System.out.println("---------------");
        System.out.println("1. View Interviews");
        System.out.println("2. Return To Student Menu");
        System.out.println("3. Change interview date order");
    }

    /** Displays the student interview menu and returns a choice from 1 to 3; invalid tokens are discarded by line. */
    public int displayInterviewMenuStudentOptions() {
        displayInterviewMenuStudent();
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Please enter a valid choice between 1 and 3.");
                } else {
                    flag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // clear the invalid input
            }
        }
        return choice;
    }


    /** Prompts for sort direction and returns true for 1 (ascending) or false for 2 (descending). */
    public boolean inputSortAscending() {
        return SortUI.inputAscending(scanner);
    }

    /** Prints the supplied interview text. */
    public void displayInterview(String info) {
        System.out.println(info);
    }

    /** Prints job rows and returns the entered integer selection; 0 is shown as the exit choice and the rest of the line is consumed. */
    public int displayJobs(String info) {
        // see wan add header anot
        System.out.println(info);
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("Enter 0 to exit.");
                System.out.print("Enter number of job to schedule interview: ");
                choice = scanner.nextInt();
                flag = false;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // clear the invalid input
            }
        }
        scanner.nextLine();
        return choice;
    }

    /** Reads yyyy-MM-dd HH:mm and repeats until parsing succeeds and the time is not before now. */
    public LocalDateTime getInterviewDateAndTime() {
        // get date and time
        LocalDateTime dateTime = null;
        while (dateTime == null) {
            System.out.print("Enter interview date and time (yyyy-MM-dd HH:mm): ");
            String dateTimeStr = scanner.nextLine().trim();
            try {
                dateTime = LocalDateTime.parse(dateTimeStr, dateFormatter);
                if (dateTime.isBefore(LocalDateTime.now())) {
                    System.out.println("Cannot schedule interviews in the past. Please enter a future date and time.");
                    dateTime = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date/time format. Please use yyyy-MM-dd HH:mm format.");
            }
        }
        return dateTime;
    }

    /** Returns 1 for reject or 2 for accept, repeating until one of those choices is entered. */
    public int acceptApplicant() {
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("1. Reject Applicant");
                System.out.println("2. Accept");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                if (choice < 1 || choice > 2) {
                    System.out.println("Please enter a valid choice between 1 and 2.");
                } else {
                    flag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // clear the invalid input
            }
        }
        return choice;
    }

    /** Returns the entered interview number; 0 is shown as exit, but other values are not range-checked. */
    public int inputSelectedInterview(){
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("Enter 0 to exit.");
                System.out.print("Enter the number of interview: ");
                choice = scanner.nextInt();
                flag = false;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // clear the invalid input
            }
        }
        return choice;
    }

    /** Returns 1 for yes or 2 for no, repeating until one of those choices is entered. */
    public int inputInterviewComing(){
        int choice = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("Are you coming to the interview?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                if (choice < 1 || choice > 2) {
                    System.out.println("Please enter a valid choice between 1 and 2.");
                } else {
                    flag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // clear the invalid input
            }
        }
        return choice;
    }
}

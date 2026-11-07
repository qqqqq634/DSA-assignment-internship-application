package boundary;

import control.JobManager;
import entity.JobPosting;
import adt.ListInterface;
import java.util.InputMismatchException;
import java.util.Scanner;

public class JobPostingUI {
    private Scanner sc = new Scanner(System.in);

    /** Prints the job posting actions. */
    private void jobPostingMenu() {
        System.out.println();
        System.out.println();
        System.out.println("---------------------");
        System.out.println("Job Posting Menu");
        System.out.println("---------------------");
        System.out.println("1. Add Job Posting");
        System.out.println("2. View Job Postings");
        System.out.println("3. Update Job Posting");
        System.out.println("4. Delete Job Posting");
        System.out.println("5. Return To Company Menu");
        System.out.println("6. Change sort order");
    }

    /** Prompts for sort direction and returns true for 1 (ascending) or false for 2 (descending). */
    public boolean inputSortAscending() {
        return SortUI.inputAscending(sc);
    }

    /** Displays the job posting menu and returns a choice from 1 to 6; consumes the rest of the line. */
    public int getMenuChoice() {
        jobPostingMenu();
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

    /** Prompts for and returns the title from the next line. */
    public String inputJobPostingTitle() {
        System.out.print("Enter Job Posting Title: ");
        String jobPostingTitle = sc.nextLine();
        return jobPostingTitle;
    }

    /** Prompts for and returns the description from the next line. */
    public String inputJobPostingDescription() {
        System.out.print("Enter Job Posting Description: ");
        String jobPostingDescription = sc.nextLine();
        return jobPostingDescription;
    }

    /** Reads a non-negative integer, consumes the rest of its line, and returns it. */
    public int inputJobPostingExperienceRequired() {
        int jobPostingExperienceRequired = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter Job Posting Experience Years Required: ");
                jobPostingExperienceRequired = sc.nextInt();
                if (jobPostingExperienceRequired < 0) {
                    System.out.println("Please enter a non-negative number.");
                } else {
                    flag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        sc.nextLine();
        return jobPostingExperienceRequired;
    }

    /** Prompts for and returns the location from the next line. */
    public String inputJobPostingLocation() {
        System.out.print("Enter Job Posting Location: ");
        String jobPostingLocation = sc.nextLine();
        return jobPostingLocation;
    }

    /** Reads minimum and maximum values until maximum is at least minimum, then returns them in a two-element array in that order; consumes the rest of the line. */
    public double[] inputJobPostingSalary() {
        double jobPostingSalary = 0;
        double jobPostingMaxSalary = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter Job Posting minimum salary: $");
                jobPostingSalary = sc.nextDouble();
                System.out.print("Enter Job Posting maximum salary: $");
                jobPostingMaxSalary = sc.nextDouble();
                if (jobPostingMaxSalary < jobPostingSalary) {
                    System.out.println("Please enter a valid number. Minimum salary must be more than maximum salary.");
                } else {
                    flag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        sc.nextLine();// clear buffer
        return new double[] { jobPostingSalary, jobPostingMaxSalary };
    }

    /** Reads a value from 1 to 10, consumes the rest of the line, and returns it. */
    public int inputJobPostingExperienceImportance() {
        int jobPostingExperienceRequired = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter Job Posting Experience Importance Score weight: ");
                jobPostingExperienceRequired = sc.nextInt();
                if (jobPostingExperienceRequired < 1 || jobPostingExperienceRequired > 10) {
                    System.out.println("Please enter a valid number between 1 and 10.");
                } else {
                    flag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        sc.nextLine();
        return jobPostingExperienceRequired;
    }

    /** Reads a value from 1 to 10, consumes the rest of the line, and returns it. */
    public int inputJobPostingLocationImportance() {
        int jobPostingLocationImportance = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter Job Posting Location Importance: ");
                jobPostingLocationImportance = sc.nextInt();
                if (jobPostingLocationImportance < 1 || jobPostingLocationImportance > 10) {
                    System.out.println("Please enter a valid number between 1 and 10.");
                } else {
                    flag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        sc.nextLine();
        return jobPostingLocationImportance;
    }

    /** Reads a value from 1 to 10, consumes the rest of the line, and returns it. */
    public int inputJobPostingSkillImportance() {
        int jobPostingSkillImportance = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter Job Posting Skill Importance: ");
                jobPostingSkillImportance = sc.nextInt();
                if (jobPostingSkillImportance < 1 || jobPostingSkillImportance > 10) {
                    System.out.println("Please enter a valid number between 1 and 10.");
                } else {
                    flag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // clear the invalid input
            }
        }
        sc.nextLine();
        return jobPostingSkillImportance;
    }

    /** Prints job posting table headings and the supplied rows. */
    public void listAllJobPosting(String allJobPosting) {
        System.out.println("\t---------------------");
        System.out.println("\tJob Posting List");
        System.out.println("\t---------------------");
        System.out.println(
                "\t+----+----------+----------------------+------------------+---------------+---------------------+--------+--------+--------+----------+\n"
                        +
                        "\t| ID | Title    | Description          | Location         | Exp. Required | Salary Range        | ExpImp | LocImp | SkillImp| Skills   |\n"
                        +
                        "\t+----+----------+----------------------+------------------+---------------+---------------------+--------+--------+---------+----------+\n");
        System.out.println(allJobPosting);
    }

    /** Returns a positive displayed row number as a one-based selection and consumes the rest of the line. */
    public int inputJobPostingIndex() {
        int jobPostingIndex = 0;
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Enter Job Posting number based on the aligned table: ");
                jobPostingIndex = sc.nextInt();
                if (jobPostingIndex < 1) {
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
        return jobPostingIndex;
    }

}

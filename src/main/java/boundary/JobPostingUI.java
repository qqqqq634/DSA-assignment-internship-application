package boundary;

import control.JobManager;
import doa.JobPostingInitializer;
import utility.SearchUtil;

import java.util.InputMismatchException;
import java.util.Scanner;

public class JobPostingUI {
    private JobManager jobManager = new JobManager();
    private Scanner input = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("\n\n\nJobPosting Menu:");
        System.out.println("1. Add JobPosting");
        System.out.println("2. Remove JobPosting");
        System.out.println("3. Update JobPosting");
        System.out.println("4. List All JobPostings");
        System.out.println("5. Search JobPostings");
        System.out.println("6. Exit");
    }

    public void run() {
        int choice;
        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    addJobPosting();
                    break;
                case 2:
                    removeJobPosting();
                    break;
                case 3:
                    updateJobPosting();
                    break;
                case 4:
                    listAllJobPostings();
                    break;
                case 5:
                    searchJobPostingByTitle();
                    break;
                case 6:
                    System.out.println("Exiting JobPosting!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    input.nextLine();
            }
        } while (choice != 6);

    }

    public JobPostingUI() {
        JobPostingInitializer.initialize(jobManager); 
    }

    public static void main(String[] args) {
        JobPostingUI jobPostingUI = new JobPostingUI();
        jobPostingUI.run();
    }

    private String getInputJobTittle() {
        System.out.print("Enter job title: ");
        String title = input.nextLine();
        return title;
    }

    private String getInputJobDescription() {
        System.out.print("Enter job description: ");
        String description = input.nextLine();
        return description;
    }

    private String getInputJobLocation() {
        System.out.print("Enter job location: ");
        String location = input.nextLine();
        return location;
    }

    private double[] getInputJobSalaryRange() {
        boolean isValid = true;
        double[] salaryRange = new double[2];
        do {

            System.out.print("Enter job salary range (separated by space): ");

            for (int i = 0; i < 2; i++) {
                boolean isNumberNValidRange = true;
                while (isNumberNValidRange) {
                    try {
                        salaryRange[i] = Double.parseDouble(input.next());
                        if (salaryRange[i] < 0) {
                            throw new NumberFormatException();
                        }
                        isNumberNValidRange = false;
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid input. Please enter a valid non-negative number: ");
                        input.nextLine(); // consume newline left-over
                    }

                }
            }

            if (salaryRange[0] > salaryRange[1]) {
                System.out.println("Invalid input. Min salary cannot be greater than max salary.");

            } else {
                isValid = false;
            }

        } while (isValid);

        return salaryRange;
    }

    private void getInputSkillRequirement() {
        boolean isValid = true;
        int numSkills = 0;
        do {
            try {
                System.out.print("Enter how many skills are required: ");
                numSkills = input.nextInt();
                if (numSkills <= 0) {
                    System.out.println("Invalid input. Please enter a positive integer: ");
                } else {
                    isValid = false;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer: ");
                input.nextLine(); // consume invalid input
            }
            input.nextLine(); // consume newline left-over
            for (int i = 0; i < numSkills; i++) {
                System.out.print("Enter skill " + (i + 1) + " name : ");
                String skill = input.nextLine();
                System.out.print("Enter importance of skill " + (i + 1) + " (1-10): ");
                int importance = 0;
                boolean isValidImportance = false;
                do {
                    try {
                        importance = input.nextInt();
                        if (importance < 1 || importance > 10) {
                            System.out.println("Invalid input. Please enter an integer between 1 and 10: ");
                        } else {
                            isValidImportance = false;
                        }
                    } catch (InputMismatchException e) {
                        System.out.print("Invalid input. Please enter an integer between 1 and 10: ");
                        
                    }
                } while (isValidImportance);
                input.nextLine(); // consume newline left-over
                jobManager.addSkillRequirement(skill, importance);
            }
        } while (isValid);
    }

    public void addJobPosting() {
        input.nextLine();// clear buffer
        String title, description, location;
        double[] salaryRange = new double[2];

        title = getInputJobTittle();

        description = getInputJobDescription();

        location = getInputJobLocation();

        salaryRange = getInputJobSalaryRange();

        getInputSkillRequirement();

        jobManager.addJobPosting(title, description, location, salaryRange);

        System.out.println("JobPosting added successfully!");

    }

    public void removeJobPosting() {

        // clear buffer
        input.nextLine();
        // Enter the ID of the job posting to remove
        System.out.print("Enter the ID of the job posting to remove: ");
        String jobIdToRemove = input.nextLine();

        // Remove the job posting with the specified ID
        boolean removed = jobManager.removeJobPosting(jobIdToRemove);

        if (removed) {
            System.out.println("JobPosting removed successfully!");
        } else {
            System.out.println("JobPosting not found!");
        }

    }

    public void updateJobPosting() {
        input.nextLine(); // clear buffer
        // enter the ID of the job posting to update
        System.out.print("Enter the ID of the job posting to update: ");
        String jobIdToUpdate = input.nextLine();

        // update the job posting with the specified ID
        boolean exsists = jobManager.containsJobPosting(jobIdToUpdate);
        if (exsists) {
            updateJob(jobIdToUpdate);
        } else {
            System.out.println("JobPosting not found!");
        }

    }

    private void updateJobMenu() {
        System.out.println("\n\n\nUpdate Job Menu:");
        System.out.println("1. Update Job Title");
        System.out.println("2. Update Job Description");
        System.out.println("3. Update Job Location");
        System.out.println("4. Update Job Salary Range");
        System.out.println("5. Update Job Skill Requirements");
        System.out.println("6. Exit");
    }

    private void updateJob(String jobIdToUpdate) {
        String title, description, location;
        double[] salaryRange = new double[2];
        int choice = 0;
        do {
            updateJobMenu();
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            input.nextLine();// clear buffer
            switch (choice) {
                case 1:
                    title = getInputJobTittle();
                    jobManager.setJobPostingTitle(jobIdToUpdate, title);
                    break;
                case 2:
                    description = getInputJobDescription();
                    jobManager.setJobPostingDescription(jobIdToUpdate, description);
                    break;
                case 3:
                    location = getInputJobLocation();
                    jobManager.setJobPostingLocation(jobIdToUpdate, location);
                    break;
                case 4:
                    salaryRange = getInputJobSalaryRange();
                    jobManager.setJobPostingSalaryRange(jobIdToUpdate, salaryRange);
                    break;
                case 5:
                    jobManager.clearSkillRequirements();
                    getInputSkillRequirement();
                    jobManager.setJobPostingRequiredSkills(jobIdToUpdate, jobManager.getSkillRequirements());
                    break;
                case 6:
                    break;
                
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 6);

    }

    public void listAllJobPostings() {
        jobManager.listAllJobPostings();
    }


    private void searchJobPostingByTitle() {
        input.nextLine(); 
        System.out.print("Enter title to search for: ");
        String query = input.nextLine().toLowerCase();
    
        System.out.print("Enter fuzzy threshold (e.g. 2): ");
        int threshold = input.nextInt();
        input.nextLine(); 
    
        boolean found = false;
        for (var job : jobManager.getJobPostings()) {
            String jobTitle = job.getTitle().toLowerCase();
            String[] words = jobTitle.split("\\s+"); 
    
            for (String word : words) {
                if (SearchUtil.fuzzySearch(query, word, threshold)) {
                    System.out.println("\nMatched Job Posting:");
                    System.out.println(job);
                    found = true;
                    break; 
                }
            }
        }
    
        if (!found) {
            System.out.println("No job postings matched your query.");
        }
    }
    
    
    // getter
    public JobManager getJobManager() {
        return jobManager;
    }

}

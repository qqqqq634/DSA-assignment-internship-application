package boundary;

import java.util.InputMismatchException;
import java.util.Scanner;
import control.ApplicantManager;


public class ApplicantProfileUI {
    private ApplicantManager applicantManager = new ApplicantManager();
    private Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        ApplicantProfileUI applicantProfileUI = new ApplicantProfileUI();
        applicantProfileUI.run();
    }

    public void displayMenu() {
        System.out.println("\n\n\nApplicant Profile Management:");
        System.out.println("1. Create Applicant Profile");
        System.out.println("2. View Applicant Profile");
        System.out.println("3. Update Applicant Profile");
        System.out.println("4. Delete Applicant Profile");
        System.out.println("5. Filter Applicant Profiles");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    public void run() {
        // TODO: Implement applicant profile management logic
        int choice;
        do {
            displayMenu();
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    createApplicantProfile();
                    break;
                case 2:
                    viewApplicantProfile();
                    break;
                case 3:
                    updateApplicantProfile();
                    break;
                case 4:
                    deleteApplicantProfile();
                    break;
                case 5:
                    filterApplicantProfiles();
                    break;
                case 6:
                    System.out.println("Exiting Applicant Profile Management!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    public void viewApplicantProfile() {
        // TODO: Implement applicant profile viewing logic
        //clear buffer
        input.nextLine();
        // Enter student ID to view
        System.out.print("Enter the ID of the student to view: ");
        String studentId = input.nextLine();
        // View the student with the specified ID
        String student = applicantManager.listSpecificApplicants(studentId);
        if (student != null) {
            System.out.println("Applicant Profile:");
            System.out.println(student);
        } else {
            System.out.println("Student not found!");
        }
    }

    public String getInputStudentName() {
        System.out.print("Enter student name: ");
        return input.nextLine();
    }

    public String getInputStudentLocation() {
        System.out.print("Enter student location: ");
        return input.nextLine();
    }

    public int getInputStudentExperience() {
        int experience =0;
        boolean isValid = true;
        while (isValid) {
            System.out.print("Enter student working year experience: ");
            try {
                experience = input.nextInt();
                if (experience < 0) {
                    System.out.println("Experience must be 0 or above.");
                } else {
                    isValid = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Experience years of working must be a number.");
            }
            input.nextLine(); // clear buffer
        }
        return experience;
    }

    public void getInputSkills() {
        boolean isValid = true;
        int numSkills = 0;
        do {
            try {
                System.out.print("Enter how many skills you are proficient in: ");
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
                System.out.print("Enter proficiency of skill " + (i + 1) + " (1-10): ");
                int experience = 0;
                boolean isValidImportance = true;
                do {
                    try {
                        experience = input.nextInt();
                        if (experience < 1 || experience > 10) {
                            System.out.print("Invalid input. Please enter an integer between 1 and 10: ");
                        } else {
                            isValidImportance = false;
                        }
                    } catch (InputMismatchException e) {
                        System.out.print("Invalid input. Please enter an integer between 1 and 10: ");
                        input.nextLine(); // consume invalid input
                    }
                } while (isValidImportance);
                input.nextLine(); // consume newline left-over
                applicantManager.addSkillProficiency(skill, experience);
            }
        } while (isValid);
    }
    public void createApplicantProfile() {
        
        String name, location;
        int experience;
        input.nextLine(); // clear buffer

        name = getInputStudentName();
        location = getInputStudentLocation();
        experience = getInputStudentExperience();
        getInputSkills();
        applicantManager.registerStudent(name,  location, experience);

        System.out.println("Applicant profile created successfully!");
        

        
    }

    private void updateAppMenu() {
        System.out.println("\n\n\nUpdate Applicant Profile Menu:");
        System.out.println("1. Update Student Name");
        System.out.println("2. Update Student Location");
        System.out.println("3. Update Student Working Year Experience");
        System.out.println("4. Update Student Skills");
        System.out.println("5. Exit");
    }

    private void updateApp(String id) {
        
        updateAppMenu();
        input.nextLine(); // clear buffer
        int choice = input.nextInt();
        switch (choice) {
            case 1:
                String name = getInputStudentName();
                applicantManager.updateStudentName(id, name);
                break;
            case 2:
                String location = getInputStudentLocation();
                applicantManager.updateStudentLocation(id, location);
                break;
            case 3:
                int experience = getInputStudentExperience();
                applicantManager.updateStudentExperience(id, experience);
                break;
            case 4:
                getInputSkills();
                applicantManager.updateStudentSkills(id, applicantManager.getSkillProficiencies());
                applicantManager.clearSkillProficiencies();
                break;
            case 5:
                System.out.println("Exiting update menu.");

        }
    }

    public void updateApplicantProfile() {
        
        // Enter student ID to update
        System.out.print("Enter the ID of the student to update: ");
        String id = input.nextLine();
        // update the student with the specified ID
        boolean exists = applicantManager.getStudentById(id);

        if (exists) {
            
            System.out.println("Updating student with ID: " + id);
            updateApp(id);
        } else {
            System.out.println("Student not found!");
        }
        
    }

    public void deleteApplicantProfile() {
        // Enter student ID to delete
        input.nextLine(); // clear buffer
        System.out.print("Enter the ID of the student to delete: ");
        String id = input.nextLine();
        boolean deleted = applicantManager.removeStudentById(id);
        if (deleted) {
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student not found!");
        }
    }

    public void filterApplicantProfiles() {
        int choice;
        do {
            System.out.println("\nFilter Applicant Profiles By:");
            System.out.println("1. Search Name");
            System.out.println("2. Search Location");
            System.out.println("3. Minimum Years of Experience");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            
            choice = input.nextInt();
            input.nextLine(); // clear buffer
    
            switch (choice) {
                case 1:
                    System.out.print("Enter name to search: ");
                    String name = input.nextLine();
                    System.out.println(applicantManager.searchByName(name));
                    break;
                case 2:
                    System.out.print("Enter location to search: ");
                    String location = input.nextLine();
                    System.out.println(applicantManager.searchByLocation(location));
                    break;
                case 3:
                    System.out.print("Enter minimum years of experience: ");
                    int minExp = input.nextInt();
                    input.nextLine(); // clear buffer
                    System.out.println(applicantManager.filterByExperience(minExp));
                    break;
                case 4:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }
    

}

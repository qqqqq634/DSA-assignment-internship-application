package utility;

public class MessageUI {
    /** Prints a retry message for an unsupported menu choice. */
    public static void displayInvalidChoiceMessage() {
        System.out.println("Invalid choice. Please try again.");
    }

    /** Prints that the required-skill menu is closing. */
    public static void displayExitMessageSkillRequirement() {
        System.out.println("Exiting the Skill Requirement Management System.");
    }

    /** Prints that the proficiency menu is closing. */
    public static void displayExitMessageSkillProficiency() {
        System.out.println("Exiting the Skill Proficiency Management System.");
    }

    /** Prints that required-skill management is starting. */
    public static void displayEnteringMessageSkillRequirement() {
        System.out.println("Entering the Skill Requirement Management System.");
    }

    /** Prints that proficiency management is starting. */
    public static void displayEnteringMessageSkillProficiency() {
        System.out.println("Entering the Skill Proficiency Management System.");
    }

    /** Prints a logout message; the caller controls the actual session flow. */
    public static void displayLogOutMessage() {
        System.out.println("Logging out.");
    }

    /** Prints confirmation after the caller removes an applicant account. */
    public static void displayDeleteApplicantAccountMessage() {
        System.out.println("Applicant account deleted.");
    }

    /** Prints confirmation after the caller removes a company account. */
    public static void displayDeleteCompanyAccountMessage() {
        System.out.println("Company account deleted.");
    }

    /** Prints that the supplied company ID was not found. */
    public static void displayCompanyInfoNotFoundMessage() {
        System.out.println("Company Id not found.");
    }

    /** Prints that the applicant job-search menu is closing. */
    public static void displayExitMessageMatch() {
        System.out.println("Exiting the Match Management System.");
    }

    /** Prints that the interview menu is closing. */
    public static void displayExitingMessageInterviewScheduler() {
        System.out.println("Exiting the Interview Scheduler.");
    }

    /** Prints confirmation after a new application is added to the in-memory list. */
    public static void displayJobApplicationSuccessMessage() {
        System.out.println("Job application submitted successfully!");
    }

    /** Prints that job search cannot start without a student; it does not throw an exception. */
    public static void displayErrorMessageStudentNull() {
        System.out.println("ERROR: Cannot run job search with null student");
    }

    /** Prints the name of the applicant whose job-search session is starting. */
    public static void displayMessageRunningJobSearchStudent(String studentName) {
        System.out.println("Running job search for student: " + studentName);
    }

    /** Prints that the current search produced no candidate jobs. */
    public static void displayNoJobsFoundYourCriteria() {
        System.out.println("No jobs found matching your criteria.");
    }

    /** Prints the entered job title when automatic title matching begins. */
    public static void displayStartingJobSearch(String jobTitle) {
        System.out.println("Starting job search for job title: " + jobTitle);
    }

    /** Prints that the search currently lacks a company manager; recovery is handled by the caller. */
    public static void displayErrorMessageCompanyManagerNull() {
        System.out.println("ERROR: Cannot run job search with null companyManager");
    }
}

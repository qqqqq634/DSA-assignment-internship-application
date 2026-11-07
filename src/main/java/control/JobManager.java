package control;

import adt.*;
import boundary.JobPostingUI;
import entity.JobPosting;
import utility.MessageUI;
import utility.EntitySorting;
import utility.SearchUtil;
import entity.SkillRequirement;
import java.io.Serializable;

public class JobManager implements Serializable {
    private ListInterface<JobPosting> jobPostings = new ArrayList<>();
    private JobPostingUI jobPostingUI = new JobPostingUI();
    private SkillRequirementManager skillRequirementManager = new SkillRequirementManager(new ArrayList<>());

    private boolean sortAscending = true;

    /** Uses the supplied list as the current job postings. */
    public JobManager(ListInterface<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
    }

    /** Runs the job menu until choice 5; choice 6 changes this manager’s display sort direction. The overload first adopts the supplied list. */
    public void runJobPosting() {
        int choice = 0;
        do {
            choice = jobPostingUI.getMenuChoice();
            switch (choice) {
                case 1:
                    addJobPosting();
                    break;
                case 2:
                    displayAllJobPosting();
                    break;
                case 3:
                    updateJobPosting();
                    break;
                case 4:
                    deleteJobPosting();
                    break;
                case 5:
                    return;
                case 6:
                    sortAscending = jobPostingUI.inputSortAscending();
                    displayAllJobPosting();
                    break;
                default:
                    MessageUI.displayInvalidChoiceMessage();
            }
        } while (choice != 5);
    }

    /** Runs the job menu until choice 5; choice 6 changes this manager’s display sort direction. The overload first adopts the supplied list. */
    protected void runJobPosting(ListInterface<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
        int choice = 0;
        do {
            choice = jobPostingUI.getMenuChoice();
            switch (choice) {
                case 1:
                    addJobPosting();
                    break;
                case 2:
                    displayAllJobPosting();
                    break;
                case 3:
                    updateJobPosting();
                    break;
                case 4:
                    deleteJobPosting();
                    break;
                case 5:
                    return;
                case 6:
                    sortAscending = jobPostingUI.inputSortAscending();
                    displayAllJobPosting();
                    break;
                default:
                    MessageUI.displayInvalidChoiceMessage();
            }
        } while (choice != 5);
    }

    /** Collects job details and skill requirements, adds a posting, and displays the sorted list. */
    public void addJobPosting() {
        String jobPostingTitle = jobPostingUI.inputJobPostingTitle();// got
        String jobPostingDescription = jobPostingUI.inputJobPostingDescription();// got
        MessageUI.displayEnteringMessageSkillRequirement();
        skillRequirementManager.runSkillRequirement();
        ListInterface<SkillRequirement> skillRequirements = skillRequirementManager.getSkillRequirements();// got
        String jobPostingLocation = jobPostingUI.inputJobPostingLocation();// got
        double[] jobPostingSalary = jobPostingUI.inputJobPostingSalary();// got
        int jobPostingExperienceRequired = jobPostingUI.inputJobPostingExperienceRequired();
        int jobPostingExperienceImportance = jobPostingUI.inputJobPostingExperienceImportance();
        int jobPostingLocationImportance = jobPostingUI.inputJobPostingLocationImportance();
        int jobPostingSkillImportance = jobPostingUI.inputJobPostingSkillImportance();
        JobPosting jobPosting = new JobPosting(
                jobPostingTitle,
                jobPostingDescription,
                skillRequirements, jobPostingLocation, jobPostingSalary, jobPostingExperienceRequired,
                jobPostingLocationImportance, jobPostingExperienceImportance, jobPostingSkillImportance);
        jobPostings.add(jobPosting);

        displayAllJobPosting();
    }

    /** Displays the current postings after sorting them. */
    public void displayAllJobPosting() {
        jobPostingUI.listAllJobPosting(getAllJobPosting());
    }

    /** Copies posting references into a new list, clears the current list, and returns the copy. */
    public ListInterface<JobPosting> getJobPostings() {
        ListInterface<JobPosting> jobPostings = new ArrayList<>();
        for (int i = 0; i < this.jobPostings.size(); i++) {
            jobPostings.add(this.jobPostings.get(i));
        }

        this.jobPostings.clear();
        return jobPostings;
    }

    /** Sorts the live list in the selected direction and formats its postings. */
    private String getAllJobPosting() {
        EntitySorting.sortJobs(jobPostings, sortAscending);
        String allJobPosting = "";
        for (JobPosting jobPosting : jobPostings) {
            allJobPosting += "\t" + jobPosting.toString();
        }
        return allJobPosting;
    }

    /** Removes a valid one-based selection and displays the remaining jobs. */
    public void deleteJobPosting() {
        int index = jobPostingUI.inputJobPostingIndex();
        if (index > 0 && index <= jobPostings.size()) {
            jobPostings.remove(index);
        } else {
            MessageUI.displayInvalidChoiceMessage();
        }
        displayAllJobPosting();
    }

    /** Updates the selected existing posting in place, preserving its identity; the one-based choice is converted for zero-based access. */
    public void updateJobPosting() {
        int index = jobPostingUI.inputJobPostingIndex();
        if (index > 0 && index <= jobPostings.size()) {
            String jobPostingTitle = jobPostingUI.inputJobPostingTitle();// got
            String jobPostingDescription = jobPostingUI.inputJobPostingDescription();// got
            MessageUI.displayEnteringMessageSkillRequirement();
            skillRequirementManager.runSkillRequirement(jobPostings.get(index - 1).getRequiredSkills());
            ListInterface<SkillRequirement> skillRequirements = skillRequirementManager.getSkillRequirements();// got
            String jobPostingLocation = jobPostingUI.inputJobPostingLocation();// got
            double[] jobPostingSalary = jobPostingUI.inputJobPostingSalary();// got
            int jobPostingExperienceRequired = jobPostingUI.inputJobPostingExperienceRequired();
            int jobPostingExperienceImportance = jobPostingUI.inputJobPostingExperienceImportance();
            int jobPostingLocationImportance = jobPostingUI.inputJobPostingLocationImportance();
            int jobPostingSkillImportance = jobPostingUI.inputJobPostingSkillImportance();
            JobPosting jobPosting = jobPostings.get(index - 1);
            jobPosting.setTitle(jobPostingTitle);
            jobPosting.setDescription(jobPostingDescription);
            jobPosting.setRequiredSkills(skillRequirements);
            jobPosting.setLocation(jobPostingLocation);
            jobPosting.setSalaryRange(jobPostingSalary);
            jobPosting.setExperienceRequired(jobPostingExperienceRequired);
            jobPosting.setLocationImprotance(jobPostingLocationImportance);
            jobPosting.setExperienceImportance(jobPostingExperienceImportance);
            jobPosting.setSkillImportance(jobPostingSkillImportance);
        } else {
            MessageUI.displayInvalidChoiceMessage();
        }
        displayAllJobPosting();
    }



    /** Starts a standalone job menu with an empty list. */
    public static void main(String[] args) {
        JobManager jobManager = new JobManager(new ArrayList<>());
        jobManager.runJobPosting();
    }

    /** Returns matching jobs ordered by title relevance and then title. */
    public ListInterface<JobPosting> searchJobs(String jobTitle, ListInterface<JobPosting> companyJobs) {
        ListInterface<JobPosting> result = new ArrayList<>();
        for (int i = 0; i < companyJobs.size(); i++) {
            JobPosting job = companyJobs.get(i);
            if (SearchUtil.matchesJobTitle(jobTitle, job.getTitle())) result.add(job);
        }
        result.sort((a, b) -> {
            int relevance = SearchUtil.compareJobTitles(jobTitle, a.getTitle(), b.getTitle());
            return relevance != 0 ? relevance : a.getTitle().compareToIgnoreCase(b.getTitle());
        });
        return result;
    }
}

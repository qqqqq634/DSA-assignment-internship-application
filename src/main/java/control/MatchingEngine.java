package control;

import adt.*;
import boundary.MatchUI;
import entity.JobPosting;
import entity.Match;
import entity.SkillProficiency;
import entity.SkillRequirement;
import entity.Student;
import dao.MatchDAO;
import utility.MessageUI;
import utility.EntitySorting;
import utility.SearchUtil;

public class MatchingEngine {
    private ListInterface<Match> matches;
    private CompanyManager companyManager;
    private Student student;
    private boolean matchAscending = false;
    private boolean explicitMatchSort = false;
    private final MatchUI matchUI = new MatchUI();
    private final MatchDAO matchDAO = new MatchDAO();

    /** Loads saved applications and uses the supplied company manager, or creates one when none is supplied. */
    public MatchingEngine() {
        this.matches = new ArrayList<>();
        this.companyManager = new CompanyManager();
        matches = matchDAO.retrieveFromFile();
    }

    /** Loads saved applications and uses the supplied company manager, or creates one when none is supplied. */
    public MatchingEngine(CompanyManager companyManager) {
        this.matches = new ArrayList<>();
        this.companyManager = companyManager;
        matches = matchDAO.retrieveFromFile();
    }

    /** Builds scored candidates and runs the application menu until -1. Search results start in title relevance order unless the user selects score sorting with -2. */
    public ListInterface<Match> calculateJobScoreMatches(ListInterface<JobPosting> jobPostings) {
        return calculateJobScoreMatches(jobPostings, null);
    }

    /** Builds scored candidates and runs the application menu until -1. Search results start in title relevance order unless the user selects score sorting with -2. */
    private ListInterface<Match> calculateJobScoreMatches(ListInterface<JobPosting> jobPostings, String query) {
        ListInterface<Match> newMatches = new ArrayList<>();

        for (int i = 0; i < jobPostings.size(); i++) {
            JobPosting jobPosting = jobPostings.get(i);

            if (student == null) {
                MessageUI.displayErrorMessageStudentNull();
                return newMatches;
            }

            double score = calculateMatchScore(student, jobPosting);
            newMatches.add(new Match(new Student(student), new JobPosting(jobPosting), score));
        }

        if (newMatches.size() == 0) {
            MessageUI.displayNoJobsFoundYourCriteria();
            return newMatches;
        }

        if (query != null) {
            newMatches.sort((a, b) -> {
                int relevance = SearchUtil.compareJobTitles(query, a.getJobPosting().getTitle(), b.getJobPosting().getTitle());
                if (relevance != 0) return relevance;
                return a.getScore() > b.getScore() ? -1 : a.getScore() < b.getScore() ? 1 : 0;
            });
        }

        // menu for applying to jobs
        int choice = 0;
        do {
            // display matches
            String matchesInfo = query == null || explicitMatchSort ? listAllMatches(newMatches) : formatMatches(newMatches);
            displayMatches(matchesInfo);

            // select match
            choice = matchUI.inputMatchIndex();
            if (choice == -2) {
                matchAscending = matchUI.inputSortAscending();
                explicitMatchSort = true;
                EntitySorting.sortMatches(newMatches, matchAscending);
            } else if (choice >= 0 && choice < newMatches.size()) {
                // upload to the match list
                Match selected = newMatches.get(choice);
                if (containsPair(this.matches, selected)) {
                    System.out.println("You have already applied to this job.");
                } else {
                    this.matches.add(new Match(selected));
                    MessageUI.displayJobApplicationSuccessMessage();
                }
                matchUI.pressEnterToContinueMessage();
            }
        } while (choice != -1);

        return newMatches;
    }

    /** Passes match text to the UI. */
    public void displayMatches(String info) {
        if (info.isEmpty()) {
            matchUI.displayMatch("No matching jobs found.");
        } else {
            matchUI.displayMatch(info);
        }
    }

    /** Sorts the supplied matches by score in the selected direction and formats them. */
    public String listAllMatches(ListInterface<Match> matches) {
        // Keep the displayed ranking and the selectable match indexes aligned.
        EntitySorting.sortMatches(matches, matchAscending);
        return formatMatches(matches);
    }

    /** Formats numbered match choices or the empty-result message. */
    private String formatMatches(ListInterface<Match> matches) {
        String output = "";
        if (matches.isEmpty()) {
            return "No matching jobs found.";
        }

        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            output += "\n" + match.toString(i) + "\n";
        }
        return output;
    }

    /** Runs the job search and application menu for the student, using displayed ordering for selections and saving applications on exit. */
    public void runLookForJobs(Student student) {
        if (student == null) {
            MessageUI.displayErrorMessageStudentNull();

            return;
        }

        this.student = student;
        MessageUI.displayMessageRunningJobSearchStudent(student.getName());

        ListInterface<Match> CurrentMatches = new ArrayList<>();
        // Get the student's existing matches from the system
        CurrentMatches = findStudentMatches();

        int choice;
        do {
            choice = matchUI.matchMenuInput();
            switch (choice) {
                case 1 -> {
                    // look for jobs
                    ListInterface<Match> newMatches = searchJobs();
                    if (newMatches.size() > 0) {
                        CurrentMatches = addMatches(newMatches, CurrentMatches);
                        displayMatches(listAllMatches(CurrentMatches));
                    } else {
                        MessageUI.displayNoJobsFoundYourCriteria();
                    }
                }
                case 2 -> // view all available jobs
                    searchAllJobs();
                case 3 ->
                    MessageUI.displayExitMessageMatch();
                case 4 -> {
                    matchAscending = matchUI.inputSortAscending();
                    explicitMatchSort = true;
                    displayMatches(listAllMatches(CurrentMatches));
                }
                case 5 -> {
                    companyManager.setBrowseAscending(matchUI.inputSortAscending());
                    searchAllJobs();
                }
                default -> MessageUI.displayInvalidChoiceMessage();
            }
        } while (choice != 3); // Changed from 3 to 4 to match the menu

        // Save matches before exiting
        matchDAO.saveToFile(this.matches);
    }

    /** Copies search candidates into the displayed list; candidates are not necessarily saved applications. */
    private ListInterface<Match> addMatches(ListInterface<Match> newMatches, ListInterface<Match> matches) {
        for (int i = 0; i < newMatches.size(); i++) {
            matches.add(new Match(newMatches.get(i)));
        }
        return matches;
    }

    /** Selects the current student’s saved applications. */
    private ListInterface<Match> findStudentMatches() {
        ListInterface<Match> studentMatches = new ArrayList<>();

        for (int i = 0; i < this.matches.size(); i++) {
            if (this.matches.get(i).getStudent().getId().equals(student.getId())) {
                studentMatches.add(this.matches.get(i));
            }
        }
        return studentMatches;
    }

    /** Prompts for a title and builds scored candidates from matching jobs. */
    private ListInterface<Match> searchJobs() {
        String jobTitle = matchUI.inputJobTitle();
        MessageUI.displayStartingJobSearch(jobTitle);

        // Ensure companyManager is initialized
        if (companyManager == null) {
            MessageUI.displayErrorMessageCompanyManagerNull();
            companyManager = new CompanyManager();
        }

        // Get job postings that match the search criteria
        ListInterface<JobPosting> matchingJobs = companyManager.searchJobs(jobTitle);

        // Calculate match scores for each job
        return calculateJobScoreMatches(matchingJobs, jobTitle);
    }

    /** Displays company and job information for browsing. */
    public void searchAllJobs() {
        String result = companyManager.displayCompanyInfo();
        displayMatches(result);
    }

    /** Gets matches associated with the given job. */
    public ListInterface<Match> getMatchesFromCompany(JobPosting jobPostings) {
        ListInterface<Match> matches = new ArrayList<>();

        String jobPosting = jobPostings.getId();
        for (int j = 0; j < this.matches.size(); j++) {
            if (this.matches.get(j).getJobPosting().getId().equals(jobPosting)) {
                matches.add(this.matches.get(j));
            }
        }
        return matches;
    }

    /**
     * Calculate the match score of a student and a job posting. The score is
     * a weighted sum of the skill score, location score, and experience score.
     *
     * The skill score is the sum of the products of the proficiency of the
     * student in a skill and the importance of the skill in the job posting,
     * normalized to a value between 0 and 1.
     *
     * The location score is 1 if the student's location matches the job posting's
     * location, and 0 otherwise.
     *
     * The experience score is the student's experience divided by the requirement,
     * capped at 1; a zero requirement gives 1.
     *
     * The weights of the skill, location, and experience scores are given by
     * the job posting's skill, location, and experience importance.
     *
     * @param student the student to calculate the score for
     * @param job     the job posting to calculate the score for
     * @return the match score
     */
    private double calculateMatchScore(Student student, JobPosting job) {
        // weightings
        double skillWeight = job.getSkillImportance() / 10.0;
        double locationWeight = job.getLocationImprotance() / 10.0;
        double experienceWeight = job.getExperienceImportance() / 10.0;

        // Calculate the skill score.
        double skillScore = calculateSkillScore(student, job);

        // Calculate the location score.
        double locationScore;
        if (student.getLocation().equalsIgnoreCase(job.getLocation())) {
            locationScore = 1.0;
        } else {
            locationScore = 0.0;
        }
        locationScore *= locationWeight;

        // Calculate the experience score.
        double experienceRatio;
        if (job.getExperienceRequired() == 0) {
            experienceRatio = 1.0;
        } else {
            experienceRatio = (double) student.getExperience() / job.getExperienceRequired();
        }
        double experienceScore = Math.min(1.0, experienceRatio) * experienceWeight;

        // Calculate the match score as a weighted average of the skill, location,
        // and experience scores.
        double totalWeight = skillWeight + locationWeight + experienceWeight;
        double matchScore = totalWeight == 0 ? 0 : (skillScore + locationScore + experienceScore)
                / totalWeight;

        return matchScore;
    }

    /**
     * Calculate the skill score between a student and a job posting. The score
     * sums proficiency times importance for matching skill names, divides by
     * the total requirement importance times 10, and applies the job skill weight.
     *
     * @param student the student to calculate the score for
     * @param job     the job posting to calculate the score for
     * @return the skill score
     */
    private double calculateSkillScore(Student student, JobPosting job) {
        double scoreSum = 0;
        ListInterface<SkillRequirement> jobSkills = job.getRequiredSkills();
        ListInterface<SkillProficiency> studentSkills = student.getSkillProficiencies();
        double skillWeights = job.getSkillImportance() / 10.0;
        double importanceSum = 0;

        // Iterate over each skill requirement of the job
        for (int i = 0; i < jobSkills.size(); i++) {
            SkillRequirement req = jobSkills.get(i);

            // Check if the student has the required skill and calculate score contribution
            for (int j = 0; j < studentSkills.size(); j++) {
                SkillProficiency prof = studentSkills.get(j);
                if (req.getSkillName().equalsIgnoreCase(prof.getSkillName())) {
                    // Add the product of proficiency and importance to the score sum
                    double contribution = prof.getProficiency() * req.getImportance();
                    scoreSum += contribution;
                }
            }
            // Sum up the importance for normalization
            importanceSum += req.getImportance() * 10;
        }

        // Avoid division by zero
        if (importanceSum == 0) {
            System.out.println("No skill importance factors found, returning skill score of 0");
            return 0;
        }

        // Normalize the score sum by the total importance and scale by skill weight
        double finalScore = scoreSum / importanceSum * skillWeights;
        return finalScore;
    }

    /** Returns copies of saved applications whose job ID matches the selected job. */
    protected ListInterface<Match> getMatchesBySeletedJob(JobPosting job) {
        ListInterface<Match> selected = new ArrayList<>();
        for (int i = 0; i < this.matches.size(); i++) {
            if (job.getId().equals(this.matches.get(i).getJobPosting().getId())) {
                selected.add(new Match(this.matches.get(i)));
            }
        }
        return selected;
    }

    /** Tests whether a list contains the same student ID and job ID pair. */
    private boolean containsPair(ListInterface<Match> candidates, Match match) {
        for (int i = 0; i < candidates.size(); i++) {
            Match candidate = candidates.get(i);
            if (candidate.getStudent().getId().equals(match.getStudent().getId())
                    && candidate.getJobPosting().getId().equals(match.getJobPosting().getId())) {
                return true;
            }
        }
        return false;
    }

    /** Builds a remaining-application list by student/job ID pair and replaces the in-memory list only if saving it succeeds. */
    boolean removeScheduledApplications(ListInterface<Match> scheduledMatches) {
        ListInterface<Match> remaining = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            remaining.add(matches.get(i));
        }
        for (int i = remaining.size() - 1; i >= 0; i--) {
            if (containsPair(scheduledMatches, remaining.get(i))) {
                remaining.remove(i + 1);
            }
        }
        if (!matchDAO.saveToFile(remaining)) {
            return false;
        }
        matches = remaining;
        return true;
    }
}

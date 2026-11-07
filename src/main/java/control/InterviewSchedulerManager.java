package control;

/**
 *
 * @author Teoh Yu Xiang
 */
import java.time.LocalDateTime;
import adt.*;
import boundary.InterviewUI;
import entity.Interview;
import entity.JobPosting;
import entity.Match;
import utility.MessageUI;
import utility.EntitySorting;
import dao.InterviewDAO;

public class InterviewSchedulerManager {

    private ListInterface<Interview> interviews = new ArrayList<>();
    private final InterviewUI interviewUI = new InterviewUI();
    private final InterviewDAO interviewDAO = new InterviewDAO();
    private final MatchingEngine matchingEngine;
    private boolean ascending = true;

    /** Loads saved interviews, sorts them chronologically, and retains the matching engine. */
    public InterviewSchedulerManager(MatchingEngine matchingEngine) {
        interviews = interviewDAO.retrieveFromFile();
        EntitySorting.sortInterviews(interviews, ascending);
        this.matchingEngine = matchingEngine;
    }

    /** Runs the company or student interview menu until choice 2; choice 3 changes this scheduler’s sort direction. The student menu rebuilds applicant indexes after sorting and saves interviews on exit. */
    public void runInterviewScheduler(ListInterface<JobPosting> companyJobs) {
        ListInterface<Interview> companyInterviews = getInterviewsFromCompany(companyJobs);
        int choice = 0;

        do {
            choice = interviewUI.displayInterviewMenuOptions();
            switch (choice) {
                case 1:
                    // schedule interview for a job
                    // select a job

                    ListInterface<Interview> addedInterview = scheduleInterview(companyJobs);
                    for (int i = 0; i < addedInterview.size(); i++) {
                        companyInterviews.add(addedInterview.get(i));
                    }
                    displayInterview(ListAllInterview(companyInterviews));
                    break;
                // case 2:
                //     // view interviewers
                //     displayInterviews(listInterviews(companyInterviews));
                    // break;
                case 2:
                    // return to company menu
                    MessageUI.displayExitingMessageInterviewScheduler();
                    break;
                case 3:
                    ascending = interviewUI.inputSortAscending();
                    displayInterview(ListAllInterview(companyInterviews));
                    break;
                default:
                    MessageUI.displayInvalidChoiceMessage();
                    break;
            }
        } while (choice != 2);
    }

    /** Runs the company or student interview menu until choice 2; choice 3 changes this scheduler’s sort direction. The student menu rebuilds applicant indexes after sorting and saves interviews on exit. */
    public void runInterviewScheduler(String studentId) {
        // Establish chronological order before building index mappings.
        EntitySorting.sortInterviews(interviews, ascending);
        ListInterface<Integer> interviewIndex = new ArrayList<>();
        ListInterface<Integer> matchIndex = new ArrayList<>();
        buildStudentIndexes(studentId, interviewIndex, matchIndex);
        int choice = 0;
        do {
            choice = interviewUI.displayInterviewMenuStudentOptions();
            switch (choice) {
                case 1:
                    // view interviewers for student
                    displayInterviews(ListAllInterview(interviewIndex, matchIndex));
                    break;
                // case 2:
                //     // set interview status
                //     setInterviewStatus(interviewIndex, matchIndex);
                //     break;
                case 2:
                    // return to student menu
                    MessageUI.displayExitingMessageInterviewScheduler();
                    break;
                case 3:
                    ascending = interviewUI.inputSortAscending();
                    EntitySorting.sortInterviews(interviews, ascending);
                    interviewIndex = new ArrayList<>();
                    matchIndex = new ArrayList<>();
                    buildStudentIndexes(studentId, interviewIndex, matchIndex);
                    displayInterviews(ListAllInterview(interviewIndex, matchIndex));
                    break;
                default:
                    MessageUI.displayInvalidChoiceMessage();
                    break;
            }
        } while (choice != 2);
        interviewDAO.saveToFile(interviews);
    }

    /** Records parallel zero-based interview and match indexes for the given student ID. */
    private void buildStudentIndexes(String studentId, ListInterface<Integer> interviewIndex,
            ListInterface<Integer> matchIndex) {
        for (int i = 0; i < interviews.size(); i++) {
            for (int j = 0; j < interviews.get(i).getMatches().size(); j++) {
                if (interviews.get(i).getMatches().get(j).getStudent().getId().equals(studentId)) {
                    interviewIndex.add(i);
                    matchIndex.add(j);
                }
            }
        }
    }

    /** Legacy helper outside the active student menu. Its indexing and defensive state copy mean it cannot be relied on to persist status changes. */
    public void setInterviewStatus(ListInterface<Integer> interviewIndex, ListInterface<Integer> matchIndex) {
        int choice = 0;
        do {
            displayInterviews(ListAllInterview(interviewIndex, matchIndex));
            choice = interviewUI.inputSelectedInterview();
            if (choice != 0 && choice <= interviewIndex.size()) {
                Interview interview = interviews.get(choice-1);
                // display specific interview
                displayInterview(listInterview(interview, matchIndex, choice));
                // check whether it is company accept
                if (interview.getState()[matchIndex.get(choice)] == 2) {
                    interview.getState()[matchIndex.get(choice)] = interviewUI.acceptApplicant();
                }
                displayInterview(listInterview(interview, matchIndex, choice));
            }
        } while (choice != 0);
    }

    /** Formats an interview and the match and state selected through the supplied index mapping. */
    public String listInterview(Interview interview, ListInterface<Integer> matchIndex, int choice) {
        String result = "";
        result += "\nInterviews\n";
        result += "=====================\n";
        result += "Interview Id:" + interview.getId() + "\n";
        result += "Interview Scheduled Time:" + interview.getScheduledTime() + "\n";
        result += "Status:" + interview.getDisplayState(interview.getState()[matchIndex.get(choice)]) + "\n";
        result += "\nMatch Details  :\n"
                + interview.getMatches().get(matchIndex.get(choice)).toString();
        result += "\n";
        return result;
    }

    /** Formats the selected student matches and statuses using the already ordered parallel index mappings. */
    public String ListAllInterview(ListInterface<Integer> interviewIndex, ListInterface<Integer> matchIndex) {

        String result = "";
        if (interviewIndex.isEmpty()) {
            result += "No Interviews Scheduled\n";
        } else {

            for (int i = 0; i < interviewIndex.size(); i++) {
                result += "\nInterviews" + (1 + i) + "\n";
                result += "=====================\n";
                Interview interview = interviews.get(interviewIndex.get(i));
                result += "Interview Id:" + interview.getId() + "\n";
                result += "Interview Scheduled Time:" + interview.getScheduledTime() + "\n";
                result += "Status:" + interview.getDisplayState(interview.getState()[matchIndex.get(i)]) + "\n";
                result += "\nMatch Details  :\n"
                        + interviews.get(interviewIndex.get(i)).getMatches().get(matchIndex.get(i)).toString();
                result += "\n";
            }
        }
        return result;
    }

    /** Displays matches selected from company interviews using parallel interview and match indexes. */
    public void displayInterviews(ListInterface<Interview> companyInterviews, ListInterface<Integer> interviewIndex,
            ListInterface<Integer> matchIndex) {
        String info = "";
        if (companyInterviews.isEmpty()) {
            info += "No Interviews Scheduled\n";
        } else {

            for (int i = 0; i < interviewIndex.size(); i++) {
                info += companyInterviews.get(interviewIndex.get(i)).getMatches().get(matchIndex.get(i)).toString();
            }
        }
        displayInterview(info);
    }

    /** Displays supplied text, or formats matches selected by parallel indexes before displaying them. */
    public void displayInterviews(String info) {
        interviewUI.displayInterview(info);
    }

    /** Sorts the supplied company interviews in the selected direction and formats them. */
    public String listInterviews(ListInterface<Interview> companyInterviews) {
        EntitySorting.sortInterviews(companyInterviews, ascending);
        String info = "";
        if (companyInterviews.isEmpty()) {
            info += "No Interviews Scheduled\n";

        } else {
            info += "\nInterviews\n";
            info += "=====================\n";
            for (int i = 0; i < companyInterviews.size(); i++) {
                info += companyInterviews.get(i).toString();
            }
        }
        return info;
    }

    /** Prompts for jobs until choice 0, filters already scheduled applicant/job pairs, and saves each interview before removing its applications. A failed interview save retains applications; a failed removal leaves the saved interview. */
    private ListInterface<Interview> scheduleInterview(ListInterface<JobPosting> companyJobs) {
        int choice = 0;
        ListInterface<Interview> interview = new ArrayList<>();
        do {
            boolean flag = true;
            do {

                // select a job
                choice = interviewUI.displayJobs(displayJobs(companyJobs));
                if (choice < 0 || choice > companyJobs.size()) {
                    flag = true;
                    // display invalid choice message
                    MessageUI.displayInvalidChoiceMessage();

                } else {
                    flag = false;
                }
            } while (flag);
            if (choice != 0) {

                // retrieve all applicants from a selected job
                ListInterface<Match> matches = getAllMatchesBySeletedJobPosting(companyJobs.get(choice - 1));

                // A prior interview may have been saved even if removing applications failed.
                for (int i = matches.size() - 1; i >= 0; i--) {
                    if (alreadyScheduled(matches.get(i))) matches.remove(i + 1);
                }
                if (matches.isEmpty()) {
                    System.out.println("No unscheduled applicants for this job.");
                    continue;
                }

                // get interview date and time
                LocalDateTime scheduledTime = interviewUI.getInterviewDateAndTime();

                // accept applicants
                int[] array = new int[matches.size()];
                for (int i = 0; i < matches.size(); i++) {
                    // print out applicant match information
                    matchingEngine.displayMatches(matches.get(i).toString());
                    int accept = interviewUI.acceptApplicant();
                    array[i] = accept;
                }
                Interview scheduled = new Interview(scheduledTime, matches, array);
                this.interviews.add(scheduled);
                if (interviewDAO.saveToFile(this.interviews)) {
                    EntitySorting.sortInterviews(this.interviews, ascending);
                    interview.add(scheduled);
                    if (!matchingEngine.removeScheduledApplications(matches)) {
                        System.out.println("Interview saved, but applications could not be removed.");
                    }
                } else {
                    this.interviews.remove(this.interviews.size());
                    System.out.println("Interview was not saved; applications were retained.");
                }
            }
        } while (choice != 0);
        return interview;
    }

    /** Gets copied applications for a job and sorts them by score. */
    private ListInterface<Match> getAllMatchesBySeletedJobPosting(JobPosting jobPosting) {
        ListInterface<Match> matches = matchingEngine.getMatchesBySeletedJob(jobPosting);
        EntitySorting.sortMatches(matches);
        return matches;
    }

    /** Sorts the supplied jobs and formats one-based choices. */
    public String displayJobs(ListInterface<JobPosting> companyJobs) {
        EntitySorting.sortJobs(companyJobs);
        String info = "";
        for (int i = 0; i < companyJobs.size(); i++) {
            info += (i + 1) + ". " + companyJobs.get(i).toString();
        }
        return info;
    }

    /** Passes formatted interview text to the UI. */
    private void displayInterview(String info) {
        interviewUI.displayInterview(info);
    }

    /** Formats interviews in the selected direction, or formats student entries using parallel interview and match indexes. */
    private String ListAllInterview(ListInterface<Interview> interviews) {
        EntitySorting.sortInterviews(interviews, ascending);
        String info = "";
        for (int i = 0; i < interviews.size(); i++) {
            info += interviews.get(i).toString();
        }
        return info;
    }

    /** Checks whether an interview already contains the same student ID and job ID pair. */
    private boolean alreadyScheduled(Match candidate) {
        for (int i = 0; i < interviews.size(); i++) {
            ListInterface<Match> recorded = interviews.get(i).getMatches();
            for (int j = 0; j < recorded.size(); j++) {
                Match match = recorded.get(j);
                if (match.getStudent().getId().equals(candidate.getStudent().getId())
                        && match.getJobPosting().getId().equals(candidate.getJobPosting().getId())) return true;
            }
        }
        return false;
    }

    /** Returns interviews containing a match for one of the company’s job IDs, sorted in the selected direction. */
    private ListInterface<Interview> getInterviewsFromCompany(ListInterface<JobPosting> companyJobs) {
        ListInterface<Interview> companyInterview = new ArrayList<>();
        for (int i = 0; i < interviews.size(); i++) {
            Interview interview = interviews.get(i);
            boolean found = false;
            for (int j = 0; j < interview.getMatches().size() && !found; j++) {
                String jobId = interview.getMatches().get(j).getJobPosting().getId();
                for (int k = 0; k < companyJobs.size(); k++) {
                    if (jobId.equals(companyJobs.get(k).getId())) { found = true; break; }
                }
            }
            if (found) companyInterview.add(interview);
        }
        EntitySorting.sortInterviews(companyInterview, ascending);
        return companyInterview;
    }
}

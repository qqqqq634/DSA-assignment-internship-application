package control;

import adt.*;
import boundary.CompanyUI;
import entity.JobPosting;
import utility.MessageUI;
import utility.EntitySorting;
import utility.SearchUtil;
import entity.Company;
import control.JobManager;
import dao.CompanyDAO;

public class CompanyManager {

    private ListInterface<Company> companyList = new ArrayList<>();
    private final JobManager jobManager = new JobManager(new ArrayList<>());
    private final CompanyUI companyUI = new CompanyUI();
    private final CompanyDAO companyDAO = new CompanyDAO();
    private InterviewSchedulerManager interviewSchedulerManager;
    private int index = -1;
    private boolean browseAscending = true;

    /** Sets the direction used for subsequent company and nested job browsing. */
    public void setBrowseAscending(boolean ascending) {
        browseAscending = ascending;
    }

    /** Loads and sorts the saved companies. */
    public CompanyManager() {
        companyList = companyDAO.retrieveFromFile();
        EntitySorting.sortCompanies(companyList);
    }

    /** Runs the login and registration menu until choice 3, then saves the companies. */
    public void runCompanyProfile() {
        boolean flag = true;
        while (flag) {
            index = companyUI.getMenuChoiceLogin();
            switch (index) {
                case 1 ->
                    login();
                case 2 ->
                    signUp();
                case 3 -> {
                    flag = false;
                    MessageUI.displayLogOutMessage();
                }
            }
        }
        companyDAO.saveToFile(companyList);
    }

    /** Finds a company by ID only and runs its profile, job, and interview menus until logout or confirmed deletion. Company changes are saved when the outer menu exits. */
    public void login() {
        index = -1;
        EntitySorting.sortCompanies(companyList, true);
        String id = companyUI.inputCompanyId();
        for (int i = 0; i < companyList.size(); i++) {
            if (id.equals(companyList.get(i).getId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            int choice = 0;
            do {
                choice = companyUI.getMenuChoice();
                switch (choice) {
                    case 1:
                        companyUI.listCompanyInfo(displayCompanyInfo(companyList.get(index)));
                        break;
                    case 2:
                        // update
                        int updateChoice = companyUI.getUpdateMenuChoice();
                        switch (updateChoice) {
                            case 1 -> {
                                String name = companyUI.inputCompanyName();
                                companyList.get(index).setName(name);
                            }
                            case 2 -> {
                                String location = companyUI.inputCompanyLocation();
                                companyList.get(index).setLocation(location);
                            }
                            case 3 -> {

                            }
                            default ->
                                MessageUI.displayInvalidChoiceMessage();
                        }
                        companyUI.listCompanyInfo(displayCompanyInfo(companyList.get(index)));
                        break;

                    case 4:
                        jobManager.runJobPosting(companyList.get(index).getJobPostings());
                        ListInterface<JobPosting> jobPostings = jobManager.getJobPostings();
                        companyList.get(index).setJobPostings(jobPostings);
                        companyUI.listCompanyInfo(displayCompanyInfo(companyList.get(index)));
                        break;
                    case 5:
                        MatchingEngine matchingEngine = new MatchingEngine();
                        interviewSchedulerManager = new InterviewSchedulerManager(matchingEngine);
                        interviewSchedulerManager.runInterviewScheduler(companyList.get(index).getJobPostings());
                        break;
                    case 3:
                        // delete company
                        String choiceDelete = companyUI.comfimationDelete();
                        if (choiceDelete.equals("y")) {
                            companyList.remove(index + 1);
                            MessageUI.displayDeleteCompanyAccountMessage();
                        } else {
                            break;
                        }
                        choice = 6;
                    case 6:
                        index = -1;
                        MessageUI.displayLogOutMessage();
                        // logout
                        break;
                    default:
                        MessageUI.displayInvalidChoiceMessage();
                }
            } while (choice != 6);
        } else {
            MessageUI.displayCompanyInfoNotFoundMessage();
        }
    }

    /** Collects and adds a company, then displays its details. */
    public void signUp() {
        String name = companyUI.inputCompanyName();
        String location = companyUI.inputCompanyLocation();
        Company company = new Company(name, location);
        companyList.add(company);
        EntitySorting.sortCompanies(companyList, true);
        companyUI.listCompanyInfo(displayCompanyInfo(company));
        companyUI.displayEnterToContinueMessage();
    }

    /** Formats company details; the no-argument form sorts a company display copy and each company’s job list in the selected browse direction. */
    public String displayCompanyInfo(Company company) {
        String result = company.toString();
        return result;
    }

    /** Starts the company profile menu. */
    public static void main(String[] args) {
        CompanyManager companyManager = new CompanyManager();
        companyManager.runCompanyProfile();
    }

    /** Collects matching jobs across companies, ordered by title relevance and then title. */
    public ListInterface<JobPosting> searchJobs(String jobTitle) {
        ListInterface<JobPosting> result = new ArrayList<>();
        for (int i = 0; i < companyList.size(); i++) {
            ListInterface<JobPosting> found = jobManager.searchJobs(jobTitle, companyList.get(i).getJobPostings());
            for (int j = 0; j < found.size(); j++) result.add(found.get(j));
        }
        result.sort((a, b) -> {
            int relevance = SearchUtil.compareJobTitles(jobTitle, a.getTitle(), b.getTitle());
            return relevance != 0 ? relevance : a.getTitle().compareToIgnoreCase(b.getTitle());
        });
        System.out.println("Total matching jobs found across all companies: " + result.size());
        return result;
    }

    /** Formats company details; the no-argument form sorts a company display copy and each company’s job list in the selected browse direction. */
    protected String displayCompanyInfo() {
        // A display copy avoids changing the index of an active company account.
        ListInterface<Company> sortedCompanies = new ArrayList<>();
        for (Company company : companyList) {
            sortedCompanies.add(company);
        }
        EntitySorting.sortCompanies(sortedCompanies, browseAscending);
        for (Company company : sortedCompanies) {
            EntitySorting.sortJobs(company.getJobPostings(), browseAscending);
        }
        String result = "";
        for (int i = 0; i < sortedCompanies.size(); i++) {
            result += displayCompanyInfo(sortedCompanies.get(i));
        }
        return result;
    }
}

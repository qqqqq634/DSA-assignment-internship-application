package control;

import adt.*;
import boundary.ApplicantProfileUI;
import dao.StudentDAO;
import entity.Student;
import entity.SkillProficiency;
import utility.MessageUI;
import utility.EntitySorting;

public class ApplicantManager {
    private ListInterface<Student> students = new ArrayList<>();
    private final SkillProficiencyManager skillProficiencyManager = new SkillProficiencyManager(new ArrayList<>());
    private final ApplicantProfileUI applicantProfileUI = new ApplicantProfileUI();
    private int index = -1;
    private final StudentDAO studentDAO = new StudentDAO();
    private MatchingEngine matchingEngine;
    private InterviewSchedulerManager interviewSchedulerManager;


    /** Loads saved students and sorts them. */
    public ApplicantManager() {
        students = studentDAO.retrieveFromFile();
        EntitySorting.sortStudents(students);
    }

    /** Runs the login and registration menu until choice 3, then saves the student list. */
    public void runApplicantProfile() {
        int choice = 0;
        do {
            choice = applicantProfileUI.getMenuChoiceLogin();
            switch (choice) {
                case 1 -> login();
                case 2 -> signUp();
                case 3 -> {
                    MessageUI.displayLogOutMessage();
                }
            }
        } while (choice != 3);
        studentDAO.saveToFile(students);
    }

    /** Finds an account by ID only and runs its profile menu until logout or confirmed deletion. Profile edits are saved when the outer menu exits. */
    public void login() {
        index = -1;
        EntitySorting.sortStudents(students, true);
        String id = applicantProfileUI.inputApplicantId();
        for (int i = 0; i < students.size(); i++) {
            if (id.equals(students.get(i).getId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            int choice = 0;
            do {
                choice = applicantProfileUI.getMenuChoice();
                switch (choice) {
                    case 1:
                        applicantProfileUI.listStudentInfo(displayStudentInfo(students.get(index)));
                        break;
                    case 2:
                        // update
                        int updateChoice = applicantProfileUI.getUpdateMenuChoice();
                        switch (updateChoice) {
                            case 1 -> {
                                String name = applicantProfileUI.inputApplicantName();
                                students.get(index).setName(name);
                        }
                            case 2 -> {
                                int experience = applicantProfileUI.inputApplicantWorkingExperience();
                                students.get(index).setExperience(experience);
                        }
                            case 3 -> {
                                String location = applicantProfileUI.inputApplicantLocation();
                                students.get(index).setLocation(location);
                        }
                            case 4 -> {
                                skillProficiencyManager
                                        .runSkillProficiency(students.get(index).getSkillProficiencies());
                                ListInterface<SkillProficiency> skills = skillProficiencyManager.getSkillProficiencys();
                                students.get(index).setSkills(skills);
                        }
                            default -> MessageUI.displayInvalidChoiceMessage();
                        }
                        applicantProfileUI.listStudentInfo(displayStudentInfo(students.get(index)));
                        break;


                    case 4:
                        matchingEngine = new MatchingEngine();
                        matchingEngine.runLookForJobs(students.get(index));
                        break;
                    case 5:
                        // schedule
                        matchingEngine = new MatchingEngine();
                        interviewSchedulerManager = new InterviewSchedulerManager(matchingEngine);
                        interviewSchedulerManager.runInterviewScheduler(students.get(index).getId());
                        break;
                    case 3:
                        // delete
                        String choiceDelete = applicantProfileUI.comfimationDelete();
                        if (!choiceDelete.equals("y")) {
                            break;
                        }
                        choice = 6;
                        students.remove(index + 1);
                        MessageUI.displayDeleteApplicantAccountMessage();
                    case 6:
                        MessageUI.displayLogOutMessage();
                        index = -1;
                        break;
                    default:
                        MessageUI.displayInvalidChoiceMessage();
                }
            } while (choice != 6);
        }
    }

    /** Collects a student profile and skills, adds it to the in-memory list, and displays it. */
    public void signUp() {
        String name = applicantProfileUI.inputApplicantName();
        String location = applicantProfileUI.inputApplicantLocation();
        int experience = applicantProfileUI.inputApplicantWorkingExperience();
        skillProficiencyManager.runSkillProficiency();
        ListInterface<SkillProficiency> skills = skillProficiencyManager.getSkillProficiencys();
        Student student = new Student(name, skills, location, experience);

        applicantProfileUI.listStudentInfo(displayStudentInfo(student));
        students.add(student);
        EntitySorting.sortStudents(students, true);
        applicantProfileUI.displayEnterToContinueMessage();
    }

    /** Formats the student using its string representation. */
    private String displayStudentInfo(Student student) {
        String result = "";
        result += student.toString();
        return result;
    }

    /** Starts the applicant profile menu. */
    public static void main(String[] args) {
        ApplicantManager applicantManager = new ApplicantManager();
        applicantManager.runApplicantProfile();
    }

}

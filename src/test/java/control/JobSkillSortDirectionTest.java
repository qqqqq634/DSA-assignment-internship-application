package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

public class JobSkillSortDirectionTest {
    private static int checks;
    /** Exercises sort direction through job and skill managers and checks displayed row updates and retained ordering; throws when an expectation fails. */
    public static void main(String[] args) throws Exception {
        ListInterface<SkillProficiency> skills = new ArrayList<>();
        SkillProficiency high = new SkillProficiency("HighSkill", 9), low = new SkillProficiency("LowSkill", 2), tie = new SkillProficiency("TieSkill", 2);
        skills.add(high); skills.add(low); skills.add(tie);
        input("6\ninvalid\n9\n1\n4\n1\n5\n");
        SkillProficiencyManager proficiency = new SkillProficiencyManager(skills);
        capture(proficiency::runSkillProficiency);
        expect(skills.size() == 2 && skills.get(0) == tie && skills.get(1) == high, "ascending proficiency and displayed row deletion");
        expect(proficiency.getAllSkillProficiencys().indexOf("TieSkill") < proficiency.getAllSkillProficiencys().indexOf("HighSkill"), "proficiency direction retained");
        ListInterface<SkillProficiency> returned = proficiency.getSkillProficiencys();
        expect(returned.get(0) == tie, "proficiency getter preserves selected order");

        ListInterface<SkillRequirement> requirements = new ArrayList<>();
        SkillRequirement more = new SkillRequirement("More", 9), less = new SkillRequirement("Less", 2), equal = new SkillRequirement("Equal", 2);
        requirements.add(more); requirements.add(less); requirements.add(equal);
        input("6\n1\n3\n1\nUpdated\n3\n5\n");
        SkillRequirementManager requirement = new SkillRequirementManager(requirements);
        capture(requirement::runSkillRequirement);
        expect(requirements.get(0) == equal && requirements.get(1).getSkillName().equals("Updated") && requirements.get(2) == more, "ascending requirement and displayed row update");
        expect(requirement.getSkillRequirements().get(0) == equal, "requirement getter preserves selected order");

        ListInterface<JobPosting> jobs = new ArrayList<>();
        JobPosting alpha = job("Alpha"), zebra = job("Zebra"), zebraTie = job("zebra");
        jobs.add(alpha); jobs.add(zebra); jobs.add(zebraTie);
        input("6\n2\n4\n1\n5\n");
        JobManager jobManager = new JobManager(jobs);
        capture(jobManager::runJobPosting);
        expect(jobs.size() == 2 && jobs.get(0) == zebraTie && jobs.get(1) == alpha, "descending jobs stable and displayed row deletion");
        expect(jobManager.getJobPostings().get(0) == zebraTie, "job getter preserves selected order");
        Student student = new Student("Student", returned, "Penang", 1);
        input("");
        ApplicantManager applicant = new ApplicantManager();
        Method profile = ApplicantManager.class.getDeclaredMethod("displayStudentInfo", Student.class);
        profile.setAccessible(true);
        String profileText = (String) profile.invoke(applicant, student);
        expect(profileText.indexOf("TieSkill") < profileText.indexOf("HighSkill"), "profile preserves proficiency direction");
        System.out.println("Job and skill sort direction: " + checks + " checks passed");
    }
    /** Creates a JobPosting with the supplied title and fixed test values. */
    private static JobPosting job(String title) { return new JobPosting(title, "", new ArrayList<>(), "", new double[]{1,2}, 0, 1, 1, 1); }
    /** Replaces standard input with a byte stream containing the supplied text. */
    private static void input(String text) { System.setIn(new ByteArrayInputStream(text.getBytes())); }
    /** Runs an action with standard output captured and restores output in a finally block. */
    private static void capture(Runnable action) { PrintStream original = System.out; try { System.setOut(new PrintStream(new ByteArrayOutputStream())); action.run(); } finally { System.setOut(original); } }
    /** Counts the check and throws AssertionError with the message if the condition is false. */
    private static void expect(boolean condition, String message) { checks++; if (!condition) throw new AssertionError(message); }
}

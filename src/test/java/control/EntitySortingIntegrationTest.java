package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/** Tests the existing application views and selections, not just standalone comparisons. */
public class EntitySortingIntegrationTest {
    private static int checks;

    /** Exercises sorting through managers and views, including row selection and retained entities; throws when an expectation fails. */
    public static void main(String[] args) throws Exception {
        ListInterface<SkillProficiency> skills = new ArrayList<>();
        SkillProficiency low = new SkillProficiency("LowSkill", 2);
        SkillProficiency high = new SkillProficiency("HighSkill", 9);
        skills.add(low); skills.add(high);
        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        SkillProficiencyManager skillManager = new SkillProficiencyManager(skills);
        String skillOutput = skillManager.getAllSkillProficiencys();
        expect(skillOutput.indexOf("HighSkill") < skillOutput.indexOf("LowSkill"), "proficiency display highest first");
        expect(skills.get(0) == high, "proficiency selection matches displayed row");
        capture(skillManager::deleteSkillProficiency);
        expect(skills.size() == 1 && skills.get(0) == low, "delete displayed first proficiency");

        ListInterface<SkillRequirement> requirements = new ArrayList<>();
        SkillRequirement less = new SkillRequirement("LessImportant", 2);
        SkillRequirement more = new SkillRequirement("MoreImportant", 8);
        requirements.add(less); requirements.add(more);
        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        SkillRequirementManager requirementManager = new SkillRequirementManager(requirements);
        String requirementOutput = requirementManager.getAllSkillRequirements();
        expect(requirementOutput.indexOf("MoreImportant") < requirementOutput.indexOf("LessImportant"), "requirement display highest first");
        capture(requirementManager::deleteSkillRequirement);
        expect(requirements.size() == 1 && requirements.get(0) == less, "delete displayed first requirement");

        JobPosting zebra = job("ZebraJob");
        JobPosting alpha = job("AlphaJob");
        ListInterface<JobPosting> jobs = new ArrayList<>();
        jobs.add(zebra); jobs.add(alpha);
        JobManager jobManager = new JobManager(jobs);
        String jobOutput = (String) invoke(jobManager, "getAllJobPosting");
        expect(jobOutput.indexOf("AlphaJob") < jobOutput.indexOf("ZebraJob"), "job display alphabetic");
        expect(jobs.get(0) == alpha, "job selection matches displayed order");
        ListInterface<JobPosting> found = jobManager.searchJobs("Job", jobs);
        expect(found.get(0) == alpha, "job search results alphabetic");

        Student zoe = new Student("ZoeStudent", new ArrayList<>(), "Penang", 1);
        Student amy = new Student("AmyStudent", new ArrayList<>(), "Johor", 2);
        ListInterface<Student> students = new ArrayList<>();
        students.add(zoe); students.add(amy);
        System.setIn(new ByteArrayInputStream((zoe.getId() + "\n1\n6\n").getBytes()));
        ApplicantManager applicantManager = new ApplicantManager();
        field(applicantManager, "students").set(applicantManager, students);
        String applicantOutput = capture(() -> applicantManager.login());
        expect(students.get(0) == amy, "students alphabetic before login");
        expect(applicantOutput.contains("ZoeStudent") && !applicantOutput.contains("AmyStudent"), "sorting retains selected applicant");

        Company zebraCompany = new Company("ZebraCompany", "Penang", jobs);
        Company alphaCompany = new Company("AlphaCompany", "Johor");
        ListInterface<Company> companies = new ArrayList<>();
        companies.add(zebraCompany); companies.add(alphaCompany);
        System.setIn(new ByteArrayInputStream((zebraCompany.getId() + "\n1\n6\n").getBytes()));
        CompanyManager companyManager = new CompanyManager();
        field(companyManager, "companyList").set(companyManager, companies);
        String companyOutput = capture(() -> companyManager.login());
        expect(companies.get(0) == alphaCompany, "companies alphabetic before login");
        expect(companyOutput.contains("ZebraCompany") && !companyOutput.contains("AlphaCompany"), "sorting retains selected company");
        // A renamed account must stay selected even when an all-company view is rendered.
        field(companyManager, "index").setInt(companyManager, 0);
        alphaCompany.setName("ZZZCompany");
        String allCompanies = companyManager.displayCompanyInfo();
        expect(allCompanies.indexOf("ZebraCompany") < allCompanies.indexOf("ZZZCompany"), "all-company view alphabetic after rename");
        expect(companies.get(0) == alphaCompany, "all-company display preserves active account index");

        ListInterface<Match> applicants = new ArrayList<>();
        Match first = new Match(zoe, alpha, 0.2), second = new Match(amy, alpha, 0.9);
        applicants.add(first); applicants.add(second);
        Interview late = new Interview(LocalDateTime.of(2026, 11, 2, 9, 0), applicants, new int[] {1, 2});
        Interview early = new Interview(LocalDateTime.of(2026, 11, 1, 9, 0), applicants, new int[] {3, 4});
        ListInterface<Interview> interviews = new ArrayList<>();
        interviews.add(late); interviews.add(early);
        InterviewSchedulerManager scheduler = new InterviewSchedulerManager(new MatchingEngine());
        String interviewOutput = scheduler.listInterviews(interviews);
        expect(interviewOutput.indexOf("2026-11-01") < interviewOutput.indexOf("2026-11-02"), "interviews earliest first");
        expect(late.getMatches().get(0) == first && late.getState()[0] == 1
                && early.getMatches().get(1) == second && early.getState()[1] == 4, "interview applicant states remain aligned");
        String interviewJobs = scheduler.displayJobs(jobs);
        expect(interviewJobs.indexOf("AlphaJob") < interviewJobs.indexOf("ZebraJob"), "interview job selection alphabetic");
        System.out.println("Entity sorting integration: " + checks + " checks passed");
    }

    /** Creates a JobPosting with the supplied title and fixed test values. */
    private static JobPosting job(String title) {
        return new JobPosting(title, "", new ArrayList<>(), "Penang", new double[] {1000, 2000}, 0, 1, 1, 1);
    }

    /** Finds a declared field, makes it accessible, and returns it. */
    private static Field field(Object target, String name) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    /** Finds and invokes a declared no-argument method on the target, returning its result. */
    private static Object invoke(Object target, String name) throws Exception {
        Method method = target.getClass().getDeclaredMethod(name);
        method.setAccessible(true);
        return method.invoke(target);
    }

    /** Runs an action with standard output captured, restores output in a finally block, and returns captured text. */
    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(bytes));
            action.run();
        } finally {
            System.setOut(original);
        }
        return bytes.toString();
    }

    /** Counts the check and throws AssertionError with the description if the condition is false. */
    private static void expect(boolean condition, String description) {
        checks++;
        if (!condition) throw new AssertionError(description);
    }
}

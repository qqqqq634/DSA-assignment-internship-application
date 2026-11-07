package control;
import adt.ArrayList;
import adt.ListInterface;
import dao.MatchDAO;
import entity.*;
import java.lang.reflect.*;
import java.io.ByteArrayInputStream;
public class MatchingPersistenceTest {
    /** Throws an assertion error with the supplied message when the condition fails. */
    private static void check(boolean value, String message) { if (!value) throw new AssertionError(message); }
    /** Invokes a named method reflectively for the persistence checks. */
    private static Object invoke(Object target, String name, Class<?>[] types, Object... args) throws Exception {
        Method method = target.getClass().getDeclaredMethod(name, types);
        method.setAccessible(true);
        return method.invoke(target, args);
    }
    /** Exercises application persistence and related matching behavior. */
    public static void main(String[] args) throws Exception {
        ListInterface<SkillProficiency> skills = new ArrayList<>();
        skills.add(new SkillProficiency("Java", 10));
        Student student = new Student("Student", skills, "KL", 2);
        Student other = new Student("Other", skills, "KL", 2);
        ListInterface<SkillRequirement> requirements = new ArrayList<>();
        requirements.add(new SkillRequirement("Java", 10));
        JobPosting job = new JobPosting("Job", "Description", requirements, "KL", new double[]{1, 2}, 2, 10, 10, 10);
        JobPosting otherJob = new JobPosting(job);
        otherJob.setId("other-job");
        ListInterface<Match> stored = new ArrayList<>();
        stored.add(new Match(student, job, 1));
        stored.add(new Match(other, job, 1));
        stored.add(new Match(student, otherJob, 1));
        new MatchDAO().saveToFile(stored);
        MatchingEngine engine = new MatchingEngine();
        double score = (double) invoke(engine, "calculateMatchScore", new Class<?>[]{Student.class, JobPosting.class}, student, job);
        check(Math.abs(score - 1.0) < 0.00001, "perfect score should equal 1, got " + score);
        Student partial = new Student("Partial", new ArrayList<>(), "elsewhere", 1);
        score = (double) invoke(engine, "calculateMatchScore", new Class<?>[]{Student.class, JobPosting.class}, partial, job);
        check(Math.abs(score - 1.0 / 6.0) < 0.00001, "partial score should be one sixth, got " + score);
        JobPosting zero = new JobPosting("Zero", "", requirements, "KL", new double[]{1, 2}, 2, 0, 0, 0);
        score = (double) invoke(engine, "calculateMatchScore", new Class<?>[]{Student.class, JobPosting.class}, student, zero);
        check(score == 0, "zero weights should produce zero");
        Field studentField = MatchingEngine.class.getDeclaredField("student");
        studentField.setAccessible(true);
        studentField.set(engine, new Student(student));
        ListInterface<?> found = (ListInterface<?>) invoke(engine, "findStudentMatches", new Class<?>[]{});
        check(found.size() == 2, "lookup must use student ID after reload");
        check(engine.getMatchesFromCompany(job).size() == 2, "company lookup should find both applications");
        ListInterface<Match> selected = engine.getMatchesBySeletedJob(job);
        check(selected.size() == 2, "selection must include adjacent applications");
        check(new MatchDAO().retrieveFromFile().size() == 3, "selection must not persist removal");
        check(engine.getMatchesBySeletedJob(job).size() == 2, "selection must not remove applications");
        System.setIn(new ByteArrayInputStream("0\n\n0\n\n-1\n".getBytes()));
        MatchingEngine applying = new MatchingEngine();
        studentField.set(applying, new Student(student));
        ListInterface<JobPosting> choices = new ArrayList<>();
        choices.add(job);
        applying.calculateJobScoreMatches(choices);
        Field matchesField = MatchingEngine.class.getDeclaredField("matches");
        matchesField.setAccessible(true);
        check(((ListInterface<?>) matchesField.get(applying)).size() == 3,
                "repeated application must not duplicate a loaded pair");
        JobPosting fresh = new JobPosting(otherJob);
        fresh.setId("fresh-job");
        System.setIn(new ByteArrayInputStream("0\n\n0\n\n-1\n".getBytes()));
        MatchingEngine freshApplying = new MatchingEngine();
        studentField.set(freshApplying, new Student(student));
        choices = new ArrayList<>();
        choices.add(fresh);
        freshApplying.calculateJobScoreMatches(choices);
        check(((ListInterface<?>) matchesField.get(freshApplying)).size() == 4,
                "repeated application must add a new pair only once");
        Method remove = MatchingEngine.class.getDeclaredMethod("removeScheduledApplications", ListInterface.class);
        remove.setAccessible(true);
        check((boolean) remove.invoke(engine, selected), "removal should succeed");
        check(new MatchDAO().retrieveFromFile().size() == 1, "removal should persist all scheduled pairs");
        System.out.println("Matching persistence checks passed");
    }
}

package control;

import adt.ArrayList;
import adt.ListInterface;
import dao.InterviewDAO;
import dao.MatchDAO;
import entity.*;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class InterviewPersistenceTest {
    /** Throws an assertion error with the supplied message when the condition fails. */
    private static void check(boolean value, String message) { if (!value) throw new AssertionError(message); }
    /** Replaces standard input with the scripted UTF-8 menu choices. */
    private static void input(String s) { System.setIn(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8))); }
    /** Creates a job fixture for this test. */
    private static JobPosting job() { return new JobPosting("Job", "Description", new ArrayList<>(), "KL", new double[]{1,2}, 0, 1, 1, 1); }
    /** Creates a student fixture with the supplied name. */
    private static Student student(String name) { return new Student(name, new ArrayList<>(), "KL", 0); }
    /** Exercises interview persistence, failure handling, and retry behavior. */
    public static void main(String[] args) throws Exception {
        JobPosting job = job();
        ListInterface<JobPosting> jobs = new ArrayList<>(); jobs.add(job);
        ListInterface<Match> matches = new ArrayList<>();
        matches.add(new Match(student("A"), job, .5)); matches.add(new Match(student("B"), job, .9));
        new MatchDAO().saveToFile(matches);
        ListInterface<Interview> existing = new ArrayList<>();
        existing.add(new Interview(LocalDateTime.of(2098, 1, 1, 0, 0), matches, new int[]{1,2}));
        new InterviewDAO().saveToFile(existing);
        input("2\n");
        InterviewSchedulerManager manager = new InterviewSchedulerManager(new MatchingEngine());
        Method filter = InterviewSchedulerManager.class.getDeclaredMethod("getInterviewsFromCompany", ListInterface.class);
        filter.setAccessible(true);
        ListInterface<JobPosting> copiedJobs = new ArrayList<>(); copiedJobs.add(new JobPosting(job));
        @SuppressWarnings("unchecked") ListInterface<Interview> filtered = (ListInterface<Interview>) filter.invoke(manager, copiedJobs);
        check(filtered.size() == 1, "separately loaded job IDs must match one interview once");
        manager.runInterviewScheduler(jobs);
        check(new InterviewDAO().retrieveFromFile().size() == 1, "return must not duplicate existing interviews");
        // Start a new slot with adjacent applicants and preserve an existing unrelated slot.
        JobPosting secondJob = job();
        ListInterface<JobPosting> secondJobs = new ArrayList<>(); secondJobs.add(secondJob);
        ListInterface<Match> newMatches = new ArrayList<>();
        newMatches.add(new Match(student("C"), secondJob, .2));
        newMatches.add(new Match(student("D"), secondJob, .8));
        new MatchDAO().saveToFile(newMatches);
        input("1\n1\n2099-01-01 10:00\n1\n2\n0\n2\n");
        new InterviewSchedulerManager(new MatchingEngine()).runInterviewScheduler(secondJobs);
        ListInterface<Interview> saved = new InterviewDAO().retrieveFromFile();
        check(saved.size() == 2, "one new interview must be saved without multiplying old interviews");
        Interview added = saved.get(1);
        check(added.getMatches().size() == 2, "both adjacent applications must be scheduled");
        check(added.getMatches().get(0).getScore() == .8 && added.getState()[0] == 1
                && added.getState()[1] == 2, "decisions must align with sorted matches");
        check(new MatchDAO().retrieveFromFile().isEmpty(), "saved interview must transfer applications");

        // Preserve an existing interview in memory when a new save fails.
        JobPosting failureJob = job();
        ListInterface<JobPosting> failureJobs = new ArrayList<>(); failureJobs.add(failureJob);
        ListInterface<Match> failureMatches = new ArrayList<>();
        failureMatches.add(new Match(student("F"), failureJob, .5));
        new MatchDAO().saveToFile(failureMatches);
        input("1\n1\n2099-01-01 10:00\n1\n0\n2\n");
        InterviewSchedulerManager failedSave = new InterviewSchedulerManager(new MatchingEngine());
        java.nio.file.Files.delete(java.nio.file.Paths.get("Interview.dat"));
        java.nio.file.Files.createDirectory(java.nio.file.Paths.get("Interview.dat"));
        failedSave.runInterviewScheduler(failureJobs);
        java.lang.reflect.Field field = InterviewSchedulerManager.class.getDeclaredField("interviews");
        field.setAccessible(true);
        @SuppressWarnings("unchecked") ListInterface<Interview> inMemory = (ListInterface<Interview>) field.get(failedSave);
        check(inMemory.size() == 2 && inMemory.get(0).getId().equals(saved.get(0).getId())
                && inMemory.get(1).getId().equals(saved.get(1).getId()), "failed save must retain existing interviews only");
        check(new MatchDAO().retrieveFromFile().size() == 1, "failed interview save must retain applications");
        java.nio.file.Files.delete(java.nio.file.Paths.get("Interview.dat"));

        // A failed application save leaves the match on disk; a retry must not create a duplicate.
        JobPosting thirdJob = job();
        ListInterface<JobPosting> thirdJobs = new ArrayList<>(); thirdJobs.add(thirdJob);
        ListInterface<Match> thirdMatches = new ArrayList<>(); thirdMatches.add(new Match(student("E"), thirdJob, .5));
        new MatchDAO().saveToFile(thirdMatches);
        input("1\n1\n2099-01-01 10:00\n2\n0\n2\n");
        MatchingEngine engine = new MatchingEngine();
        InterviewSchedulerManager firstAttempt = new InterviewSchedulerManager(engine);
        java.nio.file.Files.delete(java.nio.file.Paths.get("Match.dat"));
        java.nio.file.Files.createDirectory(java.nio.file.Paths.get("Match.dat"));
        firstAttempt.runInterviewScheduler(thirdJobs);
        check(new InterviewDAO().retrieveFromFile().size() == 1, "interview must survive application removal failure");
        java.nio.file.Files.delete(java.nio.file.Paths.get("Match.dat"));
        new MatchDAO().saveToFile(thirdMatches);
        input("1\n1\n0\n2\n");
        new InterviewSchedulerManager(new MatchingEngine()).runInterviewScheduler(thirdJobs);
        check(new InterviewDAO().retrieveFromFile().size() == 1, "retry must not duplicate the existing applicant interview");
        check(new MatchDAO().retrieveFromFile().size() == 1, "retry must retain already recorded application");
        System.out.println("Interview persistence regression checks passed");
    }
}

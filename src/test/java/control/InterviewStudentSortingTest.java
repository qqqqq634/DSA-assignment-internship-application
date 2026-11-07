package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

/** The test runner executes this in a data-file sandbox under target. */
public class InterviewStudentSortingTest {
    /** Checks chronological student interviews and preserved applicant/status pairing. */
    public static void main(String[] args) throws Exception {
        Student other = new Student("Other", new ArrayList<>(), "Penang", 1);
        Student selected = new Student("Selected", new ArrayList<>(), "Johor", 2);
        JobPosting job = new JobPosting("Developer", "", new ArrayList<>(), "Penang",
                new double[] {1000, 2000}, 0, 1, 1, 1);
        ListInterface<Match> applicants = new ArrayList<>();
        applicants.add(new Match(other, job, 0.9));
        applicants.add(new Match(selected, job, 0.2));
        Interview late = new Interview(LocalDateTime.of(2026, 11, 2, 9, 0), applicants, new int[] {1, 2});
        Interview early = new Interview(LocalDateTime.of(2026, 11, 1, 9, 0), applicants, new int[] {2, 4});
        ListInterface<Interview> interviews = new ArrayList<>();
        interviews.add(late); interviews.add(early);
        System.setIn(new ByteArrayInputStream("1\n2\n".getBytes()));
        InterviewSchedulerManager scheduler = new InterviewSchedulerManager(new MatchingEngine());
        Field field = InterviewSchedulerManager.class.getDeclaredField("interviews");
        field.setAccessible(true);
        field.set(scheduler, interviews);
        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(bytes));
            scheduler.runInterviewScheduler(selected.getId());
        } finally {
            System.setOut(original);
        }
        String output = bytes.toString();
        int earliest = output.indexOf("2026-11-01");
        int latest = output.indexOf("2026-11-02");
        if (earliest < 0 || latest <= earliest) throw new AssertionError("Student interviews must be chronological");
        String earlyOutput = output.substring(earliest, latest);
        String lateOutput = output.substring(latest);
        if (!earlyOutput.contains("Rejected by applicant") || !lateOutput.contains("Scheduled")) {
            throw new AssertionError("Sorted interview mappings must preserve selected applicant statuses");
        }
        if (output.contains("Name      : Other") || !output.contains("Name      : Selected")) {
            throw new AssertionError("Sorted mappings must retain the selected applicant");
        }
        if (early.getMatches().get(0).getStudent() != other || early.getState()[0] != 2) {
            throw new AssertionError("Sorting interviews must not reorder their applicants or states");
        }
        System.out.println("Student interview sorting: 4 checks passed");
    }
}

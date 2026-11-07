package utility;

import adt.ArrayList;
import adt.ListInterface;
import boundary.SortUI;
import entity.*;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Scanner;

public class SortDirectionTest {
    private static int checks;
    /** Checks both directions for all seven entity types, stable ties, NaN placement, and direction-input validation. */
    public static void main(String[] args) {
        ListInterface<Student> students = new ArrayList<>();
        Student a = new Student("Alpha", new ArrayList<>(), "KL", 1);
        Student z = new Student("Zulu", new ArrayList<>(), "KL", 2);
        students.add(z); students.add(a);
        EntitySorting.sortStudents(students, true); check(students.get(0) == a, "student ascending");
        EntitySorting.sortStudents(students, false); check(students.get(0) == z, "student descending");
        ListInterface<Company> companies = new ArrayList<>();
        Company c1 = new Company("Alpha", "KL"), c2 = new Company("Zulu", "KL");
        companies.add(c2); companies.add(c1);
        EntitySorting.sortCompanies(companies, true); check(companies.get(0) == c1, "company ascending");
        EntitySorting.sortCompanies(companies, false); check(companies.get(0) == c2, "company descending");
        ListInterface<JobPosting> jobs = new ArrayList<>();
        JobPosting j1 = job("Alpha"), j2 = job("Zulu");
        jobs.add(j2); jobs.add(j1);
        EntitySorting.sortJobs(jobs, true); check(jobs.get(0) == j1, "job ascending");
        EntitySorting.sortJobs(jobs, false); check(jobs.get(0) == j2, "job descending");
        ListInterface<SkillProficiency> skills = new ArrayList<>();
        SkillProficiency low = new SkillProficiency("Low", 1), high = new SkillProficiency("High", 9), tie = new SkillProficiency("Tie", 9);
        skills.add(high); skills.add(tie); skills.add(low);
        EntitySorting.sortProficiencies(skills, true); check(skills.get(0) == low, "proficiency ascending");
        EntitySorting.sortProficiencies(skills, false); check(skills.get(0) == high && skills.get(1) == tie, "descending stable proficiency ties");
        ListInterface<SkillRequirement> requirements = new ArrayList<>();
        SkillRequirement r1 = new SkillRequirement("Low", 1), r2 = new SkillRequirement("High", 9);
        requirements.add(r2); requirements.add(r1);
        EntitySorting.sortRequirements(requirements, true); check(requirements.get(0) == r1, "importance ascending");
        EntitySorting.sortRequirements(requirements, false); check(requirements.get(0) == r2, "importance descending");
        ListInterface<Match> matches = new ArrayList<>();
        Match m1 = new Match(a, j1, 0.1), m2 = new Match(z, j2, 0.9), nan = new Match(a, j2, Double.NaN);
        matches.add(nan); matches.add(m2); matches.add(m1);
        EntitySorting.sortMatches(matches, true); check(matches.get(0) == m1 && matches.get(2) == nan, "score ascending with NaN last");
        EntitySorting.sortMatches(matches, false); check(matches.get(0) == m2 && matches.get(2) == nan, "score descending with NaN last");
        ListInterface<Interview> interviews = new ArrayList<>();
        Interview early = new Interview(LocalDateTime.of(2099, 1, 1, 9, 0), new ArrayList<>(), new int[0]);
        Interview late = new Interview(LocalDateTime.of(2099, 1, 2, 9, 0), new ArrayList<>(), new int[0]);
        interviews.add(late); interviews.add(early);
        EntitySorting.sortInterviews(interviews, true); check(interviews.get(0) == early, "interview ascending");
        EntitySorting.sortInterviews(interviews, false); check(interviews.get(0) == late, "interview descending");
        check(SortUI.inputAscending(new Scanner(new ByteArrayInputStream("bad\n3\n1\n".getBytes()))), "direction validates input then accepts ascending");
        check(!SortUI.inputAscending(new Scanner(new ByteArrayInputStream("2\n".getBytes()))), "direction accepts descending");
        System.out.println("SortDirectionTest: " + checks + " checks passed");
    }
    /** Builds a minimal job fixture with the requested title for name-order comparisons. */
    private static JobPosting job(String title) { return new JobPosting(title, "", new ArrayList<>(), "KL", new double[]{1,2}, 0,1,1,1); }
    /** Counts an assertion and throws AssertionError when a direction regression is detected. */
    private static void check(boolean condition, String message) { checks++; if (!condition) throw new AssertionError(message); }
}

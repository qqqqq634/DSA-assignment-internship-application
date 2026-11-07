package control;

import adt.ArrayList;
import adt.ListInterface;
import dao.CompanyDAO;
import dao.MatchDAO;
import entity.Company;
import entity.JobPosting;
import entity.Match;
import entity.Student;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

public class AutomaticJobSearchTest {
    /** Throws an assertion error with the supplied message when the condition fails. */
    private static void check(boolean value, String message) { if (!value) throw new AssertionError(message); }
    /** Creates a job fixture for this test. */
    private static JobPosting job(String title, String location) {
        return new JobPosting(title, "", new ArrayList<>(), location, new double[]{0, 0}, 1, 10, 1, 1);
    }
    /** Invokes company job search and returns its results. */
    @SuppressWarnings("unchecked")
    private static ListInterface<JobPosting> search(CompanyManager manager, String title) throws Exception {
        Method method = CompanyManager.class.getMethod("searchJobs", String.class);
        return (ListInterface<JobPosting>) method.invoke(manager, title);
    }
    /** Checks title relevance, search input, and selection of the displayed job. */
    public static void main(String[] args) throws Exception {
        ListInterface<JobPosting> first = new ArrayList<>();
        first.add(job("Senior Developer", "KL"));
        first.add(job("Developex", "KL"));
        first.add(job("Accountant", "KL"));
        ListInterface<JobPosting> second = new ArrayList<>();
        second.add(job("Developer", "Penang"));
        second.add(job("Java Developer", "KL"));
        ListInterface<Company> companies = new ArrayList<>();
        companies.add(new Company("Alpha", "KL", first));
        companies.add(new Company("Zulu", "KL", second));
        new CompanyDAO().saveToFile(companies);
        CompanyManager manager = new CompanyManager();
        ListInterface<JobPosting> found = search(manager, "  dEvElOpEr  ");
        check(found.size() == 4, "exact, substring and fuzzy results");
        check(found.get(0).getTitle().equals("Developer"), "exact globally first");
        check(found.get(1).getTitle().equals("Java Developer") && found.get(2).getTitle().equals("Senior Developer"), "substring before fuzzy");
        check(found.get(3).getTitle().equals("Developex"), "typo after substring");
        check(search(manager, "Developr").size() >= 3, "typo matches title words");
        check(search(manager, "Java Developr").get(0).getTitle().equals("Java Developer"), "multiword typo");
        check(search(manager, "   ").size() == 0 && search(manager, "astronaut").size() == 0 && search(manager, "xy").size() == 0, "blank and unrelated queries");
        new MatchDAO().saveToFile(new ArrayList<>());
        System.setIn(new ByteArrayInputStream("1\nDeveloper\n0\n\n-1\n3\n".getBytes("UTF-8")));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(output));
        try { new MatchingEngine(manager).runLookForJobs(new Student("Test", new ArrayList<>(), "KL", 0)); }
        finally { System.setOut(old); }
        String text = output.toString("UTF-8");
        check(!text.contains("Enter Weighting"), "title-only input");
        int exact = text.indexOf("Title           : Developer");
        int substring = text.indexOf("Title           : Java Developer");
        int fuzzy = text.indexOf("Title           : Developex");
        check(exact >= 0 && substring > exact && fuzzy > substring, "application display relevance before score");
        ListInterface<Match> saved = new MatchDAO().retrieveFromFile();
        check(saved.size() == 1 && saved.get(0).getJobPosting().getTitle().equals("Developer"), "selected index uses displayed relevance order");
        System.out.println("AutomaticJobSearchTest passed");
    }
}

package control;
import adt.ArrayList;
import adt.ListInterface;
import entity.*;
import dao.CompanyDAO;
import dao.MatchDAO;
import java.io.*;
import java.time.LocalDateTime;
import java.lang.reflect.Field;
public class MatchInterviewSortDirectionTest {
 /** Checks match and interview directions, applicant state pairing, and menu flows. */
 public static void main(String[] args) throws Exception {
  ListInterface<Match> matches = new ArrayList<>();
  Match low = new Match(null,null,0.2), high = new Match(null,null,0.9), nan = new Match(null,null,Double.NaN);
  matches.add(high); matches.add(nan); matches.add(low);
  MatchingEngine engine = new MatchingEngine();
  Field f = MatchingEngine.class.getDeclaredField("matchAscending"); f.setAccessible(true);
  f.setBoolean(engine,true); engine.listAllMatches(matches);
  if (matches.get(0)!=low || matches.get(1)!=high || matches.get(2)!=nan) throw new AssertionError("ascending");
  f.setBoolean(engine,false); engine.listAllMatches(matches);
  if (matches.get(0)!=high || matches.get(1)!=low || matches.get(2)!=nan) throw new AssertionError("descending");
  Student student = new Student("Selected", new ArrayList<>(), "Johor", 2);
  Student other = new Student("Other", new ArrayList<>(), "Penang", 1);
  JobPosting job = new JobPosting("Developer", "", new ArrayList<>(), "Penang", new double[]{1000,2000}, 0, 1, 1, 1);
  ListInterface<Match> applicants = new ArrayList<>();
  applicants.add(new Match(other,job,0.9)); applicants.add(new Match(student,job,0.2));
  Interview early = new Interview(LocalDateTime.of(2026,11,1,9,0), applicants, new int[]{2,4});
  Interview late = new Interview(LocalDateTime.of(2026,11,2,9,0), applicants, new int[]{1,2});
  ListInterface<Interview> interviews = new ArrayList<>(); interviews.add(early); interviews.add(late);
  InputStream in = System.in; PrintStream out = System.out; ByteArrayOutputStream bytes = new ByteArrayOutputStream();
  try {
   System.setIn(new ByteArrayInputStream("3\n2\n2\n".getBytes()));
   InterviewSchedulerManager scheduler = new InterviewSchedulerManager(new MatchingEngine());
   Field field = InterviewSchedulerManager.class.getDeclaredField("interviews"); field.setAccessible(true); field.set(scheduler, interviews);
   System.setOut(new PrintStream(bytes)); scheduler.runInterviewScheduler(student.getId());
  } finally { System.setIn(in); System.setOut(out); }
  String text = bytes.toString(); int first = text.indexOf("2026-11-02"), second = text.indexOf("2026-11-01");
  if (first < 0 || second <= first || !text.substring(first,second).contains("Scheduled") || !text.substring(second).contains("Rejected by applicant")) throw new AssertionError("student mappings");
  if (early.getMatches().get(0).getStudent()!=other || early.getState()[0]!=2) throw new AssertionError("applicant state pairing");
  bytes.reset();
  try {
   System.setIn(new ByteArrayInputStream("3\n2\n2\n".getBytes()));
   InterviewSchedulerManager scheduler = new InterviewSchedulerManager(new MatchingEngine());
   Field field = InterviewSchedulerManager.class.getDeclaredField("interviews"); field.setAccessible(true); field.set(scheduler, interviews);
   ListInterface<JobPosting> companyJobs = new ArrayList<>(); companyJobs.add(job);
   System.setOut(new PrintStream(bytes)); scheduler.runInterviewScheduler(companyJobs);
  } finally { System.setIn(in); System.setOut(out); }
  text = bytes.toString();
  if (text.indexOf("2026-11-02") < 0 || text.indexOf("2026-11-01") <= text.indexOf("2026-11-02"))
   throw new AssertionError("company interview direction");
  menuFlows();
  System.out.println("Match and interview direction checks passed");
 }
 /** Checks menu selections and company/job browsing after sort direction changes. */
 private static void menuFlows() throws Exception {
  JobPosting low = new JobPosting("Developer", "", new ArrayList<>(), "Penang", new double[]{0,0}, 1,10,1,1);
  JobPosting high = new JobPosting("Java Developer", "", new ArrayList<>(), "KL", new double[]{0,0}, 1,10,1,1);
  ListInterface<JobPosting> alphaJobs = new ArrayList<>(); alphaJobs.add(low); alphaJobs.add(high);
  JobPosting zebraJob = new JobPosting("Zebra Job", "", new ArrayList<>(), "KL", new double[]{0,0}, 1,10,1,1);
  JobPosting alphaJob = new JobPosting("Alpha Job", "", new ArrayList<>(), "KL", new double[]{0,0}, 1,10,1,1);
  ListInterface<JobPosting> zebraJobs = new ArrayList<>(); zebraJobs.add(zebraJob); zebraJobs.add(alphaJob);
  Company alpha = new Company("Alpha Company", "KL", alphaJobs), zebra = new Company("Zebra Company", "KL", zebraJobs);
  ListInterface<Company> companies = new ArrayList<>(); companies.add(zebra); companies.add(alpha);
  new CompanyDAO().saveToFile(companies); new MatchDAO().saveToFile(new ArrayList<>());
  Student student = new Student("Applicant", new ArrayList<>(), "KL", 0);
  InputStream in = System.in; PrintStream out = System.out; ByteArrayOutputStream bytes = new ByteArrayOutputStream();
  try {
   System.setIn(new ByteArrayInputStream("1\nDeveloper\n-2\n1\n0\n\n-2\n2\n0\n\n-1\n3\n".getBytes()));
   System.setOut(new PrintStream(bytes));
   new MatchingEngine(new CompanyManager()).runLookForJobs(student);
  } finally { System.setIn(in); System.setOut(out); }
  ListInterface<Match> saved = new MatchDAO().retrieveFromFile();
  if (saved.size()!=2 || !saved.get(0).getStudent().getId().equals(student.getId())
      || !saved.get(1).getStudent().getId().equals(student.getId())
      || !saved.get(0).getJobPosting().getId().equals(low.getId())
      || !saved.get(1).getJobPosting().getId().equals(high.getId())) throw new AssertionError("selection after direction changes");
  bytes.reset();
  try {
   System.setIn(new ByteArrayInputStream("5\n2\n5\n1\n3\n".getBytes()));
   CompanyManager manager = new CompanyManager();
   Field companyField = CompanyManager.class.getDeclaredField("companyList"); companyField.setAccessible(true);
   @SuppressWarnings("unchecked") ListInterface<Company> live = (ListInterface<Company>) companyField.get(manager);
   Field index = CompanyManager.class.getDeclaredField("index"); index.setAccessible(true); index.setInt(manager, 0);
   Company selected = live.get(0);
   System.setOut(new PrintStream(bytes)); new MatchingEngine(manager).runLookForJobs(student);
   if (live.get(0)!=selected || index.getInt(manager)!=0) throw new AssertionError("browse changed account index");
  } finally { System.setIn(in); System.setOut(out); }
  String display = bytes.toString();
  int z1=display.indexOf("Zebra Company"), a1=display.indexOf("Alpha Company");
  int a2=display.indexOf("Alpha Company",a1+1), z2=display.indexOf("Zebra Company",z1+1);
  if (z1<0 || a1<=z1 || a2<=a1 || z2<=a2) throw new AssertionError("company browse direction");
  int zj1=display.indexOf("Zebra Job"), aj1=display.indexOf("Alpha Job");
  int aj2=display.indexOf("Alpha Job",aj1+1), zj2=display.indexOf("Zebra Job",zj1+1);
  if (zj1<0 || aj1<=zj1 || aj2<=aj1 || zj2<=aj2) throw new AssertionError("nested job browse direction");
 }

}

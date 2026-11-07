package control;

import adt.ArrayList;
import adt.ListInterface;
import boundary.JobPostingUI;
import entity.JobPosting;
import entity.SkillRequirement;
import java.lang.reflect.Field;

public class JobEditingTest {
    private static class FakeUI extends JobPostingUI {
        boolean editing;
        /** Selects the first posting. */
        public int inputJobPostingIndex() { return 1; }
        /** Returns the fixture title for the current add or edit phase. */
        public String inputJobPostingTitle() { return editing ? "Edited" : "Original"; }
        /** Returns the fixture description for the current add or edit phase. */
        public String inputJobPostingDescription() { return editing ? "New description" : "Old description"; }
        /** Returns the fixture location for the current add or edit phase. */
        public String inputJobPostingLocation() { return editing ? "Penang" : "KL"; }
        /** Returns the fixture salary range for the current add or edit phase. */
        public double[] inputJobPostingSalary() { return editing ? new double[] {2000, 3000} : new double[] {1000, 2000}; }
        /** Returns the fixture experience requirement for the current phase. */
        public int inputJobPostingExperienceRequired() { return editing ? 4 : 2; }
        /** Returns the fixture experience weight for the current phase. */
        public int inputJobPostingExperienceImportance() { return editing ? 7 : 3; }
        /** Returns the fixture location weight for the current phase. */
        public int inputJobPostingLocationImportance() { return editing ? 2 : 8; }
        /** Returns the fixture skill weight for the current phase. */
        public int inputJobPostingSkillImportance() { return editing ? 9 : 5; }
        /** Suppresses job output in the test double. */
        public void listAllJobPosting(String ignored) { }
    }
    private static class FakeSkills extends SkillRequirementManager {
        final ListInterface<SkillRequirement> skills = new ArrayList<>();
        /** Initializes the fake manager with one Java requirement. */
        FakeSkills() { super(new ArrayList<>()); skills.add(new SkillRequirement("Java", 6)); }
        /** Suppresses interactive skill editing in the test double. */
        public void runSkillRequirement() { }
        /** Suppresses interactive skill editing in the test double. */
        protected void runSkillRequirement(ListInterface<SkillRequirement> existing) { }
        /** Returns the fake requirements used by the edit test. */
        protected ListInterface<SkillRequirement> getSkillRequirements() { return skills; }
    }
    /** Replaces a JobManager field with a test double through reflection. */
    private static void inject(JobManager manager, String name, Object value) throws Exception {
        Field field = JobManager.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(manager, value);
    }
    /** Throws an assertion error with the supplied message when the condition fails. */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    /** Checks that editing preserves job identity while updating its fields. */
    public static void main(String[] args) throws Exception {
        ListInterface<JobPosting> jobs = new ArrayList<>();
        JobManager manager = new JobManager(jobs);
        FakeUI ui = new FakeUI();
        FakeSkills skills = new FakeSkills();
        inject(manager, "jobPostingUI", ui);
        inject(manager, "skillRequirementManager", skills);
        manager.addJobPosting();
        JobPosting original = jobs.get(0);
        String id = original.getId();
        check(original.getLocationImprotance() == 8 && original.getExperienceImportance() == 3 && original.getSkillImportance() == 5, "add importance values");
        ui.editing = true;
        manager.updateJobPosting();
        JobPosting edited = jobs.get(0);
        check(edited == original, "edit must preserve object references");
        check(id.equals(edited.getId()), "edit must preserve ID for matching lookup");
        check(edited.getLocationImprotance() == 2 && edited.getExperienceImportance() == 7 && edited.getSkillImportance() == 9, "edit importance values");
        check(edited.getTitle().equals("Edited") && edited.getDescription().equals("New description") && edited.getLocation().equals("Penang"), "edit text fields");
        check(edited.getSalaryRange()[0] == 2000 && edited.getSalaryRange()[1] == 3000 && edited.getExperienceRequired() == 4 && edited.getRequiredSkills() == skills.skills, "edit remaining fields");
        JobPosting next = new JobPosting("Next", "", new ArrayList<>(), "", new double[] {0, 0}, 0, 1, 1, 1);
        check(Integer.parseInt(next.getId().substring(1)) == Integer.parseInt(id.substring(1)) + 1, "edit must not consume an ID");
        boolean found = false;
        for (int i = 0; i < jobs.size(); i++) if (id.equals(jobs.get(i).getId())) found = true;
        check(found, "lookup by saved job ID");
        System.out.println("JobEditingTest passed");
    }
}

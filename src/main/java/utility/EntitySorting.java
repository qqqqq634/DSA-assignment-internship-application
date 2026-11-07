package utility;

import adt.Comparison;
import adt.ListInterface;
import entity.*;

/** Default and selectable orders, all using the project's hand-written merge sort. */
public final class EntitySorting {
    /** Prevents instances because this class provides only shared sorting functions. */
    private EntitySorting() { }

    /**
     * Applies a custom comparison to the same list, swapping arguments for descending order.
     * This preserves merge-sort stability rather than reversing the finished list.
     * @param list list to reorder
     * @param comparison ascending-key comparison
     * @param ascending true for the comparison order, false for its reverse
     */
    private static <T> void sort(ListInterface<T> list, Comparison<T> comparison, boolean ascending) {
        list.sort(ascending ? comparison : (first, second) -> comparison.compare(second, first));
    }

    /** Sorts students by name A-Z and initializes each available proficiency list highest first. */
    public static void sortStudents(ListInterface<Student> students) {
        sortStudents(students, true);
        for (Student student : students) {
            if (student.getSkillProficiencies() != null) sortProficiencies(student.getSkillProficiencies());
        }
    }

    /** Sorts student names A-Z when ascending is true, otherwise Z-A; nested skill order is unchanged. */
    public static void sortStudents(ListInterface<Student> students, boolean ascending) {
        sort(students, (a, b) -> a.getName().compareToIgnoreCase(b.getName()), ascending);
    }

    /** Sorts company names A-Z, their job titles A-Z, and nested requirements highest first. */
    public static void sortCompanies(ListInterface<Company> companies) {
        sortCompanies(companies, true);
        for (Company company : companies) sortJobs(company.getJobPostings());
    }

    /** Sorts only company names in the chosen direction; nested job order is unchanged. */
    public static void sortCompanies(ListInterface<Company> companies, boolean ascending) {
        sort(companies, (a, b) -> a.getName().compareToIgnoreCase(b.getName()), ascending);
    }

    /** Sorts job titles A-Z and initializes each job requirement list highest importance first. */
    public static void sortJobs(ListInterface<JobPosting> jobs) {
        sortJobs(jobs, true);
        for (JobPosting job : jobs) sortRequirements(job.getRequiredSkills());
    }

    /** Sorts job titles A-Z or Z-A without resetting the selected order of nested requirements. */
    public static void sortJobs(ListInterface<JobPosting> jobs, boolean ascending) {
        sort(jobs, (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()), ascending);
    }

    /** Sorts skill proficiency values highest first, preserving equal-value order. */
    public static void sortProficiencies(ListInterface<SkillProficiency> skills) {
        sortProficiencies(skills, false);
    }

    /** Sorts proficiency values lowest first when ascending is true, otherwise highest first. */
    public static void sortProficiencies(ListInterface<SkillProficiency> skills, boolean ascending) {
        sort(skills, (a, b) -> a.getProficiency() < b.getProficiency() ? -1
                : a.getProficiency() > b.getProficiency() ? 1 : 0, ascending);
    }

    /** Sorts required skills highest importance first, preserving equal-value order. */
    public static void sortRequirements(ListInterface<SkillRequirement> skills) {
        sortRequirements(skills, false);
    }

    /** Sorts requirement importance lowest first when ascending is true, otherwise highest first. */
    public static void sortRequirements(ListInterface<SkillRequirement> skills, boolean ascending) {
        sort(skills, (a, b) -> a.getImportance() < b.getImportance() ? -1
                : a.getImportance() > b.getImportance() ? 1 : 0, ascending);
    }

    /** Sorts matches highest score first, keeping undefined NaN scores at the end. */
    public static void sortMatches(ListInterface<Match> matches) {
        sortMatches(matches, false);
    }

    /** Sorts scores in the chosen direction; NaN stays last in either order and ties stay stable. */
    public static void sortMatches(ListInterface<Match> matches, boolean ascending) {
        matches.sort((a, b) -> {
            double first = a.getScore(), second = b.getScore();
            // Undefined scores stay last in either direction.
            if (first != first) return second != second ? 0 : 1;
            if (second != second) return -1;
            if (first < second) return ascending ? -1 : 1;
            if (first > second) return ascending ? 1 : -1;
            return 0;
        });
    }

    /** Sorts whole interviews by earliest scheduled time without changing applicant/status pairing. */
    public static void sortInterviews(ListInterface<Interview> interviews) {
        sortInterviews(interviews, true);
    }

    /**
     * Sorts whole interviews earliest first when ascending is true, otherwise latest first.
     * Internal match lists and parallel state arrays are not reordered.
     */
    public static void sortInterviews(ListInterface<Interview> interviews, boolean ascending) {
        // Sort whole interviews; their matches and states retain their alignment.
        sort(interviews, (a, b) -> a.getScheduledTime().compareTo(b.getScheduledTime()), ascending);
    }
}

package adt;

import entity.*;
import java.time.LocalDateTime;

/** Exercises the same custom sorting API with every domain entity. */
public class EntitySortingTest {
    /**
     * Checks sorting of two values for each supported entity type.
     * @param args the args value
     */
    public static void main(String[] args) {
        Student alice = new Student("Alice", new ArrayList<>(), "Penang", 2);
        Student bob = new Student("Bob", new ArrayList<>(), "Johor", 5);
        checkFirst(alice, bob, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        checkFirst(new Company("Alpha", "Penang"), new Company("Beta", "Johor"),
                (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        JobPosting junior = new JobPosting("Junior", "", new ArrayList<>(), "Penang",
                new double[] {1000, 2000}, 0, 1, 1, 1);
        JobPosting senior = new JobPosting("Senior", "", new ArrayList<>(), "Johor",
                new double[] {3000, 4000}, 3, 1, 1, 1);
        checkFirst(junior, senior, (a, b) -> {
            double first = a.getSalaryRange()[0], second = b.getSalaryRange()[0];
            return first < second ? -1 : first > second ? 1 : 0;
        });
        checkFirst(new SkillProficiency("Java", 8), new SkillProficiency("SQL", 3),
                (a, b) -> a.getProficiency() > b.getProficiency() ? -1
                        : a.getProficiency() < b.getProficiency() ? 1 : 0);
        checkFirst(new SkillRequirement("Java", 9), new SkillRequirement("SQL", 4),
                (a, b) -> a.getImportance() > b.getImportance() ? -1
                        : a.getImportance() < b.getImportance() ? 1 : 0);
        checkFirst(new Match(alice, junior, 0.9), new Match(bob, senior, 0.3),
                (a, b) -> a.getScore() > b.getScore() ? -1
                        : a.getScore() < b.getScore() ? 1 : 0);
        checkFirst(new Interview(LocalDateTime.of(2026, 11, 1, 9, 0), new ArrayList<>(), new int[0]),
                new Interview(LocalDateTime.of(2026, 11, 2, 9, 0), new ArrayList<>(), new int[0]),
                (a, b) -> a.getScheduledTime().compareTo(b.getScheduledTime()));
        System.out.println("Entity sorting: 7 checks passed");
    }

    /**
     * Sorts two values with the supplied comparison and checks their resulting order.
     * @param first the first value
     * @param second the second value
     * @param comparison the comparison value
     */
    private static <T> void checkFirst(T first, T second, Comparison<T> comparison) {
        ListInterface<T> list = new ArrayList<>();
        list.add(second); list.add(first);
        list.sort(comparison);
        if (list.size() != 2 || list.get(0) != first || list.get(1) != second) {
            throw new AssertionError("Wrong ordering for " + first.getClass().getSimpleName());
        }
    }
}

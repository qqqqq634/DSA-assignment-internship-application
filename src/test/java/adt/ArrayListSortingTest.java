package adt;

/** Dependency-free tests. Run the main method; failures throw AssertionError. */
public class ArrayListSortingTest {
    private static int checks;

    /** Runs dependency-free regression checks for order, stability, indexing, null comparison, and serialization compatibility. */
    public static void main(String[] args) {
        Comparison<Integer> ascending = (a, b) -> a < b ? -1 : a > b ? 1 : 0;
        ListInterface<Integer> numbers = new ArrayList<>();
        numbers.sort(ascending);
        expect(numbers.size() == 0, "empty list");
        numbers.add(7);
        numbers.sort(ascending);
        expect(numbers.get(0) == 7, "single entry");
        numbers.clear();
        int[] input = {5, -2, 5, 0, 9, 1, -7, 3, 8, 2, 4, 6, 10};
        for (int value : input) numbers.add(value);
        numbers.sort(ascending);
        checkOrder(numbers, new int[] {-7, -2, 0, 1, 2, 3, 4, 5, 5, 6, 8, 9, 10});
        numbers.sort(ascending);
        checkOrder(numbers, new int[] {-7, -2, 0, 1, 2, 3, 4, 5, 5, 6, 8, 9, 10});
        numbers.sort((a, b) -> ascending.compare(b, a));
        checkOrder(numbers, new int[] {10, 9, 8, 6, 5, 5, 4, 3, 2, 1, 0, -2, -7});
        expect(numbers.remove(1) == 10, "one-based remove after sort");
        numbers.add(11);
        numbers.sort(ascending);
        expect(numbers.get(12) == 11 && numbers.size() == 13, "add after sort");
        numbers.clear();
        numbers.add(3);
        numbers.add(1);
        numbers.sort(ascending);
        checkOrder(numbers, new int[] {1, 3});

        ListInterface<Entry> entries = new ArrayList<>();
        Entry first = new Entry(2), second = new Entry(1), third = new Entry(2), fourth = new Entry(1);
        entries.add(first); entries.add(second); entries.add(third); entries.add(fourth);
        entries.sort((a, b) -> a.key < b.key ? -1 : a.key > b.key ? 1 : 0);
        expect(entries.get(0) == second && entries.get(1) == fourth
                && entries.get(2) == first && entries.get(3) == third, "stable equal keys and identity");
        entries.sort((a, b) -> 0);
        expect(entries.get(0) == second && entries.get(3) == third, "all equal keys");

        ListInterface<Integer> extremes = new ArrayList<>();
        extremes.add(Integer.MAX_VALUE); extremes.add(Integer.MIN_VALUE); extremes.add(0);
        extremes.sort(ascending);
        checkOrder(extremes, new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
        boolean rejected = false;
        try { numbers.sort(null); } catch (IllegalArgumentException expected) { rejected = true; }
        expect(rejected, "null comparison rejected");
        expect(numbers.size() == 2 && numbers.get(0) == 1, "rejected comparison preserves list");
        expect(java.io.ObjectStreamClass.lookup(ArrayList.class).getSerialVersionUID()
                == 5021716475358437778L, "existing serialized lists remain compatible");
        System.out.println("ArrayList sorting: " + checks + " checks passed");
    }

    /** Checks the list size and each zero-based value against a hand-written expected sequence. */
    private static void checkOrder(ListInterface<Integer> actual, int[] expected) {
        expect(actual.size() == expected.length, "size preserved");
        for (int i = 0; i < expected.length; i++) {
            expect(actual.get(i) == expected[i], "unexpected order at " + i);
        }
    }

    /** Counts a regression assertion and throws AssertionError with its description when it fails. */
    private static void expect(boolean condition, String message) {
        checks++;
        if (!condition) throw new AssertionError(message);
    }

    private static class Entry {
        final int key;
        /** Creates a distinct object with a sortable key so equal-key identity order can be verified. */
        Entry(int key) { this.key = key; }
    }
}

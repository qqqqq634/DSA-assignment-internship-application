package adt;

/** Defines an order without using a library comparator or sorting algorithm. */
@FunctionalInterface
public interface Comparison<T> {
    /**
     * Compares two entries without a library comparator.
     * @param first first entry to compare
     * @param second second entry to compare
     * @return negative if first comes before second, zero for equal order, positive otherwise
     */
    int compare(T first, T second);
}

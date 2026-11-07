
package adt;

import java.io.Serializable;
import java.util.Iterator;

public class ArrayList<T> implements ListInterface<T>, Serializable {

    // Preserve compatibility with lists serialized before sorting was added.
    private static final long serialVersionUID = 5021716475358437778L;

    private T[] elements;
    private int size;
    private static final int INITIAL_CAPACITY = 10;

    /** Creates an empty list with the default backing-array capacity. */
    public ArrayList() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Creates an empty list with the supplied backing-array capacity.
     * @param capacity starting array length; positive capacity is required for later growth
     */
    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        elements = (T[]) new Object[capacity];
        size = 0;
    }

    /** Appends an entry, growing the array when full, and returns true. */
    @Override
    public boolean add(T newEntry) {
        ensureCapacity();
        elements[size++] = newEntry;
        return true;
    }

    /**
     * Inserts at a one-based position and shifts later entries right.
     * @return false when the position is outside 1 through size + 1
     */
    @Override
    public boolean add(int newPosition, T newEntry) {
        if (newPosition < 1 || newPosition > size + 1) {
            return false;
        }
        ensureCapacity();
        removeGapRight(newPosition);
        elements[newPosition - 1] = newEntry;
        size++;
        return true;
    }

    /**
     * Removes a one-based position and shifts later entries left.
     * @return the removed entry, or null for an invalid position
     */
    @Override
    public T remove(int givenPosition) {
        if (givenPosition < 1 || givenPosition > size) {
            return null;
        }
        T removedElement = elements[givenPosition - 1];
        removeGapLeft(givenPosition);
        size--;
        return removedElement;
    }

    /** Marks the list empty by resetting its logical size; the backing array is retained. */
    @Override
    public void clear() {
        size = 0;
    }

    /** Reads a one-based position without removing it; invalid positions return null. */
    @Override
    public T getEntry(int givenPosition) {
        if (givenPosition < 1 || givenPosition > size) {
            return null;
        }
        return elements[givenPosition - 1];
    }

    /** Scans active entries using their equals methods; stored entries must be non-null. */
    @Override
    public boolean contains(T anEntry) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(anEntry)) {
                return true;
            }
        }
        return false;
    }

    /** Returns the number of active entries, excluding unused array capacity. */
    @Override
    public int getNumberOfEntries() {
        return size;
    }

    /** Returns whether the list currently contains no active entries. */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns false because this resizable list has no fixed logical size limit. */
    @Override
    public boolean isFull() {
        return false;
    }

    /** Doubles a full backing array and copies active entries; expects positive initial capacity. */
    private void ensureCapacity() {
        if (size == elements.length) {
            @SuppressWarnings("unchecked")
            T[] newArray = (T[]) new Object[elements.length * 2];
            System.arraycopy(elements, 0, newArray, 0, size);
            elements = newArray;
        }
    }

    /** Shifts entries right to make space at the supplied one-based insertion position. */
    private void removeGapRight(int position) {
        for (int i = size; i >= position; i--) {
            elements[i] = elements[i - 1];
        }
    }

    /** Closes a removed one-based position and clears the old final occupied slot. */
    private void removeGapLeft(int position) {
        for (int i = position - 1; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
    }

    /** Returns each active entry in list order, followed by a line break. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]).append("\n");
        }
        return sb.toString();
    }

    /** Returns the current entry count, equivalent to getNumberOfEntries(). */
    @Override
    public int size() {
        return size;
    }

    /**
     * Reads an entry using a zero-based index.
     * @throws IndexOutOfBoundsException when index is outside the active list
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        return (T) elements[index];
    }

    /**
     * Replaces an entry at a zero-based index without changing list size.
     * @throws IndexOutOfBoundsException when index is outside the active list
     */
    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        elements[index] = element; // Replace the element at the specified index
    }

    /**
     * Reorders active entries using stable merge sort: O(n log n) comparisons and O(n) temporary storage.
     * Equal keys keep their relative order; the callback defines direction and null-entry handling.
     * @param comparison custom ordering rule; no library sorting algorithm is used
     * @throws IllegalArgumentException if comparison is null
     */
    @Override
    public void sort(Comparison<T> comparison) {
        if (comparison == null) {
            throw new IllegalArgumentException("Comparison cannot be null");
        }
        if (size < 2) {
            return;
        }

        @SuppressWarnings("unchecked")
        T[] temporary = (T[]) new Object[size];
        mergeSort(0, size - 1, temporary, comparison);
    }

    /**
     * Recursively sorts the inclusive zero-based range, reusing one temporary array.
     * @param left first index in the range
     * @param right last index in the range
     * @param temporary shared merge buffer
     * @param comparison ordering rule applied during merging
     */
    private void mergeSort(int left, int right, T[] temporary, Comparison<T> comparison) {
        if (left >= right) {
            return;
        }
        int middle = left + (right - left) / 2;
        mergeSort(left, middle, temporary, comparison);
        mergeSort(middle + 1, right, temporary, comparison);
        merge(left, middle, right, temporary, comparison);
    }

    /**
     * Merges the sorted ranges left..middle and middle+1..right back into the backing array.
     * Choosing the left entry on a tie makes the overall sort stable.
     * @param left first zero-based index
     * @param middle final index of the left half
     * @param right final index of the right half
     * @param temporary shared merge buffer
     * @param comparison ordering rule
     */
    private void merge(int left, int middle, int right,
            T[] temporary, Comparison<T> comparison) {
        int first = left;
        int second = middle + 1;
        int destination = left;

        while (first <= middle && second <= right) {
            // Taking the left entry on a tie preserves the original order.
            if (comparison.compare(elements[first], elements[second]) <= 0) {
                temporary[destination++] = elements[first++];
            } else {
                temporary[destination++] = elements[second++];
            }
        }
        while (first <= middle) {
            temporary[destination++] = elements[first++];
        }
        while (second <= right) {
            temporary[destination++] = elements[second++];
        }
        for (int index = left; index <= right; index++) {
            elements[index] = temporary[index];
        }
    }

    // Implement the Iterable interface
    /** Creates a new forward iterator for the active entries in their current order. */
    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    // Inner class for the iterator
    private class ArrayListIterator implements Iterator<T> {
        private int currentIndex = 0;

        /** Reports whether the iterator cursor is still before the current logical list size. */
        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        /** Returns the current backing-array entry and advances; callers must check hasNext first. */
        @Override
        public T next() {
            return elements[currentIndex++];
        }
    }

}

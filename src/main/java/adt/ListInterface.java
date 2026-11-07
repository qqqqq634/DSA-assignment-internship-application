
package adt;

public interface ListInterface<T> extends Iterable<T> {

  /** Appends an entry at the end of the list and reports success. */
  public boolean add(T newEntry);

  /** Inserts at a one-based position; invalid positions return false. */
  public boolean add(int newPosition, T newEntry);

  /** Removes a one-based position and returns its entry, or null for an invalid position. */
  public T remove(int givenPosition);

  /** Removes all entries from the logical list. */
  public void clear();

  /** Reads a one-based position, returning null when the position is invalid. */
  public T getEntry(int givenPosition);

  /** Reports whether an equal entry exists; equality is determined by stored entries. */
  public boolean contains(T anEntry);

  /** Returns the current number of list entries. */
  public int getNumberOfEntries();

  /** Reports whether the list contains zero entries. */
  public boolean isEmpty();

  /** Reports whether the implementation has reached a fixed capacity limit. */
  public boolean isFull();

  /** Returns the entry count for zero-based list operations. */
  public int size();

  /** Reads a zero-based index, throwing IndexOutOfBoundsException when invalid. */
  public T get(int index);

  /** Replaces a zero-based index without changing size; invalid indexes throw. */
  public void set(int index, T element);

  /**
   * Stably reorders the existing list using a custom comparison.
   * @param comparison direction and key comparison; must handle null entries when present
   */
  public void sort(Comparison<T> comparison);
}

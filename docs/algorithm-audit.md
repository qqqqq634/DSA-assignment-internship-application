# Algorithm library audit

Date: 2026-10-30. Scope: every Java file under `src/main/java` and `src/test/java`.

## Result

No `java.util` algorithm implementation, built-in collection, stream helper, comparator, sorting helper, or binary-search helper is used by the current source. All seven entity sort calls resolve to the project's `adt.ListInterface.sort(Comparison<T>)` and hand-written merge sort in `adt.ArrayList`.

Removed during this audit:

- The unused `java.util.Comparator` import in `utility/SearchUtil.java`.
- `java.util.Arrays.stream` and `java.util.stream.Collectors.joining` from the identity regression test; classpath assembly now uses a loop and `StringBuilder`.

## Remaining java.util imports

| Type | Where | Purpose |
| --- | --- | --- |
| `Scanner` | Nine classes in `boundary` | Console input only |
| `InputMismatchException` | Nine classes in `boundary` | Input validation only |
| `Iterator` | `adt/ArrayList.java` | Standard interface required by `Iterable`; traversal itself is implemented by the custom `ArrayListIterator` |

These are not delegated sorting/searching algorithms or library collection implementations. This audit applies the stated ban to algorithm helpers; it does not remove console input support or standard iteration interfaces.

## Implemented algorithms

- Array-backed storage, growth, insertion, removal, and traversal: `adt.ArrayList`.
- Stable merge sort with manual merging: `adt.ArrayList`.
- Ordering rules for the seven entities: `utility.EntitySorting`, through custom `adt.Comparison` callbacks.
- Levenshtein distance and threshold check: `utility.SearchUtil`, using explicit dynamic-programming loops. Job-title search now invokes this helper automatically, with exact/substring/typo relevance ranked by the custom merge sort and no user-entered distance.
- Student/job matching and weighted scores: `control.MatchingEngine`, using explicit list loops.

`String` comparisons, date comparisons, numeric operations, serialization, and file I/O remain standard Java APIs. None delegates a DSA algorithm to `java.util`.

## Verification

The final source scan accepts only the three listed `java.util` types across production and test Java files. The regression runner includes thirteen suites, including job editing, identity restoration, persistence failures, custom sorting, and selectable sort directions. Original supplied `.dat` files are preserved.

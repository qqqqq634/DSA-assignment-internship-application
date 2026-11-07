# Internship Application Program

A Java 17 console application for applicant profiles, company profiles, job postings, skill management, job matching, and interview scheduling. Data is held in a custom array-backed list and persisted through Java object serialization.

## Source scan summary

Scanned `src/main/java` on 2026-10-30: **42 Java files** across six packages plus the application entry point. This README describes the current source and the custom sorting implementation.

| Location | Files | Responsibility |
| --- | ---: | --- |
| Root | 1 | Application entry point |
| `adt` | 3 | Custom generic list interface and implementation |
| `boundary` | 9 | Console menus, input, and display |
| `control` | 8 | Application workflows and business logic |
| `dao` | 11 | Serialized file access and sample data initializers |
| `entity` | 7 | Domain objects |
| `utility` | 3 | Shared messages, string similarity, and entity sort orders |

## Application flow

`InternshipApplicationProgram.main()` creates `control.Main` and calls `runMain()`.

- Applicants can sign up, log in by ID, manage their profile and skills, search for jobs, apply to matches, and view interviews.
- Companies can sign up, log in by ID, manage their profile and job postings, and schedule interviews.
- Managers coordinate console UI classes, entity lists, and DAO file operations.

## Package and class inventory

### Data structures: `adt`

| Class | Current behavior |
| --- | --- |
| `ListInterface<T>` | Defines add, remove, clear, contains, size, get, set, and iteration operations. Extends `Iterable<T>`. |
| `ArrayList<T>` | Custom resizable array, iterator, serialization, and stable merge sort. |
| `Comparison<T>` | Custom functional interface defining how two entries are ordered; no library comparator is needed. |

**Indexing matters:** `get(index)` and `set(index, value)` use zero-based indexes. `getEntry(position)`, `add(position, value)`, and `remove(position)` use one-based positions. Sorting code using `get` and `set` must use zero-based indexes.

### Console interface: `boundary`

| Class | Responsibility |
| --- | --- |
| `MainUI` | Applicant, company, and exit menu |
| `ApplicantProfileUI` | Applicant login, signup, profile updates, and input |
| `CompanyUI` | Company login, signup, profile updates, and input |
| `JobPostingUI` | Job CRUD menus, salary, experience, and matching importance inputs |
| `SkillProficiencyUI` | Applicant skill CRUD menus and proficiency input |
| `SkillRequirementUI` | Job skill CRUD menus and importance input |
| `MatchUI` | Title-only job search, results, application selection, and sort controls |
| `SortUI` | Shared ascending/descending input using the active UI scanner |
| `InterviewUI` | Company/student interview menus, date-time input, and applicant decisions |

### Business logic: `control`

| Class | Responsibility |
| --- | --- |
| `Main` | Routes the main menu to applicant and company workflows |
| `ApplicantManager` | Loads students; handles signup, login, profile CRUD, skills, matching, and student interview access |
| `CompanyManager` | Loads/saves companies; handles profile CRUD, company jobs, interviews, and searching across companies |
| `JobManager` | Adds, displays, updates, deletes, and searches job postings |
| `SkillProficiencyManager` | Manages a student's skill names and proficiency values |
| `SkillRequirementManager` | Manages a job's required skills and importance values |
| `MatchingEngine` | Calculates student/job scores, displays matches, records applications, and retrieves matches for interviews |
| `InterviewSchedulerManager` | Loads/saves interviews, schedules company interviews, and displays student interview details |

### Persistence and seed data: `dao`

| Class | Responsibility |
| --- | --- |
| `CompanyDAO` | Reads/writes company lists in `Company.dat` |
| `StudentDAO` | Reads/writes student lists in `Student.dat` |
| `MatchDAO` | Reads/writes match lists in `Match.dat` |
| `InterviewDAO` | Reads/writes interview lists in `Interview.dat` |
| `CompanyInitializer` | Builds sample companies with job postings; its `main` writes company seed data |
| `StudentInitializer` | Builds sample students with skills; its `main` writes student seed data |
| `JobPostingInitializer` | Builds sample jobs, salary ranges, requirements, and matching weights |
| `SkillProficiencyInitializer` | Supplies sample proficiency lists for different roles |
| `SkillRequiredInitializer` | Supplies sample required-skill lists for different roles |
| `MatchInitializer` | Empty placeholder class |
| `InterviewInitializer` | Empty placeholder class |

DAO paths are relative to the process working directory. Jobs and required skills are nested inside companies; proficiency lists are nested inside students. They do not have separate DAO files. Seed initializer entry points overwrite their corresponding data files when run.

### Domain objects: `entity`

| Class | Data and relationships |
| --- | --- |
| `Company` | ID, name, location, and job postings |
| `Student` | ID, name, location, experience, and skill proficiencies |
| `JobPosting` | ID, title, description, location, salary range, experience requirement, required skills, and matching importance weights |
| `SkillProficiency` | Skill name and proficiency |
| `SkillRequirement` | Skill name and importance |
| `Match` | Student, job posting, and calculated score |
| `Interview` | ID, scheduled date/time, matches, and an array of per-match states |

All seven entity classes implement `Serializable`. None defines a natural order through `Comparable`.

### Helpers: `utility`

- `MessageUI`: shared console messages.
- `EntitySorting`: default orders for all seven entities, using the custom merge sort. Students include sorted proficiencies; companies include sorted jobs and required skills.
- `SearchUtil`: hand-written dynamic-programming Levenshtein distance and automatic job-title matching/ranking. The search flow calculates typo tolerance internally; users enter only a job title.

## Current search and sorting behavior

- Job search asks only for a title. Exact titles rank first, then substring matches, then close typo matches found with Levenshtein distance. Input is trimmed and case-insensitive; blank and unrelated searches return no results.
- Automatic typo tolerance is based on query length and capped at three edits. One- and two-character queries do not receive fuzzy matching. Multiword queries are compared against title-word windows, so `Java Developr` can match `Senior Java Developer`.
- Search relevance is combined across companies before display. In the application search menu, title relevance comes before applicant/job match score until the user explicitly selects score sorting. The selected index follows the displayed order. Normal match-list views use the selected score direction.
- `MatchingEngine.listAllMatches` now sorts matches by highest score first before displaying them, keeping selection indexes aligned. Equal scores retain their original order; undefined (`NaN`) scores go last.
- `ListInterface.sort(Comparison<T>)` is implemented by the custom `ArrayList` using hand-written stable merge sort: O(n log n) time and O(n) temporary storage.
- Sorting uses no `java.util` comparator, collection, array sorting, or copying helper. Existing iterator code still uses `java.util.Iterator`.
- Default sorting is now connected to every entity manager. Orders are applied when accounts are loaded/selected and when lists are displayed or returned by job searches. Users can change direction through the menu controls below.
- A null comparison throws `IllegalArgumentException`. Comparisons must define a consistent order and handle null entries if the list contains any.
- The original `ArrayList` serialization identifier is preserved for existing `.dat` files.

## Sorting options for the entities

The default orders below are implemented in the application through `utility.EntitySorting`. The default keys support ascending and descending order through the controls below. Other candidate keys remain available through custom comparisons; they are not menu options.

| Entity | Other available keys and existing getters | Implemented default order |
| --- | --- | --- |
| `Company` | ID (`getId`), name (`getName`), location (`getLocation`), job count (`getJobPostings().size()`) | Name A-Z |
| `Student` | ID (`getId`), name (`getName`), location (`getLocation`), experience (`getExperience`), skill count (`getSkillProficiencies().size()`) | Name A-Z |
| `JobPosting` | ID, title, description, location, minimum/maximum salary (`getSalaryRange()[0/1]`), experience required, required-skill count, and location/experience/skill importance | Title A-Z |
| `SkillProficiency` | Skill name (`getSkillName`), proficiency (`getProficiency`) | Proficiency highest first |
| `SkillRequirement` | Skill name (`getSkillName`), importance (`getImportance`) | Importance highest first |
| `Match` | Score (`getScore`), student ID/name via `getStudent`, job ID/title via `getJobPosting` | Score highest first |
| `Interview` | ID (`getId`), scheduled time (`getScheduledTime`), applicant count (`getMatches().size()`) | Scheduled time earliest first |

`JobPosting` exposes `getLocationImprotance()` with that exact spelling. Other weight getters are `getExperienceImportance()` and `getSkillImportance()`.

Account lists are sorted before login ID lookup so stored account indexes remain valid throughout the session. The all-company view sorts a list copy. Interview lists are sorted before student index mappings are built, and their internal match/state order is preserved. Both skill-delete operations use the list's one-based positions, matching the displayed row numbers.

### Changing ascending and descending order

Choose a sort-control option, then enter **1 for ascending** or **2 for descending**. Direction applies to the existing key for that view (title, proficiency, importance, score, or interview date).

| View | Control |
| --- | --- |
| Job postings | Option 6: Change sort order; titles A-Z or Z-A |
| Skill proficiencies | Option 6: Change sort order; proficiency lowest or highest first |
| Skill requirements | Option 6: Change sort order; importance lowest or highest first |
| Find Job menu | Option 4: Change match score order |
| Application results | Enter -2 to change score sort order, then continue selecting the displayed match number |
| Find Job menu / all jobs | Option 5: Change company/job display order; company names and job titles A-Z or Z-A |
| Company or student interview menu | Option 3: Change interview date order; earliest or latest first |

Existing exit options remain unchanged. Sort choices stay in the current manager session; they are not stored as account settings. Reversing direction preserves the order of equal keys, and undefined match scores remain last. Interview direction changes rebuild student index mappings without changing applicant/status pairing. Individual profile display does not undo the order selected in the job or skill menus.

The `EntitySorting.sortStudents(list, ascending)` and other two-argument overloads expose direction control for all seven entity types. The application has no public all-students directory, so it does not add a student-directory menu.

### Using the custom sorting API

`Comparison<T>.compare(first, second)` returns a negative number when the first entry should come first, zero for a tie, and a positive number when it should come after the second. A lambda implements this project-defined interface directly.

Managers call the corresponding `EntitySorting.sortStudents`, `sortCompanies`, `sortJobs`, `sortProficiencies`, `sortRequirements`, `sortMatches`, and `sortInterviews` methods. The following examples show how to write alternative comparisons directly:

```java
// Student names A-Z.
students.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));

// Company names A-Z.
companyList.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));

// Minimum salary: lowest first.
jobPostings.sort((a, b) -> {
    double first = a.getSalaryRange()[0];
    double second = b.getSalaryRange()[0];
    if (first < second) return -1;
    if (first > second) return 1;
    return 0;
});

// Applicant proficiency: highest first.
skillProficiencys.sort((a, b) -> {
    if (a.getProficiency() > b.getProficiency()) return -1;
    if (a.getProficiency() < b.getProficiency()) return 1;
    return 0;
});

// Required skill importance: highest first.
skillRequirements.sort((a, b) -> {
    if (a.getImportance() > b.getImportance()) return -1;
    if (a.getImportance() < b.getImportance()) return 1;
    return 0;
});

// Match score: highest first (for finite scores).
newMatches.sort((a, b) -> {
    if (a.getScore() > b.getScore()) return -1;
    if (a.getScore() < b.getScore()) return 1;
    return 0;
});

// Interview time: earliest first.
interviews.sort((a, b) -> a.getScheduledTime().compareTo(b.getScheduledTime()));
```

`String.compareToIgnoreCase` and `LocalDateTime.compareTo` compare individual values; merge sort itself is implemented in this project. The examples assume non-null keys and valid salary ranges.

To reverse any comparison without a library helper:

```java
adt.Comparison<Student> byName =
        (a, b) -> a.getName().compareToIgnoreCase(b.getName());
students.sort((a, b) -> byName.compare(b, a));
```

The list is reordered in place; its size and object references are preserved. Equal keys retain their original relative order. Do not change the list from inside a comparison.

Before applying sorts, account for these relationships:

- IDs such as `S2` and `S10` sort lexicographically as `S10`, `S2`. Compare their numeric suffixes if numeric ID order is intended.
- `Interview.state[i]` belongs to `Interview.matches.get(i)`. Sorting matches alone would associate states with the wrong applicants; reorder them together or sort a display copy containing both values.
- Applicant/company managers and interview views retain indexes. Sorting their underlying lists can change what an index points to; retain selection by stable ID or recompute indexes.
- Decide whether sorting changes only a displayed copy or the stored list, since saving a reordered list also persists its order.

## Algorithm library restrictions

Sorting and data-structure operations use the custom implementation. The source and test audit found no `java.util` algorithm, collection, comparator, or stream helper usage after cleanup. Remaining `java.util` imports are console input types and the interface for the custom iterator. See [the algorithm audit](docs/algorithm-audit.md) for the complete inventory.

## Correctness and persistence scan

The source scan identified 10 issues. All ten reported persistence, identity, lookup, duplicate-record, score, and job-edit bugs are fixed and covered by regression tests. [The scan report](docs/code-scan.md) preserves the original evidence and records the repair status.

All seven entity sorts remain connected. Tests run against isolated data copies, and the supplied `.dat` files are preserved.

## Build and run

The Maven configuration targets Java 17 and declares no application dependencies. From the project root, with JDK 17 and Maven installed:

```sh
mvn compile
java -cp target/classes InternshipApplicationProgram
```

Run from the project root so the DAO paths resolve to the supplied `.dat` files. All production sources and the dependency-free sorting tests can be compiled and checked with:

```sh
bash scripts/test-sorting.sh
```

The script requires JDK 17+ (`java` and `javac` on PATH). The thirteen suites include the existing 90 sorting checks plus persistence regression suites covering duplicate application prevention, interview save/transfer failures, ID restoration, applicant saves, lookup after reload, normalized scores, job editing that preserves IDs and the correct importance weights, title-only fuzzy search with correct application selection, and ascending/descending UI controls with stable row selection. The tests use copies of the supplied `.dat` files under `target/sort-fixtures`; DAO saves during workflow tests only affect those copies.

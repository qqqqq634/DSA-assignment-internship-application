# Correctness and persistence scan

Date: 2026-10-30. Scope: the current `src/main/java` application, focusing on duplicate records, file saves, record identity, and matching/interview workflows. The current version includes the custom sorting integration.

**Original scan: 10 actionable findings.** Eight were reproduced with Java 17 against isolated copies or synthetic fixtures under `target/persistence-scan`; the final two were validated from source. The descriptions and line references below record that pre-fix scan. The four original `.dat` files are kept unchanged.

## Repair status

Findings 1–10 are now repaired. The final two repairs preserve the existing job object and ID during editing, and assign location/experience importance in the correct order. The original findings below are retained as historical evidence; verification is recorded after the full regression run.

- ID counters advance from IDs encountered while deserializing existing entities; their original serialization identifiers are preserved.
- Applicant profiles save on leaving the profile workflow.
- Company interview return exits normally; one interview object is saved per scheduled slot.
- Applications are selected without deleting them, and removal happens only after the interview save succeeds. Failed writes keep the previous application list.
- Applications and lookups use student/job IDs. Repeated pair insertion is rejected, and company interview filtering yields each interview once.
- Skill weighting uses the same scale as other components, with zero total weight returning zero.

Regression coverage is in `IdentityPersistenceTest`, `InterviewPersistenceTest`, `MatchingPersistenceTest`, and `JobEditingTest`, alongside the existing sorting suite. Run `bash scripts/test-sorting.sh` with JDK 17+. Each suite uses its own copies of the supplied data, with a separate fixture directory for each run. The regression runner includes thirteen test suites; the production sources compile with Java 17. Original data bytes and the four entity serialization identifiers were verified unchanged.

## Original findings

The DAOs use `new FileOutputStream(file)`, which rewrites the serialized list. Repeated calls do not append another serialization stream to the file. The duplicate-record bugs originate in how lists and IDs are built before saving.

## High priority

### 1. IDs are reused after restarting the application

Locations: `src/main/java/entity/Company.java:12`, `Student.java:13`, `JobPosting.java:18`, `Interview.java:14`.

Each class assigns IDs from a static `COUNTER`. Java serialization does not restore static counters, and DAO loading does not initialize them from stored records. After a fresh JVM loads existing records, newly created entities can reuse their IDs. This can make ID-based login and job/interview associations ambiguous.

Runtime result against copies of the supplied files: the first newly created company, student, and job all collided with existing IDs. The interview class uses the same counter pattern (source validation).

Suggested fix: restore each counter from the maximum stored numeric suffix before creating new records, or use a persisted ID allocator. Check for collisions before insertion. Keep IDs unchanged when editing existing records.

### 2. Applicant changes are never saved by the normal profile workflow

Location: `src/main/java/control/ApplicantManager.java:38`.

On exit, `runApplicantProfile()` calls `retrieveFromFile()` and discards the returned list. It never calls `studentDAO.saveToFile(students)`. Signup, profile edits, skills, and account deletion therefore remain only in memory.

Runtime reproduction: log in, rename the applicant, log out, and leave the applicant menu. Observed `memory=Changed`, `disk=Applicant`.

Suggested fix: save the current student list at the intended persistence boundary and propagate save failures to the caller.

### 3. The company interview menu cannot exit normally or reach its save

Locations: `src/main/java/control/InterviewSchedulerManager.java:51`, `:59`, `:63`; `src/main/java/boundary/InterviewUI.java:33`.

The UI accepts only options 1 and 2, with option 2 meaning return. The controller exits only on option 3. Choosing return prints the exit message and prompts again, so the save after the loop is unreachable through valid menu choices.

Runtime reproduction: provide input `2`. The scheduler requests more input instead of returning; a finite input stream then ends with `NoSuchElementException`.

Suggested fix: make the loop terminate on option 2, and address finding 4 before enabling the existing merge/save block.

### 4. Interview creation and merging duplicate the same scheduled slot

Locations: `src/main/java/control/InterviewSchedulerManager.java:225`, `:227`, `:60`, `:275`.

`scheduleInterview()` creates one interview directly in `this.interviews` and a second interview in its returned list. Both represent the same slot but receive different IDs. The caller adds returned interviews to `companyInterviews`, then its post-loop merge appends copies of every entry to the main list, including existing interviews. The company filter also copies an interview once for each matching applicant rather than once per interview.

Runtime reproduction: one scheduling action produced `main_count=1`, `returned_count=1`, with IDs `I1` and `I2`. Filtering one interview containing two applicants produced two results.

Important qualification: finding 3 currently prevents the normal company menu from reaching the post-loop save. The duplicate objects are reproduced in memory; the duplicate merge/save path is source-confirmed and becomes reachable when that exit bug is fixed.

Suggested fix: construct one interview per slot, add it at one ownership boundary, return/filter existing interviews once, and save the authoritative list once. Do not re-add existing interviews on exit.

### 5. Applicants are removed and saved before an interview is safely stored, and adjacent records are skipped

Location: `src/main/java/control/MatchingEngine.java:292` (especially `:298` and `:301`), called by `InterviewSchedulerManager.java:235`.

`getMatchesBySeletedJob()` removes from the source list while iterating forwards. The next entry shifts into the removed position and is skipped. It then immediately saves the shortened `Match.dat`, before interview date/decisions and the interview save complete. A scheduling failure or the company exit bug can leave removed applications without a persisted interview.

Runtime reproduction: two adjacent applications for the selected job produced only one selected record and left one record on disk.

Suggested fix: collect without deleting first. Commit application removal only after the interview is successfully persisted; when removal is needed, iterate backwards or retain the current index after removing. Handle partial failure explicitly.

## Medium priority

### 6. Applying to the same job twice persists duplicate applications

Locations: `src/main/java/control/MatchingEngine.java:64`, `:133`.

Every selection unconditionally appends a new match. There is no uniqueness check for the student ID/job ID pair, either within the current session or against previously saved applications.

Runtime reproduction: apply to match 0 twice, then leave the job-search menu. `Match.dat` contained two records for one student/job pair.

Suggested fix: reject an existing student/job pair before adding it. A single save still writes duplicates if the list already contains them.

### 7. Student and company lookups lose associations after separate file loads

Locations: `src/main/java/control/MatchingEngine.java:147`; `src/main/java/control/InterviewSchedulerManager.java:275`; `src/main/java/adt/ArrayList.java:72`.

`findStudentMatches()` compares student objects with inherited reference equality. The company interview filter uses `contains()` on job objects, also relying on reference equality. Students/jobs inside independently deserialized match/interview files are different object instances even when IDs match.

Runtime reproduction: a persisted application with the selected student's ID was present, but `findStudentMatches()` returned zero. The company interview filter returned two copies with shared object references (finding 4), but zero when given separately reloaded company jobs.

Suggested fix: compare stable IDs in these lookups, or define consistent entity equality based on immutable IDs. Fix ID uniqueness first.

### 8. Match scoring weights the skill component ten times too strongly

Locations: `src/main/java/control/MatchingEngine.java:214`, `:241`, `:261`, `:288`.

The denominator uses `skillImportance / 10.0`, but `calculateSkillScore()` multiplies its normalized skill score by the unscaled `skillImportance`. Location and experience use scaled weights, so the components are inconsistent.

Runtime reproduction: proficiency 10, required-skill importance 10, exact location, sufficient experience, and all three weights 10 produced score **4.0**, instead of the documented normalized perfect score **1.0**. All-zero top-level weights also leave the final denominator at zero.

Suggested fix: use the same weight scale for every component and define a zero-total-weight result. Test perfect, partial, absent-skill, and all-zero-weight cases before relying on sorted rankings.

### 9. Editing a job changes its ID and breaks existing references

Location: `src/main/java/control/JobManager.java:141` and `:146`.

Source-confirmed: update creates a fresh `JobPosting` through the allocating constructor, then replaces the existing entry. This generates a new ID instead of preserving the edited job's ID. Saved matches and interviews can continue pointing to the old ID.

Suggested fix: mutate the existing job's editable fields, or copy the old ID into a replacement object before storing it. Treat the ID as immutable.

### 10. Location and experience importance values are passed in reverse order

Locations: `src/main/java/control/JobManager.java:88`, `:145`; constructor contract at `src/main/java/entity/JobPosting.java:35`.

Source-confirmed: the constructor expects location importance followed by experience importance. Both add and update pass experience importance followed by location importance. Distinct user-entered weights are therefore assigned to the wrong dimensions.

Suggested fix: pass the arguments in the declared order. Add a regression fixture with deliberately different values so a swap cannot pass unnoticed.

## Verification and limits

Before the fixes, the isolated probe printed:

```text
RELOAD_ID_COLLISION company=true student=true job=true
APPLICANT_SAVE memory=Changed disk=Applicant
STUDENT_LOOKUP matching_student_id_exists=true found=0
PERFECT_MATCH_SCORE expected=1.0 actual=4.0
ADJACENT_APPLICATIONS expected_selected=2 actual_selected=1 remaining_on_disk=1
SCHEDULE_CREATES_TWO_OBJECTS main_count=1 returned_count=1 main_id=I1 returned_id=I2
COMPANY_INTERVIEW_FILTER one_interview_two_applicants=2 after_separate_reload=0
REPEATED_APPLICATION persisted_pairs=2 expected_unique_pairs=1
COMPANY_EXIT returned=false requested_more_input_after_exit=true
```

The probe source and isolated data are under `target/persistence-scan` (ignored build artifacts). The earlier sorting suite covered 90 checks. The runner now also includes the three persistence regression suites described above. This scan is not a guarantee that no other bugs exist.

All ten findings from this scan have been addressed. This change prevents new instances of the reported bugs; it does not rewrite or deduplicate historical records in the supplied data files.

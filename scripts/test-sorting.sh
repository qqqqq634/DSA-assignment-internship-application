#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p target/classes target/sort-tests
find src/main/java -name '*.java' > target/sort-main-sources.txt
find src/test/java -name '*.java' > target/sort-test-sources.txt
javac --release 17 -d target/classes @target/sort-main-sources.txt
javac --release 17 -cp target/classes -d target/sort-tests @target/sort-test-sources.txt
sort_test_classpath="$PWD/target/classes:$PWD/target/sort-tests"
mkdir -p target/sort-fixtures
sort_run_dir="$(mktemp -d "$PWD/target/sort-fixtures/run-XXXXXX")"
for test in \
    adt.ArrayListSortingTest \
    adt.EntitySortingTest \
    control.MatchSortingTest \
    control.EntitySortingIntegrationTest \
    control.InterviewStudentSortingTest \
    control.MatchingPersistenceTest \
    control.InterviewPersistenceTest \
    control.IdentityPersistenceTest \
    control.JobEditingTest \
    control.AutomaticJobSearchTest \
    utility.SortDirectionTest \
    control.JobSkillSortDirectionTest \
    control.MatchInterviewSortDirectionTest; do
    # Each workflow gets fresh data copies; tests cannot alter supplied files
    # or depend on data mutations performed by a previous test class.
    sort_fixture_dir="$sort_run_dir/$test"
    mkdir -p "$sort_fixture_dir"
    for data in Company.dat Student.dat Match.dat Interview.dat; do
        cp "$data" "$sort_fixture_dir/"
    done
    if (cd "$sort_fixture_dir" && java -cp "$sort_test_classpath" "$test") > "$sort_fixture_dir/test-output.log" 2>&1; then
        printf '%s: PASS\n' "$test"
    else
        cat "$sort_fixture_dir/test-output.log"
        exit 1
    fi
done

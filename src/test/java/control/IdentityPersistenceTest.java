package control;

import adt.ArrayList;
import adt.ListInterface;
import dao.StudentDAO;
import entity.Company;
import entity.Interview;
import entity.JobPosting;
import entity.SkillProficiency;
import entity.SkillRequirement;
import entity.Student;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/** Dependency-free regression checks. Run from the repository root after compiling main and test sources. */
public final class IdentityPersistenceTest {
    /**
     * Prevents instantiation of this test utility.
     */
    private IdentityPersistenceTest() { }

    /**
     * Runs counter-restoration and applicant signup, edit, and deletion persistence checks; a mode argument runs a child profile flow.
     * @param args the args value
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            runApplicantFlow(args[0]);
            return;
        }
        verifyCounterRestoration(Company.class, "C", () -> new Company("new", "here"), "C42");
        verifyCounterRestoration(Student.class, "S", () -> new Student("new", new ArrayList<>(), "here", 0), "Sbad");
        verifyCounterRestoration(JobPosting.class, "J", () -> new JobPosting("new", "d", new ArrayList<SkillRequirement>(), "here", new double[] {1, 2}, 0, 0, 0, 0), "J42");
        verifyCounterRestoration(Interview.class, "I", () -> new Interview(LocalDateTime.now(), new ArrayList<>(), new int[0]), "I42");
        runFlow("signup", "2\nApplicant\nKuala Lumpur\n1\n5\n\n3\n");
        assertStudentCount(1, "signup");
        runFlow("edit", "1\nS1\n2\n1\nRenamed\n6\n3\n");
        assertStudentName("Renamed", "edit");
        runFlow("delete", "1\nS1\n3\ny\n3\n");
        assertStudentCount(0, "delete");
        System.out.println("IdentityPersistenceTest: all checks passed");
    }

    /**
     * Checks that deserialization restores the next ID and tolerates malformed stored IDs.
     * @param type the type value
     * @param prefix the prefix value
     * @param factory the factory value
     * @param malformedId the malformedId value
     */
    private static void verifyCounterRestoration(Class<?> type, String prefix, Factory factory, String malformedId) throws Exception {
        Object stored = sample(type);
        setId(stored, prefix + "41");
        Path fixture = Files.createTempFile("identity-", ".ser");
        try {
            write(fixture, stored);
            setCounter(type, 0);
            Object loaded = read(fixture);
            check(loaded != null, type.getSimpleName() + " deserialized");
            Object next = factory.create();
            check((prefix + "42").equals(id(next)), type.getSimpleName() + " counter restored, got " + id(next));

            setId(loaded, malformedId);
            write(fixture, loaded);
            setCounter(type, 0);
            read(fixture); // A malformed historical ID must not prevent loading.
        } finally {
            Files.deleteIfExists(fixture);
        }
    }

    /**
     * Creates a representative entity instance for the requested entity type.
     * @param type the type value
     * @return creates a representative entity instance for the requested entity type.
     */
    private static Object sample(Class<?> type) {
        if (type == Company.class) return new Company("old", "there");
        if (type == Student.class) return new Student("old", new ArrayList<>(), "there", 1);
        if (type == JobPosting.class) return new JobPosting("old", "d", new ArrayList<SkillRequirement>(), "there", new double[] {1, 2}, 1, 1, 1, 1);
        return new Interview(LocalDateTime.now(), new ArrayList<>(), new int[0]);
    }

    /**
     * Serializes the supplied object to the specified fixture file.
     * @param path the path value
     * @param value the value value
     */
    private static void write(Path path, Object value) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) { out.writeObject(value); }
    }

    /**
     * Deserializes and returns the object stored in the specified fixture file.
     * @param path the path value
     * @return deserializes and returns the object stored in the specified fixture file.
     */
    private static Object read(Path path) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) { return in.readObject(); }
    }

    /**
     * Reads an entity identifier through its public getter using reflection.
     * @param value the value value
     * @return reads an entity identifier through its public getter using reflection.
     */
    private static String id(Object value) throws Exception {
        return (String) value.getClass().getMethod("getId").invoke(value);
    }

    /**
     * Sets an entity identifier through reflection for persistence test setup.
     * @param value the value value
     * @param id the id value
     */
    private static void setId(Object value, String id) throws Exception {
        Field field = value.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(value, id);
    }

    /**
     * Sets an entity ID allocator through reflection for persistence test setup.
     * @param type the type value
     * @param value the value value
     */
    private static void setCounter(Class<?> type, int value) throws Exception {
        Field field = type.getDeclaredField("COUNTER");
        field.setAccessible(true);
        field.setInt(null, value);
    }

    /**
     * Runs a profile action in a child JVM with fixture data and scripted input.
     * @param mode the mode value
     * @param input the input value
     */
    private static void runFlow(String mode, String input) throws Exception {
        Path fixture = Path.of("target", "identity-persistence", mode);
        Files.createDirectories(fixture);
        Files.deleteIfExists(fixture.resolve("Student.dat"));
        if (!mode.equals("signup")) {
            Student seed = new Student("Applicant", new ArrayList<SkillProficiency>(), "Kuala Lumpur", 1);
            setId(seed, "S1");
            ListInterface<Student> seeded = new ArrayList<>();
            seeded.add(seed);
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(fixture.resolve("Student.dat")))) {
                out.writeObject(seeded);
            }
        }
        String javaExe = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        String[] classpathEntries = System.getProperty("java.class.path").split(java.io.File.pathSeparator);
        StringBuilder classpathBuilder = new StringBuilder();
        for (int i = 0; i < classpathEntries.length; i++) {
            if (i > 0) classpathBuilder.append(java.io.File.pathSeparator);
            classpathBuilder.append(Path.of(classpathEntries[i]).toAbsolutePath());
        }
        String classpath = classpathBuilder.toString();
        Process process = new ProcessBuilder(javaExe, "-cp", classpath, IdentityPersistenceTest.class.getName(), mode)
                .directory(fixture.toFile()).redirectErrorStream(true).start();
        try (OutputStream out = process.getOutputStream()) { out.write(input.getBytes(java.nio.charset.StandardCharsets.UTF_8)); }
        String output = new String(process.getInputStream().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        int status = process.waitFor();
        check(status == 0, mode + " flow exited " + status + ": " + output);
    }

    /**
     * Runs an applicant profile action and checks the persisted result.
     * @param mode the mode value
     */
    private static void runApplicantFlow(String mode) throws Exception {
        System.setIn(System.in);
        ApplicantManager manager = new ApplicantManager();
        shareApplicantScanners(manager);
        manager.runApplicantProfile();
        ListInterface<Student> saved = new StudentDAO().retrieveFromFile();
        if (mode.equals("signup")) check(saved.size() == 1, "signup persisted");
        if (mode.equals("edit")) check("Renamed".equals(saved.get(0).getName()), "edit persisted");
        if (mode.equals("delete")) check(saved.isEmpty(), "delete persisted");
    }

    /**
     * Checks the number of student records persisted by the named profile action.
     * @param expected the expected value
     * @param label the label value
     */
    private static void assertStudentCount(int expected, String label) throws Exception {
        ListInterface<Student> students = readStudents(Path.of("target", "identity-persistence", label, "Student.dat"));
        check(students.size() == expected, label + " persisted count " + students.size());
    }

    /**
     * Checks the first persisted student name produced by the named profile action.
     * @param expected the expected value
     * @param label the label value
     */
    private static void assertStudentName(String expected, String label) throws Exception {
        ListInterface<Student> students = readStudents(Path.of("target", "identity-persistence", label, "Student.dat"));
        check(!students.isEmpty() && expected.equals(students.get(0).getName()), label + " persisted name");
    }

        /**
     * Reads and returns the serialized student list from the fixture file.
     * @param path the path value
     * @return reads and returns the serialized student list from the fixture file.
     */

    @SuppressWarnings("unchecked")
    private static ListInterface<Student> readStudents(Path path) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            return (ListInterface<Student>) in.readObject();
        }
    }

    /**
     * Makes applicant and skill interfaces share one scanner for scripted input.
     * @param manager the manager value
     */
    private static void shareApplicantScanners(ApplicantManager manager) throws Exception {
        Field applicantUiField = ApplicantManager.class.getDeclaredField("applicantProfileUI");
        applicantUiField.setAccessible(true);
        Object applicantUi = applicantUiField.get(manager);
        Field skillManagerField = ApplicantManager.class.getDeclaredField("skillProficiencyManager");
        skillManagerField.setAccessible(true);
        Object skillManager = skillManagerField.get(manager);
        Field skillUiField = skillManager.getClass().getDeclaredField("skillProficiencyUI");
        skillUiField.setAccessible(true);
        Object skillUi = skillUiField.get(skillManager);
        Field scannerField = applicantUi.getClass().getDeclaredField("sc");
        scannerField.setAccessible(true);
        Object scanner = scannerField.get(applicantUi);
        Field skillScannerField = skillUi.getClass().getDeclaredField("sc");
        skillScannerField.setAccessible(true);
        skillScannerField.set(skillUi, scanner);
    }

    /**
     * Throws an assertion error with the supplied message when the condition is false.
     * @param condition the condition value
     * @param message the message value
     */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    @FunctionalInterface
    private interface Factory {
        /** Creates the entity used by a counter-restoration check. */
        Object create();
    }
}

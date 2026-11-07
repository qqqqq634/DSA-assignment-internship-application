package dao;

import adt.*;
import entity.Student;

public class StudentInitializer {

    /**
     * Creates and returns the sample student profiles.
     * @return creates and returns the sample student profiles.
     */
    public ListInterface<Student> getStudents() {
        ListInterface<Student> students = new ArrayList<>();

        students.add(new Student("Alex Johnson", SkillProficiencyInitializer.getDeveloperProficiencies(), "Kuala Lumpur", 2));
        students.add(new Student("Sarah Chen", SkillProficiencyInitializer.getDeveloperProficiencies(), "Penang", 8));
        students.add(new Student("Michael Lee", SkillProficiencyInitializer.getQAProficiencies(), "Johor", 4));
        students.add(new Student("Emma Rodriguez", SkillProficiencyInitializer.getUXProficiencies(), "Kuala Lumpur", 5));
        students.add(new Student("James Wilson", SkillProficiencyInitializer.getPMProficiencies(), "Kuala Lumpur", 7));

        students.add(new Student("David Garcia", SkillProficiencyInitializer.getUXProficiencies(), "Penang", 6));

        students.add(new Student("Sophia Kim", SkillProficiencyInitializer.getMobileDeveloperProficiencies(), "Kedah", 4));
        students.add(new Student("John Smith", SkillProficiencyInitializer.getBackendDeveloperProficiencies(), "Penang", 5));
        students.add(new Student("Olivia Martinez", SkillProficiencyInitializer.getQAEngineerProficiencies(), "Kelantan", 6));
        students.add(new Student("Liam Brown", SkillProficiencyInitializer.getProductManagerProficiencies(), "Penang", 7));
        students.add(new Student("Ethan Davis", SkillProficiencyInitializer.getDevOpsEngineerProficiencies(), "Pahang", 4));
        students.add(new Student("Mia Wilson", SkillProficiencyInitializer.getDataScientistProficiencies(), "Putrajaya", 5));
        students.add(new Student("Noah Clark", SkillProficiencyInitializer.getNetworkEngineerProficiencies(), "Sabah", 4));
        students.add(new Student("Ava Lewis", SkillProficiencyInitializer.getBusinessAnalystProficiencies(), "Sarawak", 6));
        students.add(new Student("Isabella Walker", SkillProficiencyInitializer.getCloudEngineerProficiencies(), "Labuan", 5));

        return students;
    }

    /**
     * Creates sample data and writes it to the corresponding data file, replacing any existing file.
     * @param args the args value
     */
    public static void main(String[] args) {
        StudentInitializer studentInitializer = new StudentInitializer();
        ListInterface<Student> students = studentInitializer.getStudents();
        StudentDAO studentDAO = new StudentDAO();
        studentDAO.saveToFile(students);

        System.out.println("Student list successfully written to Student.dat");
    }
}

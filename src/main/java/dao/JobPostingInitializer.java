package dao;

import adt.*;
import entity.JobPosting;

public class JobPostingInitializer {
    private SkillRequiredInitializer skillRequiredInitializer = new SkillRequiredInitializer();

    /**
     * Creates and returns the sample job postings.
     * @return creates and returns the sample job postings.
     */
    public ListInterface<JobPosting> initializeJobPosting() {
        ListInterface<JobPosting> jobPostings = new ArrayList<>();
        jobPostings.add(
                new JobPosting(
                        "Java Developer",
                        "Develop backend systems with Java and Spring Boot.",
                        skillRequiredInitializer.initializeDeveloperSkills(),
                        "Kuala Lumpur",
                        new double[] { 4000, 6000 },
                        3,
                        2,
                        8,
                        10));

        jobPostings.add(
                new JobPosting(
                        "Frontend Developer",
                        "Create and maintain responsive web interfaces.",
                        skillRequiredInitializer.initializeUXSkills(),
                        "Penang",
                        new double[] { 3000, 4500 },
                        2,
                        2,
                        8,
                        10));
        jobPostings.add(
                new JobPosting(
                        "Data Analyst",
                        "Analyze data to identify trends and insights for decision making.",
                        skillRequiredInitializer.initializeDataScienceSkills(),
                        "Remote",
                        new double[] { 5000, 7000 },
                        4,
                        2,
                        8,
                        2));

        jobPostings.add(
                new JobPosting(
                        "UI/UX Designer",
                        "Design intuitive and engaging user interfaces for web and mobile apps.",
                        skillRequiredInitializer.initializeUXSkills(),
                        "Johor Bahru",
                        new double[] { 3500, 5200 },
                        5,
                        2,
                        8,
                        5));
        jobPostings.add(
                new JobPosting(
                        "Backend Developer",
                        "Develop and maintain server-side logic and APIs.",
                        skillRequiredInitializer.initializeDevOpsSkills(),
                        "Selangor",
                        new double[] { 4500, 6500 },
                        3,
                        2,
                        8,
                        3));
        jobPostings.add(
                new JobPosting(
                        "QA Engineer",
                        "Test and ensure the quality of web applications through manual and automated testing.",
                        skillRequiredInitializer.initializeQASkills(),
                        "Penang",
                        new double[] { 3500, 5000 },
                        4,
                        2,
                        8,
                        10));
        jobPostings.add(
                new JobPosting(
                        "Product Manager",
                        "Lead the development of new products and ensure timely delivery.",
                        skillRequiredInitializer.initializeProjectManagerSkills(),
                        "Kuala Lumpur",
                        new double[] { 7000, 10000 },
                        5,
                        9,
                        8,
                        7));
        jobPostings.add(
                new JobPosting(
                        "DevOps Engineer",
                        "Manage infrastructure, deployment pipelines, and automate deployment processes.",
                        skillRequiredInitializer.initializeDevOpsSkills(),
                        "Cyberjaya",
                        new double[] { 6000, 8000 },
                        4,
                        7,
                        8,
                        4));
        jobPostings.add(
                new JobPosting(
                        "Data Scientist",
                        "Develop machine learning models and conduct statistical analysis on large datasets.",
                        skillRequiredInitializer.initializeDataScienceSkills(),
                        "Remote",
                        new double[] { 8000, 12000 },
                        5,
                        9,
                        8,
                        5));
        jobPostings.add(
                new JobPosting(
                        "Network Engineer",
                        "Design, implement, and maintain network infrastructure and security.",
                        skillRequiredInitializer.initializeNetworkEngineerSkills(),
                        "Johor Bahru",
                        new double[] { 4000, 6000 },
                        3,
                        7,
                        8,
                        7));
        jobPostings.add(
                new JobPosting(
                        "Business Analyst",
                        "Analyze and document business processes, identify improvement opportunities.",
                        skillRequiredInitializer.initializeBusinessAnalystSkills(),
                        "Penang",
                        new double[] { 4000, 5500 },
                        2,
                        7,
                        8,
                        5));
        jobPostings.add(
                new JobPosting(
                        "Cloud Engineer",
                        "Design, implement, and manage cloud solutions on AWS, Azure, and Google Cloud.",
                        skillRequiredInitializer.initializeDevOpsSkills(),
                        "Kuala Lumpur",
                        new double[] { 6000, 9000 },
                        4,
                        1,
                        8,
                        7));
        jobPostings.add(
                new JobPosting(
                        "Security Engineer",
                        "Ensure the security of systems, applications, and networks from potential threats.",
                        skillRequiredInitializer.initializeSecurityEngineerSkills(),
                        "Cyberjaya",
                        new double[] { 7000, 10000 },
                        5,
                        1,
                        8,
                        2));
        return jobPostings;
    }
}
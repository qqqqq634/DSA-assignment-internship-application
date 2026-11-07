package dao;

import adt.*;
import control.JobManager;
import entity.SkillRequirement;


public class SkillRequiredInitializer {
    /**
     * Creates and returns sample skill requirements for developer skills.
     * @return creates and returns sample skill requirements for developer skills.
     */
    public ListInterface<SkillRequirement> initializeDeveloperSkills() {
        ListInterface<SkillRequirement> developerSkills = new ArrayList<>();
        developerSkills.add(new SkillRequirement("Java", 5));
        developerSkills.add(new SkillRequirement("C++", 4));
        developerSkills.add(new SkillRequirement("Python", 4));
        developerSkills.add(new SkillRequirement("HTML", 3));
        developerSkills.add(new SkillRequirement("CSS", 3));
        return developerSkills;
    }

    /**
     * Creates and returns sample skill requirements for qaskills.
     * @return creates and returns sample skill requirements for qaskills.
     */
    public ListInterface<SkillRequirement> initializeQASkills() {
        ListInterface<SkillRequirement> qaSkills = new ArrayList<>();
        qaSkills.add(new SkillRequirement("HTML", 7));
        qaSkills.add(new SkillRequirement("CSS", 6));
        qaSkills.add(new SkillRequirement("JavaScript", 8));
        qaSkills.add(new SkillRequirement("Test Automation", 8));
        qaSkills.add(new SkillRequirement("Selenium", 7));
        qaSkills.add(new SkillRequirement("Jenkins", 7));
        qaSkills.add(new SkillRequirement("SQL", 6));
        qaSkills.add(new SkillRequirement("API Testing", 7));
        qaSkills.add(new SkillRequirement("Load Testing", 6));
        qaSkills.add(new SkillRequirement("Performance Testing", 6));

        return qaSkills;
    }

    /**
     * Creates and returns sample skill requirements for uxskills.
     * @return creates and returns sample skill requirements for uxskills.
     */
    public ListInterface<SkillRequirement> initializeUXSkills() {
        ListInterface<SkillRequirement> UXSkills = new ArrayList<>();
        UXSkills.add(new SkillRequirement("Figma", 7));
        UXSkills.add(new SkillRequirement("User Research", 6));
        UXSkills.add(new SkillRequirement("Prototyping", 8));
        UXSkills.add(new SkillRequirement("Wireframing", 7));
        UXSkills.add(new SkillRequirement("Interaction Design", 8));
        UXSkills.add(new SkillRequirement("Usability Testing", 6));
        UXSkills.add(new SkillRequirement("Visual Design", 7));
        UXSkills.add(new SkillRequirement("User Interface Design", 7));
        UXSkills.add(new SkillRequirement("Persona Development", 6));

        return UXSkills;
    }

    /**
     * Creates and returns sample skill requirements for pmskills.
     * @return creates and returns sample skill requirements for pmskills.
     */
    public ListInterface<SkillRequirement> initializePMSkills() {
        ListInterface<SkillRequirement> PMSkills = new ArrayList<>();
        PMSkills.add(new SkillRequirement("Agile", 7));
        PMSkills.add(new SkillRequirement("Scrum", 6));
        PMSkills.add(new SkillRequirement("Kanban", 8));
        PMSkills.add(new SkillRequirement("Jira", 7));
        PMSkills.add(new SkillRequirement("Trello", 8));
        PMSkills.add(new SkillRequirement("Confluence", 6));
        return PMSkills;
    }

    /**
     * Creates and returns sample skill requirements for dev ops skills.
     * @return creates and returns sample skill requirements for dev ops skills.
     */
    public ListInterface<SkillRequirement> initializeDevOpsSkills() {
        ListInterface<SkillRequirement> devOpsSkills = new ArrayList<>();
        devOpsSkills.add(new SkillRequirement("Git", 7));
        devOpsSkills.add(new SkillRequirement("Docker", 6));
        devOpsSkills.add(new SkillRequirement("Kubernetes", 8));
        devOpsSkills.add(new SkillRequirement("AWS", 7));
        devOpsSkills.add(new SkillRequirement("Azure", 8));
        devOpsSkills.add(new SkillRequirement("Jenkins", 6));
        return devOpsSkills;
    }

    /**
     * Creates and returns sample skill requirements for project manager skills.
     * @return creates and returns sample skill requirements for project manager skills.
     */
    public ListInterface<SkillRequirement> initializeProjectManagerSkills() {
        ListInterface<SkillRequirement> projectManagerSkills = new ArrayList<>();
        projectManagerSkills.add(new SkillRequirement("Agile", 7));
        projectManagerSkills.add(new SkillRequirement("Scrum", 6));
        projectManagerSkills.add(new SkillRequirement("Kanban", 8));
        projectManagerSkills.add(new SkillRequirement("Jira", 7));
        projectManagerSkills.add(new SkillRequirement("Trello", 8));
        projectManagerSkills.add(new SkillRequirement("Confluence", 6));
        return projectManagerSkills;
    }

    /**
     * Creates and returns sample skill requirements for data science skills.
     * @return creates and returns sample skill requirements for data science skills.
     */
    public ListInterface<SkillRequirement> initializeDataScienceSkills() {
        ListInterface<SkillRequirement> DataScienceSkills = new ArrayList<>();
        DataScienceSkills.add(new SkillRequirement("Python", 8));
        DataScienceSkills.add(new SkillRequirement("R", 7));
        DataScienceSkills.add(new SkillRequirement("Machine Learning", 8));
        DataScienceSkills.add(new SkillRequirement("Deep Learning", 7));
        DataScienceSkills.add(new SkillRequirement("TensorFlow", 7));
        DataScienceSkills.add(new SkillRequirement("SQL", 8));
        DataScienceSkills.add(new SkillRequirement("Data Visualization", 7));
        DataScienceSkills.add(new SkillRequirement("Pandas", 8));
        DataScienceSkills.add(new SkillRequirement("Scikit-learn", 7));
        DataScienceSkills.add(new SkillRequirement("Big Data", 6));
        return DataScienceSkills;
    }

    /**
     * Creates and returns sample skill requirements for network engineer skills.
     * @return creates and returns sample skill requirements for network engineer skills.
     */
    public ListInterface<SkillRequirement> initializeNetworkEngineerSkills() {
        ListInterface<SkillRequirement> NetworkEngineerSkills = new ArrayList<>();
        NetworkEngineerSkills.add(new SkillRequirement("TCP/IP", 8));
        NetworkEngineerSkills.add(new SkillRequirement("Routing", 7));
        NetworkEngineerSkills.add(new SkillRequirement("Switching", 7));
        NetworkEngineerSkills.add(new SkillRequirement("Firewall Configuration", 8));
        NetworkEngineerSkills.add(new SkillRequirement("DNS", 7));
        NetworkEngineerSkills.add(new SkillRequirement("VPN", 6));
        NetworkEngineerSkills.add(new SkillRequirement("Wi-Fi Configuration", 7));
        NetworkEngineerSkills.add(new SkillRequirement("Network Security", 8));
        NetworkEngineerSkills.add(new SkillRequirement("Load Balancing", 6));
        NetworkEngineerSkills.add(new SkillRequirement("Cloud Networking", 7));
        return NetworkEngineerSkills;
    }

    /**
     * Creates and returns sample skill requirements for business analyst skills.
     * @return creates and returns sample skill requirements for business analyst skills.
     */
    public ListInterface<SkillRequirement> initializeBusinessAnalystSkills(){
        ListInterface<SkillRequirement> BusinessAnalystSkills = new ArrayList<>();
        BusinessAnalystSkills.add(new SkillRequirement("Business Analysis", 8));
        BusinessAnalystSkills.add(new SkillRequirement("Business Requirements", 7));
        BusinessAnalystSkills.add(new SkillRequirement("Business Planning", 7));
        BusinessAnalystSkills.add(new SkillRequirement("Business Process", 8));
        BusinessAnalystSkills.add(new SkillRequirement("Business Strategy", 7));
        BusinessAnalystSkills.add(new SkillRequirement("Requirements Gathering", 8));
        BusinessAnalystSkills.add(new SkillRequirement("Data Analysis", 7));
        BusinessAnalystSkills.add(new SkillRequirement("Business Process Modeling", 8));
        BusinessAnalystSkills.add(new SkillRequirement("SQL", 7));
        BusinessAnalystSkills.add(new SkillRequirement("Excel", 8));
        return BusinessAnalystSkills;
    }

    /**
     * Creates and returns sample skill requirements for security engineer skills.
     * @return creates and returns sample skill requirements for security engineer skills.
     */
    public ListInterface<SkillRequirement> initializeSecurityEngineerSkills(){
        ListInterface<SkillRequirement> SecurityEngineerSkills = new ArrayList<>();
        SecurityEngineerSkills.add(new SkillRequirement("Firewall Configuration", 8));
        SecurityEngineerSkills.add(new SkillRequirement("Network Security", 7));
        SecurityEngineerSkills.add(new SkillRequirement("Vulnerability Assessment", 7));
        SecurityEngineerSkills.add(new SkillRequirement("Penetration Testing", 8));
        SecurityEngineerSkills.add(new SkillRequirement("Threat Hunting", 7));
        SecurityEngineerSkills.add(new SkillRequirement("Incident Management", 8));
        SecurityEngineerSkills.add(new SkillRequirement("Cybersecurity Awareness", 7));
        SecurityEngineerSkills.add(new SkillRequirement("Cloud Security", 8));
        SecurityEngineerSkills.add(new SkillRequirement("Penetration Testing", 7));
        SecurityEngineerSkills.add(new SkillRequirement("Threat Hunting", 8));
        return SecurityEngineerSkills;
    }
}

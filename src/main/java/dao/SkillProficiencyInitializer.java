package dao;

import adt.*;
import entity.SkillProficiency;

public class SkillProficiencyInitializer {
    /**
     * Returns the developer proficiencies.
     * @return returns the developer proficiencies.
     */
    public static ListInterface<SkillProficiency> getDeveloperProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Java", 5));
        proficiencies.add(new SkillProficiency("Spring Boot", 4));
        proficiencies.add(new SkillProficiency("SQL", 3));
        proficiencies.add(new SkillProficiency("JavaScript", 4));
        proficiencies.add(new SkillProficiency("React", 3));
        return proficiencies;
    }

    // QA (Quality Assurance) proficiencies
    /**
     * Returns the qaproficiencies.
     * @return returns the qaproficiencies.
     */
    public static ListInterface<SkillProficiency> getQAProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Selenium", 4));
        proficiencies.add(new SkillProficiency("JUnit", 3));
        proficiencies.add(new SkillProficiency("Manual Testing", 5));
        proficiencies.add(new SkillProficiency("Test Automation", 4));
        proficiencies.add(new SkillProficiency("API Testing", 4));
        return proficiencies;
    }

    // UX (User Experience) proficiencies
    /**
     * Returns the uxproficiencies.
     * @return returns the uxproficiencies.
     */
    public static ListInterface<SkillProficiency> getUXProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Figma", 4));
        proficiencies.add(new SkillProficiency("User Research", 3));
        proficiencies.add(new SkillProficiency("Prototyping", 4));
        proficiencies.add(new SkillProficiency("Wireframing", 3));
        proficiencies.add(new SkillProficiency("Usability Testing", 3));
        return proficiencies;
    }

    // PM (Project Management) proficiencies
    /**
     * Returns the pmproficiencies.
     * @return returns the pmproficiencies.
     */
    public static ListInterface<SkillProficiency> getPMProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Agile", 5));
        proficiencies.add(new SkillProficiency("Scrum", 4));
        proficiencies.add(new SkillProficiency("Communication", 5));
        proficiencies.add(new SkillProficiency("Risk Management", 4));
        proficiencies.add(new SkillProficiency("Stakeholder Management", 3));
        return proficiencies;
    }

    // Backend Developer proficiencies
    /**
     * Returns the backend developer proficiencies.
     * @return returns the backend developer proficiencies.
     */
    public static ListInterface<SkillProficiency> getBackendDeveloperProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Java", 5));
        proficiencies.add(new SkillProficiency("Spring Boot", 4));
        proficiencies.add(new SkillProficiency("SQL", 4));
        proficiencies.add(new SkillProficiency("Node.js", 3));
        proficiencies.add(new SkillProficiency("REST APIs", 4));
        return proficiencies;
    }

    // QA Engineer proficiencies
    /**
     * Returns the qaengineer proficiencies.
     * @return returns the qaengineer proficiencies.
     */
    public static ListInterface<SkillProficiency> getQAEngineerProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Selenium", 5));
        proficiencies.add(new SkillProficiency("JUnit", 4));
        proficiencies.add(new SkillProficiency("API Testing", 5));
        proficiencies.add(new SkillProficiency("Automation", 5));
        proficiencies.add(new SkillProficiency("Load Testing", 4));
        return proficiencies;
    }

    // Product Manager proficiencies
    /**
     * Returns the product manager proficiencies.
     * @return returns the product manager proficiencies.
     */
    public static ListInterface<SkillProficiency> getProductManagerProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Agile", 5));
        proficiencies.add(new SkillProficiency("Scrum", 4));
        proficiencies.add(new SkillProficiency("Communication", 5));
        proficiencies.add(new SkillProficiency("Product Roadmap", 5));
        proficiencies.add(new SkillProficiency("Market Research", 4));
        return proficiencies;
    }

    // DevOps Engineer proficiencies
    /**
     * Returns the dev ops engineer proficiencies.
     * @return returns the dev ops engineer proficiencies.
     */
    public static ListInterface<SkillProficiency> getDevOpsEngineerProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Docker", 5));
        proficiencies.add(new SkillProficiency("Kubernetes", 4));
        proficiencies.add(new SkillProficiency("CI/CD", 5));
        proficiencies.add(new SkillProficiency("Terraform", 4));
        proficiencies.add(new SkillProficiency("Linux", 4));
        return proficiencies;
    }

    // Data Scientist proficiencies
    /**
     * Returns the data scientist proficiencies.
     * @return returns the data scientist proficiencies.
     */
    public static ListInterface<SkillProficiency> getDataScientistProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Python", 5));
        proficiencies.add(new SkillProficiency("Machine Learning", 4));
        proficiencies.add(new SkillProficiency("R", 3));
        proficiencies.add(new SkillProficiency("Data Visualization", 4));
        proficiencies.add(new SkillProficiency("SQL", 4));
        return proficiencies;
    }

    // Network Engineer proficiencies
    /**
     * Returns the network engineer proficiencies.
     * @return returns the network engineer proficiencies.
     */
    public static ListInterface<SkillProficiency> getNetworkEngineerProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("TCP/IP", 5));
        proficiencies.add(new SkillProficiency("Routing", 4));
        proficiencies.add(new SkillProficiency("Switching", 4));
        proficiencies.add(new SkillProficiency("Firewall Configuration", 5));
        proficiencies.add(new SkillProficiency("VPN", 4));
        return proficiencies;
    }

    // Business Analyst proficiencies
    /**
     * Returns the business analyst proficiencies.
     * @return returns the business analyst proficiencies.
     */
    public static ListInterface<SkillProficiency> getBusinessAnalystProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Requirements Gathering", 5));
        proficiencies.add(new SkillProficiency("Data Analysis", 4));
        proficiencies.add(new SkillProficiency("SQL", 4));
        proficiencies.add(new SkillProficiency("Business Process Modeling", 5));
        proficiencies.add(new SkillProficiency("Excel", 5));
        return proficiencies;
    }

    // Cloud Engineer proficiencies
    /**
     * Returns the cloud engineer proficiencies.
     * @return returns the cloud engineer proficiencies.
     */
    public static ListInterface<SkillProficiency> getCloudEngineerProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("AWS", 5));
        proficiencies.add(new SkillProficiency("Azure", 4));
        proficiencies.add(new SkillProficiency("Google Cloud", 4));
        proficiencies.add(new SkillProficiency("Cloud Security", 5));
        proficiencies.add(new SkillProficiency("Terraform", 4));
        return proficiencies;
    }

    // Security Engineer proficiencies
    /**
     * Returns the security engineer proficiencies.
     * @return returns the security engineer proficiencies.
     */
    public static ListInterface<SkillProficiency> getSecurityEngineerProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Network Security", 5));
        proficiencies.add(new SkillProficiency("Firewall Management", 5));
        proficiencies.add(new SkillProficiency("Penetration Testing", 4));
        proficiencies.add(new SkillProficiency("Cryptography", 4));
        proficiencies.add(new SkillProficiency("Incident Response", 5));
        return proficiencies;
    }

   
    // Mobile Developer proficiencies
    /**
     * Returns the mobile developer proficiencies.
     * @return returns the mobile developer proficiencies.
     */
    public static ListInterface<SkillProficiency> getMobileDeveloperProficiencies() {
        ListInterface<SkillProficiency> proficiencies = new ArrayList<>();
        proficiencies.add(new SkillProficiency("Swift", 5));
        proficiencies.add(new SkillProficiency("Kotlin", 4));
        proficiencies.add(new SkillProficiency("React Native", 3));
        return proficiencies;
    }
}

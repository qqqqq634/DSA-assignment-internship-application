package entity;

import adt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;


public class Company implements Serializable {
    /** Keeps the serialized class form compatible across application versions. */
    private static final long serialVersionUID = -657265688132988846L;
    private String id;
    private String name;
    private String location;
    private ListInterface<JobPosting> jobPostings = new ArrayList<>();
    private static int COUNTER = 0;
    /**
     * Reads serialized fields and restores the ID allocator from the stored identifier.
     * @param input the input value
     */
    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        restoreCounter(id, "C");
    }

    /**
     * Advances the ID allocator when a stored identifier has a valid higher numeric suffix.
     * @param storedId the storedId value
     * @param prefix the prefix value
     */
    private static synchronized void restoreCounter(String storedId, String prefix) {
        if (storedId == null || !storedId.startsWith(prefix) || storedId.length() == prefix.length()) return;
        try {
            int suffix = Integer.parseInt(storedId.substring(prefix.length()));
            if (suffix > COUNTER) COUNTER = suffix;
        } catch (NumberFormatException ignored) {
            // Ignore legacy or malformed IDs and keep the current allocator state.
        }
    }


    // Constructor
    /**
     * Allocates a new company ID and retains the supplied job list by reference.
     * @param name the name value
     * @param location the location value
     * @param jobPostings the jobPostings value
     */
    public Company(String name, String location, ListInterface<JobPosting> jobPostings) {
        COUNTER++;
        this.id = "C" + COUNTER;
        this.name = name;
        this.location = location;
        this.jobPostings = jobPostings;
    }

    /**
     * Copies the company ID and fields without allocating an ID; the job list is shared.
     * @param company the company value
     */
    public Company(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.location = company.getLocation();
        this.jobPostings = company.getJobPostings();
    }

    /**
     * Allocates a new company ID and starts with an empty job list.
     * @param name the name value
     * @param location the location value
     */
    public Company(String name, String location) {
        COUNTER++;
        this.id = "C" + COUNTER;
        this.name = name;
        this.location = location;
    }

    // Getters
    /**
     * Returns the id.
     * @return returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name.
     * @return returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the location.
     * @return returns the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the live job list; changes to it affect this company.
     * @return returns the job postings.
     */
    public ListInterface<JobPosting> getJobPostings() {
        return jobPostings;
    }

    // Setters
    /**
     * Sets the stored ID directly; it does not advance the ID allocator.
     * @param id the id value
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the name.
     * @param name the name value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the location.
     * @param location the location value
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Replaces the job list with the supplied reference.
     * @param jobPostings the jobPostings value
     */
    public void setJobPostings(ListInterface<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
    }

        /**
     * Formats this entity and its relevant details for display.
     * @return formats this entity and its relevant details for display.
     */

    @Override
    public String toString() {
        String result = String.format(
                "|%4s|%10s| %9s| %13s       |\n" +
                        "+----+----------+----------+-----------------------+\n",
                id, name, location, getStringJobPosting());
        return result;
    }

    /**
     * Formats the company job postings for display.
     * @return formats the company job postings for display.
     */
    private String getStringJobPosting() {
        if (jobPostings.isEmpty()) {
            return "No job posting.";
        }
        String result = "";
        for (int i = 0; i < jobPostings.size(); i++) {
            result += "\t"+jobPostings.get(i).toString();
        }
        return result;
    }

}

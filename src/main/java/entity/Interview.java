package entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDateTime;

import adt.ListInterface;
import entity.Match;

public class Interview implements Serializable{
    /** Keeps the serialized class form compatible across application versions. */
    private static final long serialVersionUID = -3743795455819210110L;

    // Interview DateTime
    private LocalDateTime scheduledTime;
    private String id;
    private static int COUNTER = 0;
    /**
     * Reads serialized fields and restores the ID allocator from the stored identifier.
     * @param input the input value
     */
    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        restoreCounter(id, "I");
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


    // list of the Specific Job match
    private ListInterface<Match> matches;

    // state = pending / rejectedByCompany / sheduled / acceptedBy application //
    // reject applicationByStudent
    private int[] state;

    /**
     * Returns the id.
     * @return returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the scheduled time.
     * @return returns the scheduled time.
     */
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    /**
     * Returns the live list of matches; its order corresponds to the state array.
     * @return returns the matches.
     */
    public ListInterface<Match> getMatches() {
        return matches;
    }

    /**
     * Returns a defensive copy of the interview state array.
     * @return returns a defensive copy of the interview state array.
     */
    public int[] getState() {
        int[] newState = new int[state.length];
        System.arraycopy(state, 0, newState, 0, state.length);
        return newState;
    }

    /**
     * Allocates a new interview ID and retains the supplied match list and state array by reference.
     * @param scheduledTime the scheduledTime value
     * @param matches the matches value
     * @param state the state value
     */
    public Interview(LocalDateTime scheduledTime, ListInterface<Match> matches, int[] state) {
        COUNTER++;
        this.id = "I" + COUNTER;
        this.scheduledTime = scheduledTime;
        this.matches = matches;
        this.state = state;
    }

    /**
     * Copies the interview ID and time without allocating an ID, shares the match list, and copies the state array.
     * @param interview the interview value
     */
    public Interview(Interview interview) {
        this.id = interview.getId();
        this.scheduledTime = interview.getScheduledTime();
        this.matches = interview.getMatches();
        this.state = interview.getState();
    }

        /**
     * Formats this entity and its relevant details for display.
     * @return formats this entity and its relevant details for display.
     */

    @Override
    public String toString() {
        return "======Interview=====\n" + "id :" + id + "\n scheduledTime :" + scheduledTime
                + "Jobs :\n" + matches.get(0).getJobPosting().toString()
                + "\n Interview Applicants : \n" + "=====================\n" + getMatchingDetails();
    }

    /**
     * Formats each interview applicant with the current state and match score.
     * @return formats each interview applicant with the current state and match score.
     */
    private String getMatchingDetails() {
        String result = "";
        for (int i = 0; i < matches.size(); i++) {
            result += matches.get(i).getStudent().toString() + "\n";
            result += "State" + getDisplayState(state[i]) + "\n";
            result += "Match Score : " + matches.get(i).getScore() + "\n";
            result += "=============================================\n";

        }
        return result;
    }

    /**
     * Maps an interview state code to its display label.
     * @param i the i value
     * @return maps an interview state code to its display label.
     */
    public String getDisplayState(int i) {
        if (i == 0) {
            return "Pending";
        } else if (i == 1) {
            return "Rejected by company";
        } else if (i == 2) {
            return "Scheduled";
        } else if (i == 3) {
            return "Accepted by applicant";
        } else {
            return "Rejected by applicant";
        }
    }

}

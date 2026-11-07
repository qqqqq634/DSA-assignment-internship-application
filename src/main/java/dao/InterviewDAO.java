package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import adt.ArrayList;
import adt.ListInterface;
import entity.Interview;

public class InterviewDAO {
        private String fileName = "Interview.dat"; // For security and maintainability, should not have filename hardcoded
                                              // here.

    /**
     * Serializes to a temporary file, then replaces the data file, using an atomic move when available. Prints an error and returns false on an I/O failure.
     * @param interviewList the interviewList value
     * @return true if the replacement succeeded; false on an I/O failure
     */
    public boolean saveToFile(ListInterface<Interview> interviewList) {
        java.nio.file.Path destination = new File(fileName).toPath().toAbsolutePath();
        java.nio.file.Path temp = null;
        try {
            temp = java.nio.file.Files.createTempFile(destination.getParent(), "interviews-", ".tmp");
            try (ObjectOutputStream stream = new ObjectOutputStream(java.nio.file.Files.newOutputStream(temp))) {
                stream.writeObject(interviewList);
            }
            try {
                java.nio.file.Files.move(temp, destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                        java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException ex) {
                java.nio.file.Files.move(temp, destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException ex) {
            System.out.println("\nCannot save to file: " + ex.getMessage());
            return false;
        } finally {
            if (temp != null) {
                try { java.nio.file.Files.deleteIfExists(temp); } catch (IOException ignored) { }
            }
        }
    }

        /**
     * Loads and returns the serialized list, or an empty list when loading fails.
     * @return loads and returns the serialized list, or an empty list when loading fails.
     */

    @SuppressWarnings({ "unchecked", "finally" })
    public ListInterface<Interview> retrieveFromFile() {
        File file = new File(fileName);
        ListInterface<Interview> InterviewList = new ArrayList<>();
        try {
            ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
            InterviewList = (ArrayList<Interview>) (oiStream.readObject());
            oiStream.close();
        } catch (FileNotFoundException ex) {
            System.out.println("\nNo such file.");
        } catch (IOException ex) {
            System.out.println("\nCannot read from file.");
        } catch (ClassNotFoundException ex) {
            System.out.println("\nClass not found.");
        } finally {
            return InterviewList;
        }
    }
}

package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.AtomicMoveNotSupportedException;

import adt.*;
import entity.Match;

public class MatchDAO {
    private String fileName = "Match.dat"; // For security and maintainability, should not have filename hardcoded
                                             // here.

    /**
     * Serializes to a temporary file, then replaces the data file, using an atomic move when available. Prints an error and returns false on an I/O failure.
     * @param matchList the matchList value
     * @return true if the replacement succeeded; false on an I/O failure
     */
    public boolean saveToFile(ListInterface<Match> matchList) {
        Path target = new File(fileName).toPath().toAbsolutePath();
        Path temp = null;
        try {
            temp = Files.createTempFile(target.getParent(), "match-", ".tmp");
            try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(temp.toFile()))) {
                stream.writeObject(matchList);
            }
            try {
                Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException ex) {
            System.out.println("\nCannot save to file");
            return false;
        } finally {
            if (temp != null) {
                try { Files.deleteIfExists(temp); } catch (IOException ignored) { }
            }
        }
    }

        /**
     * Loads and returns the serialized list, or an empty list when loading fails.
     * @return loads and returns the serialized list, or an empty list when loading fails.
     */

    @SuppressWarnings({ "unchecked", "finally" })
    public ListInterface<Match> retrieveFromFile() {
        File file = new File(fileName);
        ListInterface<Match> matchList = new ArrayList<>();
        try {
            ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
            matchList = (ArrayList<Match>) (oiStream.readObject());
            oiStream.close();
        } catch (FileNotFoundException ex) {
            System.out.println("\nNo such file.");
        } catch (IOException ex) {
            System.out.println("\nCannot read from file.");
        } catch (ClassNotFoundException ex) {
            System.out.println("\nClass not found.");
        } finally {
            return matchList;
        }
    }
}

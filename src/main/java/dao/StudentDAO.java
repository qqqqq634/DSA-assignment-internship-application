package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import adt.*;
import entity.Student;

public class StudentDAO {
    private String fileName = "Student.dat"; // For security and maintainability, should not have filename hardcoded
                                             // here.

    /**
     * Writes the serialized list directly to the data file, replacing it; I/O errors are printed and no success value is returned.
     * @param productList the productList value
     */
    public void saveToFile(ListInterface<Student> productList) {
        File file = new File(fileName);
        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(productList);
            ooStream.close();
        } catch (FileNotFoundException ex) {
            System.out.println("\nFile not found");
        } catch (IOException ex) {
            System.out.println("\nCannot save to file");
        }
    }

        /**
     * Loads and returns the serialized list, or an empty list when loading fails.
     * @return loads and returns the serialized list, or an empty list when loading fails.
     */

    @SuppressWarnings({ "finally", "unchecked" })
    public ListInterface<Student> retrieveFromFile() {
        File file = new File(fileName);
        ListInterface<Student> studentList = new ArrayList<>();
        try {
            ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
            studentList = (ArrayList<Student>) (oiStream.readObject());
            oiStream.close();
        } catch (FileNotFoundException ex) {
            System.out.println("\nNo such file.");
        } catch (IOException ex) {
            System.out.println("\nCannot read from file.");
        } catch (ClassNotFoundException ex) {
            System.out.println("\nClass not found.");
        } finally {
            return studentList;
        }
    }
}

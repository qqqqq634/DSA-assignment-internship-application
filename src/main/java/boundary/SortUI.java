package boundary;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class SortUI {
    /** Prevents construction of this utility class. */
    private SortUI() { }

    /** Prompts for order and returns true for 1 (ascending) or false for 2 (descending); consumes the rest of a valid line and discards invalid input lines. */
    public static boolean inputAscending(Scanner scanner) {
        System.out.println("Sort order:");
        System.out.println("1. Ascending (A-Z, lowest or earliest first)");
        System.out.println("2. Descending (Z-A, highest or latest first)");
        while (true) {
            System.out.print("Enter sort order: ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) return true;
                if (choice == 2) return false;
            } catch (InputMismatchException ex) {
                scanner.nextLine();
            }
            System.out.println("Please enter 1 or 2.");
        }
    }
}

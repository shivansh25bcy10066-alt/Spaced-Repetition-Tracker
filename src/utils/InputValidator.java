package utils;

import java.util.Scanner;

/**
 * Centralizes defensive console-input handling so the SessionRunner never
 * crashes on malformed user input (e.g. letters typed where a number is
 * expected). Every read loops until valid input is supplied.
 */
public final class InputValidator {

    private InputValidator() {
        // Utility class; no instances.
    }

    public static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.println("[Input Error] Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[Input Error] Please enter a valid whole number.");
            }
        }
    }

    public static String readNonEmptyLine(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("[Input Error] This field cannot be empty.");
        }
    }
}

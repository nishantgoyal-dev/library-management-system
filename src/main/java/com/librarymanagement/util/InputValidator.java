package com.librarymanagement.util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class InputValidator {
    // static Scanner sc;
    private static final Scanner sc = new Scanner(System.in);
    //ai generated
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    InputValidator(){
        // private static final Scanner sc = new Scanner(System.in);
    }
    //ai generated
    private static final Pattern ISBN_PATTERN = Pattern.compile(
            "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");

    public static long getLongInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please try again.");
                    continue;
                }
                long value = Long.parseLong(input);
                if (value < 0) {
                    System.out.println("Value cannot be negative. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid number.");
            }
        }
    }

    public static int getPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please try again.");
                    continue;
                }
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("Value must be positive. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid number.");
            }
        }
    }

    public static String getNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }
            return input;
        }
    }

    public static String getValidEmail(String prompt) {
        while (true) {
            System.out.print(prompt);
            String email = sc.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("Email cannot be empty. Please try again.");
                continue;
            }
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                System.out.println("Invalid email format. Please enter a valid email (e.g., user@example.com).");
                continue;
            }
            return email;
        }
    }

    public static String getValidISBN(String prompt) {
        while (true) {
            System.out.print(prompt);
            String isbn = sc.nextLine().trim();
            if (isbn.isEmpty()) {
                System.out.println("ISBN cannot be empty. Please try again.");
                continue;
            }
            if (!ISBN_PATTERN.matcher(isbn).matches()) {
                System.out.println("Invalid ISBN format. Please enter a valid ISBN-10 or ISBN-13.");
                System.out.println("Example: 978-0-123456-78-9 or 0-123456-78-9");
                continue;
            }
            return isbn;
        }
    }

    public static boolean getConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (yes/no): ");
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("no") || input.equals("n")) {
                return false;
            } else {
                System.out.println("Please enter 'yes' or 'no'.");
            }
        }
    }

    public static String getMenuChoice(String prompt, String... validChoices) {
        while (true) {
            System.out.print(prompt);
            String choice = sc.nextLine().trim();
            for (String valid : validChoices) {
                if (choice.equals(valid)) {
                    return choice;
                }
            }
            System.out.println("Invalid choice. Please try again.");
        }
    }
}

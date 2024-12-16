/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TaxCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author matheuscastilho
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DatabaseConnection.connect(); // Replace with your DB connection method

        if (connection == null) {
            System.out.println("Failed to establish a database connection.");
            return;
        }

        while (true) {
            System.out.println("\nWelcome! Please choose an option:");
            System.out.println("1. Log in");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    User user = User.login(username, password, connection);
                    if (user == null) {
                        System.out.println("Login failed. Invalid credentials.");
                    } else {
                        System.out.println("Login successful. Welcome, " + user.getUsername() + "!");

                        // Display menu based on user role
                        if (user instanceof Admin) {
                            adminMenu((Admin) user, connection, scanner);
                        } else if (user instanceof Regular) {
                            regularMenu((Regular) user, connection, scanner);
                        } else {
                            System.out.println("Unknown role. Logging out.");
                        }
                    }
                }

                case 2 -> {
                    System.out.println("\nSelf Registration");
                    System.out.print("Enter username: ");
                    String newUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String newPassword = scanner.nextLine();
                    System.out.print("Enter first name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();

                    User newUser = new User(0, newUsername, newPassword, firstName, lastName, email, "Regular");
                    if (User.registerUser(newUser, connection)) {
                        System.out.println("Registration successful. You can now log in.");
                    } else {
                        System.out.println("Registration failed. Please try again.");
                    }
                }

                case 3 -> {
                    System.out.println("Exiting. Goodbye!");
                    DatabaseConnection.close(connection);
                    scanner.close();
                    return;
                }

                default ->
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminMenu(Admin admin, Connection connection, Scanner scanner) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Register a new user");
            System.out.println("2. Delete a user");
            System.out.println("3. Review operations");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter details of the new user.");
                    System.out.print("Username: ");
                    String newUsername = scanner.nextLine();
                    System.out.print("Password: ");
                    String newPassword = scanner.nextLine();
                    System.out.print("First Name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Last Name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Role (Admin/Regular): ");
                    String role = scanner.nextLine();

                    User newUser = new User(0, newUsername, newPassword, firstName, lastName, email, role);
                    if (admin.registerNewUser(admin.getUserID(), newUser, connection)) {
                        System.out.println("User registered successfully.");
                    } else {
                        System.out.println("Failed to register user.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter the ID of the user to delete: ");
                    int userIdToDelete = scanner.nextInt();
                    scanner.nextLine();
                    if (admin.deleteUser(userIdToDelete, connection)) {
                        System.out.println("User deleted successfully.");
                    } else {
                        System.out.println("Failed to delete user.");
                    }
                }
                case 3 -> {
                    System.out.println("Review Operations.");
                    admin.reviewOperations(connection);
                }

                case 4 -> {
                    System.out.println("Logging out.");
                    return;
                }
                default ->
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void regularMenu(Regular regular, Connection connection, Scanner scanner) {
        while (true) {
            System.out.println("\nRegular User Menu:");
            System.out.println("1. Update profile");
            System.out.println("2. Calculate taxes");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    regular.updateProfile(newEmail, newPassword, connection);
                }
                case 2 -> {
                    System.out.print("Enter gross income: ");
                    double grossIncome = scanner.nextDouble();
                    System.out.print("Enter tax credits: ");
                    double taxCredits = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    regular.calculateTax(grossIncome, taxCredits, connection);
                }
                case 3 -> {
                    System.out.println("Logging out.");
                    return;
                }
                default ->
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

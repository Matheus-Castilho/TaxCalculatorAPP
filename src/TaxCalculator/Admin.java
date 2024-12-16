package TaxCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author matheuscastilho
 */
public class Admin extends User {

    public Admin(int userID, String username, String password, String firstName, String lastName, String email, String role) {
        super(userID, username, firstName, lastName, password, email, role);
    }

    public static boolean listAllUsers(Connection connection) {
        String sql = "SELECT * FROM Users";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("List of all users: ");
            System.out.printf("%-10s %-20s %-30s %-10s%n", "UserID", "Username", "Email", "Role");

            // Loop through the result set and print user details
            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String username = resultSet.getString("Username");
                String email = resultSet.getString("Email");
                String role = resultSet.getString("Role");

                System.out.printf("%-10d %-20s %-30s %-10s%n", userId, username, email, role);
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Error during connection: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int userIDToDelete, Connection connection) {
        String sql = "DELETE FROM Users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userIDToDelete);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Deleted user with ID: " + userIDToDelete);
                // Log the deletion operation
                Operation.logOperation(this.getUserID(), OperationType.DELETE_USER, connection);
                return true;
            } else {
                System.out.println("No user found with ID: " + userIDToDelete);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error while deleting user: " + e.getMessage());
            return false;
        }
    }

    public boolean reviewOperations(Connection connection) {
        String sql = "SELECT * FROM AuditLog;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("List of all Operations: ");
            System.out.printf("%-10s %-10s %-20s %-30s%n", "log_id", "user_id", "Action", "action_time");
            System.out.println("----------------------------------------------------------------------------------------");

            // Loop through the result set and print user details
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String user_id = resultSet.getString("user_id");
                String action = resultSet.getString("action");
                String action_time = resultSet.getString("action_time");

                System.out.printf("%-10d %-10s %-20s %-30s%n", id, user_id, action, action_time);
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Error during connection: " + e.getMessage());
            return false;
        }
    }

    // Method for admin to register a new user (can set role explicitly)
    public boolean registerNewUser(int mainAdmin, User user, Connection connection) {
        String sql = "INSERT INTO Users (username, password, first_name, Last_name, email, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword()); // Ensure password is hashed in real applications
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getRole());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Registered user: " + user.getUsername());
                // Log the registration operation
                Operation.logOperation(mainAdmin, OperationType.REGISTER, connection);
                return true;
            } else {
                System.out.println("Failed to register user: " + user.getUsername());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error while registering user: " + e.getMessage());
            return false;
        }
    }
}

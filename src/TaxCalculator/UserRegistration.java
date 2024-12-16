package TaxCalculator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author matheuscastilho
 */

public class UserRegistration {

    public static boolean registerNewUser(String username, String password, String email, Connection connection) {
        String checkUserSQL = "SELECT COUNT(*) FROM Users WHERE Username = ?";
        String insertUserSQL = "INSERT INTO Users (Username, Password, Email, Role, CreatedAt) VALUES (?, ?, ?, ?, NOW())";

        try {
            // Step 1: Check if the username already exists
            try (PreparedStatement checkStatement = connection.prepareStatement(checkUserSQL)) {
                checkStatement.setString(1, username);
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    System.out.println("Username already exists. Please choose a different username.");
                    return false;
                }
            }

            // Step 2: Insert the new user into the database
            try (PreparedStatement insertStatement = connection.prepareStatement(insertUserSQL)) {
                insertStatement.setString(1, username);
                insertStatement.setString(2, password); // Ensure this password is hashed!
                insertStatement.setString(3, email);
                insertStatement.setString(4, "Regular");

                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("User registered successfully.");

                    // Step 3: Log the registration operation
                    Operation.logOperation(getUserIdByUsername(username, connection), OperationType.REGISTER, connection);

                    return true;
                } else {
                    System.out.println("Failed to register user.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during user registration: " + e.getMessage());
            return false;
        }
    }

    private static int getUserIdByUsername(String username, Connection connection) {
        String query = "SELECT UserID FROM Users WHERE Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("UserID");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user ID: " + e.getMessage());
        }
        return -1; // Indicate failure
    }
}

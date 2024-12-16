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
public class User {

    private int userID;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public User(int userID, String username, String password, String firstName, String lastName, String email, String role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    // Login Method
    public static User login(String username, String password, Connection connection) {
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                int userID = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String role = resultSet.getString("role");

                System.out.println("Login successful for user: " + username);

                // Log the login operation
                Operation.logOperation(userID, OperationType.LOGIN, connection);

                // Return the appropriate object based on the role
                if ("Admin".equalsIgnoreCase(role)) {
                    return new Admin(userID, username, firstName, lastName, password, email, role);
                } else {
                    return new Regular(userID, username, password, firstName, lastName, email, role);
                }

            } else {
                System.out.println("Invalid username or password.");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            return null;
        }
    }

    // Method to update user profile
    public void updateProfile(String newEmail, String newPassword, Connection connection) {
        String sql = "UPDATE Users SET Email = ?, Password = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setString(2, newPassword); // Ensure password is hashed in real applications
            preparedStatement.setInt(3, this.getUserID());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Profile updated successfully for user: " + this.getUsername());
                // Optionally log the update operation
                Operation.logOperation(this.getUserID(), OperationType.UPDATE_PROFILE, connection);
            } else {
                System.out.println("Failed to update profile for user: " + this.getUsername());
            }
        } catch (SQLException e) {
            System.err.println("Error while updating profile: " + e.getMessage());
        }
    }

    public String getRole() {
        return role;
    }

    //Register new User
    // Method for self-registration (role defaults to "Regular")
    public static boolean selfRegister(Connection connection) {
        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();

            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            System.out.println("Enter your first name: ");
            String firstName = scanner.nextLine();

            System.out.println("Enter your last name: ");
            String lastName = scanner.nextLine();

            System.out.println("Enter your email: ");
            String email = scanner.nextLine();

            // Default role is Regular
            User newUser = new User(0, username, password, firstName, lastName, email, "regular");
            return registerUser(newUser, connection);
        }
    }

    // General registerUser method (used by both selfRegister and admin registration)
    public static boolean registerUser(User user, Connection connection) {
        String sql = "INSERT INTO Users (username, password, first_name, last_name, email, role) VALUES (?, ?, ?, ?, ?, ?)";

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
                Operation.logOperation(null, OperationType.REGISTER, connection);
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

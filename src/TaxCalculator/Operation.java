package TaxCalculator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author matheuscastilho
 */
public class Operation {

    private int operationID;
    private Integer userID;
    private OperationType operationType;
    private Timestamp timestamp;

    // Constructor
    public Operation(int operationID, int userID, OperationType operationType, Timestamp timestamp) {
        this.operationID = operationID;
        this.userID = userID;
        this.operationType = operationType;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getOperationID() {
        return operationID;
    }

    public void setOperationID(int operationID) {
        this.operationID = operationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    // Method to log an operation into the database
    public static void logOperation(Integer userID, OperationType operationType, Connection connection) {
        String sql = "INSERT INTO AuditLog (user_id, action, action_time) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            if (userID == null) {
                preparedStatement.setNull(1, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(1, userID);
            }
            preparedStatement.setString(2, operationType.toString());
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Operation logged successfully.");
            } else {
                System.out.println("Failed to log operation.");
            }
        } catch (SQLException e) {
            System.err.println("Error logging operation: " + e.getMessage());
        }
    }
}

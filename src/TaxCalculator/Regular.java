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
public class Regular extends User {

    public Regular(int userID, String username, String password, String firstName, String lastName, String email, String role) {
        super(userID, username, password, firstName, lastName, email, role);
    }

    // Method to calculate taxes and save results to the database
    public void calculateTax(double grossIncome, double taxCredits, Connection connection) {
        double incomeTax = grossIncome * 0.20; // Example: 20% income tax
        double usc = grossIncome * 0.05;      // Example: 5% USC
        double prsi = grossIncome * 0.04;     // Example: 4% PRSI
        double totalTax = incomeTax + usc + prsi - taxCredits;

        if (totalTax < 0) {
            totalTax = 0; // Ensure total tax isn't negative
        }

        String sql = "INSERT INTO TaxCalculations (user_id, gross_income, tax_credits, income_tax, usc, prsi, total_tax) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, this.getUserID());
            preparedStatement.setDouble(2, grossIncome);
            preparedStatement.setDouble(3, taxCredits);
            preparedStatement.setDouble(4, incomeTax);
            preparedStatement.setDouble(5, usc);
            preparedStatement.setDouble(6, prsi);
            preparedStatement.setDouble(7, totalTax);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Tax details saved successfully for user: " + this.getUsername());
                System.out.printf("Income Tax: %.2f, USC: %.2f, PRSI: %.2f, Total Tax: %.2f%n", incomeTax, usc, prsi, totalTax);
            } else {
                System.out.println("Failed to save tax details for user: " + this.getUsername());
            }
        } catch (SQLException e) {
            System.err.println("Error while saving tax details: " + e.getMessage());
        }
    }
}

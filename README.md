# Java Tax Calculation System

This project is a role-based Java application for managing users and calculating taxes. It uses a MySQL database to store user data and tax details.

## Features

- **User Login and Registration**:
  - Admins can manage users (add or delete users).
  - Regular users can self-register and update their profiles.
- **Role-Based Menus**:
  - Admin Menu: Register new users, delete users.
  - Regular Menu: Update profile, calculate taxes.
- **Tax Calculation**:
  - Calculates income tax, USC, PRSI, and total tax based on gross income and tax credits.

## Requirements

- **Java JDK**: Version 8 or later.
- **MySQL Server**: Ensure a database named `TaxCalculationDB` is set up.
- **IDE**: (Optional) NetBeans, IntelliJ IDEA, or Eclipse.

## Setup

### Database Setup

1. Create a MySQL database named `TaxCalculationDB`.
2. Run the following SQL commands to create the necessary tables:

   ```sql
    CREATE TABLE Users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        first_name VARCHAR(50),
        last_name VARCHAR(50),
        email VARCHAR(100) UNIQUE,
        role ENUM('admin', 'regular') NOT NULL
    );

    CREATE TABLE TaxCalculations (
        id INT AUTO_INCREMENT PRIMARY KEY,
        user_id INT,
        gross_income DECIMAL(10, 2) NOT NULL,
        tax_credits DECIMAL(10, 2) NOT NULL,
        income_tax DECIMAL(10, 2) NOT NULL,
        usc DECIMAL(10, 2) NOT NULL,
        prsi DECIMAL(10, 2) NOT NULL,
        total_tax DECIMAL(10, 2) NOT NULL,
        FOREIGN KEY (user_id) REFERENCES Users(id)
    );

    CREATE TABLE AuditLog (
        id INT AUTO_INCREMENT PRIMARY KEY,
        user_id INT,
        action VARCHAR(255) NOT NULL,
        action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (user_id) REFERENCES Users(id)
    );

    -- Pre-register admin user
    INSERT INTO Users (username, password, first_name, last_name, role)
    VALUES ('CCT', 'Dublin', 'Admin', 'User', 'admin');
   ```

## Usage

### Admin Menu

- **Register New User**: Add users with roles (Admin/Regular).
- **Delete User**: Remove users by ID.

### Regular User Menu

- **Update Profile**: Update email or password.
- **Calculate Taxes**: Compute taxes based on gross income and tax credits.

---

If you encounter issues or have questions, please open an issue or submit a pull request!
```

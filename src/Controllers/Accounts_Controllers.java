package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts_Controllers {
    public static void get_accounts(Connection con, int user_id) {
        String Query = "SELECT * FROM account_details WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(Query)) {
            ps.setInt(1, user_id);
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    int account_id = rs.getInt("account_id");
                    long account_number = rs.getLong("account_number");
                    int balance = rs.getInt("balance");
                    System.out.println("Account ID: " + account_id+ " || " +
                            " Account Number: " + account_number + " || " +
                            " Balance: " + balance);
                }
                if (!found) {
                    System.out.println("You have no Account in our bank. Please create an account to access the services.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void open_account(Connection con, int user_id) {
        System.out.println("Welcome to the Accounts Management System");

        String Query = "INSERT INTO account_details (user_id, account_number, balance) VALUES (?, ?, ?)";
        Scanner input = new Scanner(System.in);

        System.out.print("Please enter your initial balance: ");
        int balance = input.nextInt();

        // generates a random 12-digit account number
        long account_number = (long)(Math.random() * 1_000_000_000_000L);

        try (PreparedStatement ps = con.prepareStatement(Query)) {
            ps.setInt(1, user_id);
            ps.setLong(2, account_number);
            ps.setInt(3, balance);

            int rowInserted = ps.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("You have successfully created an Account.");
                System.out.println("Your Account Number: " + account_number);
                System.out.println("Login again to view the balance.");
            } else {
                System.out.println("Error creating an account.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

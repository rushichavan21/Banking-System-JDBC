package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserTasks_Controllers {
    public static void credit(Connection con, long account_number) {
        UI_Controllers.CreatePartition();
        System.out.println("Welcome to credit counter");

        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the amount you would like to credit: ");
        int amount = sc.nextInt();

        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive number.");
            return;
        }

        String updateQuery = "UPDATE account_details SET balance = balance + ? WHERE account_number = ?";
        String selectQuery = "SELECT balance FROM account_details WHERE account_number = ?";

        try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setInt(1, amount);
            ps.setLong(2, account_number);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                try (PreparedStatement ps2 = con.prepareStatement(selectQuery)) {
                    ps2.setLong(1, account_number);
                    try (ResultSet rs = ps2.executeQuery()) {
                        if (rs.next()) {
                            long newBalance = rs.getLong("balance");
                            System.out.println("Thank you! Amount credited successfully.");
                            System.out.println("Your new balance is: " + newBalance);
                        }
                    }
                }
            } else {
                System.out.println("Credit failed. Please check your account number.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while crediting amount", e);
        }
    }


    public static void debit(Connection con, long account_number) {
       UI_Controllers.CreatePartition();
        System.out.println("Welcome to debit counter");

        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the amount you would like to debit: ");
        int amount = sc.nextInt();

        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive number.");
            return;
        }

        String checkBalanceQuery = "SELECT balance FROM account_details WHERE account_number = ?";
        String updateQuery = "UPDATE account_details SET balance = balance - ? WHERE account_number = ?";

        try (
                PreparedStatement psCheck = con.prepareStatement(checkBalanceQuery);
                PreparedStatement psUpdate = con.prepareStatement(updateQuery)
        ) {
            psCheck.setLong(1, account_number);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    long currentBalance = rs.getLong("balance");
                    if (currentBalance < amount) {
                        System.out.println("Insufficient balance. Transaction failed.");
                        return;
                    }

                    psUpdate.setInt(1, amount);
                    psUpdate.setLong(2, account_number);
                    int rowsAffected = psUpdate.executeUpdate();

                    if (rowsAffected == 1) {
                        long newBalance = currentBalance - amount;
                        System.out.println("Debit successful. Amount debited: " + amount);
                        System.out.println("Your new balance is: " + newBalance);
                    } else {
                        System.out.println("Debit failed. Please check your account number.");
                    }
                } else {
                    System.out.println("Account not found.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while debiting amount", e);
        }
    }

    public static void Check_Balance(Connection con, long account_number) {
        UI_Controllers.CreatePartition();
        String Query = "SELECT balance FROM account_details WHERE account_number = ?;";

        try (PreparedStatement ps = con.prepareStatement(Query)) {
            ps.setLong(1, account_number);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int balance = rs.getInt("balance");
                    System.out.println("Your current balance is: " + balance);
                } else {
                    System.out.println("⚠️ No account found with number: " + account_number);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void transfer(Connection con, long account_number_from) {
        UI_Controllers.CreatePartition();
        System.out.println("Welcome to transfer counter");
        Scanner sc = new Scanner(System.in);

        System.out.print("Please enter the amount you would like to transfer: ");
        int amount = sc.nextInt();
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive number.");
            return;
        }

        System.out.print("Please enter the Account Number to which you would like to transfer: ");
        long account_number_to = sc.nextLong();

        String debitQuery = "UPDATE account_details SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
        String creditQuery = "UPDATE account_details SET balance = balance + ? WHERE account_number = ?";

        try {
            con.setAutoCommit(false);
            try (PreparedStatement psDebit = con.prepareStatement(debitQuery)) {
                psDebit.setInt(1, amount);
                psDebit.setLong(2, account_number_from);
                psDebit.setInt(3, amount);

                int rowsDebited = psDebit.executeUpdate();

                if (rowsDebited == 0) {
                    System.out.println("❌ Transfer failed: Insufficient balance or invalid sender account.");
                    con.rollback();
                    return;
                }
            }

            try (PreparedStatement psCredit = con.prepareStatement(creditQuery)) {
                psCredit.setInt(1, amount);
                psCredit.setLong(2, account_number_to);

                int rowsCredited = psCredit.executeUpdate();

                if (rowsCredited == 0) {
                    System.out.println("❌ Transfer failed: Receiver account not found.");
                    con.rollback();
                    return;
                }
            }
            con.commit();
            System.out.println("✅ Transfer successful: " + amount + " transferred from "
                    + account_number_from + " to " + account_number_to);

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}

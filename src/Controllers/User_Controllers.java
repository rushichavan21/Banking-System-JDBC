package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User_Controllers {
    static String User_Exists_QUERY = "SELECT * FROM user_data WHERE username = ?;";
    public static boolean user_exists(Connection con) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username=sc.nextLine();
        try (PreparedStatement pst = con.prepareStatement(User_Exists_QUERY)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User exists");
                    return true;
                } else {
                    System.out.println("User does not exist");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean user_exists(Connection con,String username) {
        try (PreparedStatement pst = con.prepareStatement(User_Exists_QUERY)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean user_register(Connection con) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = sc.nextLine();

        if (user_exists(con, username)) {
            System.out.println("User already exists");
            return false;
        }

        System.out.println("Enter password: ");
        String password = sc.nextLine();

        System.out.println("Enter the Initial Amount: ");
        double initial_balance = sc.nextDouble();

        try {
            String insertUser = "INSERT INTO user_data (username, password_hash) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                ps.setString(2, password);
                int rows = ps.executeUpdate();

                if (rows == 0) {
                    System.out.println("User register failed");
                    return false;
                }


                int userId = -1;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    }
                }

                String insertAccount = "INSERT INTO account_details (user_id, account_number, balance) VALUES (?, ?, ?)";
                try (PreparedStatement ps2 = con.prepareStatement(insertAccount)) {
                    ps2.setInt(1, userId);
                    ps2.setLong(2, System.currentTimeMillis());
                    ps2.setDouble(3, initial_balance);
                    ps2.executeUpdate();
                }

                System.out.println("User register success");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

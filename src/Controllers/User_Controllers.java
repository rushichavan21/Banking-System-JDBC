package Controllers;

import Pages.Accounts_page;

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
// just creates an entry in the user_data table and thus a userId will be provided.
// It's not mandatory for the user to like open the new account while registering.
// After registering user can create one or more accounts;

    public static void user_register(Connection con) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = sc.nextLine();

        if (user_exists(con, username)) {
            System.out.println("User already exists");
            return;
        }

        System.out.println("Enter password: ");
        String password = sc.nextLine();


        try {
            String insertUser = "INSERT INTO user_data (username, password_hash) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                ps.setString(2, password);
                int rows = ps.executeUpdate();

                if (rows == 0) {
                    System.out.println("User register failed");
                    return;
                }


                int userId = -1;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    }
                }

                System.out.println("User register success");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void login(Connection con) {
        Scanner sc = new Scanner(System.in);
        UI_Controllers.CreatePartition();
        System.out.println("Welcome to the login page");
        System.out.println("Enter the Username :");
        String username = sc.nextLine();
        System.out.println("Enter Password :");
        String password = sc.nextLine();
        String Query ="Select * from user_data where username = ?;";
        try(PreparedStatement ps = con.prepareStatement(Query)) {
            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()){

            if(rs.next()) {
                if(rs.getString("password_hash").equals(password)) {
                    System.out.println("Login success");
                    int user_id=rs.getInt("user_id");

                    Accounts_page.AccountsStart(con,user_id);
                }else{
                    System.out.println("Invalid Password");
                }
            }else{
                System.out.println("Username :" + username + " is not registered , Please register before login.");
            }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

package Pages;

import Controllers.*;

import java.sql.*;
import java.util.Scanner;

public class Banking_page {
   public static void BankingStart()
    {
        Connection con= Connection_Controllers.connect();
        Scanner sc = new Scanner(System.in);

        while (true) {
        UI_Controllers.CreatePartition();
        System.out.println("Welcome to the Banking System");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                User_Controllers.login(con);
                break;

            case 2:
                User_Controllers.user_register(con);
                break;

            case 3:
                System.out.println("Thank you for using the Banking System. Goodbye!");
                try {
                    if (con != null && !con.isClosed()) {
                        con.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    }
}

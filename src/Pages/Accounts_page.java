package Pages;

import Controllers.Accounts_Controllers;
import Controllers.UI_Controllers;

import java.sql.Connection;
import java.util.Scanner;

public class Accounts_page {
    public static void transactionWrapper(Connection con,int user_id) {
        Accounts_Controllers.get_accounts(con,user_id);
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your Account Number to proceed:");
        long accountNumber = sc.nextLong();
        Transactions_page.TransactionsStart(con,accountNumber);
    }

    public static void AccountsStart(Connection con, int user_id) {
        Scanner sc = new Scanner(System.in);
            while (true) {
                UI_Controllers.CreatePartition();
                System.out.println("Welcome to the Accounts Section");
                System.out.println("1. View Accounts");
                System.out.println("2. Create Account");
                System.out.println("3. Move to Transaction Page");
                System.out.println("4. Back");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        Accounts_Controllers.get_accounts(con, user_id);
                        break;

                    case 2:
                        Accounts_Controllers.open_account(con, user_id);
                        break;

                    case 3:
                        transactionWrapper(con,user_id);
                        break;

                    case 4:
                        System.out.println("Returning to Main Menu...");
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

    }



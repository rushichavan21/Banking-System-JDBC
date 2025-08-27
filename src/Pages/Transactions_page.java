package Pages;

import Controllers.UI_Controllers;
import Controllers.UserTasks_Controllers;

import java.sql.Connection;
import java.util.Scanner;

public class Transactions_page {
    public static void TransactionsStart(Connection con, Long accountNumber) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            UI_Controllers.CreatePartition();
            System.out.println("Welcome to the Transaction Section");
            System.out.println("1. Debit");
            System.out.println("2. Credit");
            System.out.println("3. Transfer");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    UserTasks_Controllers.debit(con, accountNumber);
                    break;

                case 2:
                    UserTasks_Controllers.credit(con, accountNumber);
                    break;

                case 3:
                    UserTasks_Controllers.transfer(con, accountNumber);
                    break;

                case 4:
                    UserTasks_Controllers.Check_Balance(con, accountNumber);
                    break;

                case 5:
                    System.out.println("Returning to Accounts Section...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

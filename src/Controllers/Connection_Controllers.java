package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection_Controllers {
    private static final String URL = "jdbc:mysql://localhost:3306/banking_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2327";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static Connection connect() {
        try {
            Class.forName(DRIVER);
            System.out.println("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver: " + e.getMessage());
        }

        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to database successfully");
            return con;
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return con;
    }
}

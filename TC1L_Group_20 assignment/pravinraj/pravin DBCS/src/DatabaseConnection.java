package event_management_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=EventManagementDB;encrypt=true;trustServerCertificate=true;";

    private static final String USER = "ems_user";
    private static final String PASSWORD = "Admin12345!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
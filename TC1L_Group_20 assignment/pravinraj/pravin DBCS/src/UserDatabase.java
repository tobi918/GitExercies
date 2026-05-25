package event_management_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDatabase {

    public static boolean isValidCustomer(String username, String password) {
        String sql = "SELECT * FROM Customers WHERE username = ? AND password = ?";
        String hashedPassword = hashPassword(password);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException ex) {
            System.out.println("Login database error: " + ex.getMessage());
            return false;
        }
    }

    public static boolean addCustomer(String username, String password) {
        String sql = "INSERT INTO Customers (username, password) VALUES (?, ?)";
        String hashedPassword = hashPassword(password);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            stmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Register database error: " + ex.getMessage());
            return false;
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();

            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Password hashing error: " + ex.getMessage());
        }
    }
}
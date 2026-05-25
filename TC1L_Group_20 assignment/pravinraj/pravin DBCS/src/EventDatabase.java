package event_management_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventDatabase {

    public static ArrayList<Event> getAllEvents() {
        return EventManager.getEvents();
    }

    public static void addEvent(Event e) {
        EventManager.addEvent(e);
    }

    public static void registerEvent(Event e) {

        String username = Session.getCurrentUsername();

        if (username == null || username.isEmpty()) {
            System.out.println("No logged-in user found. Cannot register event.");
            return;
        }

        String sql =
                "INSERT INTO RegisteredEvents (customer_username, event_id, payment_status) " +
                "SELECT ?, e.event_id, 'UNPAID' " +
                "FROM Events e " +
                "WHERE e.name = ? " +
                "AND e.event_date = ? " +
                "AND e.venue = ? " +
                "AND e.type = ? " +
                "AND NOT EXISTS ( " +
                "    SELECT 1 FROM RegisteredEvents r " +
                "    WHERE r.customer_username = ? " +
                "    AND r.event_id = e.event_id " +
                ")";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, username);
            stmt.setString(2, e.getName());
            stmt.setString(3, e.getDate());
            stmt.setString(4, e.getVenue());
            stmt.setString(5, e.getType());
            stmt.setString(6, username);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Registered event successfully for " + username + ": " + e.getName());
            } else {
                System.out.println("No event inserted. Event may already be registered or not found.");
            }

        } catch (SQLException ex) {
            System.out.println("Unable to register event in database: " + ex.getMessage());
        }
    }

    public static void unregisterEvent(Event e) {

        String username = Session.getCurrentUsername();

        if (username == null || username.isEmpty()) {
            System.out.println("No logged-in user found. Cannot unregister event.");
            return;
        }

        String sql =
                "DELETE FROM RegisteredEvents " +
                "WHERE customer_username = ? " +
                "AND event_id IN ( " +
                "    SELECT event_id FROM Events " +
                "    WHERE name = ? " +
                "    AND event_date = ? " +
                "    AND venue = ? " +
                "    AND type = ? " +
                ")";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, username);
            stmt.setString(2, e.getName());
            stmt.setString(3, e.getDate());
            stmt.setString(4, e.getVenue());
            stmt.setString(5, e.getType());

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Unregistered event successfully for " + username + ": " + e.getName());
            } else {
                System.out.println("No registered event found to delete.");
            }

        } catch (SQLException ex) {
            System.out.println("Unable to unregister event from database: " + ex.getMessage());
        }
    }

    public static ArrayList<Event> getRegisteredEvents() {

        ArrayList<Event> registeredEvents = new ArrayList<>();

        String username = Session.getCurrentUsername();

        if (username == null || username.isEmpty()) {
            return registeredEvents;
        }

        String sql =
                "SELECT e.name, e.event_date, e.venue, e.type, e.capacity, e.fee " +
                "FROM RegisteredEvents r " +
                "JOIN Events e ON r.event_id = e.event_id " +
                "WHERE r.customer_username = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Event event = new Event(
                        rs.getString("name"),
                        rs.getString("event_date"),
                        rs.getString("venue"),
                        rs.getString("type"),
                        rs.getInt("capacity"),
                        rs.getDouble("fee")
                );

                registeredEvents.add(event);
            }

        } catch (SQLException ex) {
            System.out.println("Unable to load registered events from database: " + ex.getMessage());
        }

        return registeredEvents;
    }

    public static void markEventAsPaid(Event e, double paidAmount) {

        String username = Session.getCurrentUsername();

        if (username == null || username.isEmpty()) {
            System.out.println("No logged-in user found. Cannot update payment.");
            return;
        }

        String sql =
                "UPDATE RegisteredEvents " +
                "SET payment_status = 'PAID', paid_amount = ?, payment_date = GETDATE() " +
                "WHERE customer_username = ? " +
                "AND event_id IN ( " +
                "    SELECT event_id FROM Events " +
                "    WHERE name = ? " +
                "    AND event_date = ? " +
                "    AND venue = ? " +
                "    AND type = ? " +
                ")";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setDouble(1, paidAmount);
            stmt.setString(2, username);
            stmt.setString(3, e.getName());
            stmt.setString(4, e.getDate());
            stmt.setString(5, e.getVenue());
            stmt.setString(6, e.getType());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Payment updated successfully for " + username + ": RM" + paidAmount);
            } else {
                System.out.println("No matching registered event found to update payment.");
            }

        } catch (SQLException ex) {
            System.out.println("Unable to update payment in database: " + ex.getMessage());
        }
    }

    public static void clearRegisteredEvents() {

        String username = Session.getCurrentUsername();

        if (username == null || username.isEmpty()) {
            System.out.println("No logged-in user found. Cannot clear registered events.");
            return;
        }

        String sql = "DELETE FROM RegisteredEvents WHERE customer_username = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, username);

            int rowsDeleted = stmt.executeUpdate();

            System.out.println("Cleared registered events for " + username + ". Rows deleted: " + rowsDeleted);

        } catch (SQLException ex) {
            System.out.println("Unable to clear registered events from database: " + ex.getMessage());
        }
    }

    public static void saveRegisteredEvents() {
        // Not needed anymore because registered events are saved directly into SQL Server.
    }
}
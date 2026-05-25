package event_management_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventManager {

    public static void addEvent(Event event) {
        String sql = "INSERT INTO Events (name, event_date, venue, type, capacity, fee) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, event.getName());
            stmt.setString(2, event.getDate());
            stmt.setString(3, event.getVenue());
            stmt.setString(4, event.getType());
            stmt.setInt(5, event.getCapacity());
            stmt.setDouble(6, event.getFee());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Event added successfully: " + event.getName());
            }

        } catch (SQLException ex) {
            System.out.println("Unable to add event to database: " + ex.getMessage());
        }
    }

    public static ArrayList<Event> getEvents() {
        ArrayList<Event> events = new ArrayList<>();

        String sql = "SELECT * FROM Events ORDER BY event_id";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Event event = new Event(
                        rs.getString("name"),
                        rs.getString("event_date"),
                        rs.getString("venue"),
                        rs.getString("type"),
                        rs.getInt("capacity"),
                        rs.getDouble("fee")
                );

                events.add(event);
            }

        } catch (SQLException ex) {
            System.out.println("Unable to load events from database: " + ex.getMessage());
        }

        return events;
    }

    public static void removeEvent(int index) {
        ArrayList<Event> events = getEvents();

        if (index >= 0 && index < events.size()) {
            Event selectedEvent = events.get(index);

            String sql = "DELETE FROM Events WHERE event_id = (" +
                    "SELECT TOP 1 event_id FROM Events " +
                    "WHERE name = ? AND event_date = ? AND venue = ? AND type = ? " +
                    "ORDER BY event_id" +
                    ")";

            try (
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
                stmt.setString(1, selectedEvent.getName());
                stmt.setString(2, selectedEvent.getDate());
                stmt.setString(3, selectedEvent.getVenue());
                stmt.setString(4, selectedEvent.getType());

                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Event deleted successfully: " + selectedEvent.getName());
                } else {
                    System.out.println("No matching event found to delete: " + selectedEvent.getName());
                }

            } catch (SQLException ex) {
                System.out.println("Unable to delete event from database: " + ex.getMessage());
            }
        }
    }

    public static void updateEvent(int index, Event updatedEvent) {
        ArrayList<Event> events = getEvents();

        if (index >= 0 && index < events.size()) {
            Event oldEvent = events.get(index);

            String sql = "UPDATE Events SET " +
                    "name = ?, event_date = ?, venue = ?, type = ?, capacity = ?, fee = ? " +
                    "WHERE event_id = (" +
                    "SELECT TOP 1 event_id FROM Events " +
                    "WHERE name = ? AND event_date = ? AND venue = ? AND type = ? " +
                    "ORDER BY event_id" +
                    ")";

            try (
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
                stmt.setString(1, updatedEvent.getName());
                stmt.setString(2, updatedEvent.getDate());
                stmt.setString(3, updatedEvent.getVenue());
                stmt.setString(4, updatedEvent.getType());
                stmt.setInt(5, updatedEvent.getCapacity());
                stmt.setDouble(6, updatedEvent.getFee());

                stmt.setString(7, oldEvent.getName());
                stmt.setString(8, oldEvent.getDate());
                stmt.setString(9, oldEvent.getVenue());
                stmt.setString(10, oldEvent.getType());

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Event updated successfully in SQL Server: " + updatedEvent.getName());
                } else {
                    System.out.println("No matching event found to update.");
                    System.out.println("Old event name: " + oldEvent.getName());
                    System.out.println("Old event date: " + oldEvent.getDate());
                    System.out.println("Old event venue: " + oldEvent.getVenue());
                    System.out.println("Old event type: " + oldEvent.getType());
                }

            } catch (SQLException ex) {
                System.out.println("Unable to update event in database: " + ex.getMessage());
            }
        }
    }

    public static void saveEvents() {
        // Not needed anymore. Events are saved directly into SQL Server.
    }
}
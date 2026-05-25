package event_management_system;

import java.io.Serializable;
import java.util.Objects;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String date;
    private String venue;
    private String type;
    private int capacity;
    private double fee;

    public Event(String name, String date, String venue, String type, int capacity, double fee) {
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.type = type;
        this.capacity = capacity;
        this.fee = fee;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFee() {
        return fee;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Event)) {
            return false;
        }
        Event other = (Event) obj;
        return capacity == other.capacity
                && Double.compare(fee, other.fee) == 0
                && Objects.equals(name, other.name)
                && Objects.equals(date, other.date)
                && Objects.equals(venue, other.venue)
                && Objects.equals(type, other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, venue, type, capacity, fee);
    }
}

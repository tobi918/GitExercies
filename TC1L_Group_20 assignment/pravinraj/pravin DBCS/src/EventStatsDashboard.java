package event_management_system;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.border.LineBorder;

public class EventStatsDashboard extends JFrame {
    public EventStatsDashboard() {
        setTitle("Event Statistics Dashboard");
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header Label
        JLabel header = new JLabel("Event Statistics", SwingConstants.CENTER);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 26));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(header, BorderLayout.NORTH);
        
        // Stats Panel with border
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        ArrayList<Event> events = EventManager.getEvents();
        if (events.isEmpty()) {
            JLabel empty = new JLabel("No events found.", SwingConstants.CENTER);
            empty.setFont(new Font("Arial", Font.PLAIN, 16));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            statsPanel.add(empty);
        } else {
            // Calculate statistics
            int total = events.size();
            double sum = 0;
            double max = Double.MIN_VALUE;
            String mostExpensive = "";
            HashMap<String, Integer> typeCount = new HashMap<>();

            for (Event e : events) {
                sum += e.getFee();
                if (e.getFee() > max) {
                    max = e.getFee();
                    mostExpensive = e.getName();
                }
                typeCount.put(e.getType(), typeCount.getOrDefault(e.getType(), 0) + 1);
            }

            double avg = sum / total;

            // Add stats with consistent styling
            addStat(statsPanel, "Total Events: " + total);
            addStat(statsPanel, "Average Fee: RM" + String.format("%.2f", avg));
            addStat(statsPanel, "Most Expensive: " + mostExpensive + " (RM" + String.format("%.2f", max) + ")");
            
            // Add type breakdown
            JLabel typeHeader = new JLabel("Events by Type:");
            typeHeader.setFont(new Font("Arial", Font.BOLD, 16));
            typeHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            typeHeader.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
            statsPanel.add(typeHeader);
            
            for (String type : typeCount.keySet()) {
                addStat(statsPanel, "- " + type + ": " + typeCount.get(type));
            }
        }
        
        // Scroll pane for stats
        JScrollPane scrollPane = new JScrollPane(statsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }

    private void addStat(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(label);
    }
}

package event_management_system;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.border.LineBorder;

public class CancelEventForm extends JFrame {
    private JList<String> eventList;
    private DefaultListModel<String> listModel;

    public CancelEventForm() {
        setTitle("Cancel Event");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Label
        JLabel title = new JLabel("Cancel Event", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);
        
        // Event List Panel with border
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        
        // Initialize list model and load events
        listModel = new DefaultListModel<>();
        loadEvents();
        
        eventList = new JList<>(listModel);
        eventList.setFont(new Font("Arial", Font.PLAIN, 14));
        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventList.setVisibleRowCount(5);
        eventList.setFixedCellHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(eventList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        listPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton cancelButton = createStyledButton("Cancel Selected Event");
        cancelButton.setPreferredSize(new Dimension(220, 45));
        cancelButton.addActionListener(e -> {
            int index = eventList.getSelectedIndex();
            if (index != -1) {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to cancel this event?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    EventManager.removeEvent(index);
                    listModel.remove(index);
                    JOptionPane.showMessageDialog(this, "Event cancelled successfully.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an event to cancel.");
            }
        });
        
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }

    private void loadEvents() {
        ArrayList<Event> events = EventManager.getEvents();
        listModel.clear();
        for (Event e : events) {
            listModel.addElement(formatEventString(e));
        }
    }
    
    private String formatEventString(Event e) {
        return String.format("%s | %s | %s | %s | Capacity: %d | Fee: RM%.2f",
            e.getName(),
            e.getType(),
            e.getDate(),
            e.getVenue(),
            e.getCapacity(),
            e.getFee());
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
        
        return button;
    }
}
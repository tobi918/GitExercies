package event_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ViewEventList extends JFrame {
    private ArrayList<Event> events;
    private List<Event> selectedEvents;
    private DefaultListModel<String> listModel;
    private JList<String> eventJList;

    public ViewEventList() {
        setTitle("Register Event List");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        events = EventManager.getEvents(); // all events
        selectedEvents = new ArrayList<>();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Event List", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        for (Event e : events) {
            listModel.addElement(formatEventDetails(e));
        }

        eventJList = new JList<>(listModel);
        eventJList.setFont(new Font("Arial", Font.PLAIN, 14));
        eventJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        eventJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (EventDatabase.getRegisteredEvents().contains(events.get(index))) {
                    c.setBackground(new Color(144, 238, 144)); // Light green
                    c.setForeground(Color.BLACK);
                } else if (isSelected) {
                    c.setBackground(list.getSelectionBackground());
                    c.setForeground(list.getSelectionForeground());
                } else {
                    c.setBackground(list.getBackground());
                    c.setForeground(list.getForeground());
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(eventJList);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton registerButton = new JButton("Register for Selected Events");
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(250, 40));

        registerButton.addActionListener(e -> {
            int[] selectedIndices = eventJList.getSelectedIndices();
            if (selectedIndices.length == 0) {
                JOptionPane.showMessageDialog(this, "Please select at least one event first.",
                        "No Event Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (int index : selectedIndices) {
                Event selectedEvent = events.get(index);
                if (!EventDatabase.getRegisteredEvents().contains(selectedEvent)) {
                    EventDatabase.registerEvent(selectedEvent);
                    selectedEvents.add(selectedEvent);
                }
            }

            eventJList.repaint();

            StringBuilder confirmationMessage = new StringBuilder();
            confirmationMessage.append("You have been registered for the following events:\n\n");
            for (Event event : selectedEvents) {
                confirmationMessage.append("- ").append(event.getName()).append("\n");
            }
            confirmationMessage.append("\nPlease continue with your payment.");
            JOptionPane.showMessageDialog(this, confirmationMessage.toString(),
                    "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private String formatEventDetails(Event e) {
        return String.format("%s | %s | %s | %s | Capacity: %d | Fee: RM%.2f",
                e.getName(), e.getType(), e.getDate(), e.getVenue(), e.getCapacity(), e.getFee());
    }
}

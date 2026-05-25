package event_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;
import java.util.ArrayList;

public class UpdateEventForm extends JFrame {
    private JList<String> eventList;
    private DefaultListModel<String> listModel;
    private ArrayList<Event> events;
    private JPanel mainPanel;

    public UpdateEventForm() {
        initializeUI();
        setupEventList();
        setupLoadButton();
    }

    private void initializeUI() {
        setTitle("Update Event");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel title = new JLabel("Update Existing Event", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        add(mainPanel);
    }

    private void setupEventList() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        listModel = new DefaultListModel<>();
        events = EventManager.getEvents();

        for (Event e : events) {
            listModel.addElement(e.getName() + " - " + e.getDate());
        }

        eventList = new JList<>(listModel);
        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventList.setFont(new Font("Arial", Font.PLAIN, 14));
        eventList.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(eventList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        listPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(listPanel, BorderLayout.CENTER);
    }

    private void setupLoadButton() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton loadButton = createStyledButton("Load Selected Event");
        loadButton.setPreferredSize(new Dimension(200, 40));
        loadButton.addActionListener(e -> handleLoadButtonClick());

        buttonPanel.add(loadButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLoadButtonClick() {
        int index = eventList.getSelectedIndex();

        if (index != -1) {
            showUpdateForm(index);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event to update.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void showUpdateForm(int index) {
        Event event = events.get(index);
        String[] dateParts = event.getDate().split(" ");

        JDialog updateDialog = new JDialog(this, "Update Event Details", true);
        updateDialog.setSize(600, 700);
        updateDialog.setLocationRelativeTo(this);
        updateDialog.getContentPane().setBackground(Color.WHITE);

        JPanel dialogMainPanel = new JPanel(new BorderLayout());
        dialogMainPanel.setBackground(Color.WHITE);
        dialogMainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel title = new JLabel("Update Event: " + event.getName(), SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        dialogMainPanel.add(title, BorderLayout.NORTH);

        JTextField nameField = createFormTextField(event.getName());

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setBackground(Color.WHITE);

        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }

        JComboBox<Integer> dayBox = new JComboBox<>(days);
        styleComboBox(dayBox);

        if (dateParts.length >= 1) {
            try {
                dayBox.setSelectedItem(Integer.parseInt(dateParts[0]));
            } catch (NumberFormatException ignored) {
                dayBox.setSelectedItem(1);
            }
        }

        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        JComboBox<String> monthBox = new JComboBox<>(months);
        styleComboBox(monthBox);

        if (dateParts.length >= 2) {
            monthBox.setSelectedItem(dateParts[1]);
        }

        Integer[] years = new Integer[16];
        for (int i = 0; i < 16; i++) {
            years[i] = 2020 + i;
        }

        JComboBox<Integer> yearBox = new JComboBox<>(years);
        styleComboBox(yearBox);

        if (dateParts.length >= 3) {
            try {
                yearBox.setSelectedItem(Integer.parseInt(dateParts[2]));
            } catch (NumberFormatException ignored) {
                yearBox.setSelectedItem(2026);
            }
        }

        datePanel.add(dayBox);
        datePanel.add(monthBox);
        datePanel.add(yearBox);

        JTextField venueField = createFormTextField(event.getVenue());
        JComboBox<String> typeBox = createTypeComboBox(event.getType());
        JTextField capacityField = createFormTextField(String.valueOf(event.getCapacity()));
        JTextField feeField = createFormTextField(String.valueOf(event.getFee()));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormField(formPanel, gbc, "Event Name:", 0, nameField);

        JLabel dateLabel = new JLabel("Event Date:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(datePanel, gbc);

        addFormField(formPanel, gbc, "Venue:", 2, venueField);
        addFormField(formPanel, gbc, "Type:", 3, typeBox);
        addFormField(formPanel, gbc, "Capacity:", 4, capacityField);
        addFormField(formPanel, gbc, "Registration Fee (RM):", 5, feeField);

        dialogMainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton updateButton = createStyledButton("Update Event");
        updateButton.setPreferredSize(new Dimension(200, 40));

        updateButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();

                int day = (int) dayBox.getSelectedItem();
                String month = (String) monthBox.getSelectedItem();
                int year = (int) yearBox.getSelectedItem();

                String date = day + " " + month + " " + year;
                String venue = venueField.getText().trim();
                String type = (String) typeBox.getSelectedItem();

                int capacity = Integer.parseInt(capacityField.getText().trim());
                double fee = Double.parseDouble(feeField.getText().trim());

                if (name.isEmpty() || venue.isEmpty()) {
                    throw new IllegalArgumentException("Fields cannot be empty.");
                }

                if (capacity <= 0 || fee < 0) {
                    throw new IllegalArgumentException("Capacity and fee must be valid.");
                }

                Event updatedEvent = new Event(name, date, venue, type, capacity, fee);

                EventManager.updateEvent(index, updatedEvent);

                events = EventManager.getEvents();

                listModel.clear();
                for (Event updatedListEvent : events) {
                    listModel.addElement(updatedListEvent.getName() + " - " + updatedListEvent.getDate());
                }

                JOptionPane.showMessageDialog(
                        updateDialog,
                        "Event updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                updateDialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        updateDialog,
                        "Please enter valid numbers for capacity and fee.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        updateDialog,
                        "All fields must be filled correctly.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        buttonPanel.add(updateButton);
        dialogMainPanel.add(buttonPanel, BorderLayout.SOUTH);

        updateDialog.add(dialogMainPanel);
        updateDialog.setVisible(true);
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, int row, Component field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    private JTextField createFormTextField(String initialValue) {
        JTextField field = new JTextField(initialValue, 20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JComboBox<String> createTypeComboBox(String selectedType) {
        String[] types = {
                "Seminar",
                "Workshop",
                "Cultural Event",
                "Sports Event"
        };

        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setSelectedItem(selectedType);
        styleComboBox(typeBox);

        return typeBox;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        return button;
    }
}
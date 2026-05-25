package event_management_system;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class CreateEventForm extends JFrame {
    public CreateEventForm() {
        setTitle("Create New Event");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        
        // Main panel with BorderLayout for better title positioning
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Title Label (positioned closer to form)
        JLabel title = new JLabel("Create New Event", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Reduced bottom padding
        mainPanel.add(title, BorderLayout.NORTH);
        
        // Form Panel with border
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(12, 15, 12, 15);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.anchor = GridBagConstraints.WEST;
        
        // Form Fields with consistent styling
        addFormField(formPanel, formGbc, "Event Name:", 0);
        JTextField nameField = createFormTextField();
        formGbc.gridx = 1;
        formPanel.add(nameField, formGbc);
        
        addFormField(formPanel, formGbc, "Event Date:", 1);
        
        // Create date selection components
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setBackground(Color.WHITE);
        
        // Day dropdown (1-31)
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }
        JComboBox<Integer> dayBox = new JComboBox<>(days);
        styleComboBox(dayBox);
        
        // Month dropdown (January-December)
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthBox = new JComboBox<>(months);
        styleComboBox(monthBox);
        
        // Year dropdown (2020-2035)
        Integer[] years = new Integer[16];
        for (int i = 0; i < 16; i++) {
            years[i] = 2020 + i;
        }
        JComboBox<Integer> yearBox = new JComboBox<>(years);
        styleComboBox(yearBox);
        
        datePanel.add(dayBox);
        datePanel.add(monthBox);
        datePanel.add(yearBox);
        
        formGbc.gridx = 1;
        formPanel.add(datePanel, formGbc);
        
        addFormField(formPanel, formGbc, "Venue:", 2);
        JTextField venueField = createFormTextField();
        formGbc.gridx = 1;
        formPanel.add(venueField, formGbc);
        
        addFormField(formPanel, formGbc, "Type:", 3);
        String[] types = { "Seminar", "Workshop", "Cultural Event", "Sports Event" };
        JComboBox<String> typeBox = new JComboBox<>(types);
        styleComboBox(typeBox);
        formGbc.gridx = 1;
        formPanel.add(typeBox, formGbc);
        
        addFormField(formPanel, formGbc, "Capacity:", 4);
        JTextField capacityField = createFormTextField();
        formGbc.gridx = 1;
        formPanel.add(capacityField, formGbc);
        
        addFormField(formPanel, formGbc, "Registration Fee (RM):", 5);
        JTextField feeField = createFormTextField();
        formGbc.gridx = 1;
        formPanel.add(feeField, formGbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JButton saveButton = createStyledButton("Add Event");
        saveButton.setPreferredSize(new Dimension(200, 40));
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                // Get date components
                int day = (int) dayBox.getSelectedItem();
                String month = (String) monthBox.getSelectedItem();
                int year = (int) yearBox.getSelectedItem();
                String date = day + " " + month + " " + year;
                
                String venue = venueField.getText().trim();
                String type = (String) typeBox.getSelectedItem();
                int capacity = Integer.parseInt(capacityField.getText().trim());
                double fee = Double.parseDouble(feeField.getText().trim());

                if (name.isEmpty() || venue.isEmpty() || capacity <= 0 || fee < 0) {
                    throw new IllegalArgumentException("Invalid event details.");
                }

                Event newEvent = new Event(name, date, venue, type, capacity, fee);
                EventManager.addEvent(newEvent);

                JOptionPane.showMessageDialog(this, "Event created successfully!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check all fields.");
            }
        });
        
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);
    }
    
    private JTextField createFormTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
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

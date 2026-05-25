package event_management_system;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.border.*;

public class RegisterEventForm extends JFrame {
    private ArrayList<Event> registeredEvents;
    private JList<String> eventList;
    private JTextArea selectedEventDetails;
    private JCheckBox earlyBirdBox, groupBox;
    private JButton proceedButton;
    private JButton cancelButton;

    public RegisterEventForm() {
        this(null);
    }

    public RegisterEventForm(ArrayList<Event> registeredEvents) {
        if (registeredEvents == null || registeredEvents.isEmpty()) {
            this.registeredEvents = EventDatabase.getRegisteredEvents();
        } else {
            this.registeredEvents = registeredEvents;
        }

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Payment Details");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Payment Details - Select Registered Event", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(Color.WHITE);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        listPanel.setBackground(Color.WHITE);

        eventList = new JList<>();
        eventList.setFont(new Font("Arial", Font.PLAIN, 14));
        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventList.setFixedCellHeight(35);
        eventList.setBackground(Color.WHITE);

        eventList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedEventDetails();
            }
        });

        refreshEventList();

        JScrollPane scrollPane = new JScrollPane(eventList);
        scrollPane.setPreferredSize(new Dimension(760, 230));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        listPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(listPanel, BorderLayout.NORTH);

        selectedEventDetails = new JTextArea();
        selectedEventDetails.setEditable(false);
        selectedEventDetails.setLineWrap(true);
        selectedEventDetails.setWrapStyleWord(true);
        selectedEventDetails.setFont(new Font("Arial", Font.PLAIN, 14));
        selectedEventDetails.setBackground(new Color(248, 248, 248));
        selectedEventDetails.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        selectedEventDetails.setText("Select a registered event to view its details.");

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(new CompoundBorder(
                new TitledBorder("Customer Selected Event"),
                new EmptyBorder(8, 8, 8, 8)
        ));
        detailsPanel.add(selectedEventDetails, BorderLayout.CENTER);
        contentPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        proceedButton = createStyledButton("Proceed to Payment Options");
        proceedButton.setPreferredSize(new Dimension(320, 45));

        proceedButton.addActionListener(e -> {
            int index = eventList.getSelectedIndex();

            if (index == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a registered event first.",
                        "No Event Selected",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                showPaymentOptions(registeredEvents.get(index));
            }
        });

        cancelButton = createStyledButton("Cancel Registration");
        cancelButton.setPreferredSize(new Dimension(260, 45));
        cancelButton.setBackground(new Color(180, 70, 70));

        cancelButton.addActionListener(e -> cancelSelectedRegistration());

        buttonPanel.add(proceedButton);
        buttonPanel.add(cancelButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        updateButtonsState();

        if (registeredEvents == null || registeredEvents.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "You have not registered for any events yet.",
                    "No Registered Events",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        setVisible(true);
    }

    private void cancelSelectedRegistration() {
        int index = eventList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event to cancel.",
                    "No Event Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Event selectedEvent = registeredEvents.get(index);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel registration for:\n" + selectedEvent.getName() + "?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            EventDatabase.unregisterEvent(selectedEvent);

            registeredEvents = EventDatabase.getRegisteredEvents();

            refreshEventList();

            selectedEventDetails.setText("Select a registered event to view its details.");
            updateButtonsState();

            JOptionPane.showMessageDialog(
                    this,
                    "Registration cancelled successfully.",
                    "Cancelled",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void refreshEventList() {
        DefaultListModel<String> model = new DefaultListModel<>();

        if (registeredEvents != null) {
            for (Event event : registeredEvents) {
                model.addElement(formatEventString(event));
            }
        }

        eventList.setModel(model);
    }

    private void updateButtonsState() {
        boolean hasRegisteredEvents = registeredEvents != null && !registeredEvents.isEmpty();

        if (proceedButton != null) {
            proceedButton.setEnabled(hasRegisteredEvents);
        }

        if (cancelButton != null) {
            cancelButton.setEnabled(hasRegisteredEvents);
        }
    }

    private void updateSelectedEventDetails() {
        int index = eventList.getSelectedIndex();

        if (index < 0 || registeredEvents == null || index >= registeredEvents.size()) {
            selectedEventDetails.setText("Select a registered event to view its details.");
            return;
        }

        Event event = registeredEvents.get(index);

        selectedEventDetails.setText(
                "Event Name: " + event.getName() + "\n"
                        + "Type: " + event.getType() + "\n"
                        + "Date: " + event.getDate() + "\n"
                        + "Venue: " + event.getVenue() + "\n"
                        + "Capacity: " + event.getCapacity() + "\n"
                        + String.format("Registration Fee: RM%.2f", event.getFee())
        );
    }

    private void showPaymentOptions(Event selectedEvent) {
        JFrame paymentFrame = new JFrame("Payment Options");
        paymentFrame.setSize(600, 500);
        paymentFrame.setLocationRelativeTo(this);
        paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Payment Options for: " + selectedEvent.getName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBorder(new CompoundBorder(
                new TitledBorder("Additional Options"),
                new EmptyBorder(15, 15, 15, 15)
        ));
        optionsPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        earlyBirdBox = createStyledCheckBox("Apply Early Bird Discount (10%)");
        groupBox = createStyledCheckBox("Apply Group Discount (15%)");

        gbc.gridx = 0;
        gbc.gridy = 0;
        optionsPanel.add(earlyBirdBox, gbc);

        gbc.gridy = 1;
        optionsPanel.add(groupBox, gbc);

        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        JButton payButton = createStyledButton("Pay Now");
        payButton.setPreferredSize(new Dimension(180, 40));
        payButton.setBackground(new Color(46, 139, 87));

        payButton.addActionListener(e -> {
            double baseFee = selectedEvent.getFee();
            double discount = calculateDiscount(baseFee);
            double netPayable = baseFee - discount;

            int confirm = JOptionPane.showConfirmDialog(
                    paymentFrame,
                    String.format(
                            "Confirm payment for %s?\n\nTotal Payable: RM%.2f",
                            selectedEvent.getName(),
                            netPayable
                    ),
                    "Confirm Payment",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                // This line saves payment status, paid amount, and payment date into SQL Server
                EventDatabase.markEventAsPaid(selectedEvent, netPayable);

                generateBill(selectedEvent);
                paymentFrame.dispose();

                JOptionPane.showMessageDialog(
                        this,
                        "Payment successful. Bill has been generated.",
                        "Payment Completed",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(payButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        paymentFrame.add(mainPanel);
        paymentFrame.setVisible(true);
    }

    private double calculateDiscount(double baseFee) {
        double discount = 0;

        if (earlyBirdBox.isSelected()) {
            discount += baseFee * 0.10;
        }

        if (groupBox.isSelected()) {
            discount += baseFee * 0.15;
        }

        return discount;
    }

    private void generateBill(Event event) {
        double baseFee = event.getFee();
        double discount = calculateDiscount(baseFee);
        double netPayable = baseFee - discount;

        new BillDialog(event, baseFee, 0, discount, netPayable);
    }

    private String formatEventString(Event event) {
        return String.format(
                "%s | %s | %s | %s | Capacity: %d | Fee: RM%.2f",
                event.getName(),
                event.getType(),
                event.getDate(),
                event.getVenue(),
                event.getCapacity(),
                event.getFee()
        );
    }

    private JCheckBox createStyledCheckBox(String text) {
        JCheckBox box = new JCheckBox(text);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setBackground(Color.WHITE);
        box.setFocusPainted(false);
        return box;
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
                if (button.isEnabled()) {
                    if (button.getText().equals("Cancel Registration")) {
                        button.setBackground(new Color(200, 90, 90));
                    } else if (button.getText().equals("Pay Now")) {
                        button.setBackground(new Color(60, 160, 100));
                    } else {
                        button.setBackground(new Color(100, 150, 200));
                    }
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    if (button.getText().equals("Cancel Registration")) {
                        button.setBackground(new Color(180, 70, 70));
                    } else if (button.getText().equals("Pay Now")) {
                        button.setBackground(new Color(46, 139, 87));
                    } else {
                        button.setBackground(new Color(70, 130, 180));
                    }
                }
            }
        });

        return button;
    }
}
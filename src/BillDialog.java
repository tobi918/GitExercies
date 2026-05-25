package event_management_system;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BillDialog extends JFrame {
    private Event event;
    private double base, extras, discount, net;
    private Path savedReceiptPath;

    public BillDialog(Event event, double base, double extras, double discount, double net) {
        this.event = event;
        this.base = base;
        this.extras = extras;
        this.discount = discount;
        this.net = net;
        this.savedReceiptPath = saveReceiptToAppFolder();

        setTitle("Fee Breakdown");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("Billing Details", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(100, 20, 250, 30);
        add(title);

        int y = 80;
        addLabel("Event: " + event.getName(), y); y += 30;
        addLabel(String.format("Base Fee: RM%.2f", base), y); y += 30;
        addLabel(String.format("Total Before Discount: RM%.2f", base), y); y += 30;
        addLabel(String.format("Discount Applied: -RM%.2f", discount), y); y += 30;
        addLabel(String.format("Net Payable: RM%.2f", net), y); y += 30;

        JButton exportBtn = new JButton("Save Receipt");
        exportBtn.setBounds(125, y + 20, 200, 40);
        exportBtn.setBackground(new Color(70, 130, 180));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setOpaque(true);
        exportBtn.setBorderPainted(false);
        exportBtn.setFocusPainted(false);
        exportBtn.setFont(new Font("Arial", Font.BOLD, 14));
        exportBtn.addActionListener(e -> showSavedReceiptMessage());
        add(exportBtn);

        setVisible(true);
    }

    private void addLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setBounds(50, y, 350, 25);
        add(label);
    }

    private Path saveReceiptToAppFolder() {
        try {
            Path receiptDir = Paths.get(
                    System.getProperty("user.home"),
                    "EventManagementSystem",
                    "receipts"
            );
            Files.createDirectories(receiptDir);
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String sanitizedEventName = event.getName().replaceAll("[^a-zA-Z0-9]", "_");
            Path receiptPath = receiptDir.resolve(String.format("receipt_%s_%s.txt", sanitizedEventName, timestamp));

            try (FileWriter writer = new FileWriter(receiptPath.toFile())) {
                writer.write("===== Event Registration Receipt =====\n");
                writer.write("Event: " + event.getName() + "\n");
                writer.write("Date: " + event.getDate() + "\n");
                writer.write("Venue: " + event.getVenue() + "\n");
                writer.write("Type: " + event.getType() + "\n");
                writer.write("--------------------------------------\n");
                writer.write(String.format("Base Fee: RM%.2f\n", base));
                writer.write(String.format("Discount: -RM%.2f\n", discount));
                writer.write(String.format("Net Payable: RM%.2f\n", net));
                writer.write("======================================\n");
            }

            return receiptPath;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to save receipt.\n" + ex.getMessage(),
                    "Receipt Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void showSavedReceiptMessage() {
        if (savedReceiptPath == null) {
            savedReceiptPath = saveReceiptToAppFolder();
        }

        if (savedReceiptPath != null) {
            JOptionPane.showMessageDialog(this,
                    "Receipt saved to:\n" + savedReceiptPath,
                    "Receipt Saved",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

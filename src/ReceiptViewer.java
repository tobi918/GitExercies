package event_management_system;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.ArrayList;
import javax.swing.border.*;

public class ReceiptViewer extends JFrame {
    private JTextArea textArea;
    private JPanel receiptPanel;
    private JComboBox<String> eventComboBox;
    private ArrayList<Path> eventFiles;

    public ReceiptViewer() {
        setTitle("Event Bill Viewer");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        // Title label
        JLabel title = new JLabel("Event Bill Viewer", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        headerPanel.add(title, BorderLayout.NORTH);

        // Event selection panel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        selectionPanel.setBackground(Color.WHITE);
        
        JLabel selectLabel = new JLabel("Select Event:");
        selectLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        selectionPanel.add(selectLabel);
        
        eventComboBox = new JComboBox<>();
        eventComboBox.setPreferredSize(new Dimension(300, 30));
        selectionPanel.add(eventComboBox);
        
        JButton loadButton = createStyledButton("Load Bill");
        loadButton.setPreferredSize(new Dimension(120, 30));
        loadButton.addActionListener(e -> loadSelectedEventBill());
        selectionPanel.add(loadButton);
        
        headerPanel.add(selectionPanel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Receipt panel
        receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        receiptPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(receiptPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton closeButton = createStyledButton("Close");
        closeButton.setPreferredSize(new Dimension(150, 40));
        closeButton.setToolTipText("Close this window");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load list of registered events
        loadRegisteredEvents();

        add(mainPanel);
        setVisible(true);
    }

    private void loadRegisteredEvents() {
        try {
            Path receiptDir = Paths.get(
                    System.getProperty("user.home"),
                    "EventManagementSystem",
                    "receipts"
            );

            eventFiles = new ArrayList<>();
            
            if (Files.exists(receiptDir)) {
                try (var receiptStream = Files.list(receiptDir)) {
                    receiptStream
                        .filter(path -> {
                            String name = path.getFileName().toString().toLowerCase();
                            return name.startsWith("receipt_") && name.endsWith(".txt");
                        })
                        .sorted(Comparator.comparing((Path path) -> path.toFile().lastModified()).reversed())
                        .forEach(path -> eventFiles.add(path));
                }
            }
            
            if (eventFiles.isEmpty()) {
                textArea.setText("No event bills found. Generate a bill after payment first.");
                textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                textArea.setForeground(new Color(120, 120, 120));
            } else {
                eventComboBox.removeAllItems();
                for (Path file : eventFiles) {
                    String fileName = file.getFileName().toString();
                    String eventName = fileName.substring(8, fileName.lastIndexOf('.'));
                    eventComboBox.addItem(eventName);
                }
                eventComboBox.setSelectedIndex(0);
                loadSelectedEventBill();
            }
        } catch (Exception e) {
            textArea.setText("An error occurred while loading event list:\n" + e.getMessage());
            textArea.setFont(new Font("Arial", Font.PLAIN, 14));
            textArea.setForeground(Color.RED);
        }
    }

    private void loadSelectedEventBill() {
        int selectedIndex = eventComboBox.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < eventFiles.size()) {
            Path filePath = eventFiles.get(selectedIndex);
            try {
                textArea.setText("");
                
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        textArea.append(line + "\n");
                    }
                }
            } catch (Exception e) {
                textArea.setText("An error occurred while loading the bill:\n" + e.getMessage());
                textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                textArea.setForeground(Color.RED);
            }
        }
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

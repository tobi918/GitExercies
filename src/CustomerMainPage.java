package event_management_system;

import javax.swing.*;
import java.awt.*;

public class CustomerMainPage extends JFrame {
    public CustomerMainPage() {
        setTitle("Customer Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Use layered pane for proper component ordering
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(screenSize);
        
        try {
            // Background image setup
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/test2.jpg"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
            ImageIcon backgroundIcon = new ImageIcon(scaledImage);
            JLabel backgroundLabel = new JLabel(backgroundIcon);
            backgroundLabel.setBounds(0, 0, screenSize.width, screenSize.height);
            layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        } catch (Exception e) {
            System.err.println("Couldn't load background image: " + e.getMessage());
            getContentPane().setBackground(Color.WHITE);
        }
        
        // Main content panel (will go above background)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false); // Make transparent
        contentPanel.setBounds(0, 0, screenSize.width, screenSize.height);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label (centered at top)
        JLabel title = new JLabel("Customer Dashboard", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.PAGE_START;
        contentPanel.add(title, gbc);

        // Button Panel (centered in middle)
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 10, 10, 10);
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;

        // Create buttons with improved styling
        String[] buttonLabels = {
            "View Events",
            "Payment Details",
            "View Bill"
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createStyledButton(buttonLabels[i]);
            buttonGbc.gridx = 0;
            buttonGbc.gridy = i;
            buttonPanel.add(button, buttonGbc);
            
            // Add action listeners
            switch (buttonLabels[i]) {
                case "View Events":
                    button.addActionListener(e -> new ViewEventList());
                    break;
                case "Payment Details":
                    button.addActionListener(e -> new RegisterEventForm());
                    break;
                case "View Bill":
                    button.addActionListener(e -> new ReceiptViewer());
                    break;
            }
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.9;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(buttonPanel, gbc);

        // Logout Button (top right)
        JButton logoutButton = createStyledButton("Logout");
        logoutButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.addActionListener(e -> {
            dispose();
            new Event_Management_System().setVisible(true);
        });

        // Panel for logout button positioning
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(logoutButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        contentPanel.add(topPanel, gbc);
        
        // Add content panel to layered pane
        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);
        
        // Set layered pane as content pane
        setContentPane(layeredPane);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180)); // Steel blue color
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(250, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Add hover effect
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerMainPage();
        });
    }
}

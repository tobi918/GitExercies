package event_management_system;

import javax.swing.*;
import java.awt.*;

public class AdminMainPage extends JFrame {

    public AdminMainPage() {
        setTitle("Admin Dashboard");

        // Full screen setup
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Use layered pane for proper component ordering
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(screenSize);

        try {
            // Background image setup
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/test2.jpg"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(
                    screenSize.width,
                    screenSize.height,
                    Image.SCALE_SMOOTH
            );

            ImageIcon backgroundIcon = new ImageIcon(scaledImage);
            JLabel backgroundLabel = new JLabel(backgroundIcon);
            backgroundLabel.setBounds(0, 0, screenSize.width, screenSize.height);

            layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        } catch (Exception e) {
            System.err.println("Couldn't load background image: " + e.getMessage());
            getContentPane().setBackground(Color.WHITE);
        }

        // Main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBounds(0, 0, screenSize.width, screenSize.height);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logout Button at top right
        JButton logoutButton = createStyledButton("Logout");
        logoutButton.setPreferredSize(new Dimension(150, 40));

        logoutButton.addActionListener(e -> {
            dispose();
            new Event_Management_System().setVisible(true);
        });

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

        // Main center panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.insets = new Insets(10, 10, 10, 10);
        centerGbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 32));

        centerGbc.gridx = 0;
        centerGbc.gridy = 0;
        centerGbc.insets = new Insets(0, 10, 120, 10);

        centerPanel.add(title, centerGbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 10, 10, 10);
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;

        JButton createEventButton = createStyledButton("Create Event");
        createEventButton.addActionListener(e -> new CreateEventForm().setVisible(true));

        JButton updateEventButton = createStyledButton("Update Event");
        updateEventButton.addActionListener(e -> new UpdateEventForm().setVisible(true));

        JButton cancelEventButton = createStyledButton("Cancel Event");
        cancelEventButton.addActionListener(e -> new CancelEventForm().setVisible(true));

        JButton viewStatsButton = createStyledButton("View Event Stats");
        viewStatsButton.addActionListener(e -> new EventStatsDashboard().setVisible(true));

        JButton viewDatabaseButton = createStyledButton("View Database Records");
        viewDatabaseButton.addActionListener(e -> new DatabaseViewerFrame());

        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonPanel.add(createEventButton, buttonGbc);

        buttonGbc.gridy = 1;
        buttonPanel.add(updateEventButton, buttonGbc);

        buttonGbc.gridy = 2;
        buttonPanel.add(cancelEventButton, buttonGbc);

        buttonGbc.gridy = 3;
        buttonPanel.add(viewStatsButton, buttonGbc);

        buttonGbc.gridy = 4;
        buttonPanel.add(viewDatabaseButton, buttonGbc);

        centerGbc.gridx = 0;
        centerGbc.gridy = 1;
        centerGbc.insets = new Insets(0, 10, 10, 10);

        centerPanel.add(buttonPanel, centerGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        contentPanel.add(centerPanel, gbc);

        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);

        setContentPane(layeredPane);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(250, 50));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminMainPage::new);
    }
}
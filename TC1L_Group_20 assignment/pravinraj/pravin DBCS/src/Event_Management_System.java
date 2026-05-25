package event_management_system;

import java.awt.*;
import javax.swing.*;

public class Event_Management_System extends JFrame {

    private int failedLoginAttempts = 0;
    private final int MAX_LOGIN_ATTEMPTS = 3;
    private final int LOCK_TIME_SECONDS = 30;

    public Event_Management_System() {
        setTitle("Login System");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/test.jpeg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
        ImageIcon backgroundIcon = new ImageIcon(scaledImage);
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        backgroundLabel.setLayout(null);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBounds((screenSize.width - 400) / 2, (screenSize.height - 400) / 2, 400, 400);

        JLabel titleLabel = new JLabel("Login Here");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(90, 90, 255));
        titleLabel.setBounds(120, 20, 200, 30);
        loginPanel.add(titleLabel);

        JLabel subLabel = new JLabel("Event Login Page");
        subLabel.setFont(new Font("Arial", Font.BOLD, 16));
        subLabel.setBounds(130, 60, 200, 20);
        loginPanel.add(subLabel);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userLabel.setBounds(30, 90, 100, 20);
        loginPanel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(30, 115, 340, 25);
        loginPanel.add(userField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passLabel.setBounds(30, 150, 100, 20);
        loginPanel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(30, 175, 340, 25);
        loginPanel.add(passField);

        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(30, 205, 200, 20);
        showPassword.setBackground(Color.WHITE);
        showPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        showPassword.setForeground(new Color(90, 90, 255));
        loginPanel.add(showPassword);

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passField.setEchoChar((char) 0);
            } else {
                passField.setEchoChar('•');
            }
        });

        JLabel typeLabel = new JLabel("Select User Type:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        typeLabel.setBounds(30, 230, 200, 20);
        loginPanel.add(typeLabel);

        String[] userTypes = {"Customer", "Admin"};
        JComboBox<String> userTypeBox = new JComboBox<>(userTypes);
        userTypeBox.setBounds(30, 255, 340, 25);
        loginPanel.add(userTypeBox);

        JLabel forgotLabel = new JLabel("Forgot Password?");
        forgotLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotLabel.setForeground(new Color(90, 90, 255));
        forgotLabel.setBounds(30, 285, 200, 20);
        loginPanel.add(forgotLabel);

        forgotLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showForgotPasswordDialog();
            }
        });

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(30, 310, 340, 35);
        loginButton.setBackground(new Color(90, 90, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginPanel.add(loginButton);

        JButton signUpButton = new JButton("Sign Up New");
        signUpButton.setBounds(30, 350, 340, 35);
        signUpButton.setBackground(new Color(90, 90, 255));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginPanel.add(signUpButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String userType = (String) userTypeBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Username and password are required.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (userType.equals("Admin") && username.equals("admin") && password.equals("Admin123!")) {
                failedLoginAttempts = 0;

                JOptionPane.showMessageDialog(null, "Welcome Admin");
                new AdminMainPage().setVisible(true);
                dispose();

            } else if (userType.equals("Customer") && UserDatabase.isValidCustomer(username, password)) {
                failedLoginAttempts = 0;
                Session.setCurrentUsername(username);

                JOptionPane.showMessageDialog(null, "Welcome Customer");
                new CustomerMainPage().setVisible(true);
                dispose();

            } else {
                failedLoginAttempts++;

                int remainingAttempts = MAX_LOGIN_ATTEMPTS - failedLoginAttempts;

                if (failedLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
                    loginButton.setEnabled(false);

                    JOptionPane.showMessageDialog(
                            null,
                            "Too many failed login attempts.\nLogin is locked for " + LOCK_TIME_SECONDS + " seconds.",
                            "Account Temporarily Locked",
                            JOptionPane.WARNING_MESSAGE
                    );

                    Timer timer = new Timer(LOCK_TIME_SECONDS * 1000, event -> {
                        failedLoginAttempts = 0;
                        loginButton.setEnabled(true);

                        JOptionPane.showMessageDialog(
                                null,
                                "Login has been unlocked. Please try again.",
                                "Login Unlocked",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    });

                    timer.setRepeats(false);
                    timer.start();

                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Invalid credentials!\nRemaining attempts: " + remainingAttempts,
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        signUpButton.addActionListener(e -> showSignUpDialog());

        backgroundLabel.add(loginPanel);
        setContentPane(backgroundLabel);
        setVisible(true);
    }

    private void showForgotPasswordDialog() {
        JDialog forgotPasswordDialog = new JDialog(this, "Password Recovery", true);
        forgotPasswordDialog.setSize(500, 300);
        forgotPasswordDialog.setLayout(new BorderLayout());
        forgotPasswordDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel("<html><center>Please contact the administrator for password recovery.<br><br>"
                + "Admin Email: <b>admin123@gmail.com</b><br><br>"
                + "Admin Office: <b>03-40504811</b></center></html>");

        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(90, 90, 255));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> forgotPasswordDialog.dispose());

        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        forgotPasswordDialog.add(panel);
        forgotPasswordDialog.setVisible(true);
    }

    private void showSignUpDialog() {
        JDialog signUpDialog = new JDialog(this, "Customer Sign Up", true);
        signUpDialog.setSize(450, 390);
        signUpDialog.setLocationRelativeTo(this);
        signUpDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Customer Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(18);
        addSignUpField(panel, gbc, usernameLabel, usernameField, 1);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(18);
        addSignUpField(panel, gbc, passwordLabel, passwordField, 2);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        JPasswordField confirmField = new JPasswordField(18);
        addSignUpField(panel, gbc, confirmLabel, confirmField, 3);

        JLabel passwordRuleLabel = new JLabel(
                "<html>Password must be at least 8 characters and include uppercase,<br>" +
                        "lowercase, number, and special character.</html>"
        );
        passwordRuleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordRuleLabel.setForeground(Color.GRAY);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(passwordRuleLabel, gbc);

        JButton createButton = new JButton("Create Account");
        createButton.setBackground(new Color(90, 90, 255));
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(createButton, gbc);

        createButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPassword = new String(confirmField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(signUpDialog, "Username and password are required.",
                        "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(signUpDialog, "Passwords do not match.",
                        "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isStrongPassword(password)) {
                JOptionPane.showMessageDialog(
                        signUpDialog,
                        "Weak password.\n\nPassword must:\n" +
                                "- Be at least 8 characters long\n" +
                                "- Include uppercase letter\n" +
                                "- Include lowercase letter\n" +
                                "- Include number\n" +
                                "- Include special character",
                        "Weak Password",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (!UserDatabase.addCustomer(username, password)) {
                JOptionPane.showMessageDialog(signUpDialog, "This username already exists.",
                        "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(signUpDialog, "Account created successfully. You can log in now.");
            signUpDialog.dispose();
        });

        signUpDialog.add(panel, BorderLayout.CENTER);
        signUpDialog.setVisible(true);
    }

    private boolean isStrongPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecial = true;
            }
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    private void addSignUpField(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent field, int row) {
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Event_Management_System::new);
    }
}
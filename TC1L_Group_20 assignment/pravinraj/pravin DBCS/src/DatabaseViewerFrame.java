package event_management_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DatabaseViewerFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public DatabaseViewerFrame() {
        setTitle("Database Records Viewer");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Events", createTablePanel(
                "SELECT event_id, name, event_date, venue, type, capacity, fee " +
                        "FROM Events"
        ));

        tabbedPane.addTab("Customers", createTablePanel(
                "SELECT customer_id, username, password " +
                        "FROM Customers"
        ));

        tabbedPane.addTab("Registered Events", createTablePanel(
                "SELECT " +
                        "r.registration_id, " +
                        "r.customer_username, " +
                        "r.event_id, " +
                        "e.name AS event_name, " +
                        "e.event_date, " +
                        "e.venue, " +
                        "e.type, " +
                        "e.fee, " +
                        "r.payment_status, " +
                        "r.paid_amount, " +
                        "r.payment_date, " +
                        "r.registration_date " +
                        "FROM RegisteredEvents r " +
                        "INNER JOIN Events e ON r.event_id = e.event_id"
        ));

        tabbedPane.addTab("Audit Log", createTablePanel(
                "SELECT " +
                        "audit_id, " +
                        "action_type, " +
                        "event_name, " +
                        "record_id, " +
                        "customer_username, " +
                        "old_value, " +
                        "new_value, " +
                        "action_date " +
                        "FROM EventAudit " +
                        "ORDER BY action_date DESC"
        ));

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createTablePanel(String sql) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JTable table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));

        refreshButton.addActionListener(e -> loadData(table, sql));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadData(table, sql);

        return panel;
    }

    private void loadData(JTable table, String sql) {
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            DefaultTableModel model = new DefaultTableModel();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];

                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }

                model.addRow(row);
            }

            table.setModel(model);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load data from database.\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
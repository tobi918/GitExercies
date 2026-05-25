USE EventManagementDB;
GO

CREATE TABLE EventAudit (
    audit_id INT IDENTITY(1,1) PRIMARY KEY,
    action_type VARCHAR(50) NOT NULL,
    table_name VARCHAR(50) NOT NULL,
    record_id INT NULL,
    customer_username VARCHAR(50) NULL,
    old_value VARCHAR(MAX) NULL,
    new_value VARCHAR(MAX) NULL,
    action_date DATETIME DEFAULT GETDATE()
);
GO
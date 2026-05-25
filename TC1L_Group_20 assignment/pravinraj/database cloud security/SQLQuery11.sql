USE EventManagementDB;
GO

ALTER TABLE RegisteredEvents
ADD payment_status VARCHAR(20) DEFAULT 'UNPAID',
    paid_amount DECIMAL(10,2) NULL,
    payment_date DATETIME NULL;
GO
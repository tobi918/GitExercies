USE EventManagementDB;
GO

UPDATE RegisteredEvents
SET payment_status = 'UNPAID'
WHERE payment_status IS NULL;
GO
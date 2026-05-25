USE EventManagementDB;
GO

ALTER TABLE Customers
ALTER COLUMN password ADD MASKED WITH (FUNCTION = 'partial(1,"XXXX",1)');
GO
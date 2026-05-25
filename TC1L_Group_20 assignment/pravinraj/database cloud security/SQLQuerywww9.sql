USE EventManagementDB;
GO

EXECUTE AS USER = 'mask_test_user';

SELECT * FROM Customers;

REVERT;
GO
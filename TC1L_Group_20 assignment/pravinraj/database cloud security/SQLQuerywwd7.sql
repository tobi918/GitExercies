USE EventManagementDB;
GO

CREATE USER mask_test_user WITHOUT LOGIN;
GO

GRANT SELECT ON dbo.Customers TO mask_test_user;
GO
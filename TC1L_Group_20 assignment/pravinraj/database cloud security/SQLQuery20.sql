USE EventManagementDB;
GO

ALTER ROLE db_owner DROP MEMBER ems_user;
GO

DENY UNMASK TO ems_user;
GO

GRANT SELECT, INSERT, UPDATE, DELETE ON dbo.Customers TO ems_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON dbo.Events TO ems_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON dbo.RegisteredEvents TO ems_user;
GRANT SELECT, INSERT ON dbo.EventAudit TO ems_user;
GO
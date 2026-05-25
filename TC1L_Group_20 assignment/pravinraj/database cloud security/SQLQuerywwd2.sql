USE master;
GO

ALTER SERVER AUDIT EventManagement_FailedLogin_Audit
WITH (STATE = ON);
GO

ALTER SERVER AUDIT SPECIFICATION EventManagement_FailedLogin_Spec
WITH (STATE = ON);
GO
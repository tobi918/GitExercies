USE master;
GO

DROP SERVER AUDIT SPECIFICATION IF EXISTS EventManagement_FailedLogin_Spec;
GO

DROP SERVER AUDIT IF EXISTS EventManagement_FailedLogin_Audit;
GO

CREATE SERVER AUDIT EventManagement_FailedLogin_Audit
TO FILE
(
    FILEPATH = 'C:\SQLAudit\',
    MAXSIZE = 10 MB,
    MAX_ROLLOVER_FILES = 5
);
GO

ALTER SERVER AUDIT EventManagement_FailedLogin_Audit
WITH (STATE = ON);
GO

CREATE SERVER AUDIT SPECIFICATION EventManagement_FailedLogin_Spec
FOR SERVER AUDIT EventManagement_FailedLogin_Audit
ADD (FAILED_LOGIN_GROUP);
GO

ALTER SERVER AUDIT SPECIFICATION EventManagement_FailedLogin_Spec
WITH (STATE = ON);
GO
USE master;
GO

-- Create master key if it does not exist
IF NOT EXISTS (
    SELECT * FROM sys.symmetric_keys 
    WHERE name = '##MS_DatabaseMasterKey##'
)
BEGIN
    CREATE MASTER KEY ENCRYPTION BY PASSWORD = 'Admin12345!';
END
GO

-- Create backup encryption certificate if it does not exist
IF NOT EXISTS (
    SELECT * FROM sys.certificates 
    WHERE name = 'EventManagementBackupCert'
)
BEGIN
    CREATE CERTIFICATE EventManagementBackupCert
    WITH SUBJECT = 'Event Management Database Backup Encryption Certificate';
END
GO
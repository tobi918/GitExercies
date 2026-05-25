USE master;
GO

BACKUP CERTIFICATE EventManagementBackupCert
TO FILE = 'C:\SQLBackupKeys\EventManagementBackupCert.cer'
WITH PRIVATE KEY
(
    FILE = 'C:\SQLBackupKeys\EventManagementBackupCert_PrivateKey.pvk',
    ENCRYPTION BY PASSWORD = 'Admin12345!'
);
GO
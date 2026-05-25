BACKUP DATABASE EventManagementDB
TO DISK = 'C:\SQLBackup\EventManagementDB_EncryptedBackup.bak'
WITH
    FORMAT,
    INIT,
    COMPRESSION,
    ENCRYPTION
    (
        ALGORITHM = AES_256,
        SERVER CERTIFICATE = EventManagementBackupCert
    ),
    STATS = 10;
GO
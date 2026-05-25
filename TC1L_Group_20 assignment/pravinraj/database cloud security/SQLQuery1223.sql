USE msdb;
GO

SELECT TOP 10
    database_name,
    backup_start_date,
    backup_finish_date,
    type,
    is_copy_only,
    compressed_backup_size,
    key_algorithm,
    encryptor_type,
    encryptor_thumbprint
FROM backupset
WHERE database_name = 'EventManagementDB'
ORDER BY backup_finish_date DESC;
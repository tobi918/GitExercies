SELECT
    event_time,
    action_id,
    succeeded,
    server_principal_name,
    statement
FROM sys.fn_get_audit_file('C:\SQLAudit\*.sqlaudit', DEFAULT, DEFAULT)
ORDER BY event_time DESC;
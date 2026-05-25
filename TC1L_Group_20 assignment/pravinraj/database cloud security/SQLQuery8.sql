USE EventManagementDB;
GO

CREATE TRIGGER trg_Registration_Delete
ON RegisteredEvents
AFTER DELETE
AS
BEGIN
    INSERT INTO EventAudit (
        action_type,
        table_name,
        record_id,
        customer_username,
        old_value
    )
    SELECT
        'REGISTRATION_CANCELLED',
        'RegisteredEvents',
        d.registration_id,
        d.customer_username,
        CONCAT(
            'Customer ', d.customer_username,
            ' cancelled registration for Event ID: ', d.event_id
        )
    FROM deleted d;
END;
GO
USE EventManagementDB;
GO

/* Drop old triggers first so they don't break after changing EventAudit */
DROP TRIGGER IF EXISTS trg_Event_Insert;
DROP TRIGGER IF EXISTS trg_Event_Update;
DROP TRIGGER IF EXISTS trg_Event_Delete;
DROP TRIGGER IF EXISTS trg_Registration_Insert;
DROP TRIGGER IF EXISTS trg_Registration_Update;
DROP TRIGGER IF EXISTS trg_Registration_Delete;
GO

/* Add event_name column to the real EventAudit table */
IF COL_LENGTH('EventAudit', 'event_name') IS NULL
BEGIN
    ALTER TABLE EventAudit
    ADD event_name VARCHAR(100);
END
GO

/* Fill event_name for existing event audit records if possible */
IF COL_LENGTH('EventAudit', 'table_name') IS NOT NULL
BEGIN
    UPDATE a
    SET a.event_name = e.name
    FROM EventAudit a
    INNER JOIN Events e
        ON a.record_id = e.event_id
    WHERE a.event_name IS NULL
    AND a.table_name = 'Events';
END
GO

/* Remove table_name from the real EventAudit table */
IF COL_LENGTH('EventAudit', 'table_name') IS NOT NULL
BEGIN
    ALTER TABLE EventAudit
    DROP COLUMN table_name;
END
GO

/* Event Created Audit */
CREATE TRIGGER trg_Event_Insert
ON Events
AFTER INSERT
AS
BEGIN
    INSERT INTO EventAudit
    (
        action_type,
        event_name,
        record_id,
        customer_username,
        old_value,
        new_value,
        action_date
    )
    SELECT
        'EVENT_CREATED',
        i.name,
        i.event_id,
        NULL,
        NULL,
        CONCAT('Created event: ', i.name, ', Date: ', i.event_date, ', Venue: ', i.venue),
        GETDATE()
    FROM inserted i;
END;
GO

/* Event Updated Audit */
CREATE TRIGGER trg_Event_Update
ON Events
AFTER UPDATE
AS
BEGIN
    INSERT INTO EventAudit
    (
        action_type,
        event_name,
        record_id,
        customer_username,
        old_value,
        new_value,
        action_date
    )
    SELECT
        'EVENT_UPDATED',
        i.name,
        i.event_id,
        NULL,
        CONCAT('Old event: ', d.name, ', Date: ', d.event_date, ', Venue: ', d.venue),
        CONCAT('Updated event: ', i.name, ', Date: ', i.event_date, ', Venue: ', i.venue),
        GETDATE()
    FROM inserted i
    INNER JOIN deleted d
        ON i.event_id = d.event_id;
END;
GO

/* Event Deleted Audit */
CREATE TRIGGER trg_Event_Delete
ON Events
AFTER DELETE
AS
BEGIN
    INSERT INTO EventAudit
    (
        action_type,
        event_name,
        record_id,
        customer_username,
        old_value,
        new_value,
        action_date
    )
    SELECT
        'EVENT_DELETED',
        d.name,
        d.event_id,
        NULL,
        CONCAT('Deleted event: ', d.name, ', Date: ', d.event_date, ', Venue: ', d.venue),
        NULL,
        GETDATE()
    FROM deleted d;
END;
GO

/* Customer Registered Event Audit */
CREATE TRIGGER trg_Registration_Insert
ON RegisteredEvents
AFTER INSERT
AS
BEGIN
    INSERT INTO EventAudit
    (
        action_type,
        event_name,
        record_id,
        customer_username,
        old_value,
        new_value,
        action_date
    )
    SELECT
        'EVENT_REGISTERED',
        e.name,
        i.registration_id,
        i.customer_username,
        NULL,
        CONCAT('Registered event: ', e.name, ', Payment status: ', i.payment_status),
        GETDATE()
    FROM inserted i
    LEFT JOIN Events e
        ON i.event_id = e.event_id;
END;
GO

/* Payment Updated Audit */
CREATE TRIGGER trg_Registration_Update
ON RegisteredEvents
AFTER UPDATE
AS
BEGIN
    INSERT INTO EventAudit
    (
        action_type,
        event_name,
        record_id,
        customer_username,
        old_value,
        new_value,
        action_date
    )
    SELECT
        'PAYMENT_UPDATED',
        e.name,
        i.registration_id,
        i.customer_username,
        CONCAT('Old payment status: ', d.payment_status, ', Old paid amount: ', ISNULL(CAST(d.paid_amount AS VARCHAR), 'NULL')),
        CONCAT('New payment status: ', i.payment_status, ', New paid amount: ', ISNULL(CAST(i.paid_amount AS VARCHAR), 'NULL')),
        GETDATE()
    FROM inserted i
    INNER JOIN deleted d
        ON i.registration_id = d.registration_id
    LEFT JOIN Events e
        ON i.event_id = e.event_id;
END;
GO

/* Customer Cancelled Registration Audit */
CREATE TRIGGER trg_Registration_Delete
ON RegisteredEvents
AFTER DELETE
AS
BEGIN
    INSERT INTO EventAudit
    (
        action_type,
        event_name,
        record_id,
        customer_username,
        old_value,
        new_value,
        action_date
    )
    SELECT
        'REGISTRATION_CANCELLED',
        e.name,
        d.registration_id,
        d.customer_username,
        CONCAT('Cancelled registration for event: ', e.name),
        NULL,
        GETDATE()
    FROM deleted d
    LEFT JOIN Events e
        ON d.event_id = e.event_id;
END;
GO
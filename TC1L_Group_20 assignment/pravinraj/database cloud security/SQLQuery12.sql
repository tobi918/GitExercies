USE EventManagementDB;
GO

-- Drop existing triggers first if they already exist
IF OBJECT_ID('trg_Event_Insert', 'TR') IS NOT NULL
    DROP TRIGGER trg_Event_Insert;
GO

IF OBJECT_ID('trg_Event_Update', 'TR') IS NOT NULL
    DROP TRIGGER trg_Event_Update;
GO

IF OBJECT_ID('trg_Event_Delete', 'TR') IS NOT NULL
    DROP TRIGGER trg_Event_Delete;
GO

IF OBJECT_ID('trg_Registration_Insert', 'TR') IS NOT NULL
    DROP TRIGGER trg_Registration_Insert;
GO

IF OBJECT_ID('trg_Registration_Delete', 'TR') IS NOT NULL
    DROP TRIGGER trg_Registration_Delete;
GO


-- Create EventAudit table if it does not exist
IF OBJECT_ID('EventAudit', 'U') IS NULL
BEGIN
    CREATE TABLE EventAudit (
        audit_id INT IDENTITY(1,1) PRIMARY KEY,
        action_type VARCHAR(50) NOT NULL,
        table_name VARCHAR(50) NOT NULL,
        record_id INT NULL,
        customer_username VARCHAR(50) NULL,
        old_value VARCHAR(MAX) NULL,
        new_value VARCHAR(MAX) NULL,
        action_date DATETIME DEFAULT GETDATE()
    );
END;
GO


-- Trigger: Event Created
CREATE TRIGGER trg_Event_Insert
ON Events
AFTER INSERT
AS
BEGIN
    INSERT INTO EventAudit (
        action_type,
        table_name,
        record_id,
        new_value
    )
    SELECT
        'EVENT_CREATED',
        'Events',
        event_id,
        CONCAT(
            'Name: ', name,
            ', Date: ', event_date,
            ', Venue: ', venue,
            ', Type: ', type,
            ', Capacity: ', capacity,
            ', Fee: ', fee
        )
    FROM inserted;
END;
GO


-- Trigger: Event Updated
CREATE TRIGGER trg_Event_Update
ON Events
AFTER UPDATE
AS
BEGIN
    INSERT INTO EventAudit (
        action_type,
        table_name,
        record_id,
        old_value,
        new_value
    )
    SELECT
        'EVENT_UPDATED',
        'Events',
        i.event_id,
        CONCAT(
            'Old Name: ', d.name,
            ', Old Date: ', d.event_date,
            ', Old Venue: ', d.venue,
            ', Old Type: ', d.type,
            ', Old Capacity: ', d.capacity,
            ', Old Fee: ', d.fee
        ),
        CONCAT(
            'New Name: ', i.name,
            ', New Date: ', i.event_date,
            ', New Venue: ', i.venue,
            ', New Type: ', i.type,
            ', New Capacity: ', i.capacity,
            ', New Fee: ', i.fee
        )
    FROM inserted i
    INNER JOIN deleted d ON i.event_id = d.event_id;
END;
GO


-- Trigger: Event Deleted / Cancelled
CREATE TRIGGER trg_Event_Delete
ON Events
AFTER DELETE
AS
BEGIN
    INSERT INTO EventAudit (
        action_type,
        table_name,
        record_id,
        old_value
    )
    SELECT
        'EVENT_DELETED',
        'Events',
        event_id,
        CONCAT(
            'Deleted Name: ', name,
            ', Date: ', event_date,
            ', Venue: ', venue,
            ', Type: ', type,
            ', Capacity: ', capacity,
            ', Fee: ', fee
        )
    FROM deleted;
END;
GO


-- Trigger: Customer Registered Event
CREATE TRIGGER trg_Registration_Insert
ON RegisteredEvents
AFTER INSERT
AS
BEGIN
    INSERT INTO EventAudit (
        action_type,
        table_name,
        record_id,
        customer_username,
        new_value
    )
    SELECT
        'EVENT_REGISTERED',
        'RegisteredEvents',
        i.registration_id,
        i.customer_username,
        CONCAT(
            'Customer ', i.customer_username,
            ' registered for event: ', e.name,
            ', Event ID: ', i.event_id
        )
    FROM inserted i
    INNER JOIN Events e ON i.event_id = e.event_id;
END;
GO


-- Trigger: Customer Cancelled Registration
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
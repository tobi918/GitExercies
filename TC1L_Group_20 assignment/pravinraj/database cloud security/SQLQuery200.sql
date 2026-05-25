USE EventManagementDB;
GO

CREATE OR ALTER VIEW EventAuditDetails AS
SELECT
    a.audit_id,
    a.action_type,

    CASE
        WHEN a.table_name = 'Events' THEN e.name
        WHEN a.table_name = 'RegisteredEvents' THEN ev.name
        ELSE NULL
    END AS event_name,

    a.record_id,
    a.customer_username,
    a.old_value,
    a.new_value,
    a.action_date
FROM EventAudit a
LEFT JOIN Events e
    ON a.table_name = 'Events'
    AND a.record_id = e.event_id
LEFT JOIN RegisteredEvents r
    ON a.table_name = 'RegisteredEvents'
    AND a.record_id = r.registration_id
LEFT JOIN Events ev
    ON r.event_id = ev.event_id;
GO
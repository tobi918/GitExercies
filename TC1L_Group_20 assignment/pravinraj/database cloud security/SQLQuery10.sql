USE EventManagementDB;
GO

CREATE VIEW RegisteredEventsWithDetails AS
SELECT
    r.registration_id,
    r.customer_username,
    r.event_id,
    e.name AS event_name,
    e.event_date,
    e.venue,
    e.type,
    e.fee,
    r.registration_date
FROM RegisteredEvents r
INNER JOIN Events e ON r.event_id = e.event_id;
GO
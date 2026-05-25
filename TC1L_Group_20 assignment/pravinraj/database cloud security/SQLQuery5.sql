USE EventManagementDB;
GO

SELECT
    r.registration_id,
    r.customer_username,
    r.event_id,
    e.name AS event_name,
    r.registration_date
FROM RegisteredEvents r
INNER JOIN Events e
ON r.event_id = e.event_id;
USE EventManagementDB;
GO

EXECUTE AS USER = 'ems_user';

SELECT 
    USER_NAME() AS user_name_check,
    IS_ROLEMEMBER('db_owner') AS is_db_owner,
    HAS_PERMS_BY_NAME(NULL, 'DATABASE', 'UNMASK') AS has_unmask_permission;

SELECT * FROM Customers;

REVERT;
GO
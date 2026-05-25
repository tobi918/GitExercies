# Event Management System

Java Swing event management system for administrators and customers.

## Login Details

Admin:
- User ID: `admin`
- Password: `1234`

Customer:
- User ID: `customer`
- Password: `123`

## Features

- Admin dashboard
- Create, update, cancel, and view statistics for events
- Customer dashboard
- View events and register for selected events
- Payment options with extra services and discounts
- Receipt export and receipt viewing
- Events are saved locally in `~/EventManagementSystem/events.dat`
- Registered events are saved locally in `~/EventManagementSystem/registered_events.dat`

## Run From Terminal

Compile:

```powershell
javac -encoding UTF-8 -d build\classes .\src\*.java
Copy-Item -Recurse -Force .\src\resources .\build\classes\
```

Run:

```powershell
java -cp build\classes event_management_system.Event_Management_System
```


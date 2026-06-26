# Telnet Appointment Server

A multi-client, thread-safe appointment scheduling server built in Java, accessed entirely over the Telnet protocol. Built for the Computer Networks module (COS332) at the University of Pretoria.

## Overview

The server maintains an in-memory appointment database (with file persistence) and accepts connections from any standard Telnet client. All interaction — menus, prompts, input, and feedback — happens over the raw socket connection, with the server handling character-level echoing itself (so it behaves correctly whether or not the client's `localecho` is enabled).

## Features

- **Concurrent multi-client access** — each connection is handled on its own thread (`Server.java`), so multiple users can interact with the system at the same time
- **Thread-safe shared database** — all read/write operations on `Appointmentdatabase` are `synchronized`, preventing race conditions when clients access appointments concurrently
- **Full CRUD over Telnet** — add, view, search, and delete appointments, all driven by a menu rendered with ANSI escape sequences
- **ANSI terminal control** — screen clearing, cursor positioning, and colour-coded feedback (success/error/info) implemented from raw escape codes in `Ansihelper.java`, rather than a library
- **Input validation** — regex-based validation for date (`YYYY-MM-DD`) and time (`HH:MM`) formats, with empty-field and non-numeric input handling
- **Persistence** — appointments are serialized to `appointments.dat` after every change and reloaded automatically on server startup

## Architecture

```
Server.java            → opens the ServerSocket, accepts connections, spawns a thread per client
ClientHandler.java     → per-client session logic: menu, input parsing, echoing, ANSI rendering
Appointmentdatabase.java → thread-safe in-memory store (synchronized methods)
Appointment.java        → data model for a single appointment
FileManager.java        → save/load persistence, plus CSV import/export
Ansihelper.java         → ANSI escape sequence helpers (colour, cursor, screen control)
appointments.dat        → sample persisted data
```

**Connection flow:** `Server` listens on port 8080 → each incoming connection gets a dedicated `ClientHandler` running in its own thread → all handlers share a single `Appointmentdatabase` instance, synchronized to keep concurrent access safe.

## Running it

```bash
javac *.java
java Server
```

Then, from another terminal:

```bash
telnet localhost 8080
```

To test concurrent access, open several Telnet sessions against the same running server, they'll all read and write to the same shared database in real time.

## What this demonstrates

This project was an exercise in low-level network programming: handling raw socket I/O byte-by-byte (including manual character echoing and backspace handling), implementing a text protocol UI without any external libraries, and managing shared mutable state safely across concurrent client threads.

# Flask Location Update Application

This web application uses Flask and Flask-SocketIO to provide real-time location updates by reading coordinates from a file and broadcasting them to connected clients.

Flask is a lightweight and flexible Python web framework, ideal for building quick web applications. Flask-SocketIO enhances this by adding WebSocket support, enabling real-time, bidirectional communication between the server and clients.

## Key Functions

1. **`read_coordinates_from_file()`**

   This function reads the latitude and longitude from a file named `location.txt`. The coordinates should be separated by a space. It returns the coordinates as floating-point numbers. If an error occurs, it logs the issue and returns `None` for both values.

2. **`location_updater()`**

   This function continuously monitors `location.txt` for updates, reading the location data every 0.5 seconds. It then broadcasts the new coordinates to all connected clients via SocketIO.

## Routes

- **`/` (Index Route):**
  
  The root route of the application, rendering the main `index.html` page. This serves as the entry point for users interacting with the app.

## Application Flow

- A separate thread is launched to execute the `location_updater()` function, ensuring the server continuously reads location data.
- The Flask application is then started with SocketIO enabled, running in debug mode for easy monitoring and troubleshooting.

## System Service Configuration

To ensure the Flask application starts automatically and stays running, the following systemd service configuration is used:

```ini
[Unit]
Description=Run Flask Location Update Application
After=setup_dns.service gps.service
Wants=setup_dns.service gps.service

[Service]
Type=simple
ExecStart=/usr/bin/python /home/root/webPy/app.py
Restart=on-failure
User=root

[Install]
WantedBy=multi-user.target
```

This configuration ensures the application starts after the necessary services (`setup_dns` and `gps`) and will automatically restart if it encounters any failures.

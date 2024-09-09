# User Interface for Car Infotainment System using JavaFX

### Design a HomePage layout  
- Design a suitable User Friendly Background for home page.
- Design Menu bar.
    - Home Screen Button.
    - Maps Button.
    - Youtube Button.
    - Spotify Button.
      
### Creating Fade Transision Utils
- Create Fade Transition class that contain Transition fade in and fade out , that take Parameters ( Parent, Duration, Runnable )
    -  Parent : node to apply fade transition on.
    -  Duration : time that the Fade take place.
    -  Runnable : callback Function to start at the end of Fade.
  
### Speedometer
- Adding Speed Gauge to Home screen using Medusa Library that Display live speed of the Car that are input from GPS module.
- Input Stream to receive data from GPS.
- Pass Data to loadGauge to set value.
    - Thread to read data always every 100 milli second.
    - Check for Speed if it reach certain speed Warn the Driver using sound alram.
    - Speed limit run even not in Home Screen.
    - add Speed Gauge to Home Screen
 


### Testing and Validation
- Test fade transitions for loading screens
- Test menu bar functionality and navigation
- Test WebView integration for YouTube and Spotify
- Test GPS module integration and map display
- Test speedometer gauge functionality
- Debug and fix any issues
- Test all Corner Cases

# Final Result
### https://youtu.be/X6dpX-hPdeY

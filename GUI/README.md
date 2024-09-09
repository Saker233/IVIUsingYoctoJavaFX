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

### Maps UI and Integration
 - add webView component that display map on it using URL
 - Connect Map GUI to read Position File.
 - Parse the Reading to Map to set Position on Current Location Longitude and latitude.
 - Add Pointer for Current Position.
 - Thread to read Map file continously


### YouTube and Spotify
- Use WebView to display YouTube and Spotify on.
- Design the webview to fit the screen for FullScreen feature.
- YouTube can play Shorts and Videos , rendering accourding to supported driver


### Time Display
- Display time on Home screen.
- Use Data time format to specify the format type.
- Set Time according to Local time.



### Integrate Transission Utils between Scene
- Use Fade Transission between loading screens until home page.
- Option to use Fade in and Fade out Utils
- Set a callback Function and the end of each scene to load another scene

### Integrate Different Application
- Integrate different scenes between application without change scene.
- All application runs from Menu Bar.
- Optimize all applications to fit screen.

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

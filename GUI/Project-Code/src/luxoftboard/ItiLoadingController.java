/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luxoftboard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author mfarr
 */
public class ItiLoadingController implements Initializable {

    @FXML
    private ImageView ItiLoading;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add a delay before transitioning to the main screen
        PauseTransition delay = new PauseTransition(Duration.seconds(3)); // 3-second delay
        delay.setOnFinished(event -> showMainApp());
        delay.play();
    }

    private void showMainApp() {
    try {
        // Ensure the ImageView is part of the scene and has a window
        if (ItiLoading.getScene() != null && ItiLoading.getScene().getWindow() != null) {
            // Get the current stage (window)
            Stage stage = (Stage) ItiLoading.getScene().getWindow();

            // Load the main application FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadingScreen.fxml"));
            Scene scene = new Scene(loader.load());

            // Set the new scene (main application) on the stage
            stage.setScene(scene);
            stage.show();  // Show the main app
        } else {
            System.err.println("Scene or Window is not set yet.");
        }
    } catch (IOException e) {
        e.printStackTrace();
    } 
    
}
}

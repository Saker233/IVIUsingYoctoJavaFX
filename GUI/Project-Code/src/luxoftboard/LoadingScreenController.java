package luxoftboard;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class LoadingScreenController implements Initializable {

    @FXML
    private ImageView loadingImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (loadingImage == null) {
            System.err.println("*****************loadingImage is not initialized properly!*************");
            return;
        }

        // Add a delay before transitioning to the main screen
        PauseTransition delay = new PauseTransition(Duration.seconds(3)); // 3-second delay
        delay.setOnFinished(event -> {
            // Add a slight delay to ensure scene and window are fully initialized
            Platform.runLater(() -> {
                // Check scene and window initialization
                if (loadingImage.getScene() == null) {
                    System.err.println("***************LoadingImage's scene is still null.*********************");
                } else if (loadingImage.getScene().getWindow() == null) {
                    System.err.println("**************LoadingImage's scene window is still null.*******************");
                } else {
                    showMainApp();
                }
            });
        });
        delay.play();
    }

    private void showMainApp() {
        // Defer this code until after the current JavaFX application tasks are complete
        Platform.runLater(() -> {
            try {
                if (loadingImage.getScene() == null) {
                    System.err.println("***************LoadingImage's scene is null.****************");
                    return;
                }
                
                if (loadingImage.getScene().getWindow() == null) {
                    System.err.println("******************LoadingImage's scene window is null.**************");
                    return;
                }

                // Get the current stage (window)
                Stage stage = (Stage) loadingImage.getScene().getWindow();

                // Load the main application FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("luxoftBoardHome.fxml"));
                Scene scene = new Scene(loader.load());

                // Set the new scene (main application) on the stage
                stage.setScene(scene);
                stage.show();  // Show the main app
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

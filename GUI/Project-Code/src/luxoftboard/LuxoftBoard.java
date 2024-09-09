package luxoftboard;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LuxoftBoard extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load the first loading screen FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ItiLoading.fxml"));
        Parent loadingRoot = loader.load();
        Scene loadingScene = new Scene(loadingRoot);

        // Set the loading scene and show it
        stage.setScene(loadingScene);
        stage.setTitle("Luxoft Car Screen");
        stage.show();

        // Apply fade-out transition on the loading screen
        TransitionUtils.fadeOut(loadingRoot, Duration.seconds(2), () -> {
            try {
                // Load the second loading screen (LoadingScreen.fxml) and set it immediately
                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("LoadingScreen.fxml"));
                Parent mainRoot = mainLoader.load();
                Scene mainScene = new Scene(mainRoot);

                // Set the second loading scene
                stage.setScene(mainScene);

                // Apply fade-out transition on the second screen
                TransitionUtils.fadeOut(mainRoot, Duration.seconds(2), () -> {
                    // Once the second loading screen finishes, proceed to the main app
                    loadMainApp(stage);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Method to load the main application screen
    private void loadMainApp(Stage stage) {
        try {
            // Load the main application FXML
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("luxoftBoardHome.fxml"));
            Parent mainRoot = mainLoader.load();
            Scene mainScene = new Scene(mainRoot);

            // Set the main scene without applying fade-in transition
            stage.setScene(mainScene);
            stage.setTitle("Main Screen");

            // No fade-out transition needed here

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package luxoftboard;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.util.Duration;

public class TransitionUtils {

    // Method to apply a fade-in effect with optional callback when finished
    public static void fadeIn(Parent node, Duration duration, Runnable onFinish) {
        FadeTransition fadeIn = new FadeTransition(duration, node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setOnFinished(event -> {
            if (onFinish != null) {
                onFinish.run();
            }
        });
        fadeIn.play();
    }

    // Method to apply a fade-out effect with a callback when finished
    public static void fadeOut(Parent node, Duration duration, Runnable onFinish) {
        FadeTransition fadeOut = new FadeTransition(duration, node);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> onFinish.run());
        fadeOut.play();
    }
}

package luxoftboard;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.skins.ModernSkin;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

public class LuxoftBoardHomeController {

    private volatile boolean running = true;
    private Clip audioClip;
    private final AtomicInteger previousSpeed = new AtomicInteger(-1);

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView spotifyIcon, mapsIcon, homeIcon, youtubeIcon, mediaIcon;

    private WebView sharedWebView;

    @FXML
    private GridPane innerGridPane;

    @FXML
    private Label timeLabel;

    private Gauge speedGauge;

    @FXML
    private AnchorPane mediaPane;

    @FXML
    public void initialize() {
        sharedWebView = SharedWebController.getSharedWebView();
        loadGauge();
        setTime();
    }

    @FXML
    private void mapsClk() {
        loadURL("http://127.0.0.1:5000");
    }

    @FXML
    private void youtubeClk() {
        loadURL("https://www.youtube.com");
    }

    @FXML
    private void homeClk() {
        sharedWebView.getEngine().load(null);
        gridPane.getChildren().remove(sharedWebView);
    }

    @FXML
    private void spotifyClk() {
        loadURL("https://www.spotify.com");
    }

    private void loadURL(String url) {
        Platform.runLater(() -> {
            gridPane.getChildren().remove(sharedWebView);
            sharedWebView.getEngine().load(url);
            gridPane.add(sharedWebView, 1, 0);
        });
    }

    public void stop() {
        running = false;
    }

    private void loadGauge() {
        speedGauge = GaugeBuilder.create()
                .skinType(Gauge.SkinType.MODERN)
                .title("Speed")
                .unit("km/h")
                .minValue(0)
                .maxValue(240)
                .build();

        speedGauge.setSkin(new ModernSkin(speedGauge));
        speedGauge.setPrefWidth(133);
        speedGauge.setPrefHeight(200);
        innerGridPane.add(speedGauge, 0, 1);

        new Thread(() -> {
            while (running) {
                try (FileInputStream fis = new FileInputStream("src/luxoftboard/img/speed.txt");
                     BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

                    String speedLine = reader.readLine();
                    if (speedLine != null) {
                        double speed = 3.3 * Double.parseDouble(speedLine.trim());  // Use Double.parseDouble here
                        
                    Platform.runLater(() -> {
                        
                        speedGauge.setValue(speed);
                        double prevSpeed = previousSpeed.get();
                            if (speed > 28 && prevSpeed <= 28) {
                                playSound("src/luxoftboard/img/beepSound.wav");
                            } else if (speed <= 28 && prevSpeed > 28) {
                                stopSound();
                            }
                            previousSpeed.set((int) speed);
                        });
                    }

                } catch (IOException | NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setTime() {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        timeLabel.setText(LocalTime.now().format(timeFormat));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    Platform.runLater(() -> timeLabel.setText(LocalTime.now().format(timeFormat)));
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void playSound(String filePath) {
        try {
            if (audioClip != null && audioClip.isRunning()) {
                audioClip.stop();
            }
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopSound() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }
}

package sample;

import controller.ControllerGame;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.Timer;

public class MainTank extends Tank {
    private final Timer timer = new Timer();
    private Timeline timeline = new Timeline();

    public MainTank(ControllerGame game) {
        super(game);
        tank.setImage(new Image("view/pictures/goldTank.png"));
        setBulletImage(new Image("view/pictures/bullet.png"));
        setFiringSpeed(1000);
        setMoveSpeed(1);
        setMoveSpeedInertia(0.008);
        setSizeTank(43);
        setSizeBullet(3);
        init();
    }

    private void init() {
        EventHandler<KeyEvent> handler = (KeyEvent event) -> {
            if (event.getCode() == KeyCode.UP) {
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.005), ev -> Platform.runLater(()
                        -> move("Up", getMoveSpeed(), true))));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
            if (event.getCode() == KeyCode.LEFT) {
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.005), ev -> Platform.runLater(()
                        -> move("Left", getMoveSpeed(), true))));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
            if (event.getCode() == KeyCode.RIGHT) {
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.005), ev -> Platform.runLater(()
                        -> move("Right", getMoveSpeed(), true)
                )));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
            if (event.getCode() == KeyCode.DOWN) {
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.005), ev -> Platform.runLater(()
                        -> move("Down", getMoveSpeed(), true))));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
        };

        EventHandler<KeyEvent> stopHandler = event -> {
            timeline.stop();
            timer.purge();
        };
        if (tank.getScene() != null) {
            tank.getScene().addEventHandler(KeyEvent.KEY_RELEASED, stopHandler);
            tank.getScene().addEventHandler(KeyEvent.KEY_PRESSED, handler);
        } else {
            tank.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    tank.getScene().addEventHandler(KeyEvent.KEY_RELEASED, stopHandler);
                    tank.getScene().addEventHandler(KeyEvent.KEY_PRESSED, handler);
                }
            });
        }
    }
}

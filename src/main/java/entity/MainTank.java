package entity;

import config.Resource;
import controller.GameController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.Timer;

public class MainTank extends Tank {
    private final Timer timer = new Timer();
    private final Timeline timeline = new Timeline();

    public MainTank(GameController game) {
        super(game);
        tank.setImage(Resource.goldTank);
        setBulletImage(Resource.bullet);
        setFiringSpeed(1000);
        setMoveSpeed(1);
        setInertiaSpeed(0.008);
        setSizeTank(43);
        setSizeBullet(3);
        init();
    }

    private void init() {
        EventHandler<KeyEvent> handler = (KeyEvent event) -> {
            String way = event.getCode() == KeyCode.UP ? "Up"
                    : event.getCode() == KeyCode.LEFT ? "Left"
                    : event.getCode() == KeyCode.RIGHT ? "Right"
                    : event.getCode() == KeyCode.DOWN ? "Down" : null;

            if (way != null) {
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.005), ev -> Platform.runLater(()
                        -> move(way, getMoveSpeed(), true))));
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

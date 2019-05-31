package sample;

import controller.ControllerGame;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

abstract class AITank extends Tank {
    private double speedTurns;

    void setSpeedTurns(double speedTurns) {
        this.speedTurns = speedTurns;
    }

    AITank(ControllerGame game) {
        super(game);
    }

    private void randomWay(ImageView tank) {
        int random = (int) (Math.random() * 30);
        if (tank != null) {
            if (tank.getRotate() == 0 || tank.getRotate() == 180) {
                if (random == 1) {
                    tank.setRotate(270);
                } else {
                    if (random == 2) {
                        tank.setRotate(90);
                    }
                }
            } else {
                if (random == 1) {
                    tank.setRotate(180);
                } else {
                    if (random == 2) {
                        tank.setRotate(0);
                    }
                }
            }
        }
    }

    void init() {
        Timeline timeline = new Timeline();
        Timeline timelineTurns = new Timeline();
        Timeline timelineFiring = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timelineTurns.setCycleCount(Animation.INDEFINITE);
        timelineFiring.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.005), ev -> Platform.runLater(()
                -> {
            if (tank != null) {
                if (tank.getRotate() == 0) {
                    move("Up", getMoveSpeed(), true);
                }
                if (tank.getRotate() == 90) {
                    move("Right", getMoveSpeed(), true);
                }
                if (tank.getRotate() == 180) {
                    move("Down", getMoveSpeed(), true);
                }
                if (tank.getRotate() == 270) {
                    move("Left", getMoveSpeed(), true);
                }
            }
        })));

        timelineFiring.getKeyFrames().setAll(new KeyFrame(Duration.seconds(getFiringSpeed()), ev -> Platform.runLater(this::tankFire)));

        timelineTurns.getKeyFrames().setAll(new KeyFrame(Duration.seconds(speedTurns), ev -> Platform.runLater(()
                -> randomWay(tank))));

        Timeline timelineStart = new Timeline(new KeyFrame(Duration.millis(250 / getMoveSpeed()),
                e -> timelineTurns.play()));

        timelineStart.play();
        timelineFiring.play();
        timeline.play();
    }
}


package com.battlecity.entities;

import com.battlecity.controllers.GameController;
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

    AITank(GameController game) {
        super(game);
    }

    private void randomWay(ImageView tank) {
        int random = (int) (Math.random() * 30);
        if (tank != null) {
            if (tank.getRotate() == 0 || tank.getRotate() == 180) {
                if (random == 1) tank.setRotate(270);
                if (random == 2) tank.setRotate(90);
            } else {
                if (random == 1) tank.setRotate(180);
                if (random == 2) tank.setRotate(0);
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

        timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.005),
                ev -> Platform.runLater(() -> {
                    if (tank != null) {
                        int rotation = (int) tank.getRotate();
                        String way = rotation == 0 ? "Up"
                                : rotation == 90 ? "Right"
                                : rotation == 180 ? "Down"
                                : rotation == 270 ? "Left" : null;

                        move(way, getMoveSpeed(), true);
                    }
                })));

        timelineFiring.getKeyFrames().setAll(new KeyFrame(Duration.seconds(getFiringSpeed()),
                ev -> Platform.runLater(this::tankFire)));

        timelineTurns.getKeyFrames().setAll(new KeyFrame(Duration.seconds(speedTurns),
                ev -> Platform.runLater(() -> randomWay(tank))));

        Timeline timelineStart = new Timeline(new KeyFrame(Duration.millis(250 / getMoveSpeed()),
                e -> timelineTurns.play()));

        timelineStart.play();
        timelineFiring.play();
        timeline.play();
    }
}


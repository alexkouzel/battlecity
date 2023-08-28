package com.battlecity.entities;

import com.battlecity.Resources;
import com.battlecity.controllers.GameController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

class Bullet {
    private final GameController game;
    private final ImageView bullet = new ImageView();

    Bullet(double x, double y, double size, Image image, GameController game) {
        this.game = game;
        bullet.setImage(image);
        bullet.setFitWidth(size);
        bullet.setFitHeight(size);
        bullet.setX(x);
        bullet.setY(y);
    }

    ImageView getImageView() {
        return bullet;
    }

    private boolean checkContact(ImageView owner) {
        if (game.gridPane.getChildren().contains(game.baseView)) {
            if (bullet.getBoundsInParent().intersects(game.baseView.getBoundsInParent())) {
                game.gameOver();
                return true;
            }
        }

        // check if bullet collided with a tank
        for (Tank tank : game.tanks) {
            if (bullet.getBoundsInParent().intersects(tank.getImageView().getBoundsInParent())
                    && tank.getImageView() != owner) {
                tank.explosion(tank.getImageView());

                if (tank.getImageView() == game.goldTank.getImageView()) {
                    game.gameOver();
                } else {
                    game.leftAITanks -= 1;
                    game.setALS(game.leftAITanks);
                    game.checkWin();
                }

                game.paneOfGame.getChildren().remove(bullet);
                tank.removeTank();
                return true;
            }
        }

        // check is bullet is out of bounds
        if (!bullet.getBoundsInParent().intersects(game.paneOfGame.getLayoutBounds())) {
            game.paneOfGame.getChildren().remove(bullet);
            return true;
        }

        // check if bullet collided with a block
        for (Node node : game.gridPane.getChildren()) {
            ImageView imageView = (ImageView) node;
            boolean collided = bullet.getBoundsInParent().intersects(imageView.getBoundsInParent());
            Image wall = imageView.getImage();

            if (collided && wall != null) {
                // put bullet under leaf
                if (wall == Resources.leaf) {
                    bullet.toBack();
                    game.blackBackground.toBack();
                }
                // put bullet upon water or ice
                if (wall == Resources.water || wall == Resources.ice) {
                    bullet.toFront();
                    return false;
                }
                // if brick is new
                if (wall == Resources.brickWall) {
                    imageView.setImage(Resources.brickWall2);
                }
                // if brick is half broken
                if (wall == Resources.brickWall2) {
                    imageView.setImage(Resources.brickWall3);
                }
                // if brick is almost broken
                if (wall == Resources.brickWall3) {
                    imageView.setImage(null);
                    game.staticObjects.remove(imageView);
                }
                return true;
            }
        }
        return false;
    }

    public void bulletFly(int rotation, ImageView owner) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.01), ev -> Platform.runLater(() -> {
            double x = bullet.getX();
            double y = bullet.getY();

            if (rotation == 270) x -= 4;
            if (rotation == 180) y += 4;
            if (rotation == 90) x += 4;
            if (rotation == 0) y -= 4;

            bullet.setX(x);
            bullet.setY(y);
            contact(timeline, owner);
        })));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void contact(Timeline timeline, ImageView owner) {
        if (checkContact(owner)) {
            game.paneOfGame.getChildren().remove(bullet);
            timeline.stop();
        }
    }
}

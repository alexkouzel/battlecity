package sample;

import controller.ControllerGame;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

class Bullet {
    private final ControllerGame game;
    private final ImageView bullet = new ImageView();

    Bullet(double x, double y, double size, Image image, ControllerGame game) {
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
        if (game.gridPane.getChildren().contains(game.base)) {
            if (bullet.getBoundsInParent().intersects(game.base.getBoundsInParent())) {
                game.gameOver();
                return true;
            }
        }
        for (sample.Tank tank : game.tanks) {
            if (bullet.getBoundsInParent().intersects(tank.getImageView().getBoundsInParent()) && tank.getImageView() != owner) {
                tank.explosion(tank.getImageView());
                if (tank.getImageView() == game.goldTank.getImageView()) {
                    game.gameOver();
                } else {
                    game.countAlTanksLeft -= 1;
                    game.setALS(game.countAlTanksLeft);
                    game.checkWin();
                }
                game.paneOfGame.getChildren().remove(bullet);
                tank.removeTank();
                return true;
            }
        }
        if (!bullet.getBoundsInParent().intersects(game.paneOfGame.getLayoutBounds())) {
            game.paneOfGame.getChildren().remove(bullet);
            return true;
        }
        for (Node node : game.gridPane.getChildren()) {
            ImageView imageView = (ImageView) node;
            if (bullet.getBoundsInParent().intersects(imageView.getBoundsInParent()) && imageView.getImage() != null) {
                if (imageView.getImage() == ControllerGame.leafImage) {
                    bullet.toBack();
                    game.blackBackground.toBack();
                } else {
                    if (imageView.getImage() == ControllerGame.waterImage || imageView.getImage() == ControllerGame.iceImage) {
                        bullet.toFront();
                        return false;
                    } else {
                        if (imageView.getImage() == ControllerGame.brickWallImage) {
                            imageView.setImage(ControllerGame.getBrickWall2Image());
                        } else {
                            if (imageView.getImage() == ControllerGame.getBrickWall2Image()) {
                                imageView.setImage(ControllerGame.getBrickWall3Image());
                            } else {
                                if (imageView.getImage() == ControllerGame.getBrickWall3Image()) {
                                    imageView.setImage(null);
                                    game.dontMoveObjects.remove(imageView);
                                }
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    void bulletFly(int rorate, ImageView owner) {
        Timeline timeline = new Timeline();
        if (rorate == 270)
            timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.01), ev -> Platform.runLater(()
                            -> {
                        bullet.setX(bullet.getX() - 4);
                        contact(timeline, owner);
                    }
            )));
        if (rorate == 90) timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.01), ev -> Platform.runLater(()
                        -> {
                    bullet.setX(bullet.getX() + 4);
                    contact(timeline, owner);
                }
        )));
        if (rorate == 0) timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.01), ev -> Platform.runLater(()
                        -> {
                    bullet.setY(bullet.getY() - 4);
                    contact(timeline, owner);
                }
        )));
        if (rorate == 180)
            timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.01), ev -> Platform.runLater(()
                            -> {
                        bullet.setY(bullet.getY() + 4);
                        contact(timeline, owner);
                    }
            )));
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

package entity;

import config.Resource;
import controller.GameController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Tank {
    ImageView tank = new ImageView();
    private final Timeline inertiaTimeline = new Timeline();
    private final GameController game;
    private double inertiaSpeed;
    private double firingSpeed;
    private double sizeBullet;
    private double moveSpeed;
    private double inertiaX;
    private double inertiaY;
    private double sizeTank;
    private boolean canFire = true;
    private boolean onIce;
    private Image bulletImage;

    public Tank(GameController game) {
        this.game = game;
        tank.setFitHeight(sizeTank);
        tank.setFitWidth(sizeTank);
        initialize();
    }

    void move(String way, Double length, Boolean active) {
        if (!(tank.getImage() == Resource.goldTank && (!game.isRunning || game.isWinner))) {
            switch (way) {
                case "Up":
                    if (active) {
                        tank.setRotate(0);
                        if (onIce && inertiaY > -2) {
                            inertiaY -= inertiaSpeed;
                        } else {
                            tank.setY(tank.getY() - length);
                            inertiaY = -moveSpeed * 0.7;
                            inertiaX = 0;
                        }
                    } else {
                        tank.setY(tank.getY() - length);
                    }

                    if (tank.getLayoutY() + tank.getY() <= 0) {
                        tank.setY(tank.getY() + length);
                        inertiaY = 0;
                    }
                    break;
                case "Left":
                    if (active) {
                        tank.setRotate(270);
                        if (onIce && inertiaX > -2) {
                            inertiaX -= inertiaSpeed;
                        } else {
                            tank.setX(tank.getX() - length);
                            inertiaY = 0;
                            inertiaX = -moveSpeed * 0.7;
                        }
                    } else {
                        tank.setX(tank.getX() - length);
                    }

                    if (tank.getLayoutX() + tank.getX() <= 0) {
                        tank.setX(tank.getX() + length);
                        inertiaX = 0;
                    }
                    break;
                case "Right":
                    if (active) {
                        tank.setRotate(90);
                        if (onIce && inertiaX < 2) {
                            inertiaX += inertiaSpeed;
                        } else {
                            tank.setX(tank.getX() + length);
                            inertiaY = 0;
                            inertiaX = moveSpeed * 0.7;
                        }
                    } else {
                        tank.setX(tank.getX() + length);
                    }

                    if (tank.getLayoutX() + tank.getX() >= game.paneOfGame.getWidth() - sizeTank) {
                        tank.setX(tank.getX() - length);
                        inertiaX = 0;
                    }
                    break;
                case "Down":
                    if (active) {
                        tank.setRotate(180);
                        if (onIce && inertiaY < 2) {
                            inertiaY += inertiaSpeed;
                        } else {
                            tank.setY(tank.getY() + length);
                            inertiaY = moveSpeed * 0.7;
                            inertiaX = 0;
                        }
                    } else {
                        tank.setY(tank.getY() + length);
                    }

                    if (tank.getLayoutY() + tank.getY() >= game.paneOfGame.getHeight() - sizeTank) {
                        tank.setY(tank.getY() - length);
                        inertiaY = 0;
                    }
                    break;
            }
        }
        for (ImageView imageView : game.iceArea) {
            if (tank.getBoundsInParent().intersects(imageView.getBoundsInParent())) {
                onIce = true;
                break;
            } else {
                onIce = false;
            }
        }
        for (ImageView imageView : game.staticObjects) {
            if (imageView != tank) {
                if (tank.getBoundsInParent().intersects(imageView.getBoundsInParent())) {
                    switch (way) {
                        case "Up":
                            tank.setY(tank.getY() + length);
                            inertiaY = 0;
                            break;
                        case "Left":
                            tank.setX(tank.getX() + length);
                            inertiaX = 0;
                        case "Right":
                            tank.setX(tank.getX() - length);
                            inertiaX = 0;
                        case "Down":
                            tank.setY(tank.getY() - length);
                            inertiaY = 0;
                    }
                }
            }
        }
    }

    private void initialize() {
        inertiaTimeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.005),
                ev -> Platform.runLater(() -> {
                    if (onIce) {
                        if (inertiaY < 0) {
                            move("Up", -inertiaY, false);
                            inertiaY += inertiaSpeed / 2;
                        } else {
                            move("Down", inertiaY, false);
                            inertiaY -= inertiaSpeed / 2;
                        }
                        if (inertiaX < 0) {
                            move("Left", -inertiaX, false);
                            inertiaX += inertiaSpeed / 2;
                        } else {
                            move("Right", inertiaX, false);
                            inertiaX -= inertiaSpeed / 2;
                        }
                    }
                })));
        inertiaTimeline.setCycleCount(Animation.INDEFINITE);
        inertiaTimeline.play();
    }

    void explosion(ImageView tank) {
        // show explosion image
        ImageView explosion = new ImageView();
        explosion.setImage(new Image(Resource.explosion));
        explosion.setFitHeight(50);
        explosion.setFitWidth(50);
        explosion.setX(tank.getX() + tank.getTranslateX());
        explosion.setY(tank.getY() + tank.getTranslateY());
        game.addChild(explosion);

        // destroy tank
        removeTank();
        game.staticObjects.remove(tank);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),
                e -> game.paneOfGame.getChildren().remove(explosion)));
        timeline.play();
    }

    public void tankFire() {
        boolean isTankPresent = game.paneOfGame.getChildren().contains(tank);
        if (game.isRunning && !game.isWinner && canFire && isTankPresent) {
            canFire = false;
            double bulletX = tank.getX() + sizeTank / 2 - sizeBullet / 2;
            double bulletY = tank.getY() + sizeTank / 2 - sizeBullet / 2;
            Bullet bullet = new Bullet(bulletX, bulletY, sizeBullet, bulletImage, game);
            game.paneOfGame.getChildren().add(bullet.getImageView());

            // get tank and leafs to front
            tank.toFront();
            game.gridPaneLeafs.toFront();

            // set bullet flying
            bullet.bulletFly((int) tank.getRotate(), tank);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(firingSpeed), e -> canFire = true));
            timeline.play();
        }
    }

    public void setCoordinates(double x, double y, int rotate, double size) {
        tank.setX(x);
        tank.setY(y);
        tank.setRotate(rotate);
        tank.setFitHeight(size);
        tank.setFitWidth(size);
    }

    public void removeTank() {
        game.paneOfGame.getChildren().remove(tank);
        game.tanks.remove(this);
        game.staticObjects.remove(tank);
    }

    public ImageView getImageView() {
        return tank;
    }

    public void setImage(Image image) {
        tank.setImage(image);
    }

    public void setSizeTank(double sizeTank) {
        this.sizeTank = sizeTank;
    }

    public void setInertiaSpeed(double inertiaSpeed) {
        this.inertiaSpeed = inertiaSpeed;
    }

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setFiringSpeed(double firingSpeed) {
        this.firingSpeed = firingSpeed;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public void setSizeBullet(double sizeBullet) {
        this.sizeBullet = sizeBullet;
    }

    public void setBulletImage(Image bulletImage) {
        this.bulletImage = bulletImage;
    }

    public double getFiringSpeed() {
        return firingSpeed;
    }
}

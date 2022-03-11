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

import java.util.ArrayList;

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

    private void moveOneWay(int degree, double length, boolean isActive) {
        int mult = degree == 270 ? -1 : 1;
        double shift = length * mult;

        if (isActive) {
            tank.setRotate(degree);
            if (onIce && Math.abs(inertiaX) < 2) {
                inertiaX += inertiaSpeed * mult;
            } else {
                tank.setX(tank.getX() + shift);
                inertiaY = 0;
                inertiaX = moveSpeed * mult * 0.7;
            }
        } else {
            tank.setX(tank.getX() + shift);
        }

        boolean isOutOfBounds = degree == 270
                ? tank.getLayoutX() + tank.getX() <= 0
                : tank.getLayoutX() + tank.getX() >= game.paneOfGame.getWidth() - sizeTank;

        if (isOutOfBounds) {
            tank.setX(tank.getX() - shift);
            inertiaX = 0;
        }
    }

    void move(String way, Double length, Boolean isActive) {
        if (!(tank.getImage() == Resource.goldTank && (!game.isRunning || game.hasWinner))) {
            switch (way) {
                case "Up":
                    if (isActive) {
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
                    moveOneWay(270, length, isActive);
                    break;
                case "Right":
                    moveOneWay(90, length, isActive);
                    break;
                case "Down":
                    if (isActive) {
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

        // handle collision with ice block
        onIce = isColliding(game.iceArea);

        // handle collision with static object
        handleCollision(way, length);
    }

    private boolean isColliding(ArrayList<ImageView> blocks) {
        for (ImageView block : blocks) {
            if (isColliding(block)) {
                return true;
            }
        }
        return false;
    }

    private boolean isColliding(ImageView block) {
        return tank.getBoundsInParent().intersects(block.getBoundsInParent());
    }

    private void handleCollision(String way, double length) {
        for (ImageView imageView : game.staticObjects) {
            if (imageView != tank && tank.getBoundsInParent().intersects(imageView.getBoundsInParent())) {
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
        if (game.isRunning && !game.hasWinner && canFire && isTankPresent) {
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

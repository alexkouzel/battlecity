package sample;

import controller.ControllerGame;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Tank {
    private final Timeline timelineInertia = new Timeline();
    private final ControllerGame game;
    ImageView tank = new ImageView();
    private boolean canFire = true;
    private boolean onIce;
    private double inertiaX;
    private double inertiaY;
    private double sizeTank;
    private double sizeBullet;
    private double moveSpeedInertia;
    private double moveSpeed;
    private double firingSpeed;
    private Image bulletImage;

    public Tank(ControllerGame game) {
        this.game = game;
        tank.setFitHeight(sizeTank);
        tank.setFitWidth(sizeTank);
        initialize();
    }

    void move(String way, Double length, Boolean active) {
        if (!(!game.isGame && tank.getImage() == ControllerGame.goldTankImage || game.isWin && tank.getImage() == ControllerGame.goldTankImage)){
            if (way.equals("Up")) {
                if (active) {
                    tank.setRotate(0);
                    if (onIce) {
                        if (inertiaY > -2) {
                            inertiaY -= moveSpeedInertia;
                        }
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
            } else {
                if (way.equals("Left")) {
                    if (active) {
                        tank.setRotate(270);
                        if (onIce) {
                            if (inertiaX > -2) {
                                inertiaX -= moveSpeedInertia;
                            }
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
                } else {
                    if (way.equals("Right")) {
                        if (active) {
                            tank.setRotate(90);
                            if (onIce) {
                                if (inertiaX < 2) {
                                    inertiaX += moveSpeedInertia;
                                }
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
                    } else {
                        if (active) {
                            tank.setRotate(180);
                            if (onIce) {
                                if (inertiaY < 2) {
                                    inertiaY += moveSpeedInertia;
                                }
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
                    }
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
            for (ImageView imageView : game.dontMoveObjects) {
                if (imageView != tank) {
                    if (tank.getBoundsInParent().intersects(imageView.getBoundsInParent())) {
                        if (way.equals("Up")) {
                            tank.setY(tank.getY() + length);
                            inertiaY = 0;
                        } else {
                            if (way.equals("Left")) {
                                tank.setX(tank.getX() + length);
                                inertiaX = 0;
                            } else {
                                if (way.equals("Right")) {
                                    tank.setX(tank.getX() - length);
                                    inertiaX = 0;
                                } else {
                                    tank.setY(tank.getY() - length);
                                    inertiaY = 0;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void initialize() {
        timelineInertia.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.005), ev -> Platform.runLater(()
                -> {
            if (onIce) {
                if (inertiaY < 0) {
                    move("Up", -inertiaY, false);
                } else {
                    move("Down", inertiaY, false);
                }
                if (inertiaX < 0) {
                    move("Left", -inertiaX, false);
                } else {
                    move("Right", inertiaX, false);
                }
                if (inertiaX > 0) {
                    inertiaX -= moveSpeedInertia / 2;
                }
                if (inertiaX < 0) {
                    inertiaX += moveSpeedInertia / 2;
                }
                if (inertiaY > 0) {
                    inertiaY -= moveSpeedInertia / 2;
                }
                if (inertiaY < 0) {
                    inertiaY += moveSpeedInertia / 2;
                }
            }
        })));
        timelineInertia.setCycleCount(Animation.INDEFINITE);
        timelineInertia.play();
    }

    void explosion(ImageView tank) {
        ImageView explosion = new ImageView();
        explosion.setImage(new Image("view/pictures/explosion.png"));
        explosion.setFitHeight(50);
        explosion.setFitWidth(50);
        explosion.setX(tank.getX() + tank.getTranslateX());
        explosion.setY(tank.getY() + tank.getTranslateY());
        game.addChild(explosion);
        removeTank();
        game.dontMoveObjects.remove(tank);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                e -> game.paneOfGame.getChildren().remove(explosion)));
        timeline.play();
    }

    public void tankFire() {
        if (game.isGame && !game.isWin && canFire && game.paneOfGame.getChildren().contains(tank)) {
            canFire = false;
            sample.Bullet bullet = new sample.Bullet(tank.getX() + sizeTank / 2 - sizeBullet / 2, tank.getY() + sizeTank / 2 - sizeBullet / 2, sizeBullet, bulletImage, game);
            game.paneOfGame.getChildren().add(bullet.getImageView());
            tank.toFront();
            game.gridPaneLeafs.toFront();
            bullet.bulletFly((int) tank.getRotate(), tank);
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(firingSpeed),
                    e -> canFire = true));
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

    public ImageView getImageView() {
        return tank;
    }

    public void removeTank() {
        game.paneOfGame.getChildren().remove(tank);
        game.tanks.remove(this);
        game.dontMoveObjects.remove(tank);
    }

    public ControllerGame getGame() {
        return game;
    }

    public void setImage(Image image) {
        tank.setImage(image);
    }

    void setSizeTank(double sizeTank) {
        this.sizeTank = sizeTank;
    }

    void setMoveSpeedInertia(double moveSpeedInertia) {
        this.moveSpeedInertia = moveSpeedInertia;
    }

    void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    void setFiringSpeed(double firingSpeed) {
        this.firingSpeed = firingSpeed;
    }

    double getMoveSpeed() {
        return moveSpeed;
    }

    void setSizeBullet(double sizeBullet) {
        this.sizeBullet = sizeBullet;
    }

    public void setBulletImage(Image bulletImage) {
        this.bulletImage = bulletImage;
    }

    public double getFiringSpeed() {
        return firingSpeed;
    }
}

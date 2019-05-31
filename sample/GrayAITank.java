package sample;

import controller.ControllerGame;
import javafx.scene.image.Image;

public class GrayAITank extends AITank {

    public GrayAITank(ControllerGame game) {
        super(game);
        tank.setImage(ControllerGame.grayTankImage);
        setBulletImage(new Image("view/pictures/bullet.png"));
        setSizeTank(43);
        setSpeedTurns(1);
        setFiringSpeed(1);
        setSpeedTurns(0.05);
        setMoveSpeed(1);
        setMoveSpeedInertia(0.008);
        setSizeBullet(3);
        init();
    }
}

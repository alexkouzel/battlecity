package entity;

import controller.GameController;
import config.Resource;

public class GrayAITank extends AITank {
    public GrayAITank(GameController game) {
        super(game);
        tank.setImage(Resource.grayTank);
        setBulletImage(Resource.bullet);
        setSizeTank(43);
        setSpeedTurns(1);
        setFiringSpeed(1);
        setSpeedTurns(0.05);
        setMoveSpeed(1);
        setInertiaSpeed(0.008);
        setSizeBullet(3);
        init();
    }
}
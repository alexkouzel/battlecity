package com.battlecity.entities;

import com.battlecity.controllers.GameController;
import com.battlecity.Resources;

public class GrayAITank extends AITank {
    public GrayAITank(GameController game) {
        super(game);
        tank.setImage(Resources.grayTank);
        setBulletImage(Resources.bullet);
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
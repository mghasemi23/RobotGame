package main.saveAndLoad;

import main.game.GameEngine;
import main.main;
import main.robots.*;

import java.io.Serializable;

public class SaveAndLoadClass implements Serializable {
    private int turn = 0;
    private boolean live;
    private boolean separate;
    private int attack = 0;
    private int life = 0;
    private int kind;//0 is B_diagonal    1 is B_horizontal     2 is B_vertical     3 is B_horse
    //4 is R_diagonal    5 is R_horizontal     6 is R_vertical     7 is R_horse
    private double x;
    private double y;

    public SaveAndLoadClass(MotherRobot motherRobot) {
        turn = new GameEngine().turn;
        live = motherRobot.isLive();
        separate = motherRobot.isSeparate();
        attack = motherRobot.getAttack();
        life = motherRobot.getLife();
        x = motherRobot.getImageView().getX();
        y = motherRobot.getImageView().getY();
        kind = motherRobot.findKind();
    }

    public SaveAndLoadClass() {
    }

    public MotherRobot getRobot() {
        MotherRobot motherRobot = null;
        if (kind == 0) {
            motherRobot = new Blue_DiagonalRobot(attack, life);
        } else if (kind == 1) {
            motherRobot = new Blue_HorizontalRobot(attack, life);
        } else if (kind == 2) {
            motherRobot = new Blue_VerticalRobot(attack, life);
        } else if (kind == 3) {
            motherRobot = new Blue_HorseRobot(attack, life);
        } else if (kind == 4) {
            motherRobot = new Red_DiagonalRobot(attack, life);
        } else if (kind == 5) {
            motherRobot = new Red_HorizontalRobot(attack, life);
        } else if (kind == 6) {
            motherRobot = new Red_VerticalRobot(attack, life);
        } else if (kind == 7) {
            motherRobot = new Red_HorseRobot(attack, life);
        }
        motherRobot.getImageView().setX(x);
        motherRobot.getImageView().setY(y);
        motherRobot.setLive(live);
        motherRobot.setSeparate(separate);
        GameEngine game = new GameEngine();
        game.turn = turn;
        return motherRobot;
    }
}

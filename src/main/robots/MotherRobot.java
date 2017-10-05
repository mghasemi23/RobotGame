package main.robots;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import main.game.GameEngine;
import main.game.MakeGame;
import main.main;

import java.util.ArrayList;
import java.util.Random;

public abstract class MotherRobot {
    private final DropShadow dropShadow = new DropShadow(15, 5, 5, Color.BLACK);
    private final Media death_effect = new Media(String.valueOf(main.class.getResource("file/whoosh.mp3")));
    private final MediaPlayer death = new MediaPlayer(getDeath_effect());
    private final ColorAdjust ways = new ColorAdjust(0.6, 0.5, 0.5, 0.5);
    protected final WrongWay wrongWays = new WrongWay();
    private final double pixel = 45;
    private ImageView imageView;
    private boolean live = true;
    private boolean separate = false;
    private int attack = 0;
    private int life = 0;
    private int defense;
    private int team;//0 is red 1 is blue

    public MotherRobot() {
        Random random = new Random();
        int sepInt = random.nextInt(3);
        if (sepInt == 1)
            setSeparate(true);
    }


    public DropShadow getDropShadow() {
        return dropShadow;
    }

    public Media getDeath_effect() {
        return death_effect;
    }

    public MediaPlayer getDeath() {
        return death;
    }

    public ColorAdjust getWays() {
        return ways;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public double getPixel() {
        return pixel;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public boolean isSeparate() {
        return separate;
    }

    public void setSeparate(boolean separate) {
        this.separate = separate;
    }

    //move animation
    public void move(double x, double y, boolean glory) {
        Media move = new Media(String.valueOf(main.class.getResource("file/move.wav")));
        MediaPlayer movePlayer = new MediaPlayer(move);
        GameEngine gameEngine = new GameEngine();
        gameEngine.turn();
        PathTransition pt = new PathTransition(Duration.seconds(1), new Line(this.getImageView().getX() + getPixel() / 2, this.getImageView().getY() + getPixel() / 2, x + getPixel() / 2, y + getPixel() / 2), getImageView());
        pt.play();
        movePlayer.play();
        for (Rectangle rectangle : new MakeGame().getTable())
            rectangle.setEffect(new Glow(0));
        getImageView().setX(x);
        getImageView().setY(y);
        pt.setOnFinished(event -> {
            if (glory)
                glory();
        });
    }

    //move and come back animation
    public void moveBack(double x, double y) {
        Media moveBack = new Media(String.valueOf(main.class.getResource("file/Critical.wav")));
        MediaPlayer moveBackPlayer = new MediaPlayer(moveBack);
        moveBackPlayer.play();
        GameEngine gameEngine = new GameEngine();
        gameEngine.turn();
        PathTransition pt = new PathTransition(Duration.seconds(1), new Line(this.getImageView().getX() + getPixel() / 2, this.getImageView().getY() + getPixel() / 2, x + getPixel() / 2, y + getPixel() / 2), getImageView());
        pt.setAutoReverse(true);
        pt.setCycleCount(2);
        for (Rectangle rectangle : new MakeGame().getTable())
            rectangle.setEffect(new Glow(0));
        pt.play();
    }

    //glory animation
    public void glory() {
        Media glory = new Media(String.valueOf(main.class.getResource("file/tada.wav")));
        MediaPlayer gloryPlayer = new MediaPlayer(glory);
        final boolean[] grow = {false};
        final boolean[] shrink = {false};
        final int[] counter = {0};
        getImageView().setFitWidth(45);
        getImageView().setFitHeight(45);
        AnimationTimer glory_ATimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (!grow[0] && !shrink[0]) {
                    getImageView().setEffect(new Glow(10));
                    gloryPlayer.play();
                    getImageView().setFitWidth(getImageView().getFitWidth() + 5);
                    getImageView().setFitHeight(getImageView().getFitHeight() + 5);
                    ++counter[0];
                    if (counter[0] >= 10) {
                        grow[0] = true;

                    }
                } else if (grow[0] && !shrink[0]) {
                    getImageView().setFitWidth(getImageView().getFitWidth() - 5);
                    getImageView().setFitHeight(getImageView().getFitHeight() - 5);
                    --counter[0];
                    if (counter[0] <= 0) {
                        shrink[0] = true;
                        getImageView().setEffect(getDropShadow());
                    }
                }
            }
        };
        glory_ATimer.start();
    }

    //separate animation
    public void separate() {
        setLive(false);
        final int[] counter = {0};
        getImageView().setFitWidth(45);
        getImageView().setFitHeight(45);
        double tempX = getImageView().getX();
        double tempY = getImageView().getY();
        Media separate = new Media(String.valueOf(main.class.getResource("file/separate.wav")));
        MediaPlayer separatePlayer = new MediaPlayer(separate);
        AnimationTimer separateAnimation = new AnimationTimer() {
            @Override
            public void handle(long now) {
                separatePlayer.play();
                getImageView().setFitWidth(getImageView().getFitWidth() + 5);
                getImageView().setFitHeight(getImageView().getFitHeight() + 5);
                getImageView().setOpacity(getImageView().getOpacity() - 0.1);
                ++counter[0];
                if (counter[0] >= 10) {
                    getImageView().setVisible(false);
                    getImageView().setX(-100);
                    getImageView().setY(-100);
                }
            }
        };
        separateAnimation.start();

        ArrayList<MotherRobot> all = new ArrayList<>();
        Service separateService = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        int child = new Random().nextInt(2) + 2;
                        boolean ru = ru_S(tempX, tempY)//
                                , u = u_S(tempX, tempY)//
                                , lu = lu_S(tempX, tempY)//
                                , l = l_S(tempX, tempY)//
                                , ld = ld_S(tempX, tempY)//
                                , d = d_S(tempX, tempY)//
                                , rd = rd_S(tempX, tempY)//
                                , r = r_S(tempX, tempY);

                        int kind = findKind();
                        while (child > 0) {
                            int rnd = new Random().nextInt(8);
                            MotherRobot temp = null;
                            if (kind == 0) {
                                temp = new Blue_DiagonalRobot(attack - 1, life - 1);
                            } else if (kind == 1) {
                                temp = new Blue_HorizontalRobot(attack - 1, life - 1);
                            } else if (kind == 2) {
                                temp = new Blue_VerticalRobot(attack - 1, life - 1);
                            } else if (kind == 3) {
                                temp = new Blue_HorseRobot(attack - 1, life - 1);
                            } else if (kind == 4) {
                                temp = new Red_DiagonalRobot(attack - 1, life - 1);
                            } else if (kind == 5) {
                                temp = new Red_HorizontalRobot(attack - 1, life - 1);
                            } else if (kind == 6) {
                                temp = new Red_VerticalRobot(attack - 1, life - 1);
                            } else if (kind == 7) {
                                temp = new Red_HorseRobot(attack - 1, life - 1);
                            }
                            if (rnd == 0 && ru) {
                                try {
                                    if (tempX + getPixel() > 200 && tempX + getPixel() < 920 && tempY - getPixel() > 0 && tempY - getPixel() < 720) {
                                        temp.getImageView().setX(tempX + getPixel());
                                        temp.getImageView().setY(tempY - getPixel());
                                        child--;
                                    }
                                } catch (NullPointerException e) {
                                }
                                ru = false;
                            } else if (rnd == 1 && u) {
                                try {
                                    if (tempY - getPixel() > 0 && tempY - getPixel() < 720) {
                                        temp.getImageView().setX(tempX);
                                        temp.getImageView().setY(tempY - getPixel());
                                        child--;
                                    }
                                } catch (NullPointerException e) {
                                }
                                u = false;
                            } else if (rnd == 2 && lu) {
                                try {
                                    if (tempX - getPixel() > 200 && tempX - getPixel() < 920 && tempY - getPixel() > 0 && tempY - getPixel() < 720) {
                                        temp.getImageView().setX(tempX - getPixel());
                                        temp.getImageView().setY(tempY - getPixel());
                                        child--;
                                    }
                                } catch (NullPointerException e) {
                                }
                                lu = false;
                            } else if (rnd == 3 && l) {
                                try {
                                    if (tempX - getPixel() > 200 && tempX - getPixel() < 920) {
                                        temp.getImageView().setX(tempX - getPixel());
                                        temp.getImageView().setY(tempY);
                                        child--;
                                    }
                                } catch (NullPointerException e) {
                                }
                                l = false;
                            } else if (rnd == 4 && ld) {
                                try {
                                    if (tempX - getPixel() > 200 && tempX - getPixel() < 920 && tempY + getPixel() > 0 && tempY + getPixel() < 720) {
                                        temp.getImageView().setX(tempX - getPixel());
                                        temp.getImageView().setY(tempY + getPixel());
                                        child--;
                                    }
                                } catch (NullPointerException e) {
                                }
                                ld = false;
                            } else if (rnd == 5 && d) {
                                try {
                                    if (tempY + getPixel() > 0 && tempY + getPixel() < 720) {
                                        temp.getImageView().setX(tempX);
                                        temp.getImageView().setY(tempY + getPixel());
                                        child--;
                                    }
                                } catch (NullPointerException e) {
                                }
                                d = false;
                            } else if (rnd == 6 && rd) {
                                try {
                                    if (tempX + getPixel() > 200 && tempX + getPixel() < 920 && tempY + getPixel() > 0 && tempY + getPixel() < 720) {
                                        temp.getImageView().setX(tempX + getPixel());
                                        temp.getImageView().setY(tempY + getPixel());
                                        child--;
                                    }
                                } catch (NullPointerException e) {
                                }
                                rd = false;
                            } else if (rnd == 7 && r) {
                                try {
                                    if (tempX + getPixel() > 200 && tempX + getPixel() < 920) {
                                        temp.getImageView().setX(tempX + getPixel());
                                        temp.getImageView().setY(tempY);
                                        child--;
                                    }
                                } catch (NullPointerException e) {
                                }
                                r = false;
                            }
                            all.add(temp);
                        }
                        return null;
                    }
                };
            }
        };
        separateService.setOnSucceeded(event -> {
            for (MotherRobot robot : all) {
                MakeGame makeGame = new MakeGame();
                if (robot.getImageView().getX() > 200 && robot.getImageView().getX() < 920 //
                        && robot.getImageView().getY() > 0 && robot.getImageView().getY() < 720) {
                    makeGame.getTableGroup().getChildren().add(robot.getImageView());
                    makeGame.getRobots().add(robot);
                }
            }
        });
        separateService.restart();
    }

    public abstract void start(double x, double y);

    public abstract void death();

    public abstract void ways();

    //finding way methods
    public void ru() {
        ArrayList<Rectangle> ru = new ArrayList<>();
        double px = new MakeGame().getPixel();
        for (Rectangle a : new MakeGame().getTable()) {
            //RU
            for (int i = 1; i < 16; i++) {
                if (a.getX() == this.getImageView().getX() + (i * px) && a.getY() == this.getImageView().getY() - (i * px)) {
                    ru.add(a);
                    for (MotherRobot p : new MakeGame().getRobots()) {
                        if (p.getImageView().getX() == a.getX() && p.getImageView().getY() == a.getY() && p.getTeam() == this.getTeam()) {
                            ru.remove(a);
                            a.setEffect(wrongWays);
                        }
                    }
                }
            }
        }
        for (int j = 0; j < ru.size(); j++) {
            ru.get(j).setEffect(ways);
        }
    }

    public void rd() {
        ArrayList<Rectangle> rd = new ArrayList<>();
        double px = new MakeGame().getPixel();
        for (Rectangle a : new MakeGame().getTable()) {
            //RD
            for (int i = 1; i < 16; i++) {
                if (a.getX() == this.getImageView().getX() + (i * px) && a.getY() == this.getImageView().getY() + (i * px)) {
                    rd.add(a);
                    for (MotherRobot p : new MakeGame().getRobots()) {
                        if (p.getImageView().getX() == a.getX() && p.getImageView().getY() == a.getY() && p.getTeam() == this.getTeam()) {
                            rd.remove(a);
                            a.setEffect(wrongWays);
                        }
                    }
                }
            }
        }
        for (int j = 0; j < rd.size(); j++) {
            rd.get(j).setEffect(ways);
        }
    }

    public void ld() {
        ArrayList<Rectangle> ld = new ArrayList<>();
        double px = new MakeGame().getPixel();
        for (Rectangle a : new MakeGame().getTable()) {
            //LD
            for (int i = 1; i < 16; i++) {
                if (a.getX() == this.getImageView().getX() - (i * px) && a.getY() == this.getImageView().getY() + (i * px)) {
                    ld.add(a);
                    for (MotherRobot p : new MakeGame().getRobots()) {
                        if (p.getImageView().getX() == a.getX() && p.getImageView().getY() == a.getY() && p.getTeam() == this.getTeam()) {
                            ld.remove(a);
                            a.setEffect(wrongWays);
                        }
                    }
                }
            }
        }

        for (int j = 0; j < ld.size(); j++) {
            ld.get(j).setEffect(ways);
        }
    }

    public void lu() {
        ArrayList<Rectangle> lu = new ArrayList<>();
        double px = new MakeGame().getPixel();
        for (Rectangle a : new MakeGame().getTable()) {
            //LU
            for (int i = 1; i < 16; i++) {
                if (a.getX() == this.getImageView().getX() - (i * px) && a.getY() == this.getImageView().getY() - (i * px)) {
                    lu.add(a);
                    for (MotherRobot p : new MakeGame().getRobots()) {
                        if (p.getImageView().getX() == a.getX() && p.getImageView().getY() == a.getY() && p.getTeam() == this.getTeam()) {
                            lu.remove(a);
                            a.setEffect(wrongWays);
                        }
                    }
                }
            }
        }
        for (int j = 0; j < lu.size(); j++) {
            lu.get(j).setEffect(ways);
        }
    }

    public void r() {
        ArrayList<Rectangle> r = new ArrayList<>();
        double px = new MakeGame().getPixel();
        for (Rectangle a : new MakeGame().getTable()) {
            //R
            for (int i = 1; i < 16; i++) {
                if (a.getX() == this.getImageView().getX() + (i * px) && a.getY() == this.getImageView().getY()) {
                    r.add(a);
                    for (MotherRobot p : new MakeGame().getRobots()) {
                        if (p.getImageView().getX() == a.getX() && p.getImageView().getY() == a.getY() && p.getTeam() == this.getTeam()) {
                            r.remove(a);
                            a.setEffect(wrongWays);
                        }
                    }
                }
            }
        }
        for (int j = 0; j < r.size(); j++) {
            r.get(j).setEffect(ways);
        }
    }

    public void l() {
        ArrayList<Rectangle> l = new ArrayList<>();
        double px = new MakeGame().getPixel();
        for (Rectangle a : new MakeGame().getTable()) {
            //L
            for (int i = 1; i < 16; i++) {
                if (a.getX() == this.getImageView().getX() - (i * px) && a.getY() == this.getImageView().getY()) {
                    l.add(a);
                    for (MotherRobot p : new MakeGame().getRobots()) {
                        if (p.getImageView().getX() == a.getX() && p.getImageView().getY() == a.getY() && p.getTeam() == this.getTeam()) {
                            l.remove(a);
                            a.setEffect(wrongWays);
                        }
                    }
                }
            }
        }
        for (int j = 0; j < l.size(); j++) {
            l.get(j).setEffect(ways);
        }
    }

    public void u() {
        ArrayList<Rectangle> u = new ArrayList<>();
        double px = new MakeGame().getPixel();
        for (Rectangle a : new MakeGame().getTable()) {
            //U
            for (int i = 1; i < 16; i++) {
                if (a.getX() == this.getImageView().getX() && a.getY() == this.getImageView().getY() - (i * px)) {
                    u.add(a);
                    for (MotherRobot p : new MakeGame().getRobots()) {
                        if (p.getImageView().getX() == a.getX() && p.getImageView().getY() == a.getY() && p.getTeam() == this.getTeam()) {
                            u.remove(a);
                            a.setEffect(wrongWays);
                        }
                    }
                }
            }
        }
        for (int j = 0; j < u.size(); j++) {
            u.get(j).setEffect(ways);
        }
    }

    public void d() {
        ArrayList<Rectangle> d = new ArrayList<>();
        double px = new MakeGame().getPixel();
        for (Rectangle a : new MakeGame().getTable()) {
            //U
            for (int i = 1; i < 16; i++) {
                if (a.getX() == this.getImageView().getX() && a.getY() == this.getImageView().getY() + (i * px)) {
                    d.add(a);
                    for (MotherRobot p : new MakeGame().getRobots()) {
                        if (p.getImageView().getX() == a.getX() && p.getImageView().getY() == a.getY() && p.getTeam() == this.getTeam()) {
                            d.remove(a);
                            a.setEffect(wrongWays);
                        }
                    }
                }
            }
        }
        for (int j = 0; j < d.size(); j++) {
            d.get(j).setEffect(ways);
        }
    }

    public void knight() {
        for (Rectangle a : new MakeGame().getTable()) {
            a.setEffect(new Glow(0));

            if (a.getX() == getImageView().getX() + 2 * getPixel() && a.getY() == getImageView().getY() + 1 * getPixel()) {
                a.setEffect(getWays());
                for (MotherRobot robot : new MakeGame().getRobots()) {
                    if (a.getX() == robot.getImageView().getX() && a.getY() == robot.getImageView().getY() && this.getTeam() == robot.getTeam())
                        a.setEffect(wrongWays);
                }
            }
            if (a.getX() == getImageView().getX() + 1 * getPixel() && a.getY() == getImageView().getY() + 2 * getPixel()) {
                a.setEffect(getWays());
                for (MotherRobot robot : new MakeGame().getRobots()) {
                    if (a.getX() == robot.getImageView().getX() && a.getY() == robot.getImageView().getY() && this.getTeam() == robot.getTeam())
                        a.setEffect(wrongWays);
                }
            }
            if (a.getX() == getImageView().getX() - 2 * getPixel() && a.getY() == getImageView().getY() - 1 * getPixel()) {
                a.setEffect(getWays());
                for (MotherRobot robot : new MakeGame().getRobots()) {
                    if (a.getX() == robot.getImageView().getX() && a.getY() == robot.getImageView().getY() && this.getTeam() == robot.getTeam())
                        a.setEffect(wrongWays);
                }
            }
            if (a.getX() == getImageView().getX() - 1 * getPixel() && a.getY() == getImageView().getY() - 2 * getPixel()) {
                a.setEffect(getWays());
                for (MotherRobot robot : new MakeGame().getRobots()) {
                    if (a.getX() == robot.getImageView().getX() && a.getY() == robot.getImageView().getY() && this.getTeam() == robot.getTeam())
                        a.setEffect(wrongWays);
                }
            }
            if (a.getX() == getImageView().getX() + 2 * getPixel() && a.getY() == getImageView().getY() - 1 * getPixel()) {
                a.setEffect(getWays());
                for (MotherRobot robot : new MakeGame().getRobots()) {
                    if (a.getX() == robot.getImageView().getX() && a.getY() == robot.getImageView().getY() && this.getTeam() == robot.getTeam())
                        a.setEffect(wrongWays);
                }
            }
            if (a.getX() == getImageView().getX() + 1 * getPixel() && a.getY() == getImageView().getY() - 2 * getPixel()) {
                a.setEffect(getWays());
                for (MotherRobot robot : new MakeGame().getRobots()) {
                    if (a.getX() == robot.getImageView().getX() && a.getY() == robot.getImageView().getY() && this.getTeam() == robot.getTeam())
                        a.setEffect(wrongWays);
                }
            }
            if (a.getX() == getImageView().getX() - 2 * getPixel() && a.getY() == getImageView().getY() + 1 * getPixel()) {
                a.setEffect(getWays());
                for (MotherRobot robot : new MakeGame().getRobots()) {
                    if (a.getX() == robot.getImageView().getX() && a.getY() == robot.getImageView().getY() && this.getTeam() == robot.getTeam())
                        a.setEffect(wrongWays);
                }
            }
            if (a.getX() == getImageView().getX() - 1 * getPixel() && a.getY() == getImageView().getY() + 2 * getPixel()) {
                a.setEffect(getWays());
                for (MotherRobot robot : new MakeGame().getRobots()) {
                    if (a.getX() == robot.getImageView().getX() && a.getY() == robot.getImageView().getY() && this.getTeam() == robot.getTeam())
                        a.setEffect(wrongWays);
                }
            }
        }
    }

    //finding separate home
    public boolean ru_S(double x, double y) {
        for (MotherRobot robot : new MakeGame().getRobots()) {
            if (robot.getImageView().getX() == x + getPixel() && robot.getImageView().getY() == y - getPixel())
                return false;
        }
        return true;
    }

    public boolean u_S(double x, double y) {
        for (MotherRobot robot : new MakeGame().getRobots()) {
            if (robot.getImageView().getX() == x && robot.getImageView().getY() == y - getPixel())
                return false;
        }
        return true;
    }

    public boolean lu_S(double x, double y) {
        for (MotherRobot robot : new MakeGame().getRobots()) {
            if (robot.getImageView().getX() == x - getPixel() && robot.getImageView().getY() == y - getPixel())
                return false;
        }
        return true;
    }

    public boolean l_S(double x, double y) {
        for (MotherRobot robot : new MakeGame().getRobots()) {
            if (robot.getImageView().getX() == x - getPixel() && robot.getImageView().getY() == y)
                return false;
        }
        return true;
    }

    public boolean ld_S(double x, double y) {
        for (MotherRobot robot : new MakeGame().getRobots()) {
            if (robot.getImageView().getX() == x - getPixel() && robot.getImageView().getY() == y + getPixel())
                return false;
        }
        return true;
    }

    public boolean d_S(double x, double y) {
        for (MotherRobot robot : new MakeGame().getRobots()) {
            if (robot.getImageView().getX() == x && robot.getImageView().getY() == y + getPixel())
                return false;
        }
        return true;
    }

    public boolean rd_S(double x, double y) {
        for (MotherRobot robot : new MakeGame().getRobots()) {
            if (robot.getImageView().getX() == x + getPixel() && robot.getImageView().getY() == y + getPixel())
                return false;
        }
        return true;
    }

    public boolean r_S(double x, double y) {
        for (MotherRobot robot : new MakeGame().getRobots()) {
            if (robot.getImageView().getX() == x + getPixel() && robot.getImageView().getY() == y)
                return false;
        }
        return true;
    }

    public int findKind() {
        int kind = 7;
        if (getClass().getSimpleName().equals("Blue_DiagonalRobot")) {
            kind = 0;
        } else if (getClass().getSimpleName().equals("Blue_HorizontalRobot")) {
            kind = 1;
        } else if (getClass().getSimpleName().equals("Blue_VerticalRobot")) {
            kind = 2;
        } else if (getClass().getSimpleName().equals("Blue_HorseRobot")) {
            kind = 3;
        } else if (getClass().getSimpleName().equals("Red_DiagonalRobot")) {
            kind = 4;
        } else if (getClass().getSimpleName().equals("Red_HorizontalRobot")) {
            kind = 5;
        } else if (getClass().getSimpleName().equals("Red_VerticalRobot")) {
            kind = 6;
        } else if (getClass().getSimpleName().equals("Red_HorseRobot")) {
            kind = 7;
        }
        return kind;
    }
}

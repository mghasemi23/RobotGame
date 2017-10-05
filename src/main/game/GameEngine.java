package main.game;


import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.robots.*;
import main.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameEngine {
    public static MotherRobot clicked = null;
    public static int turn = 1;

    public void randomStart(Stage stage) throws IOException {
        turn = new Random(System.currentTimeMillis()).nextInt(2);
        MakeGame makeGame = new MakeGame();
        //service to make game
        makeGame.makeRandomRobots(stage, false);
    }

    public void propertiesStart(Stage stage) throws IOException {
        turn = new Random(System.currentTimeMillis()).nextInt(2);
        MakeGame makeGame = new MakeGame();
        //service to make game
        makeGame.makePropertyRobots(stage, false);
    }

    public void loadGame(Stage stage, ArrayList<MotherRobot> robots) throws IOException {
        MakeGame makeGame = new MakeGame();
        makeGame.getRobots().clear();
        makeGame.getRobots().addAll(robots);
        makeGame.setTableGroup(new Group());
        makeGame.makeTable(stage, true);
    }

    public void game(Stage stage) {
        for (Rectangle table : new MakeGame().getTable()) {
            table.setOnMouseClicked(event -> {
                try {
                    if (table.getEffect().getClass().getSimpleName().equals("ColorAdjust")) {
                        MotherRobot dead = null;
                        for (MotherRobot robot : new MakeGame().getRobots()) {
                            if (robot.getImageView().getX() == table.getX() && robot.getImageView().getY() == table.getY()) {
                                dead = robot;
                                break;
                            }
                        }
                        if (dead == null) {
                            clicked.move(table.getX(), table.getY(), false);
                            check(stage);
                        }
                        if (dead != null) {
                            if (dead.getLife() > clicked.getAttack()) {
                                clicked.moveBack(table.getX(), table.getY());
                                dead.setLife(dead.getLife() - clicked.getAttack());
                            } else if (dead.getLife() <= clicked.getAttack() && !dead.isSeparate()) {
                                clicked.move(table.getX(), table.getY(), true);
                                dead.death();
                            } else if (dead.getLife() <= clicked.getAttack() && dead.isSeparate()) {
                                dead.separate();
                                clicked.move(table.getX(), table.getY(), true);
                            }
                            check(stage);
                        }
                        clicked = null;
                    } else if (clicked != null) {
                        table.setEffect(new WrongWay());
                        wrongEffect(table);
                    }
                } catch (NullPointerException e) {
                } catch (IOException e) {
                }
            });
        }
    }

    public void wrongEffect(Rectangle rectangle) {
        Media wrongWay = new Media(String.valueOf(main.class.getResource("file/chord.wav")));
        MediaPlayer wrongPlayer = new MediaPlayer(wrongWay);
        final boolean[] grow = {false};
        final boolean[] shrink = {false};
        final int[] counter = {0};
        AnimationTimer glory_ATimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (!grow[0] && !shrink[0]) {
                    wrongPlayer.play();
                    rectangle.setWidth(rectangle.getWidth() - 5);
                    rectangle.setHeight(rectangle.getHeight() - 5);
                    rectangle.setX(rectangle.getX() + 2.5);
                    rectangle.setY(rectangle.getY() + 2.5);
                    ++counter[0];
                    if (counter[0] >= 3) {
                        shrink[0] = true;

                    }
                } else if (!grow[0] && shrink[0]) {
                    rectangle.setWidth(rectangle.getWidth() + 5);
                    rectangle.setHeight(rectangle.getHeight() + 5);
                    rectangle.setX(rectangle.getX() - 2.5);
                    rectangle.setY(rectangle.getY() - 2.5);
                    --counter[0];
                    if (counter[0] <= 0) {
                        grow[0] = true;
                    }
                }
            }
        };
        glory_ATimer.start();

    }

    public void check(Stage stage) throws IOException {
        int blue = 0;
        int red = 0;
        for (MotherRobot robot : new MakeGame().getRobots()) {
            if (robot.getClass().getSimpleName().contains("Red")) {
                if (robot.isLive())
                    ++red;
            }
            if (robot.getClass().getSimpleName().contains("Blue")) {
                if (robot.isLive())
                    ++blue;
            }
        }
        new MakeGame().redNumber.setText(String.valueOf(red));
        new MakeGame().blueNumber.setText(String.valueOf(blue));
        if (blue == 0) {
            Parent parent = FXMLLoader.load(main.class.getResource("tablePack/RedWin.fxml"));
            stage.hide();
            stage.setScene(new Scene(parent, 400, 400));
            stage.show();
        } else if (red == 0) {
            Parent parent = FXMLLoader.load(main.class.getResource("tablePack/BlueWin.fxml"));
            stage.hide();
            stage.setScene(new Scene(parent, 400, 400));
            stage.show();
        }
        new MakeGame().tooltip();
    }

    public void turn() {

        if (turn == 1) {
            turn = 0;
            new MakeGame().redTurn.setVisible(true);
            new MakeGame().blueTurn.setVisible(false);
        } else if (turn == 0) {
            turn = 1;
            new MakeGame().blueTurn.setVisible(true);
            new MakeGame().redTurn.setVisible(false);
        }
    }
}

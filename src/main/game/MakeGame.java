package main.game;

import javafx.animation.AnimationTimer;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.propertiesPack.RobotProperties;
import main.robots.*;
import main.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MakeGame {

    public static Label redNumber = new Label();
    public static Label blueNumber = new Label();
    public static Circle redTurn = new Circle(30, Color.RED);
    public static Circle blueTurn = new Circle(30, Color.BLUE);
    public static MediaPlayer gamePlayer;
    private static Button startDeploy = new Button("START");
    private static RobotProperties properties;
    private final static double pixel = 45;
    private final static int size = 16;
    private static ArrayList<MotherRobot> robots = new ArrayList<>();
    private static ArrayList<Rectangle> table = new ArrayList<>();
    private static Group redTeam;
    private static Group blueTeam;
    private static Parent emptyBlue;
    private static Parent emptyRed;
    private static Group tableGroup = null;


    public void makeTable(Stage stage, boolean load) throws IOException {
        tooltip();
        Service makeT = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        getTable().clear();
                        startDeploy.setFont(Font.font(20));
                        startDeploy.setTextFill(Color.WHITE);
                        startDeploy.setLayoutX(7 * 45 + 200);
                        startDeploy.setLayoutY(7 * 45);
                        startDeploy.setPrefWidth(90);
                        startDeploy.setPrefHeight(90);
                        startDeploy.setVisible(true);
                        startDeploy.setStyle("-fx-background-color: #04ff00;");
                        startDeploy.setOnAction(event -> {
                            startDeploy.setVisible(false);
                            redNumber.setVisible(true);
                            blueNumber.setVisible(true);
                            startEffect();
                            if (!load)
                                deploy();
                            else {
                                if (new GameEngine().turn == 1) {
                                    blueTurn.setVisible(true);
                                } else
                                    redTurn.setVisible(true);
                                GameEngine engine = new GameEngine();
                                try {
                                    engine.check(stage);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        for (int i = 0; i < size; i++) {
                            for (int j = 0; j < size; j++) {
                                Rectangle rectangle = new Rectangle(i * getPixel() + 200, j * getPixel(), getPixel(), getPixel());
                                if ((i + j) % 2 == 0)
                                    rectangle.setFill(Color.rgb(127, 127, 127));
                                else
                                    rectangle.setFill(Color.rgb(176, 176, 176));
                                rectangle.setCursor(Cursor.HAND);
                                getTable().add(rectangle);
                            }
                        }
                        BoxBlur boxBlur = new BoxBlur(47, 47, 2);
                        boxBlur.setInput(new Glow(1));

                        //red information
                        redNumber.setFont(Font.font(20));
                        redNumber.setTextFill(Color.RED);
                        redNumber.setLayoutX(130);
                        redNumber.setLayoutY(95);
                        redNumber.setVisible(false);
                        Parent redParent = FXMLLoader.load(main.class.getResource("tablePack/RedDesign.fxml"));
                        redTeam = new Group(redParent);

                        redTurn.setEffect(boxBlur);
                        redTurn.setSmooth(true);
                        redTurn.setCenterX(100);
                        redTurn.setCenterY(620);
                        redTurn.setVisible(false);

                        //blue information
                        blueNumber.setFont(Font.font(20));
                        blueNumber.setTextFill(Color.web("#001fff"));
                        blueNumber.setLayoutX(1050);
                        blueNumber.setLayoutY(95);
                        blueNumber.setVisible(false);
                        Parent blueParent = FXMLLoader.load(main.class.getResource("tablePack/BlueDesign.fxml"));
                        blueTeam = new Group(blueParent);
                        blueTeam.setLayoutX(920);
                        blueTurn.setEffect(boxBlur);
                        blueTurn.setSmooth(true);
                        blueTurn.setCenterX(1020);
                        blueTurn.setCenterY(620);
                        blueTurn.setVisible(false);

                        turnEffect();

                        Image backGround = new Image(main.class.getResourceAsStream("file/background.jpg"), 1460, 820, true, true);
                        ImageView imageViewBG = new ImageView(backGround);
                        getTableGroup().getChildren().addAll(imageViewBG, redTeam, blueTeam);
                        getTableGroup().getChildren().addAll(getTable());
                        for (MotherRobot robot : getRobots())
                            getTableGroup().getChildren().add(robot.getImageView());
                        getTableGroup().getChildren().addAll(redNumber, blueNumber, redTurn, blueTurn);
                        getTableGroup().getChildren().add(startDeploy);
                        loadEffect();
                        return null;
                    }
                };
            }
        };
        makeT.setOnSucceeded(event -> {
            Media game = new Media(String.valueOf(main.class.getResource("file/jewel of the Orient.mp3")));
            gamePlayer = new MediaPlayer(game);
            gamePlayer.setOnEndOfMedia(new Runnable() {
                public void run() {
                    gamePlayer.seek(Duration.ZERO);
                }
            });
            gamePlayer.setVolume(0.1);
            gamePlayer.play();
            Scene scene = new Scene(getTableGroup(), 1120, 720);
            stage.hide();
            stage.setScene(scene);
            stage.show();
            new GameEngine().game(stage);
        });
        makeT.restart();

    }

    public void makeRandomRobots(Stage stage, boolean load) {
        setTableGroup(new Group());
        Service make = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        getRobots().clear();
                        Random random = new Random(System.currentTimeMillis());
                        //make red robots
                        //diagonal
                        int diagonal = random.nextInt(3) + 2;
                        for (int i = 0; i < diagonal; i++) {
                            getRobots().add(new Red_DiagonalRobot(random.nextInt(10) + 1, random.nextInt(10) + 1));
                        }
                        //horizontal
                        int horizontal = random.nextInt(3) + 2;
                        for (int i = 0; i < horizontal; i++) {
                            getRobots().add(new Red_HorizontalRobot(random.nextInt(10) + 1, random.nextInt(10) + 1));
                        }
                        //vertical
                        int vertical = random.nextInt(3) + 2;
                        for (int i = 0; i < vertical; i++) {
                            getRobots().add(new Red_VerticalRobot(random.nextInt(10) + 1, random.nextInt(10) + 1));
                        }
                        //horse
                        int horse = random.nextInt(3) + 2;
                        for (int i = 0; i < horse; i++) {
                            getRobots().add(new Red_HorseRobot(random.nextInt(10) + 1, random.nextInt(10) + 1));
                        }

                        //make red robots
                        //diagonal
                        for (int i = 0; i < diagonal; i++) {
                            getRobots().add(new Blue_DiagonalRobot(random.nextInt(10) + 1, random.nextInt(10) + 1));
                        }
                        //horizontal
                        for (int i = 0; i < horizontal; i++) {
                            getRobots().add(new Blue_HorizontalRobot(random.nextInt(10) + 1, random.nextInt(10) + 1));
                        }
                        //vertical
                        for (int i = 0; i < vertical; i++) {
                            getRobots().add(new Blue_VerticalRobot(random.nextInt(10) + 1, random.nextInt(10) + 1));
                        }
                        //horse
                        for (int i = 0; i < horse; i++) {
                            getRobots().add(new Blue_HorseRobot(random.nextInt(10) + 1, random.nextInt(10) + 1));
                        }
                        redNumber.setText(String.valueOf(diagonal + vertical + horizontal + horse));
                        blueNumber.setText(String.valueOf(diagonal + vertical + horizontal + horse));
                        return null;
                    }
                };
            }
        };
        make.setOnSucceeded(event -> {
            try {
                makeTable(stage, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        make.restart();

    }

    public void makePropertyRobots(Stage stage, boolean load) throws IOException {
        setTableGroup(new Group());
        Service make = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        properties = new RobotProperties(true);
                        getRobots().clear();
                        //make red robots
                        int diagonal = properties.getDiagonalNumber();
                        for (int i = 0; i < diagonal; i++) {
                            getRobots().add(new Red_DiagonalRobot(properties.getDiagonal_A(), properties.getDiagonal_D()));
                        }
                        int horizontal = properties.getHorizontalNumber();
                        for (int i = 0; i < horizontal; i++) {
                            getRobots().add(new Red_HorizontalRobot(properties.getHorizontal_A(), properties.getHorizontal_D()));
                        }
                        int vertical = properties.getVerticalNumber();
                        for (int i = 0; i < vertical; i++) {
                            getRobots().add(new Red_VerticalRobot(properties.getVertical_A(), properties.getVertical_D()));
                        }
                        int horse = properties.getHorseNumber();
                        for (int i = 0; i < horse; i++) {
                            getRobots().add(new Red_HorseRobot(properties.getHorse_A(), properties.getHorse_D()));
                        }

                        //make red robots
                        for (int i = 0; i < diagonal; i++) {
                            getRobots().add(new Blue_DiagonalRobot(properties.getDiagonal_A(), properties.getDiagonal_D()));
                        }
                        for (int i = 0; i < horizontal; i++) {
                            getRobots().add(new Blue_HorizontalRobot(properties.getHorizontal_A(), properties.getHorizontal_D()));
                        }
                        for (int i = 0; i < vertical; i++) {
                            getRobots().add(new Blue_VerticalRobot(properties.getVertical_A(), properties.getVertical_D()));
                        }
                        for (int i = 0; i < horse; i++) {
                            getRobots().add(new Blue_HorseRobot(properties.getHorse_A(), properties.getHorse_D()));
                        }
                        redNumber.setText(String.valueOf(diagonal + vertical + horizontal + horse));
                        blueNumber.setText(String.valueOf(diagonal + vertical + horizontal + horse));
                        return null;
                    }
                };
            }
        };
        make.setOnSucceeded(event -> {
            try {
                makeTable(stage, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        make.restart();
    }

    public void loadEffect() throws IOException {
        //blue team
        blueTeam.setEffect(new BoxBlur(5, 5, 2));
        emptyBlue = FXMLLoader.load(main.class.getResource("tablePack/Empty.fxml"));
        emptyBlue.setLayoutX(920);
        //red team
        redTeam.setEffect(new BoxBlur(5, 5, 2));
        emptyRed = FXMLLoader.load(main.class.getResource("tablePack/Empty.fxml"));

        getTableGroup().getChildren().addAll(emptyBlue, emptyRed);
    }

    public void startEffect() {
        //blue team
        emptyBlue.setVisible(false);
        blueTeam.setEffect(new Glow(0));
        //red team
        redTeam.setEffect(new Glow(0));
        emptyRed.setVisible(false);
    }

    public void deploy() {
        Service deployService = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        Media deploy = new Media(String.valueOf(main.class.getResource("file/deploy.wav")));
                        MediaPlayer deployPlayer = new MediaPlayer(deploy);
                        deployPlayer.play();
                        Random random = new Random(System.currentTimeMillis());
                        getRobots().get(0).start(random.nextInt(16) * 45 + 200, random.nextInt(16) * 45);
                        boolean empty;
                        for (int i = 1; i < getRobots().size(); i++) {
                            while (true) {
                                empty = true;
                                int x = random.nextInt(16) * 45 + 200;
                                int y = random.nextInt(16) * 45;
                                for (MotherRobot robot : getRobots()) {
                                    if (robot.getImageView().getX() == x && robot.getImageView().getY() == y && robot != getRobots().get(i))
                                        empty = false;
                                }
                                if (empty) {
                                    getRobots().get(i).start(Double.valueOf(x), Double.valueOf(y));
                                    break;
                                }
                            }
                        }
                        return null;
                    }
                };
            }
        };
        deployService.setOnSucceeded(event -> {
            if (new GameEngine().turn == 1) {
                blueTurn.setVisible(true);
            } else
                redTurn.setVisible(true);
        });
        deployService.start();
    }

    public void tooltip() {
        for (MotherRobot robot : getRobots()) {
            Tooltip tooltip;
            if (robot.isSeparate())
                tooltip = new Tooltip("Attack: " + robot.getAttack() + "\n" + "Life: " + robot.getLife() + "\n" + "S");
            else
                tooltip = new Tooltip("Attack: " + robot.getAttack() + "\n" + "Life: " + robot.getLife());

            tooltip.setFont(Font.font(15));
            Tooltip.install(robot.getImageView(), tooltip);
        }
    }

    public void turnEffect() {

        AnimationTimer glory_ATimer = new AnimationTimer() {
            int maxR = 30;
            int minR = 10;
            int step = -1;

            @Override
            public void handle(long now) {
                redTurn.setRadius(redTurn.getRadius() + step * 1);
                blueTurn.setRadius(blueTurn.getRadius() + step * 1);
                if (redTurn.getRadius() > maxR || redTurn.getRadius() < minR)
                    step *= -1;
            }
        };
        glory_ATimer.start();
    }

    public static double getPixel() {
        return pixel;
    }

    public static ArrayList<Rectangle> getTable() {
        return table;
    }

    public static ArrayList<MotherRobot> getRobots() {
        return robots;
    }

    public static Group getTableGroup() {
        return tableGroup;
    }

    public static void setTableGroup(Group tableGroup) {
        MakeGame.tableGroup = tableGroup;
    }
}

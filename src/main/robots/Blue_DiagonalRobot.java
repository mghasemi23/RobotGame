package main.robots;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import main.game.GameEngine;
import main.game.MakeGame;
import main.main;

public class Blue_DiagonalRobot extends MotherRobot {

    public Blue_DiagonalRobot(int attack, int life) {
        super();
        setAttack(attack);
        setLife(life);
        setTeam(1);
        Image image = new Image(main.class.getResourceAsStream("file/diagonal_b.png"), getPixel(), getPixel(), true, true);
        ImageView temp = new ImageView(image);
        temp.setX(1000);
        temp.setY(600);
        setImageView(temp);
        getImageView().setEffect(getDropShadow());
        //set on action
        getImageView().setOnMouseClicked(event -> {
            if (GameEngine.turn == getTeam()) {
                GameEngine.clicked = this;
                ways();
            }
        });
    }


    //find ways
    @Override
    public void ways() {
        for (Rectangle a : new MakeGame().getTable())
            a.setEffect(new Glow(0));
        ru();
        rd();
        lu();
        ld();
    }

    //death animation
    @Override
    public void death() {
        this.setLive(false);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), getImageView());
        tt.setToX(2000);
        tt.setToY(0);
        tt.play();
        getDeath().play();
        tt.setOnFinished(event -> {
            getDeath().stop();
        });
    }

    //start animation
    @Override
    public void start(double x, double y) {
        PathTransition pt = new PathTransition(Duration.seconds(2), new Line(1000 + getPixel() / 2, 600 + getPixel() / 2, x + getPixel() / 2, y + getPixel() / 2), getImageView());
        pt.play();
        this.getImageView().setX(x);
        this.getImageView().setY(y);
        pt.setOnFinished(event -> {
        });
    }
}

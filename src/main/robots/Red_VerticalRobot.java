package main.robots;

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

public class Red_VerticalRobot extends MotherRobot {


    public Red_VerticalRobot(int attack, int life) {
        super();
        setAttack(attack);
        setLife(life);
        setTeam(0);
        Image image = new Image(main.class.getResourceAsStream("file/vertical_r.png"), getPixel(), getPixel(), true, true);
        ImageView temp = new ImageView(image);
        temp.setX(80);
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
        u();
        d();
    }


    //death animation
    @Override
    public void death() {
        this.setLive(false);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), getImageView());
        tt.setToX(-2000);
        tt.setToY(0);
        tt.play();
        getDeath().play();
        tt.setOnFinished(event -> {
            getDeath().stop();
            getImageView().setX(-100);
            getImageView().setY(-100);
        });
    }


    //start animation
    @Override
    public void start(double x, double y) {
        PathTransition pt = new PathTransition(Duration.seconds(2), new Line(80 + getPixel() / 2, 600 + getPixel() / 2, x + getPixel() / 2, y + getPixel() / 2), getImageView());
        pt.play();
        getImageView().setX(x);
        getImageView().setY(y);
        pt.setOnFinished(event -> {

        });
    }
}

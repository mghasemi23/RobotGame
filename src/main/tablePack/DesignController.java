package main.tablePack;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.game.MakeGame;
import main.main;
import main.robots.MotherRobot;
import main.saveAndLoad.Serialize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DesignController implements Initializable {

    @FXML
    private Label diagonal_Lbl;
    @FXML
    private Label vertical_Lbl;
    @FXML
    private Label horse_Lbl;
    @FXML
    private Label horizontal_Lbl;
    @FXML
    private JFXButton menu;
    @FXML
    private JFXButton exit;
    @FXML
    private JFXButton save;
    @FXML
    private ImageView diagonal_Iv;
    @FXML
    private ImageView horizontal_Iv;
    @FXML
    private ImageView vertical_Iv;
    @FXML
    private ImageView horse_Iv;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //install toolTip to images
        Tooltip.install(diagonal_Iv, new Tooltip("Diagonal Robot"));
        Tooltip.install(vertical_Iv, new Tooltip("Vertical Robot"));
        Tooltip.install(horizontal_Iv, new Tooltip("Horizontal Robot"));
        Tooltip.install(horse_Iv, new Tooltip("Horse Robot"));

        MakeGame makeGame = new MakeGame();
        int diagonal = 0, vertical = 0, horizontal = 0, horse = 0;
        for (MotherRobot r : makeGame.getRobots()) {
            if (r.getClass().getSimpleName().contains("DiagonalRobot")) {
                ++diagonal;
            } else if (r.getClass().getSimpleName().contains("HorizontalRobot")) {
                ++horizontal;
            } else if (r.getClass().getSimpleName().contains("VerticalRobot")) {
                ++vertical;
            } else if (r.getClass().getSimpleName().contains("HorseRobot")) {
                ++horse;
            }
        }
        diagonal_Lbl.setText(String.valueOf(diagonal / 2));
        vertical_Lbl.setText(String.valueOf(vertical / 2));
        horizontal_Lbl.setText(String.valueOf(horizontal / 2));
        horse_Lbl.setText(String.valueOf(horse / 2));
    }

    //menu set on action
    public void menuSetOnAction(ActionEvent event) {
        Media click = new Media(String.valueOf(main.class.getResource("file/click.wav")));
        MediaPlayer clickPlayer = new MediaPlayer(click);
        clickPlayer.play();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Back to menu");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Game don't be saved\n");

        ButtonType buttonTypeYes = new ButtonType("YES");
        ButtonType buttonTypeNo = new ButtonType("NO");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeYes) {
            try {
                Parent parent = FXMLLoader.load(main.class.getResource("menuPack/Menu.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                new MakeGame().gamePlayer.stop();
                stage.hide();
                new MakeGame().getTableGroup().getChildren().clear();
                new MakeGame().setTableGroup(null);
                stage.setScene(new Scene(parent));
                stage.show();
            } catch (IOException e) {
            }
        }

    }

    //save set on action
    public void saveSetOnAction(ActionEvent event) {
        Media click = new Media(String.valueOf(main.class.getResource("file/click.wav")));
        MediaPlayer clickPlayer = new MediaPlayer(click);
        clickPlayer.play();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Choosing Folder!");
        alert.setContentText("You Must Select Empty Folder To Save Game.");
        alert.showAndWait();

        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Choose save folder");
        Stage stage = new Stage();
        File directory = new File("RobotGAme.iml");
        directory = fileChooser.showDialog(stage);
        Serialize serialize = new Serialize();
        serialize.save(directory);
    }

    //exit set on action
    public void exitSetOnAction(ActionEvent event) {
        Media click = new Media(String.valueOf(main.class.getResource("file/click.wav")));
        MediaPlayer clickPlayer = new MediaPlayer(click);
        clickPlayer.play();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit game");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Game will be saved\n");

        ButtonType buttonTypeYes = new ButtonType("YES");
        ButtonType buttonTypeNo = new ButtonType("NO");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeYes) {
            //save game
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

}

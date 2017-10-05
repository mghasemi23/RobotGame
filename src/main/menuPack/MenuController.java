package main.menuPack;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.game.GameEngine;
import main.main;
import main.saveAndLoad.Serialize;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private JFXButton start;
    @FXML
    private JFXButton load;
    @FXML
    private JFXButton exit;
    @FXML
    private JFXButton help;
    @FXML
    private JFXSlider volume;

    private MediaPlayer mainMenuPlayer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Media mainMenu = new Media(String.valueOf(main.class.getResource("file/Main Menu.mp3")));
        mainMenuPlayer = new MediaPlayer(mainMenu);
        mainMenuPlayer.volumeProperty().bind(volume.valueProperty());
        mainMenuPlayer.setAutoPlay(true);
        mainMenuPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mainMenuPlayer.seek(Duration.ZERO);
            }
        });
    }

    public void startSetOnAction(ActionEvent event) throws IOException {
        Media click = new Media(String.valueOf(main.class.getResource("file/click.wav")));
        MediaPlayer clickPlayer = new MediaPlayer(click);
        clickPlayer.play();

        GameEngine gameEngine = new GameEngine();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        //show confirmation dialog
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Robot Game");
        alert.setHeaderText(null);
        alert.setContentText("Choosing your Game type:");

        ButtonType buttonTypeRandom = new ButtonType("Random");
        ButtonType buttonTypeProp = new ButtonType("Properties file");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeRandom, buttonTypeProp, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeRandom) {
            gameEngine.randomStart(stage);
            mainMenuPlayer.stop();
        } else if (result.get() == buttonTypeProp) {
            gameEngine.propertiesStart(stage);
            mainMenuPlayer.stop();
        }
    }

    public void loadSetOnAction(ActionEvent event) throws IOException {
        Media click = new Media(String.valueOf(main.class.getResource("file/click.wav")));
        MediaPlayer clickPlayer = new MediaPlayer(click);
        clickPlayer.play();

        //show alert
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Choosing Folder!");
        alert.setContentText("You Must Select Saved Game Folder.");
        alert.showAndWait();

        //opening file chooser to choose save file
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Choose save folder");
        Stage stage = new Stage();
        File directory = new File("");
        directory = fileChooser.showDialog(stage);
        Serialize serialize = new Serialize();
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            mainMenuPlayer.stop();
            serialize.load(directory, mainStage);
        } catch (NullPointerException e) {
        }
    }

    public void helpSetOnAction(ActionEvent event) {
        Media click = new Media(String.valueOf(main.class.getResource("file/click.wav")));
        MediaPlayer clickPlayer = new MediaPlayer(click);
        clickPlayer.play();

        try {
            mainMenuPlayer.stop();
            Parent helpRoot = FXMLLoader.load(main.class.getResource("helpPack/Help.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(helpRoot));
        } catch (IOException e) {

        }
    }

    public void exitSetOnAction(ActionEvent event) {
        Media click = new Media(String.valueOf(main.class.getResource("file/click.wav")));
        MediaPlayer clickPlayer = new MediaPlayer(click);
        clickPlayer.play();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit game");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure?");

        ButtonType buttonTypeYes = new ButtonType("YES");
        ButtonType buttonTypeNo = new ButtonType("NO");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeYes) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            System.exit(0);
        }
    }

}

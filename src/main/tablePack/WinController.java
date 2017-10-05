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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.main;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class WinController implements Initializable {

    @FXML
    private JFXButton back;
    @FXML
    private JFXButton exit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Media end = new Media(String.valueOf(main.class.getResource("file/And Away We Go.mp3")));
        MediaPlayer endPlayer = new MediaPlayer(end);
        endPlayer.setVolume(0.05);
        endPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                endPlayer.seek(Duration.ZERO);
            }
        });
        endPlayer.play();
    }

    public void backSetOnAction(ActionEvent event) {
        Media click = new Media(String.valueOf(main.class.getResource("file/click.wav")));
        MediaPlayer clickPlayer = new MediaPlayer(click);
        clickPlayer.play();

        try {
            Parent parent = FXMLLoader.load(main.class.getResource("menuPack/Menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.hide();
            stage.setScene(new Scene(parent));
            stage.show();
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

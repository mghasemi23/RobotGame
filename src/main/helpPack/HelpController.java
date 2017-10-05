package main.helpPack;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import main.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    @FXML
    private JFXButton back;
    @FXML
    private Label diagonal_Lbl;
    @FXML
    private Label vertical_Lbl;
    @FXML
    private Label horizontal_Lbl;
    @FXML
    private Label horse_Lbl;
    @FXML
    private Label help_Lbl;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        diagonal_Lbl.setText("Diagonal:\n" +
                "This robot move like Bishop pieces in chess.\n" +
                "it's attack and life is random\n" +
                "or read from file.");
        horizontal_Lbl.setText("Horizontal:\n" +
                "This robot move horizontal in table.\n" +
                "it's attack and life is random\n" +
                "or read from file.");
        vertical_Lbl.setText("Vertical:\n" +
                "This robot move vertical in table.\n" +
                "it's attack and life is random\n" +
                "or read from file.");
        horse_Lbl.setText("Horse:\n" +
                "This robot move like Knight pieces in chess.\n" +
                "it's attack and life is random\n" +
                "or read from file.");
        help_Lbl.setText("Help:\n" +
                "in this game you play as Blue or Red team\n" +
                "your nuts randomly place in table\n" +
                "in your turn you can move one of your\n" +
                "nuts.\n" +
                "in game every nut have a attack\n" +
                "and life.\n" +
                "for move you must click on one of your\n" +
                "nuts then click where you want go\n" +
                "if one team lose all of the nuts\n" +
                "this team will lose Game\n" +
                "\n" +
                "Credit:\n" +
                "Programmer:\n" +
                "M.Ghasemi\n" +
                "\n" +
                "Special Thank to:\n" +
                "Michael McCann for Musics\n" +
                "and M.Ashourloo\n" +
                "\n" +
                "M.GH Entertainment 2016\n" +
                "ver. 1.0\n" +
                "\n");
    }


    public void backSetOnAction(ActionEvent event) {
        Media click = new Media(String.valueOf(main.class.getResource("file/click.wav")));
        MediaPlayer clickPlayer = new MediaPlayer(click);
        clickPlayer.play();
        try {
            Parent menuRoot = FXMLLoader.load(main.class.getResource("menuPack/Menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(menuRoot));
        } catch (IOException e) {

        }
    }
}

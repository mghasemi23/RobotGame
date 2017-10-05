package main;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.saveAndLoad.Serialize;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menuPack/Menu.fxml"));
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();

        //make stage to save file
        final BooleanProperty ignoreCloseRequest = new SimpleBooleanProperty(false);
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event) {
                if (event.getCode() == KeyCode.F4 && event.isAltDown()) {
                    event.consume();
                    ignoreCloseRequest.set(true);
                }
            }
        });
        primaryStage.setOnCloseRequest(event -> {
                    if (primaryStage.getScene().getHeight() == 720) {
                        Serialize serialize = new Serialize();
                        serialize.quickSave();
                    }
                    if (ignoreCloseRequest.get()) {
                        event.consume();
                        ignoreCloseRequest.set(false);
                    }
                }
        );
        File defDir = new File("QuickSave");
        File[] files = defDir.listFiles();
        if (files.length > 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Load Game");
            alert.setHeaderText("You Have A Unfinished Game");
            alert.setContentText("Do you want load this game?");

            ButtonType buttonTypeYes = new ButtonType("YES");
            ButtonType buttonTypeNo = new ButtonType("NO");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                Serialize serialize = new Serialize();
                serialize.quickLoad(primaryStage);
            } else {
                Thread deleteThread = new Thread() {
                    @Override
                    public void run() {
                        for (File file : files)
                            file.delete();
                    }
                };
                deleteThread.run();
            }
        }
    }
}

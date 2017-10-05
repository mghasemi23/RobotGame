package main.saveAndLoad;

import javafx.stage.Stage;
import main.game.GameEngine;
import main.game.MakeGame;
import main.robots.MotherRobot;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Serialize {
    public static ArrayList<MotherRobot> robots = new ArrayList<>();

    public void save(File directory) {
        try {
            if (directory.getName().equals("RobotGame.iml"))
                return;
        } catch (NullPointerException e) {
            return;
        }

        for (MotherRobot robot : new MakeGame().getRobots()) {
            SaveAndLoadClass temp = new SaveAndLoadClass(robot);
            try {
                FileOutputStream fileOut = new FileOutputStream(directory + "/" + System.currentTimeMillis() + ".ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(temp);
                out.close();
                fileOut.close();
            } catch (IOException i) {
                i.printStackTrace();
            }

        }
    }

    public void load(File directory, Stage stage) throws IOException {
        robots.clear();
        GameEngine gameEngine = new GameEngine();
        File[] files = directory.listFiles();
        for (File loadFile : files) {
            SaveAndLoadClass temp = null;
            try {
                FileInputStream fileIn = new FileInputStream(loadFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                temp = (SaveAndLoadClass) in.readObject();
                in.close();
                fileIn.close();
            } catch (IOException i) {
            } catch (ClassNotFoundException c) {
            }
            robots.add(temp.getRobot());
        }
        gameEngine.loadGame(stage, robots);
    }

    public void quickSave() {
        File directory = new File("QuickSave");
        save(directory);
    }

    public void quickLoad(Stage stage) throws IOException {
        File directory = new File("QuickSave");
        robots.clear();
        GameEngine gameEngine = new GameEngine();
        File[] files = directory.listFiles();
        for (File loadFile : files) {
            SaveAndLoadClass temp = null;
            try {
                FileInputStream fileIn = new FileInputStream(loadFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                temp = (SaveAndLoadClass) in.readObject();
                in.close();
                fileIn.close();
            } catch (IOException i) {
            } catch (ClassNotFoundException c) {
            }
            robots.add(temp.getRobot());
        }
        Thread deleteThread = new Thread() {
            @Override
            public void run() {
                for (File file : files)
                    file.delete();
            }
        };
        deleteThread.run();
        gameEngine.loadGame(stage, robots);
    }
}

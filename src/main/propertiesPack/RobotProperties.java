package main.propertiesPack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RobotProperties {

    private InputStream inputStream;
    private static int Horizontal_A;
    private static int Horizontal_D;
    private static int Vertical_A;
    private static int Vertical_D;
    private static int Diagonal_A;
    private static int Diagonal_D;
    private static int Horse_A;
    private static int Horse_D;
    private static int VerticalNumber;
    private static int HorizontalNumber;
    private static int HorseNumber;
    private static int DiagonalNumber;

    public RobotProperties(boolean True) throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "main/propertiesPack/Robots.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value
            //Horizontal
            HorizontalNumber = Integer.parseInt(prop.getProperty("HorizontalNumber"));
            Horizontal_A = Integer.parseInt(prop.getProperty("HorizontalAttack"));
            Horizontal_D = Integer.parseInt(prop.getProperty("HorizontalDefence"));
            //vertical
            VerticalNumber = Integer.parseInt(prop.getProperty("VerticalNumber"));
            Vertical_A = Integer.parseInt(prop.getProperty("VerticalAttack"));
            Vertical_D = Integer.parseInt(prop.getProperty("VerticalDefence"));
            //diagonal
            DiagonalNumber = Integer.parseInt(prop.getProperty("DiagonalNumber"));
            Diagonal_A = Integer.parseInt(prop.getProperty("DiagonalAttack"));
            Diagonal_D = Integer.parseInt(prop.getProperty("DiagonalDefence"));
            //horse
            HorseNumber = Integer.parseInt(prop.getProperty("HorseNumber"));
            Horse_A = Integer.parseInt(prop.getProperty("HorseAttack"));
            Horse_D = Integer.parseInt(prop.getProperty("HorseDefence"));


        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

    public RobotProperties() {
    }

    public int getHorizontal_A() {
        return Horizontal_A;
    }

    public int getHorizontal_D() {
        return Horizontal_D;
    }

    public int getVertical_A() {
        return Vertical_A;
    }

    public int getVertical_D() {
        return Vertical_D;
    }

    public int getDiagonal_A() {
        return Diagonal_A;
    }

    public int getDiagonal_D() {
        return Diagonal_D;
    }

    public int getHorse_A() {
        return Horse_A;
    }

    public int getHorse_D() {
        return Horse_D;
    }

    public int getVerticalNumber() {
        return VerticalNumber;
    }

    public int getHorizontalNumber() {
        return HorizontalNumber;
    }

    public int getHorseNumber() {
        return HorseNumber;
    }

    public int getDiagonalNumber() {
        return DiagonalNumber;
    }
}


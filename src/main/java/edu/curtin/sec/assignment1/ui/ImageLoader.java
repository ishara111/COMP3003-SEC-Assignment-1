package edu.curtin.sec.assignment1.ui;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageLoader {
    private Image robot1;
    private Image robot2;
    private Image robot3;
    private Image wall;
    private Image broken_wall;
    private Image citidel;
    private Image cross;

    List<Image> images = new ArrayList<>();
    String[] imageFiles = {"citidel.png", "robot1.png", "robot2.png", "robot3.png", "wall.png", "broken_wall.png", "cross.png"};

    Image[] robotImages;


    public ImageLoader() {
        loadImages();
    }

private void loadImages(){

    for (String imageFile : imageFiles) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(imageFile)) {
            if (is == null) {
                throw new AssertionError("Cannot find image file " + imageFile);
            }
            images.add(new Image(is));
        } catch (IOException e) {
            throw new AssertionError("Cannot load image file " + imageFile, e);
        }
    }

    citidel=images.get(0);
    robot1=images.get(1);
    robot2=images.get(2);
    robot3=images.get(3);
    wall=images.get(4);
    broken_wall=images.get(5);
    cross=images.get(6);

    robotImages = new Image[]{robot1, robot2, robot3};
}

    public Image getRandomRobot() {
        Random random = new Random();
        int randomVal = random.nextInt(robotImages.length);
        return robotImages[randomVal];
    }


    public Image getWall() {
        return wall;
    }

    public Image getBroken_wall() {
        return broken_wall;
    }

    public Image getCitidel() {
        return citidel;
    }

    public Image getCross() {
        return cross;
    }
}

/*********************************
 * Name: Ishara Gomes
 * ID: 20534521
 * CLass Name: ImageLoader (used to load all the images)
 *********************************/
package edu.curtin.sec.assignment1.ui;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageLoader {
    private Image wall;
    private Image brokenWall;
    private Image citidel;
    private Image cross;

    private List<Image> images = new ArrayList<>();
    private String[] imageFiles = {"citidel.png", "robot1.png", "robot2.png", "robot3.png", "wall.png", "broken_wall.png", "cross.png"};

    private Image[] robotImages;


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
    Image robot1 = images.get(1);
    Image robot2=images.get(2);
    Image robot3=images.get(3);
    wall=images.get(4);
    brokenWall =images.get(5);
    cross=images.get(6);

    robotImages = new Image[]{robot1, robot2, robot3};
}

    public Image getRandomRobot() { //returns a random robot image
        Random random = new Random();
        int randomVal = random.nextInt(robotImages.length);
        return robotImages[randomVal];
    }


    public Image getWall() {
        return wall;
    }

    public Image getBrokenWall() {
        return brokenWall;
    }

    public Image getCitidel() {
        return citidel;
    }

    public Image getCross() {
        return cross;
    }
}

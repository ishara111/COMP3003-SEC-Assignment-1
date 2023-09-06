package edu.curtin.sec.assignment1.robot;

import javafx.scene.image.Image;


public class Robot {
    private int id;
    private double x;
    private double y;
    private int delay;
    private Image image;

    public Robot(int id, double x, double y,int delay, Image image) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.delay = delay;
        this.image = image;
    }

    public int getId() {
        return id;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDelay() {
        return delay;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

}

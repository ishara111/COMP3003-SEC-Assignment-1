/*********************************
 * Name: Ishara Gomes
 * ID: 20534521
 * CLass Name: Robot (will be used to keep track of robot details)
 *********************************/
package edu.curtin.sec.assignment1.robot;

import javafx.scene.image.Image;


public class Robot {
    private int id;
    private double currX;
    private double currY;
    private double moveX;
    private double moveY;
    private double nextX;
    private double nextY;
    private int delay;
    private Image image;

    public Robot(int id, double currX, double currY, int delay, Image image) {
        this.id = id;
        this.currX = currX;
        this.currY = currY;
        this.delay = delay;
        this.image = image;
        this.nextX = currX;
        this.nextY = currY;
    }

    public int getId() {
        return id;
    }

    public int getDelay() {
        return delay;
    }

    public double getCurrX() {
        return currX;
    }

    public void setCurrX(double currX) {
        this.currX = currX;
    }

    public double getCurrY() {
        return currY;
    }

    public void setCurrY(double currY) {
        this.currY = currY;
    }

    public double getMoveX() {
        return moveX;
    }

    public void setMoveX(double moveX) {
        this.moveX = moveX;
    }

    public double getMoveY() {
        return moveY;
    }

    public void setMoveY(double moveY) {
        this.moveY = moveY;
    }

    public double getNextX() {
        return nextX;
    }

    public void setNextX(double nextX) {
        this.nextX = nextX;
    }

    public double getNextY() {
        return nextY;
    }

    public void setNextY(double nextY) {
        this.nextY = nextY;
    }

    public Image getImage() {
        return image;
    }

}

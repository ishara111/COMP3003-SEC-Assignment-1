package edu.curtin.sec.assignment1.robot;

public class Robot {
    private int id;
    private double x;
    private double y;
    private int delay;

    public Robot(int id, double x, double y,int delay) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.delay = delay;
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
}

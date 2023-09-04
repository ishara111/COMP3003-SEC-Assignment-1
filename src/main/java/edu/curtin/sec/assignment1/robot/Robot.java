package edu.curtin.sec.assignment1.robot;

public class Robot {
    private int id;
    private double x;
    private double y;

    public Robot(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
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
}

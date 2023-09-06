package edu.curtin.sec.assignment1.robot;

import edu.curtin.sec.assignment1.ui.JFXArena;
import edu.curtin.sec.assignment1.wall.PlaceWall;

import java.util.LinkedList;
import java.util.List;

public class RobotMovement implements Runnable{

    private JFXArena arena;
    private RobotSpawn robotSpawn;
    private PlaceWall wall;
    private Robot robot;
    private double currentX = 0;
    private double currentY = 0;

    private List<Robot> roboList;

    public RobotMovement(JFXArena arena, RobotSpawn robotSpawn, PlaceWall wall, Robot robot) {
        this.arena = arena;
        this.robotSpawn = robotSpawn;
        this.wall = wall;
        this.robot = robot;
        this.roboList = new LinkedList<>();
    }
    public RobotMovement(JFXArena arena, RobotSpawn robotSpawn, Robot robot) {
        this.arena = arena;
        this.robotSpawn = robotSpawn;
        //this.wall = wall;
        this.robot = robot;
        this.roboList = new LinkedList<>();
    }
    private boolean isInsideGrid(double x, double y) {
        // Check if the position is within bounds and there's no obstacle
        return x >= 0 && x < 9.0 && y >= 0 && y < 9.0;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            double dx = 4.0 - robot.getX();
            double  dy = 4.0 - robot.getY();

            //if (isInsideGrid(currentX + (dx > 0 ? 1 : (dx < 0 ? -1 : 0)), currentY + (dy > 0 ? 1 : (dy < 0 ? -1 : 0)))) {
                // Update the position only if it's obstacle-free
                for (int i = 0; i < 10; i++) {
                    currentX += dx > 0 ? 0.1 : (dx < 0 ? -0.1 : 0);
                    currentY += dy > 0 ? 0.1 : (dy < 0 ? -0.1 : 0);

                    System.out.println("Animal moved to position (" + currentX + ", " + currentY + ")");

                    robot.setX(currentX);
                    robot.setY(currentY);

                    arena.requestLayout();

                    try {
                        Thread.sleep(40); // Sleep for 40 milliseconds
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                //arena.drawRobot(currentX,currentY,robot.getId());
//            } else {
//                //System.out.println("Animal blocked at position (" + currentX + ", " + currentY + ")");
//            }

            try {
                Thread.sleep(robot.getDelay());
            } catch (InterruptedException e) {
                System.out.println("Goodbye robot !!!!");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

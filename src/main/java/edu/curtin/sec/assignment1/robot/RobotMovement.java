package edu.curtin.sec.assignment1.robot;

import edu.curtin.sec.assignment1.App;
import edu.curtin.sec.assignment1.ui.JFXArena;
import edu.curtin.sec.assignment1.wall.PlaceWall;

import java.util.LinkedList;
import java.util.List;

public class RobotMovement implements Runnable{

    private JFXArena arena;
    private App app;
    private Robot robot;
    private double currentX = 0;
    private double currentY = 0;

    private List<Robot> roboList;

    public RobotMovement(App app, JFXArena arena, Robot robot) {
        this.arena = arena;
       // this.robotSpawn = robotSpawn;
        this.app = app;
        this.robot = robot;
        this.roboList = new LinkedList<>();
    }
//    public RobotMovement(JFXArena arena, RobotSpawn robotSpawn, Robot robot) {
//        this.arena = arena;
//        this.robotSpawn = robotSpawn;
//        //this.wall = wall;
//        this.robot = robot;
//        this.roboList = new LinkedList<>();
//    }
    private boolean isInsideGrid(double x, double y) {
        // Check if the position is within bounds and there's no obstacle
        return x >= 0 && x < 9.0 && y >= 0 && y < 9.0;
    }
    private boolean isRobotThere(double x, double y) throws InterruptedException {
        // Check if the position is within bounds and there's no obstacle
        app.getRobotSpawn().updateBlockingQueue();
        for (Robot robot : app.getRobotSpawn().getRobotBlockingQueue().take()) {
            //System.out.println("entered llop");
            if ((x == robot.getCurrX() || x == robot.getNextX()) && (y == robot.getCurrY() || y == robot.getNextY())) {
                System.out.println("in robot "+robot.getId()+" something found at curr"+robot.getCurrX()+" "+robot.getCurrY());
                System.out.println("in robot "+robot.getId()+" something found at nextr"+robot.getNextX()+" "+robot.getNextY());
                return true;
            }
        }
        //System.out.println("outside loop false");
        return false;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            double dx = 4.0 - robot.getCurrX();
            double  dy = 4.0 - robot.getCurrY();

            currentX += dx > 0 ? 1 : (dx < 0 ? -1 : 0);
            currentY += dy > 0 ? 1 : (dy < 0 ? -1 : 0);



            try {
                if (!isRobotThere(currentX, currentY )) {
                    robot.setNextX(currentX);
                    robot.setNextY(currentY);
                     //Update the position only if it's obstacle-free
                    double moveX = robot.getMoveX();
                    double moveY = robot.getMoveY();
                    for (int i = 0; i < 10; i++) {
                        moveX += dx > 0 ? 0.1 : (dx < 0 ? -0.1 : 0);
                        moveY += dy > 0 ? 0.1 : (dy < 0 ? -0.1 : 0);

                        robot.setMoveX(moveX);
                        robot.setMoveY(moveY);

                        //System.out.println("robot moved to position (" + currentX + ", " + currentY + ")");

                        arena.requestLayout();

                        try {
                            Thread.sleep(40); // Sleep for 40 milliseconds
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    robot.setCurrX(currentX);
                    robot.setCurrY(currentY);
                    //arena.drawRobot(currentX,currentY,robot.getId());
                } else {
                    //System.out.println("Animal blocked at position (" + currentX + ", " + currentY + ")");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

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

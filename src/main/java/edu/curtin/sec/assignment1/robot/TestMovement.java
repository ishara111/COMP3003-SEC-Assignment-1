package edu.curtin.sec.assignment1.robot;

import edu.curtin.sec.assignment1.App;
import edu.curtin.sec.assignment1.ui.JFXArena;
import edu.curtin.sec.assignment1.wall.PlaceWall;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TestMovement implements Runnable {

    private JFXArena arena;
    private App app;
    private Robot robot;
    private double currentX = 0;
    private double currentY = 0;

    private double moveX = 0;
    private double moveY = 0;


    private List<Robot> roboList;

    private Random random = new Random();

    public TestMovement(App app, JFXArena arena, Robot robot) {
        this.arena = arena;
        // this.robotSpawn = robotSpawn;
        this.app = app;
        this.robot = robot;
        this.roboList = new LinkedList<>();
    }

    private boolean isInsideGrid(double x, double y) {
        // Check if the position is within bounds and there's no obstacle
        return x >= 0 && x < 9.0 && y >= 0 && y < 9.0;
    }

    private boolean isRobotThere(double x, double y) {
        // Check if the position is within bounds and there's no obstacle
        try {
            app.getRobotSpawn().updateBlockingQueue();
            for (Robot robot : app.getRobotSpawn().getRobotBlockingQueue().take()) {
                //System.out.println("entered llop");
                if ((x == robot.getCurrX() || x == robot.getNextX()) && (y == robot.getCurrY() || y == robot.getNextY())) {
                    System.out.println("in robot " + robot.getId() + " something found at curr" + robot.getCurrX() + " " + robot.getCurrY());
                    System.out.println("in robot " + robot.getId() + " something found at nextr" + robot.getNextX() + " " + robot.getNextY());
                    return true;
                }
            }
            //System.out.println("outside loop false");
            return false;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            double currentX = robot.getCurrX();
            double currentY = robot.getCurrY();
            moveX = robot.getCurrX();
            moveY = robot.getCurrY();

            double dx = 4.0 - currentX;
            double dy = 4.0 - currentY;

            // Calculate the new position based on the direction towards (4, 4)
                if (Math.abs(dx) > Math.abs(dy)) {
                    // Move horizontally

                    currentX += (dx > 0) ? 1 : -1;

                } else {
                    // Move vertically

                    currentY += (dy > 0) ? 1 : -1;

                }
                currentX = Math.max(0.0, Math.min(8.0, currentX));
                currentY = Math.max(0.0, Math.min(8.0, currentY));

            for (int i = 0; i < 10; i++) {

                if (Math.abs(dx) > Math.abs(dy)) {
                    // Move horizontally

                    moveX += (dx > 0) ? 0.1 : -0.1;

                } else {
                    // Move vertically

                    moveY += (dy > 0) ? 0.1 : -0.1;

                }

                moveX = Math.max(0.0, Math.min(8.0, moveX));
                moveY = Math.max(0.0, Math.min(8.0, moveY));

                System.out.println(moveX+" "+moveY);
                robot.setMoveX(moveX);
                robot.setMoveY(moveY);
                arena.requestLayout();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }


            robot.setCurrX(currentX);
            robot.setCurrY(currentY);


            if(robot.getCurrX()==4.0 && robot.getCurrY()==4.0)
            {
                arena.drawCross(4.0,4.0); //fix sometimes not working
                app.stopThreads();
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

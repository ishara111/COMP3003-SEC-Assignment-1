package edu.curtin.sec.assignment1.robot;

import edu.curtin.sec.assignment1.App;
import edu.curtin.sec.assignment1.ui.JFXArena;
import edu.curtin.sec.assignment1.wall.PlaceWall;
import edu.curtin.sec.assignment1.wall.Wall;
import javafx.application.Platform;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RobotMovement implements Runnable {

    private JFXArena arena;
    private App app;
    private Robot robot;
    private double moveX = 0;
    private double moveY = 0;
    private Random random = new Random();

    public RobotMovement(App app, JFXArena arena, Robot robot) {
        this.arena = arena;
        this.app = app;
        this.robot = robot;
    }

    private boolean isInsideGrid(double x, double y) {
        // Check if the position is within bounds and there's no obstacle
        return x >= 0 && x < 9.0 && y >= 0 && y < 9.0;
    }

    private void killRobot() throws InterruptedException {
        app.getRobotSpawn().updateBlockingQueue();
        List<Robot>robots = app.getRobotSpawn().getRobotBlockingQueue().take();
        robots.remove(robot);
        app.getScore().robotKilled();
        Platform.runLater(() -> {
        app.getLogger().appendText("Robot "+robot.getId()+" destroyed\n");
        });
    }
    private boolean isRobotThere(double x, double y) {
        // Check if the position is within bounds and there's no obstacle
        try {
            app.getRobotSpawn().updateBlockingQueue();
            for (Robot robot : app.getRobotSpawn().getRobotBlockingQueue().take()) {
                //System.out.println("entered llop");
                if ((x == robot.getCurrX() || x == robot.getNextX()) && (y == robot.getCurrY() || y == robot.getNextY())) {
                    // System.out.println("in robot " + robot.getId() + " something found at curr" + robot.getCurrX() + " " + robot.getCurrY());
                    //System.out.println("in robot " + robot.getId() + " something found at nextr" + robot.getNextX() + " " + robot.getNextY());
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

            double nextX = robot.getCurrX();
            double nextY = robot.getCurrY();
            moveX = robot.getCurrX();
            moveY = robot.getCurrY();

            double dx = 4.0 - nextX;
            double dy = 4.0 - nextY;

            double ranNum = random.nextInt(2);

            // Calculate the new position based on the direction towards (4, 4)
            if (ranNum==1.0) {
                // Move horizontally

                nextX += (dx > 0) ? 1 : -1;

            } else {
                // Move vertically

                nextY += (dy > 0) ? 1 : -1;

            }
            nextX = Math.max(0.0, Math.min(8.0, nextX));
            nextY = Math.max(0.0, Math.min(8.0, nextY));

//                robot.setNextX(nextX);
//                robot.setNextY(nextY);

            if(!isRobotThere(nextX,nextY))
            {
                for (int i = 0; i < 10; i++) {

                    if (ranNum==1.0) {
                        // Move horizontally

                        moveX += (dx > 0) ? 0.1 : -0.1;

                    } else {
                        // Move vertically

                        moveY += (dy > 0) ? 0.1 : -0.1;

                    }

                    moveX = Math.max(0.0, Math.min(8.0, moveX));
                    moveY = Math.max(0.0, Math.min(8.0, moveY));

                    //System.out.println(moveX+" "+moveY);
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


                robot.setCurrX(nextX);
                robot.setCurrY(nextY);


                if(robot.getCurrX()==4.0 && robot.getCurrY()==4.0)
                {
                    arena.drawCross(4.0,4.0); //fix sometimes not working
                    app.stopThreads();
                }


                try {
                    app.getWall().updateWallBlockingQueues();
                    for (Wall wall:app.getWall().getWallBlockingQueue().take()) {

                        if (wall.x == robot.getCurrX() && wall.y == robot.getCurrY())
                        {
                            killRobot();
                            app.getWall().wallBroken(wall);
                            arena.requestLayout();

                            Thread.currentThread().interrupt();
                        }
                    }

                    for (Wall wall:app.getWall().getBrokenWallBlockingQueue().take()) {

                        if (wall.x == robot.getCurrX() && wall.y == robot.getCurrY())
                        {
                            killRobot();
                            app.getWall().removeBrokenWall(wall);
                            arena.requestLayout();

                            Thread.currentThread().interrupt();
                        }
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            try {
                Thread.sleep(robot.getDelay());
            } catch (InterruptedException e) {
                System.out.println("Goodbye robot "+robot.getId());
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

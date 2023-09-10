/*********************************
 * Name: Ishara Gomes
 * ID: 20534521
 * CLass Name: RobotMovement (Runnable class which will be a thread created by threadpool and responsible for movement)
 *********************************/
package edu.curtin.sec.assignment1.robot;

import edu.curtin.sec.assignment1.App;
import edu.curtin.sec.assignment1.ui.JFXArena;
import edu.curtin.sec.assignment1.wall.Wall;
import javafx.application.Platform;

import java.util.List;
import java.util.Random;

public class RobotMovement implements Runnable {

    private JFXArena arena;
    private App app;
    private Robot robot;
    private Random random = new Random();

    public RobotMovement(App app, JFXArena arena, Robot robot) {
        this.arena = arena;
        this.app = app;
        this.robot = robot;
    }

//    private boolean isInsideGrid(double x, double y) {
//        // Check if the position is within bounds and there's no obstacle
//        return x >= 0 && x < 9.0 && y >= 0 && y < 9.0;
//    }

    private void removeRobotFromList() throws InterruptedException {//removes robot from list inblockingqueue
        app.getRobotSpawn().updateBlockingQueue();
        List<Robot>robots = app.getRobotSpawn().getRobotBlockingQueue().take();
        robots.remove(robot);
    }
    private void killRobot() throws InterruptedException { //used to destry robot and increase score by 100
        removeRobotFromList();
        app.getScore().robotKilled();
        Platform.runLater(() -> {
        app.getLogger().appendText("Robot "+robot.getId()+" destroyed\n");
        });
    }
    private boolean isRobotThere(double x, double y) {//checks for robot collisions

        try {
            app.getRobotSpawn().updateBlockingQueue();
            for (Robot robot : app.getRobotSpawn().getRobotBlockingQueue().take()) {

                if ((x == robot.getCurrX() || x == robot.getNextX()) && (y == robot.getCurrY() || y == robot.getNextY())) {

                    return true;
                }
            }

            return false;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void run() { //runs in thread for each robot it will move with collsion and game end in mind
        while (!Thread.currentThread().isInterrupted()) {

            double nextX = robot.getCurrX();
            double nextY = robot.getCurrY();
            double moveX = robot.getCurrX();
            double moveY = robot.getCurrY();

            double dx = 4.0 - nextX;
            double dy = 4.0 - nextY;

            double ranNum = random.nextInt(2);


            if (ranNum==1.0) {
                // Move horizontally

                nextX += (dx > 0) ? 1 : -1;

            } else {
                // Move vertically

                nextY += (dy > 0) ? 1 : -1;

            }

            nextX = Math.max(0.0, Math.min(8.0, nextX));
            nextY = Math.max(0.0, Math.min(8.0, nextY));

            nextX = Math.round(nextX* 10.0) /10.0;
            nextY = Math.round(nextY* 10.0) /10.0;

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

                    moveX = Math.round(moveX* 10.0) /10.0;
                    moveY = Math.round(moveY* 10.0) /10.0;
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


                robot.setCurrX(nextX);//after moving set new coords as the cuurent coords
                robot.setCurrY(nextY);


                if(robot.getCurrX()==4.0 && robot.getCurrY()==4.0)//check if robot is at citidel
                {
                    try {
                        removeRobotFromList();
                        arena.requestLayout();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    app.endGame();
                }


                try {
                    app.getWall().updateWallBlockingQueues();// checks if robot has hit a wall or broken wall
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
                Thread.sleep(robot.getDelay());//sleeps for random asigned time
            } catch (InterruptedException e) {
                System.out.println("robot "+robot.getId()+" interrupted");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

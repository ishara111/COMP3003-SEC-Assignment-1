package edu.curtin.sec.assignment1.robot;

import edu.curtin.sec.assignment1.ui.JFXArena;
import javafx.application.Platform;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class RobotSpawn implements Runnable{

    private JFXArena arena;
    private int roboCount;

    private List<Robot> roboList = new LinkedList<>();

                ExecutorService es3 = new ThreadPoolExecutor(
                    1, 81, // Minimum 4 threads, maximum 8.
                    3, TimeUnit.SECONDS, // Destroy excess idle threads after 15 seconds.
                    new SynchronousQueue<>() // Used to deliver new tasks to the threads.
            );

    public RobotSpawn(JFXArena arena) {
        this.arena = arena;
        this.roboCount = 0;
    }

    private int getRandomDelay()
    {
        return ThreadLocalRandom.current().nextInt(500, 2000 + 1);

    }
    private double[] getRandomSpawn(){
        double x=0;
        double y=0;
        int randomSpawn = ThreadLocalRandom.current().nextInt(4);

        switch (randomSpawn) {
            case 0 -> {
                // Top-Left Corner
                x = 0;
                y = 0;
            }
            case 1 -> {
                // Top-Right Corner
                x = 8;
                y = 0;
            }
            case 2 -> {
                // Bottom-Left Corner
                x = 0;
                y = 8;
            }
            case 3 -> {
                // Bottom-Right Corner
                x = 8;
                y = 8;
            }
        }
        //System.out.println("Randomly selected corner: (" + x + ", " + y + ")");
        return new double[]{x,y};
    }

    private boolean canSpawn(double x,double y)
    {
        for (Robot robot:roboList) {
            if (robot.getX()==x && robot.getY()==y)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        while (true) {

            double[] spawn = getRandomSpawn();


            //System.out.println("Random delay: (" +getRandomDelay()+ ")");
            if(canSpawn(spawn[0],spawn[1]))
            {
                roboCount++;

                Platform.runLater(() -> {

                    this.arena.drawRobot(spawn[0],spawn[1],roboCount);
                    roboList.add(new Robot(roboCount,spawn[0],spawn[1],getRandomDelay()));
                });
            }


            //es3.submit(new RobotMovement());
            //System.out.println(getScore());



            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println("Goodbye robot spawn!!!!");
                break;
            }
        }
    }
}

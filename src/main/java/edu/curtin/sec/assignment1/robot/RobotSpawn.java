package edu.curtin.sec.assignment1.robot;

import edu.curtin.sec.assignment1.ui.JFXArena;
import edu.curtin.sec.assignment1.wall.Wall;
import javafx.application.Platform;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class RobotSpawn implements Runnable{

    private BlockingQueue<List<Robot>> robotBlockingQueue = new ArrayBlockingQueue<>(1);
    private ExecutorService robotThreadPool;
    private JFXArena arena;
    private int roboCount;

    private List<Robot> roboList = new LinkedList<>();


    public RobotSpawn(JFXArena arena,ExecutorService robotThreadPool) {
        this.arena = arena;
        this.roboCount = 0;
        this.robotThreadPool = robotThreadPool;
    }

    public BlockingQueue<List<Robot>> getRobotBlockingQueue()
    {
        return robotBlockingQueue;
    }

    public void updateBlockingQueue() throws InterruptedException {
        List<Robot> oldList = robotBlockingQueue.poll();
        robotBlockingQueue.put(roboList);
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
        while (!Thread.currentThread().isInterrupted()) {

            double[] spawn = getRandomSpawn();

            while(!Thread.currentThread().isInterrupted() && !(canSpawn(spawn[0],spawn[1])) && !(roboCount==0))
            {
                spawn = getRandomSpawn();
            }
            System.out.println("Random delay: hello");
            if(canSpawn(spawn[0],spawn[1]))
            {
                roboCount++;

                double[] finalSpawn = spawn;
                Platform.runLater(() -> {

                    this.arena.drawRobot(finalSpawn[0], finalSpawn[1],roboCount);
                    Robot robot = new Robot(roboCount, finalSpawn[0], finalSpawn[1],getRandomDelay(),
                            arena.getImageLoader().getRandomRobot());
                    roboList.add(robot);

//                    try {
//                        robotBlockingQueue.poll();
//                        robotBlockingQueue.put(roboList);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                    robotThreadPool.submit(new RobotMovement(arena,this,robot));
                });
            }


            //es3.submit(new RobotMovement());
            //System.out.println(getScore());



            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println("Goodbye robot spawn!!!!");
                Thread.currentThread().interrupt(); // Restore the interrupted status
                break;
            }
        }
    }
}

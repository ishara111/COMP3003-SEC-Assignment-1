/*********************************
 * Name: Ishara Gomes
 * ID: 20534521
 * CLass Name: RobotSpawn (Runnable class which will spawn a robot every 1.5secs and add new robot to threadpool)
 *********************************/
package edu.curtin.sec.assignment1.robot;

import edu.curtin.sec.assignment1.App;
import edu.curtin.sec.assignment1.ui.JFXArena;
import javafx.application.Platform;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class RobotSpawn implements Runnable{

    private BlockingQueue<List<Robot>> robotBlockingQueue = new ArrayBlockingQueue<>(1);
    private ExecutorService robotThreadPool;
    private JFXArena arena;
    private int roboCount;
    private App app;

    private List<Robot> roboList = new LinkedList<>();


    public RobotSpawn(App app, JFXArena arena,ExecutorService robotThreadPool) {
        this.app = app;
        this.arena = arena;
        this.roboCount = 0;
        this.robotThreadPool = robotThreadPool;
    }

    public BlockingQueue<List<Robot>> getRobotBlockingQueue()
    {
        return robotBlockingQueue;
    }//returns robot blkqueue

    public void updateBlockingQueue() throws InterruptedException { //gets uptodate list of robots in blovkingqueue
        robotBlockingQueue.poll();
        robotBlockingQueue.put(roboList);
    }
    private int getRandomDelay() //returns a random delay betwen 500 and 2000
    {
        return ThreadLocalRandom.current().nextInt(500, 2000 + 1);

    }
    private double[] getRandomSpawn(){ //retuns an array of x an dy with random corner coords to spawn
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
            default -> {

            }
        }
        //System.out.println("Randomly selected corner: (" + x + ", " + y + ")");
        return new double[]{x,y};
    }

    private boolean canSpawn(double x,double y) //checks if robot is in the way
    {
        for (Robot robot:roboList) {
            if (robot.getCurrX()==x && robot.getCurrY()==y)
            {
                return false;
            }
        }
        return true;
    }

    @Override //runs in thread as a infinite loop which will create new robot and spawn it and will submit to threadppol
    public void run() {
        //int count = 0;
        while (!Thread.currentThread().isInterrupted()) {
            //count++;
            double[] spawn = getRandomSpawn();

            while(!Thread.currentThread().isInterrupted() && !(canSpawn(spawn[0],spawn[1])) && !(roboCount==0))
            {
                spawn = getRandomSpawn();//gets new spawn where robot is not there
            }

            if(canSpawn(spawn[0],spawn[1]))
            {
                roboCount++;

                double[] finalSpawn = spawn;
                Platform.runLater(() -> {

                    Robot robot = new Robot(roboCount, finalSpawn[0], finalSpawn[1],getRandomDelay(),//creating robot
                            arena.getImageLoader().getRandomRobot());
                    roboList.add(robot);

                    app.getLogger().appendText("Robot "+robot.getId()+" spawned\n");

                    arena.requestLayout();

                    robotThreadPool.submit(new RobotMovement(app,arena,robot)); // submiting robot to threadpool
                });
            }

            try {
                Thread.sleep(1500); //sleeps for 1.5 secs
            } catch (InterruptedException e) {
                System.out.println("RobotSpawn thread interrupted");
                Thread.currentThread().interrupt(); // Restore the interrupted status
                break;
            }
        }
    }
}

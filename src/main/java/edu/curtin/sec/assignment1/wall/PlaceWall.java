/*********************************
 * Name: Ishara Gomes
 * ID: 20534521
 * CLass Name: PlaceWall (Runnable class which will be a thread which will be placing or removing all types of walls)
 *********************************/
package edu.curtin.sec.assignment1.wall;

import edu.curtin.sec.assignment1.App;
import edu.curtin.sec.assignment1.robot.Robot;
import edu.curtin.sec.assignment1.ui.JFXArena;
import javafx.application.Platform;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PlaceWall implements Runnable{
    private BlockingQueue<List<Wall>> wallBlockingQueue = new ArrayBlockingQueue<>(1);
    private BlockingQueue<List<Wall>> brokenWallBlockingQueue = new ArrayBlockingQueue<>(1);

    private Queue<Wall> wallQueue;
    private List<Wall> wallList;
    private List<Wall> brokenWallList;
    //private double x,y;
    private int queueWallCount;
    private int wallCount;
    private JFXArena arena;
    private App app;
    public PlaceWall(App app,JFXArena arena) {
        this.wallQueue = new LinkedList<>();
        this.wallList = new LinkedList<>();
        this.brokenWallList = new LinkedList<>();
        this.queueWallCount = 0;
        this.arena = arena;
        this.app = app;
        this.wallCount=0;
    }
//returns blockingqueue containing walllist
    public BlockingQueue<List<Wall>> getWallBlockingQueue()
    {
        return wallBlockingQueue;
    }
    //returns blockingqueue containing brokenwall list
    public BlockingQueue<List<Wall>> getBrokenWallBlockingQueue()
    {
        return brokenWallBlockingQueue;
    }
    public void updateWallBlockingQueues() throws InterruptedException {//this will update the lists inside the queues
        wallBlockingQueue.poll();
        brokenWallBlockingQueue.poll();
        wallBlockingQueue.put(wallList);
        brokenWallBlockingQueue.put(brokenWallList);
    }


    private boolean isAvailable(double x, double y) throws InterruptedException { // checks if wall can be placed
        if (!(x == 4.0 && y == 4.0)) {
            for (Wall wall : wallList) {
                if (x == wall.x && y == wall.y) {
                    return false; // Point matches a wall, space not available
                }
            }
            for (Wall wall : brokenWallList) {
                if (x == wall.x && y == wall.y) {
                    return false; // Point matches a broken wall, space not available
                }
            }
            app.getRobotSpawn().updateBlockingQueue();
            for (Robot robot : app.getRobotSpawn().getRobotBlockingQueue().take()) {
                if (x == robot.getCurrX() && y == robot.getCurrY()) {
                    return false; // Point matches a robot, space not available
                }
            }
            return true; //space avaialble
        }
        return false; //if it is citidel
    }

    public void addWall(double x,double y) //adds a wall if walls are less than 10 and then addes new wall to a queue
    {
        if(this.wallCount <10)
        {
            wallQueue.add(new Wall(x,y));
            this.queueWallCount++;
            this.wallCount++;

            Platform.runLater(() -> {
                app.changeNoWallQ(this.queueWallCount);
            });
        }
    }
    public void wallBroken(Wall wall) //method called when wall is broken
    {

        this.wallList.remove(wall);
        this.brokenWallList.add(wall);

        Platform.runLater(() -> {
            arena.drawBrokenWall(wall.x, wall.y);
            System.out.println("broken wall placed on : "+wall.x + "," + wall.y);
            app.getLogger().appendText("wall broken at : ("+wall.x + "," + wall.y+")\n"); // gets logged on gui
        });
    }
    public void removeBrokenWall(Wall wall) throws InterruptedException { //called to destroy wall completely
        this.wallCount--; //allows a new wall to be placed

        brokenWallList.remove(wall);

        brokenWallBlockingQueue.poll();
        brokenWallBlockingQueue.put(brokenWallList);
        Platform.runLater(() -> {
        app.getLogger().appendText("wall destroyed at : ("+wall.x + "," + wall.y+")\n");// gets logged on gui
        });
    }
    @Override
    public void run() {// runs an infinite loop that has logic to place and remove normal and broken walls
        while (!Thread.currentThread().isInterrupted()) {
            if (!wallQueue.isEmpty())
            {
                while (!wallQueue.isEmpty()) {
                    this.queueWallCount--;
                    Wall wall = wallQueue.poll();

                    try {
                        if ((isAvailable(wall.x, wall.y)))
                        {
                            Platform.runLater(() -> {

                                arena.drawWallOnClick(wall.x, wall.y);
                                System.out.println("wall placed on : "+wall.x + "," + wall.y);
                                app.getLogger().appendText("wall placed on : ("+wall.x + "," + wall.y+")\n");
                                app.changeNoWallQ(this.queueWallCount);

                                wallList.add(wall);
                            });

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                System.out.println("Wall thread interrupted");
                                Thread.currentThread().interrupt(); // Restore the interrupted status
                                break;
                            }
                        }else{
                            System.out.println("something is already there");
                            wallCount--;
                            Platform.runLater(() -> {
                                app.changeNoWallQ(this.queueWallCount);
                            });

                        }
                    } catch (InterruptedException e) {
                        System.out.println("Wall thread interrupted");
                        Thread.currentThread().interrupt();
                    }
                }

            }

        }
    }
}

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

    //private List<WallCoordinates> wallList;


    //add blovking queue for wall and robo seperately
    private BlockingQueue<List<Wall>> wallBlockingQueue = new ArrayBlockingQueue<>(1);
    private BlockingQueue<List<Wall>> brokenWallBlockingQueue = new ArrayBlockingQueue<>(1);

    private Queue<Wall> wallQueue;
    private List<Wall> wallList;
    private List<Wall> brokenWallList;
    //private double x,y;
    private int QueueWallCount;
    private int wallCount;
    private JFXArena arena;
    private App app;
    public PlaceWall(App app,JFXArena arena) {
        this.wallQueue = new LinkedList<>();
        this.wallList = new LinkedList<>();
        this.brokenWallList = new LinkedList<>();
        this.QueueWallCount = 0;
        this.arena = arena;
        this.app = app;
        this.wallCount=0;
    }

    public BlockingQueue<List<Wall>> getWallBlockingQueue()
    {
        return wallBlockingQueue;
    }
    public BlockingQueue<List<Wall>> getBrokenWallBlockingQueue()
    {
        return brokenWallBlockingQueue;
    }
    public void updateWallBlockingQueues() throws InterruptedException {
        List<Wall> oldList = wallBlockingQueue.poll();
        List<Wall> oldList1 = brokenWallBlockingQueue.poll();
        wallBlockingQueue.put(wallList);
        brokenWallBlockingQueue.put(brokenWallList);
    }


    private boolean isAvailable(double x, double y) throws InterruptedException {
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
                    return false; // Point matches a broken wall, space not available
                }
            }
            return true; // Point doesn't match any wall, space available
        }
        return false;
    }




    public void addWall(double x,double y)
    {
        if(this.wallCount <10)
        {
            wallQueue.add(new Wall(x,y));
            this.QueueWallCount++;
            this.wallCount++;

            Platform.runLater(() -> {
                app.changeNoWallQ(this.QueueWallCount);
            });
        }
    }
    public void wallBroken(Wall wall)
    {

        this.wallList.remove(wall);
        this.brokenWallList.add(wall);

        Platform.runLater(() -> {
            arena.drawBrokenWall(wall.x, wall.y);
            System.out.println("broken wall placed on : "+wall.x + "," + wall.y);
            app.getLogger().appendText("wall broken at : ("+wall.x + "," + wall.y+")\n");
        });
    }
    public void removeBrokenWall(Wall wall) throws InterruptedException {
        this.wallCount--;

        brokenWallList.remove(wall);

        List<Wall> oldList = brokenWallBlockingQueue.poll();
        brokenWallBlockingQueue.put(brokenWallList);
        Platform.runLater(() -> {
        app.getLogger().appendText("wall destroyed at : ("+wall.x + "," + wall.y+")\n");
        });
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (!wallQueue.isEmpty())
            {
                while (!wallQueue.isEmpty()) {
                    this.QueueWallCount--;
                    Wall wall = wallQueue.poll();

                    try {
                        if ((isAvailable(wall.x, wall.y)))
                        {
                            Platform.runLater(() -> {

                                arena.drawWallOnClick(wall.x, wall.y);
                                System.out.println("wall placed on : "+wall.x + "," + wall.y);
                                app.getLogger().appendText("wall placed on : ("+wall.x + "," + wall.y+")\n");
                                app.changeNoWallQ(this.QueueWallCount);

                                wallList.add(wall);
                            });

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                System.out.println("Goodbye walls!!!!");
                                Thread.currentThread().interrupt(); // Restore the interrupted status
                                break;
                            }
                        }else{
                            System.out.println("something is already there");
                            wallCount--;
                            Platform.runLater(() -> {
                                app.changeNoWallQ(this.QueueWallCount);
                            });

                        }
                    } catch (InterruptedException e) {
                        System.out.println("Goodbye walls!!!!");
                        Thread.currentThread().interrupt();
                    }
                }

            }

        }
    }
}

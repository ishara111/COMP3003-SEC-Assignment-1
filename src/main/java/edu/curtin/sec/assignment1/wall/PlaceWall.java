package edu.curtin.sec.assignment1.wall;

import edu.curtin.sec.assignment1.App;
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
    private BlockingQueue<List<Wall>> blockingQueue = new ArrayBlockingQueue<>(1);

    private Queue<Wall> wallQueue;
    private List<Wall> wallList;
    private double x,y;
    private int QueueWallCount;
    private int wallCount;
    private JFXArena arena;
    private App app;
    public PlaceWall(App app,JFXArena arena) {
        this.wallQueue = new LinkedList<>();
        this.wallList = new LinkedList<>();
        this.x=0;
        this.y=0;
        this.QueueWallCount = 0;
        this.arena = arena;
        this.app = app;
        this.wallCount=0;
    }

    public BlockingQueue<List<Wall>> getWallBlockingQueue()
    {
        return blockingQueue;
    }
    public void updateBlockingQueue() throws InterruptedException {
        List<Wall> oldList = blockingQueue.poll();
        blockingQueue.put(wallList);
    }

    private boolean isAvailable(double x, double y) {
        if (!(x == 4.0 && y == 4.0)) {
            for (Wall wall : wallList) {
                if (x == wall.x && y == wall.y) {
                    return false; // Point matches a wall, space not available
                }
            }
            return true; // Point doesn't match any wall, space available
        }
        return false; // Either x or y is 4.0, space not available
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
    public void wallBroken()
    {
        this.wallCount--;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (!wallQueue.isEmpty())
            {
                while (!wallQueue.isEmpty()) {
                    this.QueueWallCount--;
                    Wall wall = wallQueue.poll();

                    if ((isAvailable(wall.x, wall.y)))
                    {
                        Platform.runLater(() -> {

                            arena.drawWallOnClick(wall.x, wall.y);
                            System.out.println("wall placed on : "+wall.x + "," + wall.y);
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
                }

            }

        }
    }
}

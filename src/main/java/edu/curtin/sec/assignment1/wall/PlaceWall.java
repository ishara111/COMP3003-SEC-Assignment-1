package edu.curtin.sec.assignment1.wall;

import edu.curtin.sec.assignment1.App;
import edu.curtin.sec.assignment1.ui.JFXArena;
import javafx.application.Platform;

import java.util.LinkedList;
import java.util.Queue;

public class PlaceWall implements Runnable{

    //private List<WallCoordinates> wallList;
    private Queue<Wall> wallQueue;
    private double x,y;
    private int wallCount;
    private JFXArena arena;
    private App app;
    public PlaceWall(App app,JFXArena arena) {
        this.wallQueue = new LinkedList<>();
        this.x=0;
        this.y=0;
        this.wallCount = 0;
        this.arena = arena;
        this.app = app;
    }

    public void addWall(double x,double y)
    {
        wallQueue.add(new Wall(x,y));
        this.wallCount++;

        Platform.runLater(() -> {
            app.changeNoWallQ(this.wallCount);
        });
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (!wallQueue.isEmpty())
            {
                while (!wallQueue.isEmpty()) {
                    this.wallCount--;
                    Wall wallCoords = wallQueue.poll();
                    Platform.runLater(() -> {

                        arena.drawWallOnClick(wallCoords.x, wallCoords.y);
                        System.out.println(wallCoords.x + " " + wallCoords.y);
                        app.changeNoWallQ(this.wallCount);
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println("Goodbye walls!!!!");
                        Thread.currentThread().interrupt(); // Restore the interrupted status
                        break;
                    }
                }

            }

        }
    }
}

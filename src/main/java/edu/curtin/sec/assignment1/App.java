package edu.curtin.sec.assignment1;

import edu.curtin.sec.assignment1.robot.RobotSpawn;
import edu.curtin.sec.assignment1.score.Score;
import edu.curtin.sec.assignment1.ui.JFXArena;
import edu.curtin.sec.assignment1.wall.PlaceWall;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App extends Application 
{
    Label scoreText = new Label("Score: 0");
    Label wallQ = new Label("walls queued: 0");
    Score score = new Score(this);
    Thread scoreThread = new Thread(score, "score-thread");

    JFXArena arena = new JFXArena(this);
    PlaceWall wall = new PlaceWall(this,arena);
    Thread wallThread = new Thread(wall, "wall-thread");

    ExecutorService robotThreadPool = new ThreadPoolExecutor(
            1, 81, // Minimum 1 threads, maximum 81.
            3, TimeUnit.SECONDS, // Destroy excess idle threads after 3 seconds.
            new SynchronousQueue<>() // Used to deliver new tasks to the threads.
    );

    RobotSpawn robotSpawn= new RobotSpawn(this,arena,robotThreadPool);
    Thread robotSpawnThread = new Thread(robotSpawn,"robot-spawn-thread");

    public static void main(String[] args) 
    {
        launch();        
    }

    public PlaceWall getWall(){
        return wall;
    }

    public RobotSpawn getRobotSpawn(){ return robotSpawn;}

    public void stopThreads(){
        scoreThread.interrupt();
        wallThread.interrupt();
        robotSpawnThread.interrupt();
        robotThreadPool.shutdownNow();
    }
    @Override
    public void stop() throws Exception {
        stopThreads();
    }

    public void changeScore(int score)
    {

        Platform.runLater(() -> { //runlater is also used here and score class for concurrency

            scoreText.setText("Score: " + score);
        });
//        synchronized(monitor) {

//            monitor.notifyAll();
//        }
    }

    public void changeNoWallQ(int count)
    {
        Platform.runLater(() -> {

            wallQ.setText("walls queued: " + count);
        });
    }

    @Override
    public void start(Stage stage) 
    {
        scoreThread.start();
        wallThread.start();
        robotSpawnThread.start();

        javafxUi(stage);
    }



    private void javafxUi(Stage stage)
    {
        stage.setTitle("Assignment-1");

        arena.addListener((x, y) ->
        {
            System.out.println("Arena click at (" + x + "," + y + ")");
            //arena.drawWallOnClick(x,y);
            wall.addWall(x,y);

        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        ToolBar toolbar = new ToolBar();
        Button btn1 = new Button("clear");
        Button btn2 = new Button("My Button 2");
        //Label wallq = new Label("walls queued: 999");
        //toolbar.getItems().addAll(btn1, btn2, scoreText,wallq);
        toolbar.getItems().addAll(scoreText,spacer,wallQ,btn1);

         btn1.setOnAction((event) ->
         {
            arena.tempClearScreen();
         });

        TextArea logger = new TextArea();
        logger.appendText("Hello\n");
        logger.appendText("World\n");

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);

        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }
}

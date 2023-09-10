/*********************************
 * Name: Ishara Gomes
 * ID: 20534521
 * CLass Name: App (Main class)
 *********************************/
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
    private TextArea logger = new TextArea();
    private Label scoreText = new Label("Score: 0");
    private Label wallQ = new Label("walls queued: 0");
    private Score score = new Score(this);
    private Thread scoreThread = new Thread(score, "score-thread");

    private JFXArena arena = new JFXArena(this);
    private PlaceWall wall = new PlaceWall(this,arena);
    private Thread wallThread = new Thread(wall, "wall-thread");

    private ExecutorService robotThreadPool = new ThreadPoolExecutor(
            1, 81, // Minimum 1 threads, maximum 81.
            3, TimeUnit.SECONDS, // Destroy excess idle threads after 3 seconds.
            new SynchronousQueue<>() // Used to deliver new tasks to the threads.
    );

    private RobotSpawn robotSpawn= new RobotSpawn(this,arena,robotThreadPool);
    private Thread robotSpawnThread = new Thread(robotSpawn,"robot-spawn-thread");

    public static void main(String[] args) 
    {
        launch();        
    }

    public TextArea getLogger(){return logger;} //reurns logger object
    public PlaceWall getWall(){
        return wall;
    }
    public Score getScore(){ //returns score object
        return score;
    }
    public RobotSpawn getRobotSpawn(){ return robotSpawn;} //returns robotspawn object

    public void changeScore(int score) //this method is called in score thread to change the value  of score in toolbar
    {

        Platform.runLater(() -> {

            scoreText.setText("Score: " + score);
        });

    }

    public void changeNoWallQ(int count) //this changes wallqueue value in toolbar
    {
        Platform.runLater(() -> {

            wallQ.setText("walls queued: " + count);
        });
    }

    public void endGame(){ //when called interuups all the threads and threadpool and draws a cross img over citidel
        scoreThread.interrupt();
        wallThread.interrupt();
        robotSpawnThread.interrupt();
        robotThreadPool.shutdownNow();
        arena.drawCross();
        Platform.runLater(() -> {
            scoreText.setText("Your HighScore: " + score.getScore());
            logger.appendText("\nYour HighScore: " + score.getScore()+"\n");
        });
    }

    @Override
    public void stop() throws Exception { //end of gui
        endGame();
    }

    @Override
    public void start(Stage stage) //start method for gui
    {
        scoreThread.start(); //starting all the threads
        wallThread.start();
        robotSpawnThread.start();

        javafxUi(stage);
    }



    private void javafxUi(Stage stage)// contains everything related to gui
    {
        stage.setTitle("Assignment-1");

        arena.addListener((x, y) ->
        {
            System.out.println("Arena click at (" + x + "," + y + ")");
            //wall will be added according to logic in wall thread;
            wall.addWall(x,y);

        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        ToolBar toolbar = new ToolBar();
        //Button btn1 = new Button("clear");
//        Button btn2 = new Button("My Button 2");

        toolbar.getItems().addAll(scoreText,spacer,wallQ);

//        btn1.setOnMouseClicked(event -> arena.requestLayout());

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

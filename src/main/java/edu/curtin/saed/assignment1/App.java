package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application 
{
    private Object monitor = new Object();
    Label scoreText = new Label("Score: 0");
    Score score = new Score(this);
    Thread scoreThread = new Thread(score, "score-thread");
    public static int timerText=0;
    public static void main(String[] args){
        launch();


    }

    @Override
    public void stop() throws Exception {
        scoreThread.interrupt();
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

    @Override
    public void start(Stage stage) throws InterruptedException {


        scoreThread.start();

        stage.setTitle("Assignment-1");
        JFXArena arena = new JFXArena();
        arena.addListener((x, y) ->
        {
            System.out.println("Arena click at (" + x + "," + y + ")");
        });
        
        ToolBar toolbar = new ToolBar();
         Button btn1 = new Button("My Button 1");
         Button btn2 = new Button("My Button 2");
        Label wallq = new Label("walls queued: 999");
        toolbar.getItems().addAll(btn1, btn2, scoreText,wallq);
//        toolbar.getItems().addAll(label);
//
//        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        executorService.scheduleAtFixedRate(() -> {
//            Platform.runLater(() -> {
//            scoreText.setText("Score: " + score.getScore());
//            });
//        }, 0, 1, TimeUnit.SECONDS); // Update every second


        
         btn1.setOnAction((event) ->
         {
             System.out.println("Button 1 pressed");
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

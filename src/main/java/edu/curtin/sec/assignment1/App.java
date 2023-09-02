package edu.curtin.sec.assignment1;

import edu.curtin.sec.assignment1.score.Score;
import edu.curtin.sec.assignment1.ui.JFXArena;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Platform;

public class App extends Application 
{
    Label scoreText = new Label("Score: 0");
    Score score = new Score(this);
    Thread scoreThread = new Thread(score, "score-thread");

    public static void main(String[] args) 
    {
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
    public void start(Stage stage) 
    {
        scoreThread.start();

        javafxUi(stage);
    }



    private void javafxUi(Stage stage)
    {
        stage.setTitle("Assignment-1");
        JFXArena arena = new JFXArena();
        arena.addListener((x, y) ->
        {
            System.out.println("Arena click at (" + x + "," + y + ")");
            arena.drawWallOnClick(x,y);
        });

        ToolBar toolbar = new ToolBar();
        Button btn1 = new Button("clear");
        Button btn2 = new Button("My Button 2");
        Label wallq = new Label("walls queued: 999");
        //toolbar.getItems().addAll(btn1, btn2, scoreText,wallq);
        toolbar.getItems().addAll(scoreText,btn1);

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

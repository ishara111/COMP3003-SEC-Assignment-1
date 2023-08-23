package edu.curtin.saed.assignment1;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Score implements Runnable{

  //  private Object monitor = new Object();
    private int score;
    private App app;
  //  private Label scoreText;

    public Score(App app) {
        this.score = 0;
       // this.scoreText =scoreText;
        this.app = app;
    }

    public int getScore() {
//        synchronized(monitor)
//        {
            return score;
        //}
    }

    private void incrementScore() {
//        synchronized(monitor)
//        {
            this.score++ ;
//            monitor.notifyAll();
////        }

    }

//    public App getScoreText() {
//        return scoreText;
//    }
//
    public void setScoreText() {
        Platform.runLater(() -> {

            this.app.changeScore(this.getScore());
        });

    }

    @Override
    public void run() {
        while (true) {
            // Your code here
            incrementScore();
            setScoreText();
            System.out.println(getScore());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Goodbye !!!!");
            }
        }
    }
}

package edu.curtin.sec.assignment1.score;

import edu.curtin.sec.assignment1.App;
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
        return score;
    }

    private void incrementScore() {
//        synchronized(monitor)
//        {
        this.score=this.score+10 ;
//            monitor.notifyAll();
////        }

    }

    public void setScoreText() {
        Platform.runLater(() -> {

            this.app.changeScore(this.getScore());
        });

    }

    public void robotKilled()
    {
        this.score = this.score+100;
    }

    @Override
    public void run() {
        while (true) {
            incrementScore();
            setScoreText();
            //System.out.println(getScore());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Goodbye !!!!");
                break;
            }
        }
    }
}

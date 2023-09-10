/*********************************
 * Name: Ishara Gomes
 * ID: 20534521
 * CLass Name: Score (Runnable class which will be a thread)
 *********************************/
package edu.curtin.sec.assignment1.score;

import edu.curtin.sec.assignment1.App;
import javafx.application.Platform;

public class Score implements Runnable{

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

    private void incrementScore() { //incrementing scor eby 10
//        synchronized(monitor)
//        {
        this.score=this.score+10 ;
//            monitor.notifyAll();
////        }

    }

    public void setScoreText() { //sets the score in toolbar
        Platform.runLater(() -> {

            this.app.changeScore(this.getScore());
        });

    }

    public void robotKilled() //incrementing score by 100 when robot destroyed
    {
        this.score = this.score+100;
        setScoreText();
    }

    @Override
    public void run() { //is an infinite loop that will sleep for 1 sec every iteration
        while (true) {
            incrementScore();
            setScoreText();
            //System.out.println(getScore());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Score thread interrupted");
                break;
            }
        }
    }
}

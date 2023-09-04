package edu.curtin.sec.assignment1.robot;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RobotSpawn implements Runnable{

    public RobotSpawn() {

    }

    private int getRandomDelay()
    {
        return ThreadLocalRandom.current().nextInt(500, 2000 + 1);

    }
    private double[] getRandomSpawn(){
        double x=0;
        double y=0;
        int randomSpawn = ThreadLocalRandom.current().nextInt(4);

        switch (randomSpawn) {
            case 0 -> {
                // Top-Left Corner
                x = 0;
                y = 0;
            }
            case 1 -> {
                // Top-Right Corner
                x = 8;
                y = 0;
            }
            case 2 -> {
                // Bottom-Left Corner
                x = 0;
                y = 8;
            }
            case 3 -> {
                // Bottom-Right Corner
                x = 8;
                y = 8;
            }
        }
        System.out.println("Randomly selected corner: (" + x + ", " + y + ")");
        return new double[]{x,y};
    }

    @Override
    public void run() {
        while (true) {

            getRandomSpawn();

            System.out.println("Random delay: (" +getRandomDelay()+ ")");

            //System.out.println(getScore());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println("Goodbye robot spawn!!!!");
                break;
            }
        }
    }
}

package edu.curtin.sec.assignment1.robot;

import java.util.Random;

public class RobotSpawn implements Runnable{
    private double x,y;

    public RobotSpawn() {
        Random random = new Random();
        int randomCorner = random.nextInt(4);

        switch (randomCorner) {
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
    }

    @Override
    public void run() {

    }
}

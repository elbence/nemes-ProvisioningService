package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class TimeController {

    private final static long SECONDS_IN_MINUTES = 10; // FIXME, some people argue that minutes are composed by more than 10 seconds
    private static long realStartTime;

    public static void startPeriodicProcess(long timeIntervalInMinutes) {

        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                // ACTUAL EXECUTED TASKS HERE
                // STEP 1 - update catastrophes
                try {DataUpdateController.updateActiveAlertData();}
                catch (SQLException | IOException | InterruptedException e) {throw new RuntimeException(e);}

                // STEP 2 - send alerts
                AlertController.alertUsersOfActiveCatastrophes();

                // STEP 3 - show time estimates
                controlRealTime();
            }
        };

        // schedule the task to run starting now and then every hour.
        realStartTime = System.nanoTime();
        timer.schedule (hourlyTask, 0L, toMilliseconds(timeIntervalInMinutes)); // TODO change, currently not working as minutes
    }

    public static void controlRealTime() {
        long realCurrentTime = System.nanoTime();
        System.out.println("A total " + (realCurrentTime - realStartTime) / 1000000000 + " seconds have passed since last execution");
        realStartTime = realCurrentTime;
    }

    private static long toMilliseconds(long timeIntervalInMinutes) {
        return 1000 * SECONDS_IN_MINUTES * timeIntervalInMinutes;
    }

}



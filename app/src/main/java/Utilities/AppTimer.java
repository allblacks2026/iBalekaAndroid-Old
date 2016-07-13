package Utilities;

import java.util.Timer;
import java.util.TimerTask;

//Experimental Class
public class AppTimer {
    //Source: https://www.youtube.com/watch?v=36jbBSQd3eU
    private Timer appTimer;
    private TimerTask appTimerTask;

    private int minutes = 0;
    private int seconds = 0;
    private int hours = 0;

    public AppTimer() {
        appTimer = new Timer();
        appTimerTask = new TimerTask() {
            @Override
            public void run() {
                seconds = seconds + 1;
                if (seconds % 60 == 0) {
                    minutes = minutes + 1;
                }
                if (minutes % 60 == 0) {
                    hours = hours + 1;
                }
            }
        };
    }

    public void startTimer() {
        appTimer.scheduleAtFixedRate(appTimerTask, 0, 1000);
    }

    public int getMinutes()
    {
        return minutes;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public int getHours() {
        return hours;
    }

    public void stopTimer()
    {
        appTimer.cancel();
        appTimerTask.cancel();
    }

    public void pauseTimer()
    {

    }
}

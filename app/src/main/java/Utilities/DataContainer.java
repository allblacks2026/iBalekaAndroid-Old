package Utilities;

import android.app.Application;
import android.location.Location;
import android.provider.ContactsContract;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Models.Checkpoint;

/**
 * Created by Okuhle on 7/24/2016.
 */
public class DataContainer {

    private static DataContainer dataContainer;
    private List<Checkpoint> checkpointList;
    private List<Location> runnerTrackList;
    private List<Location> routeList;
    private List<Double> runnerSpeedList;
    private String[] timesString;
    private double caloriesBurnt;
    private String timeElapsed;

    public static DataContainer getDataContainerInstance()
    {
        if (dataContainer == null) {
            dataContainer = new DataContainer();
        }
        return dataContainer;
    }

    public List<Checkpoint> getCheckpointList() {
        return this.checkpointList;
    }


    public void setCheckpointList(List<Checkpoint> list) {
        this.checkpointList = list;
    }

    private DataContainer()
    {
        runnerTrackList = new ArrayList<>();
        routeList = new ArrayList<>();
        checkpointList = new ArrayList<>();
        runnerSpeedList = new ArrayList<>();
        timesString = new String[3];
    }

    public List<Location> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Location> routeList) {
        this.routeList = routeList;
    }

    public List<Location> getRunnerTrackList() {
        return runnerTrackList;
    }

    public void setRunnerTrackList(List<Location> runnerTrackList) {
        this.runnerTrackList = runnerTrackList;
    }

    public List<Double> getRunnerSpeedList() {
        return runnerSpeedList;
    }

    public void setRunnerSpeedList(List<Double> runnerSpeedList) {
        this.runnerSpeedList = runnerSpeedList;
    }

    public Double getHighestSpeed()
    {
        double highestSpeed = 0;
        for (double currentSpeed : runnerSpeedList) {
            if (currentSpeed > highestSpeed) {
                highestSpeed = currentSpeed;
            }
        }
        return highestSpeed;
    }

    public String[] getTimesString() {
        return timesString;
    }

    public void setTimesString(String [] timesString) {
        this.timesString = timesString;
    }

    public double getCaloriesBurnt() {
        return caloriesBurnt;
    }

    public void setCaloriesBurnt(double caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public String getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(String timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public double getAverageSpeed()
    {
        double averageSpeed = 0;
        DecimalFormat format = new DecimalFormat("#.##");
        for (double speed : runnerSpeedList) {
            averageSpeed = averageSpeed + speed;
        }
        averageSpeed = (averageSpeed / runnerSpeedList.size());
        averageSpeed = Double.valueOf(format.format(averageSpeed));
        return averageSpeed;
    }

    //Return the total distance in kilometres
    public Double getTotalDistance()
    {
        double totalDistance = 0;
        DecimalFormat format = new DecimalFormat("#.##");
        Location currentLocation = null;
        for (Location thisLocation: routeList) {
            if (currentLocation == null) {
                currentLocation = thisLocation;
            } else {
                double distanceDiff = currentLocation.distanceTo(thisLocation);
                totalDistance += distanceDiff;
                currentLocation = thisLocation;
            }
        }
        totalDistance = totalDistance / 1000;
        totalDistance = Double.valueOf(format.format(totalDistance));
        return totalDistance;
    }

    public Double getRunnerDistance() {
        double totalDistance = 0;
        DecimalFormat format = new DecimalFormat("#.##");
        Location currentLocation = null;
        for (Location thisLocation: runnerTrackList) {
            if (currentLocation == null) {
                currentLocation = thisLocation;
            } else {
                double distanceDiff = currentLocation.distanceTo(thisLocation);
                totalDistance += distanceDiff;
                currentLocation = thisLocation;
            }
        }
        totalDistance = totalDistance / 1000;
        totalDistance = Double.valueOf(format.format(totalDistance));
        return totalDistance;
    }
}

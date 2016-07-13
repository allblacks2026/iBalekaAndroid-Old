package Models;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by Okuhle on 5/16/2016.
 */
public class Route {

    private String routeID;
    private String startPoint;
    private String endPoint;
    private String distance;
    private String dateRecorded;
    private Bitmap mapImage;

    public Route(String dateRecorded, String distance, String endPoint, Bitmap mapImage, String
            routeID, String startPoint) {
        this.dateRecorded = dateRecorded;
        this.distance = distance;
        this.endPoint = endPoint;
        this.mapImage = mapImage;
        this.routeID = routeID;
        this.startPoint = startPoint;
    }

    public String getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(String dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Bitmap getMapImage() {
        return mapImage;
    }

    public void setMapImage(Bitmap mapImage) {
        this.mapImage = mapImage;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }
}

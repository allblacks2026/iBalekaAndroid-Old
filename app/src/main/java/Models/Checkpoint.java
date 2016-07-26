package Models;

/**
 * Created by Okuhle on 7/24/2016.
 */
public class Checkpoint {

    private String checkpointID;
    private String latitude;
    private String longitude;

    public Checkpoint(String checkpointID, String latitude, String longitude) {
        this.checkpointID = checkpointID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCheckpointID() {
        return checkpointID;
    }

    public void setCheckpointID(String checkpointID) {
        this.checkpointID = checkpointID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

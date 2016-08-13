package Models;

/**
 * Created by Okuhle on 7/27/2016.
 */
public class RegisteredEvent {

    private String athleteID;
    private String eventID;
    private String description;
    private String date;
    private String time;
    private String location;

    public RegisteredEvent(String athleteID, String eventID, String description, String date, String time, String location) {
        this.athleteID = athleteID;
        this.eventID = eventID;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public String getAthleteID() {
        return athleteID;
    }

    public void setAthleteID(String athleteID) {
        this.athleteID = athleteID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

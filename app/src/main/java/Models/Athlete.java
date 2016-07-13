package Models;

import java.util.Date;

public class Athlete{

    private String athleteID;
    private String userID;
    private String weight;
    private String height;
    private String gender;
    private String licenseNo;

    public Athlete(String athleteID, String userID, String weight, String height, String gender, String licenseNo) {
        this.athleteID = athleteID;
        this.userID = userID;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.licenseNo = licenseNo;
    }

    //Constructor excluding the license number

    public Athlete(String gender, String height, String weight, String userID, String athleteID) {
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.userID = userID;
        this.athleteID = athleteID;
    }

    public String getAthleteID() {
        return athleteID;
    }

    public void setAthleteID(String athleteID) {
        this.athleteID = athleteID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }
}
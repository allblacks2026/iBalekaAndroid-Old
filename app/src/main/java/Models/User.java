package Models;

import java.util.Date;

/**
 * Created by Okuhle on 5/26/2016.
 */
public class User {

    private String userID;
    private String name;
    private String surname;
    private String emailAddress;
    private String country;
    private String userType;
    private String dateOfBirth;

    public User(String userID, String name, String surname, String emailAddress, String country, String userType, String dateOfBirth) {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.country = country;
        this.userType = userType;
        this.dateOfBirth = dateOfBirth;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
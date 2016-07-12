package Models;

/**
 * Created by Okuhle on 7/10/2016.
 */
public class UserCredential extends User {

    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;

    public UserCredential(String userID, String name, String surname, String emailAddress, String country, String userType, String dateOfBirth, String username, String password, String securityQuestion, String securityAnswer) {
        super(userID, name, surname, emailAddress, country, userType, dateOfBirth);
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
}

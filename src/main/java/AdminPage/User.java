package AdminPage;

import java.util.ArrayList;

/**
 * @author callumsmith on 2024-10-28
 */
public class User {
    String id;
    String username;
    String password;
    String email;
    String type;
    String experience;
    String accepted;


    public User(String username, String password, String email, String type, String Experience, String Accepted) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.type = type;
        this.experience = Experience;
        this.accepted = Accepted;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getType() {
        return type;
    }
    public String getExperience() {
        return experience;
    }
    public String isAccepted() {
        return accepted;
    }
    public void setStatus(String status) {
        accepted = status;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setExperience(String experience) {
        this.experience = experience;
    }




    public ArrayList<User> getUsers() {
        return null;
    }
}



package UserInfo;

/**
 * @author callumsmith on 2024-10-09
 */
public class Users {
    String username;
    String password;
    String email;
    String type;

    public Users(String username, String password, String email, String type, String experience) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
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




}

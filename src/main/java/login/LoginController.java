package login;

import AdminPage.AdminPageApplication;
import UserPage.UserPageApplication;
import UserPage.UserPageController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * @author callumsmith on 2024-10-03
 */
public class LoginController {
    @FXML
    public Button loginBtn;
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;
    public static String CurrentUser;

    Connection connection;
    boolean isAdmin = false;

    public LoginController() {
        connection = MyConnection.connect();
    }

    @FXML
    public void handleLogin() throws SQLException, IOException {
        isAdmin = false;
        String query = "SELECT * FROM Users where email = ? and password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username.getText());
        statement.setString(2, password.getText());

        ResultSet resultSet = statement.executeQuery();
        boolean isSuccessful = false;

        while(resultSet.next()){

            if(resultSet.getString("email").equals(username.getText())
                    && resultSet.getString("password").equals(password.getText())){
                if(resultSet.getString("type").equals("admin")){
                    isAdmin = true;
                }
                if(resultSet.getString("type").equals("user")){
                    isAdmin = false;
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setContentText("Welcome, " + resultSet.getString("username"));
                alert.showAndWait();
                isSuccessful = true;
            }

        }

        if(!isSuccessful){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login Unsuccessful");
            alert.setContentText("email and password dont match");
            alert.showAndWait();
        }else if(!isAdmin){
            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            Stage stage = new Stage();
            UserPageApplication application = new UserPageApplication();
            stage.setTitle("User Page");
            application.start(stage);
            UserPageController userPageController = application.getUserPageController();
            userPageController.setCurrentUserEmail(username.getText());
            userPageController.loadUserData();
            userPageController.loadUserData(); // Now call loadUserData()

            currentStage.close();



        } else{
            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            Stage stage = new Stage();
            AdminPageApplication application = new AdminPageApplication();
            stage.setTitle("Admin Page");
            application.start(stage);
            currentStage.close();
        }
    }




}


package StartLogin;

import ApplicationPortal.ApplicationPortalApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import login.LoginApplication;

import java.awt.*;
import java.io.IOException;

/**
 * @author callumsmith on 2024-10-23
 */
public class StartLoginController {
    public javafx.scene.control.Button Btnlogin;




    @FXML
    public void onbtncreate() throws IOException {
        Stage currentStage = (Stage) Btnlogin.getScene().getWindow();
        Stage stage = new Stage();
        ApplicationPortalApp application = new ApplicationPortalApp();
        stage.setTitle("Create your application");
        application.start(stage);
        currentStage.close();
        //changes to the application portal



    }
    @FXML
    public void onbtnlogin() throws IOException {
        Stage currentStage = (Stage) Btnlogin.getScene().getWindow();
        Stage stage = new Stage();
        LoginApplication application = new LoginApplication();

        application.start(stage);
        stage.setTitle("Login");
        currentStage.close();
        //brings you to existing account login



    }
}

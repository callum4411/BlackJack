package UserInfo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import login.LoginApplication;

import java.io.IOException;

/**
 * @author callumsmith on 2024-10-09
 */
public class UserInfoApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UserInfoApplication.class.getResource("TableViewUsers-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("User Information");
        stage.setScene(scene);
        stage.show();


    }
}

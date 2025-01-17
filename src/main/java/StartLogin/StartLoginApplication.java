package StartLogin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import login.LoginApplication;

import java.io.IOException;

/**
 * @author callumsmith on 2024-10-23
 */
public class StartLoginApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartLoginApplication.class.getResource("StartLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 456, 63);
        stage.setTitle("Log In or Create Account");
        stage.setScene(scene);
        stage.show();


    }
}

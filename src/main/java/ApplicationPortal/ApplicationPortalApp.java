package ApplicationPortal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import login.LoginApplication;

import java.io.IOException;

/**
 * @author callumsmith on 2024-10-21
 */
public class ApplicationPortalApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationPortalApp.class.getResource("ApplicationPortal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 435, 350);
        stage.setTitle("Application Portal");
        stage.setScene(scene);
        stage.show();


    }
}

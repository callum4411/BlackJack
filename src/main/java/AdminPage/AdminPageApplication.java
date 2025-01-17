package AdminPage;

import ApplicationPortal.ApplicationPortalApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author callumsmith on 2024-10-25
 */
public class AdminPageApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AdminPageApplication.class.getResource("AdminPage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 923, 550);
        stage.setTitle("Admin Page");
        stage.setScene(scene);
        stage.show();


    }
}
package UserPage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author callumsmith on 2024-10-28
 */
public class UserPageApplication extends Application {
    private UserPageController userPageController; // Store a reference to the controller

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UserPageApplication.class.getResource("UserPage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 430, 250);

        userPageController = fxmlLoader.getController(); // Get the controller instance
        scene.getStylesheets().add(UserPageApplication.class.getResource("UserPage.css").toExternalForm());
        stage.setTitle("User Page");
        stage.setScene(scene);
        stage.show();
    }

    public UserPageController getUserPageController() {
        return userPageController; // Provide access to the controller
    }
}

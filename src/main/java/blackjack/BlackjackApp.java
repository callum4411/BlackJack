package blackjack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BlackjackApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/blackjack/StartPage.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/blackjack/blackjack.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Blackjack - Select Player");
        primaryStage.show();
    }
}

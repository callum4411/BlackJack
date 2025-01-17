package quizApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginStatusLabel;

    // Handle login logic
    @FXML
    private void handleLogin() {//Passes email and password entered into authenticate User
        String email = emailField.getText();
        String password = passwordField.getText();

        if (authenticateUser(email, password)) {//if user authenticate is true then it loads the quiz page while passing email through to quizAppController
            loadQuizPage(email); // Load the quiz page on successful login
        } else {
            loginStatusLabel.setText("Invalid email or password");//if user dosent exist or pass is incorrect
        }
    }

    private boolean authenticateUser(String email, String password) {//checks Users database and checks if login details are valid
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = MyConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            return rs.next(); // Return true if a match is found
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    // Method to load the quiz page
    private void loadQuizPage(String userEmail) {//loads the quiz page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quizApp/quizApp-view.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();

            // Load the FXML and create a Scene
            Scene scene = new Scene(loader.load());

            // Add the CSS file to the Scene
            scene.getStylesheets().add(getClass().getResource("/quizApp/quizApp.css").toExternalForm());

            // Set the Scene to the Stage
            stage.setScene(scene);

            // Get QuizController instance and set the user email
            quizAppController quizController = loader.getController();
            quizController.setUserEmail(userEmail);//passes userEmail to quizAppController

            stage.setTitle("Quiz Application");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            loginStatusLabel.setText("Error loading quiz page.");
        }
    }

}


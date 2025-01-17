package ApplicationPortal;

import StartLogin.StartLoginApplication;
import com.example.callumaseprojects.HelloApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import login.LoginApplication;
import login.MyConnection;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author callumsmith on 2024-10-21
 */
public class ApplicationPortalController {
    public TextField txfName;
    public TextField txfEmail;
    public TextField txfPass;
    public Button btnAdd;
    public ComboBox cmbExp;
    public TextField otherText;


    @FXML
    public Button loginBtn;
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    Connection connection;

    public ApplicationPortalController() {
        connection = MyConnection.connect();
    }
    public void initialize() {
        cmbExp.setItems(FXCollections.observableArrayList("Java", "Python", "C#", "C++", "Swift"));//sets up the comboBox for the experience dropdown
        Platform.runLater(() -> {
            otherText.requestFocus(); // Focus another control so that the name enter is auto selected and you wouldnt be able to see the prompt
        });
    }



    public void onBtnAdd() throws SQLException, IOException {
        int failed=0;
        String query = "INSERT INTO Users(username, password, email, type, Experience, accepted) VALUES(?,?,?,?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Setting the values from your input fields
            preparedStatement.setString(1, txfName.getText());
            preparedStatement.setString(2, txfPass.getText());
            preparedStatement.setString(3, txfEmail.getText());
            preparedStatement.setString(4, "user");
            preparedStatement.setString(5, cmbExp.getSelectionModel().getSelectedItem().toString());
            preparedStatement.setString(6, "Pending");
            //ads a row to a database based on the info inputed by the users


            // Execute the update (not query) as it's an INSERT operation
            preparedStatement.executeUpdate();

            System.out.println("User added successfully!");
        } catch (SQLException e) {
            failed =1;
            if (e.getMessage().contains("UNIQUE constraint failed: Users.email")) {
                // Inform the user that the username already exists
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Email is linked to another account");
                alert.setContentText("The email that you entered is linked to another account, try another email or logging into an existing account.");
                alert.showAndWait();//allert to let them know if they need to chose different primary key(email)
            } else {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Application Error");
                alert.setContentText("There was a problem sending in your application");
                alert.showAndWait();
            }
        }

        if(failed ==0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Application success");
            alert.setContentText("you application has been sent");
            alert.showAndWait();//lets them know it went through


            Stage currentStage = (Stage) btnAdd.getScene().getWindow();
            Stage stage = new Stage();
            StartLoginApplication application = new StartLoginApplication();
            stage.setTitle("Login");
            application.start(stage);
            currentStage.close();//Sends them back to the start up page so they can log in to there new account
        }

    }





}

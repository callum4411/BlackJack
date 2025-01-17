package UserPage;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import login.LoginController;

import java.sql.*;

/**
 * @author callumsmith on 2024-10-28
 */
public class UserPageController extends LoginController {
    @FXML
    public Text txtStatus;
    @FXML
    public TextField txfName;
    @FXML

    public ComboBox<String> cmbExperience;

    private String currentUserEmail; // Change to instance variable
    Connection connection;

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onbtnWithdraw() {
        try {
            connectToDatabase(); // Ensure the connection is established first

            String deleteQuery = "DELETE FROM Users WHERE email = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, currentUserEmail); // Use the current user's email to identify the record

            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                txtStatus.setText("User withdrawn successfully.");

            } else {
                txtStatus.setText("No user found with that email.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            txtStatus.setText("Error withdrawing user.");
        }
    }

    @FXML
    public void initialize() {
        connectToDatabase();
        cmbExperience.getItems().addAll(FXCollections.observableArrayList("Java", "Python", "C#", "C++", "Swift"));


        // Call loadUserData() here if currentUserEmail is set
        if (currentUserEmail != null) {
            try {
                loadUserData(); // Load data for the current user
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadUserData() throws SQLException {
        String query = "SELECT * FROM Users WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, currentUserEmail);

        System.out.println("Executing query for email: " + currentUserEmail);
        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            System.out.println("No user found with the email: " + currentUserEmail);
            txtStatus.setText("No application found for your email.");
            return; // Exit early if no user is found
        }

        do {
            if (resultSet.getString("accepted").equals("Accepted")) {
                System.out.println("accepted");
                txtStatus.setText("Your Application Was Accepted");
            } else if (resultSet.getString("accepted").equals("Rejected")) {
                System.out.println("rejected");
                txtStatus.setText("Your Application Was Not Accepted");
            }
            else{
                System.out.println(resultSet.getString("accepted"));
                txtStatus.setText("Your Application is being reviewed");
            }
            txfName.setText(resultSet.getString("username"));

            cmbExperience.setValue(resultSet.getString("Experience"));
        } while (resultSet.next());
    }

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email; // Set the current user's email
        System.out.println("Current User Email set to: " + currentUserEmail);
    }

    public void onBtnApply() {
        try {
            connectToDatabase();

            String updateQuery = "UPDATE Users SET username = ?, Experience = ? WHERE email = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, txfName.getText());
            updateStatement.setString(2, cmbExperience.getValue());
            updateStatement.setString(3, currentUserEmail);

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                txtStatus.setText("User information updated successfully.");
            } else {
                txtStatus.setText("No changes made or user not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            txtStatus.setText("Error updating user information.");
        }
    }
}

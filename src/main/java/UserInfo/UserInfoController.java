package UserInfo;

import com.example.callumaseprojects.HelloApplication;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author callumsmith on 2024-10-09
 */
public class UserInfoController {

    @FXML
    private TableColumn<Users, String> col_id;
    @FXML
    private TableColumn<Users, String> col_username;
    @FXML
    private TableColumn<Users, String> col_password;
    @FXML
    private TableColumn<Users, String> col_email;
    @FXML
    private TableColumn<Users, String> col_type;

    @FXML
    private TextField txt_username;
    @FXML
    private TextField txt_password;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_type;
    @FXML
    private TextField txt_id;


    @FXML
    TableView<Users> table_users;
    //turns content of table in columns to a list type
    ObservableList<Users> usersObservableList;

    int parameterIndex = 1;
    Connection connection;
    ResultSet resultSet;
    PreparedStatement preparedStatement;

    public UserInfoController() {
        connection = MyConnection.connect();
        resultSet = null;
        preparedStatement = null;
    }

    public void Add_Users() throws SQLException {
        String query = "INSERT INTO UserInfo(username, password, email, type) VALUES(?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        // Setting the values from your input fields
        preparedStatement.setString(1, txt_username.getText());
        preparedStatement.setString(2, txt_password.getText());
        preparedStatement.setString(3, txt_email.getText());
        preparedStatement.setString(4, txt_type.getText());

        // Execute the update (not query) as it's an INSERT operation
        preparedStatement.executeUpdate();

        System.out.println("User added successfully!");


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Added");
        alert.setContentText("User Added");
        alert.showAndWait();
        
    }


    public void getSelected() {
    }

    public void Delete() {
    }

    public void Edit() {
    }
}

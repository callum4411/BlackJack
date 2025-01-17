package AdminPage;

import java.sql.*;

import UserInfo.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import login.MyConnection;

import java.util.List;

/**
 * @author callumsmith on 2024-10-25
 */
public class AdminPageController {
    @FXML
    public TableView<User> table_users;

    @FXML
    public TableColumn<User, String> col_username;
    @FXML
    public TableColumn<User, String> col_password;
    @FXML
    public TableColumn<User, String> col_email;

    @FXML
    public TableColumn<User, String> col_exp;
    @FXML
    public TableColumn<User, String> col_acc;
    public ComboBox<String> cmbExp;
    @FXML TableColumn<User, Void> col_remove;
    Connection connection;
    @FXML
    public TextField txt_username;
    @FXML
    public TextField txt_password;
    @FXML
    public TextField txt_email;



    private ObservableList<User> userList = FXCollections.observableArrayList();
    private FilteredList<User> filteredUsers = new FilteredList<>(userList, user -> "user".equals(user.getType()));

    public AdminPageController() {
        connection = MyConnection.connect();
    }

    public void initialize() {
        cmbExp.getItems().addAll(FXCollections.observableArrayList("Java", "Python", "C#", "C++", "Swift"));



        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_username.setCellFactory(TextFieldTableCell.forTableColumn()); // Enable editing
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        col_password.setCellFactory(TextFieldTableCell.forTableColumn()); // Enable editing
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));

        col_exp.setCellValueFactory(new PropertyValueFactory<>("experience"));
        col_exp.setCellFactory(ChoiceBoxTableCell.forTableColumn("Java", "Python", "C#", "C++", "Swift"));
        col_acc.setCellValueFactory(new PropertyValueFactory<>("accepted"));
        col_acc.setCellFactory(ChoiceBoxTableCell.forTableColumn("Pending", "Rejected", "Accepted"));
        table_users.setEditable(true);
        col_username.setEditable(true);
        col_password.setEditable(true);
        col_email.setEditable(false);
        col_exp.setEditable(true);
        col_acc.setEditable(true);
        col_remove.setCellFactory(column -> new TableCell<User, Void>() {
            private final Button btn = new Button("Remove");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btn.setOnAction(event -> {
                        User user = getTableRow().getItem(); // Get the user for the current row
                        if (user != null) {
                            // Remove the user from the database
                            removeUserFromDatabase(user.getEmail());

                            // Remove the user from the TableView
                            getTableView().getItems().remove(user);
                        }
                    });
                    setGraphic(btn);
                    setText(null);
                }
            }
        });


        // Add the Remove column to the TableView
        table_users.getColumns().add(col_remove);

        loadUserData();

        col_acc.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String newStatus = event.getNewValue();
            user.setStatus(newStatus);

            // Update the database to reflect the change
            updateStatusInDatabase(user.getEmail(), newStatus);
        });
        col_username.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String change = event.getNewValue();
            user.setUsername(change);

            // Update the database to reflect the change
            updateUsernameInDatabase(user.getEmail(), change);
        });
        col_password.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String change = event.getNewValue();
            user.setPassword(change);

            // Update the database to reflect the change
            updatePasswordInDatabase(user.getEmail(), change);
        });
        col_exp.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String change = event.getNewValue();
            user.setExperience(change);

            // Update the database to reflect the change
            updateExperienceInDatabase(user.getEmail(), change);
        });


    }
    private void removeUserFromDatabase(String email) {
        String query = "DELETE FROM Users WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.executeUpdate();

            // Remove the user from the userList
            userList.removeIf(user -> user.getEmail().equals(email));

            // Refresh the TableView to show the updated list
            table_users.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateExperienceInDatabase(String email, String experience) {
        String query = "UPDATE Users SET Experience = ? WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, experience);
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private  void updatePasswordInDatabase(String email, String password) {
        String query = "UPDATE Users SET password = ? WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, password);
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    private void updateUsernameInDatabase(String email, String username){
        String query = "UPDATE Users SET username = ? WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void updateStatusInDatabase(String email, String status) {
        String query = "UPDATE Users SET accepted = ? WHERE email = ?";


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserData() {
        String url = "jdbc:sqlite:identifier.sqlite";
        String query = "SELECT username, password, email, type, experience, accepted FROM Users";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {

                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String type = rs.getString("type");
                String experience = rs.getString("Experience");
                String accepted = rs.getString("accepted");

                // Create a User object and add it to the list
                userList.add(new User(username, password, email, type, experience, accepted));
            }

            // Set the list in the TableView
            table_users.setItems(filteredUsers);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private List<String> ParseUserList(){
        System.out.println("test");
        return null;
    }

    public void Add_Users() {
        int failed=0;
        String query = "INSERT INTO Users(username, password, email, type, Experience, accepted) VALUES(?,?,?,?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Setting the values from your input fields
            preparedStatement.setString(1, txt_username.getText());
            preparedStatement.setString(2, txt_password.getText());
            preparedStatement.setString(3, txt_email.getText());
            preparedStatement.setString(4, "user");
            preparedStatement.setString(5, cmbExp.getValue());
            preparedStatement.setString(6, "Pending");


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
                alert.showAndWait();
            } else {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Application Error");
                alert.setContentText("There was a problem sending in your application");
                alert.showAndWait();
            }
        }

        if(failed ==0) {
            userList.add(new User(txt_username.getText(), txt_password.getText(), txt_email.getText(), "user", cmbExp.getValue(), "Pending"));
            table_users.refresh();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Application success");
            alert.setContentText("you application has been sent");
            alert.showAndWait();
            txt_username.clear();
            txt_password.clear();
            txt_email.clear();
            cmbExp.setValue("");
        }



        }


    public void getSelected(MouseEvent mouseEvent) {

    }
}

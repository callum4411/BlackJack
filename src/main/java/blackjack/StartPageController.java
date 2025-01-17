package blackjack;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPageController {
    @FXML
    private ListView<String> playerListView;
    @FXML
    private TextField newPlayerField;
    @FXML
    private Button startGameButton;

    private PlayerManager playerManager;
    private Player selectedPlayer;

    @FXML
    public void initialize() {
        playerManager = new PlayerManager();
        loadPlayersIntoList();

        // Disable Start Game button initially
        startGameButton.setDisable(true);
    }

    private void loadPlayersIntoList() {
        playerListView.setItems(FXCollections.observableArrayList(
                playerManager.getPlayers().stream()
                        .map(player -> player.getName() + " - Wins: " + player.getWins() + ", Losses: " + player.getLosses())
                        .toList()
        ));
    }


    @FXML
    private void onPlayerSelected() {
        String selected = playerListView.getSelectionModel().getSelectedItem();
        System.out.println("Selected item: " + selected); // Debug: Log selected item

        if (selected != null) {
            String playerName = selected.split(" - ")[0];
            selectedPlayer = playerManager.getPlayers().stream()
                    .filter(player -> player.getName().equals(playerName))
                    .findFirst()
                    .orElse(null);

            System.out.println("Selected player: " + (selectedPlayer != null ? selectedPlayer.getName() : "None")); // Debug
            startGameButton.setDisable(selectedPlayer == null);
            System.out.println("Start Game button enabled: " + !startGameButton.isDisabled()); // Debug
        }
    }

    @FXML
    private void onCreatePlayer() {
        String name = newPlayerField.getText().trim();
        if (!name.isEmpty()) {
            selectedPlayer = playerManager.createPlayer(name);
            loadPlayersIntoList();
            newPlayerField.clear();
        }
    }

    @FXML
    private void onStartGame() {
        if (selectedPlayer != null) {
            try {
                // Load the game screen FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/blackjack/GameScreen.fxml"));
                Scene gameScene = new Scene(loader.load());

                // Get the GameController and set the selected player
                GameController gameController = loader.getController();
                gameController.setPlayer(selectedPlayer);

                // Get the current stage and set the game scene
                Stage stage = (Stage) startGameButton.getScene().getWindow();
                stage.setScene(gameScene);
                stage.setTitle("Blackjack Game - " + selectedPlayer.getName());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onContinue() {

        if (selectedPlayer != null) {
            try {
                // Load the game screen FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/blackjack/GameScreen.fxml"));
                Scene gameScene = new Scene(loader.load());

                // Get the GameController and set the selected player
                GameController gameController = loader.getController();
                gameController.setPlayer(selectedPlayer);

                // Get the current stage and set the game scene
                Stage stage = (Stage) startGameButton.getScene().getWindow();
                stage.setScene(gameScene);
                stage.setTitle("Blackjack Game - " + selectedPlayer.getName());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

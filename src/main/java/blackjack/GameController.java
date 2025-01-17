package blackjack;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;



import java.io.IOException;
import java.net.URL;
import java.util.List;



public class GameController {
    @FXML
    public ImageView musicToggleImage;
    private Player currentPlayer;




    @FXML
    private HBox playerHandBox, dealerHandBox;
    @FXML
    private Button hitButton, standButton, restartButton;
    @FXML
    private Label playerScoreLabel, dealerScoreLabel, statusLabel;
    @FXML
    private Label playerStatsLabel; // Label for player stats
    @FXML
    private AnchorPane gameRoot;

    private BlackjackGame game;


    private AudioPlayer audioPlayer = new AudioPlayer();
    private boolean isMusicPlaying = false;




    // Set the current player
    public void setPlayer(Player player) {
        this.currentPlayer = player;
        System.out.println("player is now not null");
        updatePlayerStats();
    }

    @FXML
    public void initialize() {
        startNewGame();

        isMusicPlaying = false;
        URL musicOffIcon = getClass().getResource("/blackjack/icons/music_off.png");
        musicToggleImage.setImage(new Image(musicOffIcon.toExternalForm()));


    }



    public void toggleImage() {
        System.out.println("toggleImage");

        if (isMusicPlaying) {
            URL musicOffIcon = getClass().getResource("/blackjack/icons/music_off.png");
            audioPlayer.stop();
            isMusicPlaying = false;
            musicToggleImage.setImage(new Image(musicOffIcon.toExternalForm()));
        } else {
            URL musicOnIcon = getClass().getResource("/blackjack/icons/music_on.png");
            isMusicPlaying = true;
            musicToggleImage.setImage(new Image(musicOnIcon.toExternalForm()));

            // Play the first track initially
            audioPlayer.play("/blackjack/audio/music1.wav");
        }
    }






    // This starts the new game, it updates the stats label and makes the buttons not disabled as they might have been at the end of the previous hand
    private void startNewGame() {
        game = new BlackjackGame();
        updateUI();
        updatePlayerStats();
        statusLabel.setText("");
        hitButton.setDisable(false);
        standButton.setDisable(false);

        if (currentPlayer != null) {
            updatePlayerStats(); // Update stats at the start of a new game
        }
        else{
            System.out.println("game thinks player = null");
        }
    }
    //just sets the stats label to display the players stats
    private void updatePlayerStats() {
        if (currentPlayer != null) {
            System.out.println("updating player stats");
            playerStatsLabel.setText("Stats: " + currentPlayer.getWins() + " Wins, " + currentPlayer.getLosses() + " Losses");
        }
    }
    // hit button logic. if the player hits it sends that to the hit function in BlackjackGame
    @FXML
    private void onHit() {
        game.hit();
        updateUI();
        if (game.isGameOver()) {
            endGame();
        }
    }
    // same thing for hit but sends stand instead. a stand from the player always results in the game ending
    @FXML
    private void onStand() {
        game.stand();
        updateUI();
        endGame();
    }
    //restarts the hand
    @FXML
    private void onRestart() {
        startNewGame();
    }
    // lots of stuff to handle the options window
    @FXML
    private void onOptions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/blackjack/OptionsMenu.fxml"));
            Scene optionsScene = new Scene(loader.load());

            // Get the OptionsMenuController and pass this GameController reference
            OptionsMenuController optionsController = loader.getController();
            optionsController.setGameController(this);

            Stage optionsStage = new Stage();
            optionsStage.setScene(optionsScene);
            optionsStage.setTitle("Options Menu");
            optionsStage.initModality(Modality.APPLICATION_MODAL);
            optionsStage.initOwner(playerHandBox.getScene().getWindow());
            optionsStage.showAndWait(); // Wait for the options menu to close
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // this updates the UI with all relevant information that might have changes
    private void updateUI() {
        // Update player hand
        displayHand(playerHandBox, game.getPlayerHand(), true); // Always show the player's full hand

        // Update dealer hand
        displayHand(dealerHandBox, game.getDealerHand(), game.isRevealDealerCard()); // Conditionally reveal the second card

        // Update scores
        playerScoreLabel.setText("Player: " + game.calculateScore(game.getPlayerHand()));

        if (game.isRevealDealerCard()) {
            dealerScoreLabel.setText("Dealer: " + game.calculateScore(game.getDealerHand()));
        } else {
            dealerScoreLabel.setText("Dealer: " + (game.getDealerHand().getFirst().getValue()));
        }

        // Dynamically style labels based on content
        if (playerScoreLabel.getText().contains("Player")) {
            playerScoreLabel.setStyle("-fx-text-fill: #008000;"); // Green for "Player"
        }
        if (dealerScoreLabel.getText().contains("Dealer")) {
            dealerScoreLabel.setStyle("-fx-text-fill: #ff4500;"); // Orange for "Dealer"
        }
    }


    public void displayHand(HBox cardContainer, List<Card> hand, boolean revealSecondCard) {
        // Clear the container to remove any existing cards
        cardContainer.getChildren().clear();

        for (int i = 0; i < hand.size(); i++) {
            String imagePath;

            // If it's the dealer's second card and it should remain hidden, use the card back image
            if (i == 1 && !revealSecondCard) {
                imagePath = "/blackjack/images/card_back.png"; // Ensure this image exists
            } else {
                imagePath = "/blackjack/images/" + hand.get(i).getImageName(); // Assuming Card has getImageName()
            }

            URL resourceUrl = getClass().getResource(imagePath);
            if (resourceUrl == null) {
                System.out.println("Image not found: " + imagePath);
                continue;
            }

            Image cardImage = new Image(resourceUrl.toExternalForm());
            ImageView cardView = new ImageView(cardImage);
            cardView.setFitWidth(100); // Adjust card size
            cardView.setPreserveRatio(true);
            cardContainer.getChildren().add(cardView);
        }
    }

    // when the game ends it disables the hit and stand buttons, it then determines the winner
    private void endGame() {
        hitButton.setDisable(true);
        standButton.setDisable(true);

        int playerScore = game.calculateScore(game.getPlayerHand());
        int dealerScore = game.calculateScore(game.getDealerHand());

        if (playerScore > 21 || (dealerScore <= 21 && dealerScore > playerScore)) {
            statusLabel.setText("You lose!");
            currentPlayer.addLoss();
        } else if (playerScore == dealerScore) {
            statusLabel.setText("It's a tie!");
        } else {
            statusLabel.setText("You win!");
            currentPlayer.addWin();
        }

        PlayerManager playerManager = new PlayerManager();
        playerManager.updatePlayer(currentPlayer); // Save updated stats
        updatePlayerStats();
    }

    // this is called by the option's menu. it changes the background
    public void setBackground(String imagePath) {
        URL resource = getClass().getResource(imagePath);

        if (resource != null) {
            String backgroundStyle = "-fx-background-image: url('" + resource.toExternalForm() + "'); -fx-background-size: cover;";
            gameRoot.setStyle(backgroundStyle);
            System.out.println("Applied background: " + resource.toExternalForm());
        } else {
            System.out.println("Failed to find resource: " + imagePath);
        }
    }



}

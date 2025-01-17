package blackjack;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class OptionsMenuController {
    @FXML
    private ComboBox<String> backgroundSelector;

    private GameController gameController;

    // Default background style
    private static String selectedBackgroundStyle = "-fx-background-image: url('/blackjack/images/background.jpeg'); -fx-background-size: cover;";

    @FXML
    public void initialize() {
        // Add options to the combo box
        backgroundSelector.getItems().addAll("Background 1", "Background 2");
        backgroundSelector.setValue("Background 1"); // Default selection
    }

    @FXML
    private void onApply() {
        // Determine the selected background style
        String choice = backgroundSelector.getValue();
        if ("Background 1".equals(choice)) {
            selectedBackgroundStyle = "/blackjack/images/background.jpeg";
        } else if ("Background 2".equals(choice)) {
            selectedBackgroundStyle = "/blackjack/images/background_alt.jpeg";
        }

        // Apply the selected background style to the game screen
        if (gameController != null) {
            gameController.setBackground(selectedBackgroundStyle);
        }
    }

    @FXML
    private void onBack() {
        // Close the options menu (assuming it's a separate modal stage)
        if (backgroundSelector.getScene() != null && backgroundSelector.getScene().getWindow() != null) {
            backgroundSelector.getScene().getWindow().hide();
        }
    }

    // Setter to pass the GameController reference
    public void setGameController(GameController gameController) {
        this.gameController = gameController;

        // Set the ComboBox to match the current background
        if (selectedBackgroundStyle.contains("background.jpeg")) {
            backgroundSelector.setValue("Background 1");
        } else if (selectedBackgroundStyle.contains("background_alt.jpeg")) {
            backgroundSelector.setValue("Background 2");
        }
    }
}

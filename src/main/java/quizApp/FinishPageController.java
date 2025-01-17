package quizApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FinishPageController {

    @FXML
    private TableView<SummaryRow> summaryTable;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label timeTakenLabel;
    @FXML
    private Label performanceLabel;
    @FXML
    private Button retakeButton;
    @FXML
    private Button closeButton;

    private int score;
    private int totalQuestions;
    private int timeTakenInSeconds;
    private int bestTimeInSeconds;
    private int highestScore;
    private List<QuestionAnswer> questionList;
    private List<String> selectedAnswers;

    private String userEmail;

    // Method to initialize the controller with quiz data
    public void setQuizData(int score, int totalQuestions, int timeTakenInSeconds, int bestTimeInSeconds, int highestScore, List<QuestionAnswer> questionList, List<String> selectedAnswers, String userEmail) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timeTakenInSeconds = timeTakenInSeconds;
        this.bestTimeInSeconds = bestTimeInSeconds;
        this.highestScore = highestScore;
        this.questionList = questionList;
        this.selectedAnswers = selectedAnswers;
        this.userEmail = userEmail;

        populateSummary();
        updatePerformanceLabels();
        scoreLabel.setText("Your Score: " + score + " out of " + totalQuestions);
        timeTakenLabel.setText("Time Taken: " + formatTime(timeTakenInSeconds));
    }

    // Populate the summary table
    private void populateSummary() {
        TableColumn<SummaryRow, String> questionCol = new TableColumn<>("Question");
        questionCol.setCellValueFactory(new PropertyValueFactory<>("question"));

        TableColumn<SummaryRow, String> yourAnswerCol = new TableColumn<>("Your Answer");
        yourAnswerCol.setCellValueFactory(new PropertyValueFactory<>("yourAnswer"));

        TableColumn<SummaryRow, String> correctAnswerCol = new TableColumn<>("Correct Answer");
        correctAnswerCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

        TableColumn<SummaryRow, String> answerStatusCol = new TableColumn<>("Answer Status");
        answerStatusCol.setCellValueFactory(new PropertyValueFactory<>("answerStatus"));

        summaryTable.getColumns().setAll(questionCol, yourAnswerCol, correctAnswerCol, answerStatusCol);

        for (int i = 0; i < questionList.size(); i++) {
            String question = questionList.get(i).getQuestion();
            String correctAnswer = questionList.get(i).getAnswer();
            String yourAnswer = selectedAnswers.get(i) == null ? "No Answer" : selectedAnswers.get(i);
            String ansStatus = "";
            if(yourAnswer.equals(correctAnswer)) {
                //ansStatus = "Correct";
                ansStatus = "✅";
            }
            else{
                //ansStatus = "Wrong";
                ansStatus = "❌";
            }

            summaryTable.getItems().add(new SummaryRow(question, yourAnswer, correctAnswer, ansStatus));
        }
    }

    // Update performance labels
    private void updatePerformanceLabels() {
        String timeComparison = timeTakenInSeconds < bestTimeInSeconds
                ? "You improved your time by " + formatTime(bestTimeInSeconds - timeTakenInSeconds) + " from your best time!"
                : "Your time increased by " + formatTime(timeTakenInSeconds - bestTimeInSeconds) + " from your best time.";

        String scoreComparison = score > highestScore
                ? "You achieved a new high score!"
                : score < highestScore
                ? "Your score is " + (highestScore - score) + " points lower than your best score."
                : "You matched your highest score!";

        performanceLabel.setText(timeComparison + "\n" + scoreComparison);
    }

    // Format seconds into mm:ss format
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    // Close the window
    @FXML
    private void onClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    // Retake the quiz
    @FXML
    private void onRetake() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quizApp/quizApp-view.fxml"));
            Stage stage = (Stage) retakeButton.getScene().getWindow();

            // Load the FXML and create a Scene
            Scene scene = new Scene(loader.load());

            // Add the CSS file to the Scene
            scene.getStylesheets().add(getClass().getResource("/quizApp/quizApp.css").toExternalForm());

            // Set the Scene to the Stage
            stage.setScene(scene);

            // Get QuizController instance and set the user email
            quizAppController quizController = loader.getController();
            quizController.setUserEmail(userEmail);

            stage.setTitle("Quiz Application");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            ;
        }
    }


    // Inner class to represent a row in the summary table
    public static class SummaryRow {
        private final String question;
        private final String yourAnswer;
        private final String correctAnswer;


        public SummaryRow(String question, String yourAnswer, String correctAnswer, String ansStatus) {
            this.question = question;
            this.yourAnswer = yourAnswer;
            this.correctAnswer = correctAnswer;

        }

        public String getQuestion() {
            return question;
        }

        public String getYourAnswer() {
            return yourAnswer;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }
        public String getAnswerStatus() {
            if(yourAnswer.equals(correctAnswer)) {
                return "✅";
            }
            else{
                return "❌";
            }
        }
    }
}

package quizApp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class quizAppController {

    @FXML
    private Button btnStart, btnNext, btnBack, btnSubmit;
    @FXML
    private Text txtQuestion;
    @FXML
    private RadioButton optionA, optionB, optionC, optionD;
    private ToggleGroup optionsGroup;

    private ArrayList<QuestionAnswer> questionList;
    private ArrayList<String> selectedAnswers;
    private ArrayList<Boolean> correctAnswers;
    private int currentQuestionIndex = 0;
    private String userEmail;

    // Timer-related fields
    private Timeline timer;
    private int elapsedTimeInSeconds = 0;
    @FXML
    private Label lbltimerDisplay;

    public quizAppController() {
        //outputs a list of questions in csv file
        QuizReader qr = new QuizReader();
        try {
            questionList = qr.readFile("/quizApp/questions.csv");
        } catch (IOException e) {
            e.printStackTrace();
            questionList = new ArrayList<>(); // Initialize to avoid null pointer issues
        }

        selectedAnswers = new ArrayList<>();
        correctAnswers = new ArrayList<>();

        // Initialize lists to match the number of questions
        for (int i = 0; i < questionList.size(); i++) {
            selectedAnswers.add(null); // Initially, no answers are selected
            correctAnswers.add(false); // Initially, no answers are marked correct
        }
    }

    @FXML
    public void initialize() {
        //Stops questions from being answered initially
        optionsGroup = new ToggleGroup();
        optionA.setToggleGroup(optionsGroup);
        optionB.setToggleGroup(optionsGroup);
        optionC.setToggleGroup(optionsGroup);
        optionD.setToggleGroup(optionsGroup);

        // Disable buttons and radio buttons initially
        disableQuizInteraction(true);

        btnBack.setDisable(true); // Disable BACK button initially
        btnSubmit.setDisable(true); // Disable SUBMIT button until the end

        if (!questionList.isEmpty()) {
            displayQuestion(); // Load the first question when the app starts
        }
    }

    public void start() {
        //now lets questions be answered after the user clicks start
        disableQuizInteraction(false); // Enable interactive elements
        btnStart.setDisable(true); // Disable START button after click
        startTimer(); // Start the timer
        displayQuestion();
    }

    public void onbtnNext() {
        saveSelectedAnswer(); // Save the current answer before moving to the next question

        if (currentQuestionIndex < questionList.size() - 1) {//checks if on last question
            currentQuestionIndex++;
            displayQuestion();
        }

        btnBack.setDisable(currentQuestionIndex == 0); // Disable BACK button if on the first question
        btnNext.setDisable(currentQuestionIndex >= questionList.size() - 1); // Disable NEXT button if on the last question
        btnSubmit.setDisable(currentQuestionIndex < questionList.size() - 1); // Enable SUBMIT button on the last question
    }

    public void onbtnBack() {
        saveSelectedAnswer(); // Save the current answer before moving back

        if (currentQuestionIndex > 0) { //checks if on first question
            currentQuestionIndex--;
            displayQuestion();
        }
        btnNext.setDisable(false); // Re-enable NEXT button
        btnBack.setDisable(currentQuestionIndex == 0); // Disable BACK button if on the first question
        btnSubmit.setDisable(true); // Disable SUBMIT button if not on the last question
    }

    public void onbtnSubmit() {
        saveSelectedAnswer();//saves the currently selected answer
        stopTimer(); // Stop the timer

        int score = calculateScore(); // Calculate the score based on correct answers

        // Format the elapsed time to append to the scoreID
        String formattedTime = formatElapsedTime();
        int attemptNumber = findAttemptNum() + 1;

        QuizScoreManager qm = new QuizScoreManager(); //creates a new QuizScoreManager to add the quiz info to database
        int previousHighScore = qm.getHighestScore(userEmail);
        String previousBestTime = qm.getBestTime(userEmail);
        qm.insertQuizScore(userEmail, attemptNumber, score, formattedTime);

        try {
            // Load the FinishPage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quizApp/finishPage-view.fxml"));
            Stage finishStage = new Stage();
            Scene finishScene = new Scene(loader.load());

            // Apply CSS to the finishPage
            String cssPath = getClass().getResource("/quizApp/finishPage.css").toExternalForm();
            finishScene.getStylesheets().add(cssPath);

            finishStage.setScene(finishScene);
            finishStage.setTitle("Quiz Summary");

            // Pass data to FinishPageController
            FinishPageController finishController = loader.getController();
            finishController.setQuizData(score, questionList.size(), elapsedTimeInSeconds, timeToSeconds(previousBestTime), previousHighScore, questionList, selectedAnswers, userEmail);

            // Show the FinishPage
            finishStage.show();

            // Close the current quiz page
            Stage quizStage = (Stage) btnSubmit.getScene().getWindow();
            quizStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private int findAttemptNum() {//finds the attempt number through QuizScoreManager with function findAttemptNumber
        QuizScoreManager manager = new QuizScoreManager();
        return manager.findAttemptNumber(userEmail);
    }

    private void displayQuestion() {
        if (questionList.isEmpty() || currentQuestionIndex >= questionList.size()) {
            return; // Safety check in case there are no questions
        }

        QuestionAnswer currentQuestion = questionList.get(currentQuestionIndex);//gets info for current question and fills it in on screen
        txtQuestion.setText(currentQuestion.getQuestion());
        optionA.setText(currentQuestion.getA());
        optionB.setText(currentQuestion.getB());
        optionC.setText(currentQuestion.getC());
        optionD.setText(currentQuestion.getD());

        // Retrieve and set the previous answer selection if it exists
        String previousAnswer = selectedAnswers.get(currentQuestionIndex);
        if (previousAnswer != null) {
            if (previousAnswer.equals(optionA.getText())) optionA.setSelected(true);
            else if (previousAnswer.equals(optionB.getText())) optionB.setSelected(true);
            else if (previousAnswer.equals(optionC.getText())) optionC.setSelected(true);
            else if (previousAnswer.equals(optionD.getText())) optionD.setSelected(true);
        } else {
            optionsGroup.selectToggle(null); // Clear selection if no previous answer
        }
    }

    private void saveSelectedAnswer() {
        RadioButton selectedOption = (RadioButton) optionsGroup.getSelectedToggle();
        if (selectedOption != null) {
            String selectedText = selectedOption.getText();
            selectedAnswers.set(currentQuestionIndex, selectedText);
            // Update the correctness of the current answer
            correctAnswers.set(currentQuestionIndex, selectedText.equals(questionList.get(currentQuestionIndex).getAnswer()));
        } else {
            selectedAnswers.set(currentQuestionIndex, null); // Clear the selection if no option is selected
            correctAnswers.set(currentQuestionIndex, false); // Mark as incorrect if no answer is selected
        }
    }

    public void setUserEmail(String email) {//this is what allows the userEmail to be passed from login to quiz app
        this.userEmail = email;
    }

    private int calculateScore() {//calculates the score
        int score = 0;
        for (boolean isCorrect : correctAnswers) {
            if (isCorrect) {
                score++;
            }
        }
        return score;
    }

    // Timer methods
    private void startTimer() {//Starts the timer that times the quiz
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsedTimeInSeconds++;
            String formattedTime = formatElapsedTime();
            lbltimerDisplay.setText("Time: " + formattedTime);
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {//stops the timer
        if (timer != null) {
            timer.stop();
        }
    }

    private String formatElapsedTime() {//formats time from seconds to minutes and seconds to be displayed and put in database
        int minutes = elapsedTimeInSeconds / 60;
        int seconds = elapsedTimeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    public int timeToSeconds(String formattedTime) {//unformats the time back to seconds
        String[] parts = formattedTime.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return (minutes * 60) + seconds;
    }


    private void disableQuizInteraction(boolean disable) {//disables all quiz interactables so that questions cant be answered until triggered(on start button)
        // Disable/enable radio buttons
        optionA.setDisable(disable);
        optionB.setDisable(disable);
        optionC.setDisable(disable);
        optionD.setDisable(disable);

        // Disable/enable navigation buttons
        btnNext.setDisable(disable);
        btnBack.setDisable(disable);
        btnSubmit.setDisable(disable);
    }
}

package quizApp;

/**
 * @author callumsmith on 2024-11-06
 */
public class QuizScoresTesting {
    public static class Main {
        public static void main(String[] args) {
            QuizScoreManager manager = new QuizScoreManager();
            String userEmail = "smithcallum918@gmail.com";
            //int attemptNumber = manager.findAttemptNumber(userEmail);
            //int score = 85;

            // Insert a new quiz score
            ///manager.insertQuizScore(userEmail, attemptNumber, score);

            // Update the score if needed
            //int updatedScore = 90;
            //manager.updateQuizScore(userEmail, attemptNumber, updatedScore);

            // find attempt number
            System.out.println("attempt number: " + manager.findAttemptNumber(userEmail));
        }
    }
}

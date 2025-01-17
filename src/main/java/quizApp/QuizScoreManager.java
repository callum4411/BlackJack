package quizApp;

import quizApp.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizScoreManager {
    // Inserts a new score record for a quiz attempt
    public void insertQuizScore(String email, int attemptNumber, int totalScore, String time) {
        String scoreID = attemptNumber + "-" + email + "-" + time;
        String sql = "INSERT INTO QuizScore(scoreID, email, totalScore) VALUES(?, ?, ?)";

        try (Connection conn = MyConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, scoreID);
            pstmt.setString(2, email);
            pstmt.setInt(3, totalScore);


            pstmt.executeUpdate();
            System.out.println("Score added successfully for " + email + " on attempt " + attemptNumber);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Updates an existing score record
    public void updateQuizScore(String email, int attemptNumber, int totalScore) {
        String scoreID = attemptNumber + "-" + email;
        String sql = "UPDATE QuizScore SET totalScore = ? WHERE scoreID LIKE ?";

        try (Connection conn = MyConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, totalScore);
            pstmt.setString(2, scoreID + "%"); // Match the scoreID with the attempt number and email prefix

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Score updated successfully for " + email + " on attempt " + attemptNumber);
            } else {
                System.out.println("No record found with scoreID " + scoreID);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Finds the highest attempt number for a given user
    public int findAttemptNumber(String email) {
        String sql = "SELECT MAX(CAST(SUBSTR(scoreID, 1, INSTR(scoreID, '-') - 1) AS INTEGER)) AS maxAttempt " +
                "FROM QuizScore WHERE email = ?";

        try (Connection conn = MyConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("maxAttempt"); // Return the maximum attempt number
            }
        } catch (SQLException e) {
            System.out.println("Error finding attempt number: " + e.getMessage());
        }
        return 0; // Return 0 if no records found or an error occurs
    }

    // Gets the highest score for a user
    public int getHighestScore(String email) {
        String sql = "SELECT MAX(totalScore) AS highestScore FROM QuizScore WHERE email = ?";

        try (Connection conn = MyConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("highestScore"); // Return the highest score
            }
        } catch (SQLException e) {
            System.out.println("Error finding highest score: " + e.getMessage());
        }
        return 0; // Return 0 if no scores found or an error occurs
    }

    // Gets the best (shortest) time for a user
    public String getBestTime(String email) {
        String sql = "SELECT scoreID FROM QuizScore WHERE email = ?";
        String bestTime = "00:00";
        int bestTimeInSeconds = Integer.MAX_VALUE;

        try (Connection conn = MyConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String scoreID = rs.getString("scoreID");
                // Extract the time portion from the scoreID
                String timePart = scoreID.substring(scoreID.lastIndexOf('-') + 1);
                int timeInSeconds = convertFormattedTimeToSeconds(timePart);

                if (timeInSeconds < bestTimeInSeconds) {
                    bestTimeInSeconds = timeInSeconds;
                    bestTime = timePart;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding best time: " + e.getMessage());
        }

        return bestTime;
    }

    // Utility method to convert formatted time to seconds
    private int convertFormattedTimeToSeconds(String formattedTime) {
        String[] parts = formattedTime.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return (minutes * 60) + seconds;
    }

}

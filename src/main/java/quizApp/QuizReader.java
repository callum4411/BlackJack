package quizApp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
    //reads the csv file to get info for quiz
public class QuizReader {

    public ArrayList<QuestionAnswer> readFile(String filename) throws IOException {
        ArrayList<QuestionAnswer> questionAnswers = new ArrayList<>();

        // Load the file as a resource
        InputStream inputStream = getClass().getResourceAsStream(filename);
        if (inputStream == null) {
            throw new IOException("File not found: " + filename);
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            String[] qa = line.split(",");
            questionAnswers.add(new QuestionAnswer(Integer.parseInt(qa[0]), qa[1], qa[2], qa[3], qa[4], qa[5], qa[6]));
        }
        return questionAnswers;
    }
}


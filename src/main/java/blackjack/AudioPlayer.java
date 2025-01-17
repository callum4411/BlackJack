package blackjack;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {
    private Clip clip;
    private Runnable onEndCallback;

    public void play(String audioFilePath) {
        try {
            // Stop any currently playing clip
            stop();

            // Load the audio file
            URL audioUrl = getClass().getResource(audioFilePath);
            if (audioUrl == null) {
                System.out.println("Audio file not found: " + audioFilePath);
                return;
            }

            // Set up audio input stream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioUrl);

            // Set up the clip and open the audio stream
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Add a LineListener to detect when the audio ends
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    if (onEndCallback != null) {
                        onEndCallback.run(); // Trigger the callback
                    }
                }
            });

            // Start playing the audio
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }


}

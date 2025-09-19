import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

public class CountdownTimer {

    private int countdownTime;
    private final Text timerText;
    private final Timeline timeline; // declare field here

    public CountdownTimer(int totalSeconds) {
        this.countdownTime = totalSeconds;

        // 1) Create your Text
        this.timerText = new Text(formatTime(countdownTime));
        timerText.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");

        // 2) Initialize the timeline as empty first
        this.timeline = new Timeline();
        this.timeline.setCycleCount(Timeline.INDEFINITE);

        // 3) Create your KeyFrame separately, using the already-assigned timeline field
        KeyFrame oneSecondFrame = new KeyFrame(
                Duration.seconds(1),
                event -> {
                    countdownTime--;
                    timerText.setText(formatTime(countdownTime));

                    if (countdownTime <= 0) {
                        timeline.stop();
                        // Optional: do something else when time hits 0
                    }
                }
        );

        // 4) Add the KeyFrame to the timeline
        this.timeline.getKeyFrames().add(oneSecondFrame);
    }

    public void start() {
        timeline.play();
    }

    /**
     * Returns how many seconds remain in this countdown.
     */
    public int getTimeRemaining() {
        return countdownTime;
    }

    public void stop() {
        timeline.stop();
    }

    public Text getTimerText() {
        return timerText;
    }

    /**
     * Waits 5 seconds, then forces the timer to be 3 seconds.
     */
    public void waitThenSetTimerTo3() {
        PauseTransition fiveSecondPause = new PauseTransition(Duration.seconds(5));
        fiveSecondPause.setOnFinished(e -> {
            countdownTime = 3;
            timerText.setText(formatTime(countdownTime));
        });
        fiveSecondPause.play();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public void reset(int newSeconds) {
        timeline.stop();         // stop the old countdown
        this.countdownTime = newSeconds;
        timerText.setText(formatTime(countdownTime));
        // You can optionally do timeline.playFromStart() here or leave it
        // to the calling code to call start() again.
    }

}



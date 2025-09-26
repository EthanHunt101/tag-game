import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameScene {
    private Scene scene;

    // Keep track of scores across rounds
    private int redScore = 0;
    private int blueScore = 0;

    // Simple references to UI text we need to update
    private Text actualRedScore;
    private Text actualBlueScore;
    private Text itText; // shows who is it
    private CountdownTimer timer;

    // score to win
    private final int winScore = 5;

    private PlayerObject playerR;
    private PlayerObject playerB;

    // Tracks who is "it" for the current round
    private boolean redIsIt;

    // Player radius and objects
    private final int playerRadius = 25;

    // round time in seconds
    private final int roundTime = 10;

    // Movement arrays [up, dwn, L, R]
    private final int[] redMoving = {0, 0, 0, 0};
    private final int[] blueMoving = {0, 0, 0, 0};

    public GameScene(Stage stage) {
        redScore = 0;
        blueScore = 0;
        this.scene = createGameScene(stage);
    }

    public Scene getScene() {
        return scene;
    }

    private Scene createGameScene(Stage stage) {
        // BorderPane for layout
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));
        layout.getStyleClass().add("game-root");

        // These will be moved to different classes once we create the map selection page
        Canvas arena = new Canvas(800, 600);
        GraphicsContext gc = arena.getGraphicsContext2D();
        layout.setCenter(arena);

        // HBoxes + VBoxes for top/bottom UI
        HBox hboxTop = new HBox();
        hboxTop.setSpacing(20);
        hboxTop.setPadding(new Insets(10));
        hboxTop.setAlignment(Pos.CENTER);
        hboxTop.getStyleClass().add("topbar");

        HBox hboxBottom = new HBox();
        hboxBottom.setSpacing(20);
        hboxBottom.setPadding(new Insets(10));
        hboxBottom.setAlignment(Pos.CENTER);
        hboxBottom.getStyleClass().add("bottombar");

        VBox vboxTopLeft = new VBox(5);
        vboxTopLeft.setAlignment(Pos.CENTER);

        VBox vboxTopRight = new VBox(5);
        vboxTopRight.setAlignment(Pos.CENTER);

        // Create Score Text
        Text staticRedScore = new Text("Red's Score:");
        staticRedScore.getStyleClass().add("score-label");
        actualRedScore = new Text(String.valueOf(redScore));
        actualRedScore.getStyleClass().add("score-value");

        Text staticBlueScore = new Text("Blue's Score:");
        staticBlueScore.getStyleClass().add("score-label");
        actualBlueScore = new Text(String.valueOf(blueScore));
        actualBlueScore.getStyleClass().add("score-value");

        vboxTopLeft.getChildren().addAll(staticRedScore, actualRedScore);
        vboxTopRight.getChildren().addAll(staticBlueScore, actualBlueScore);

        // Create Timer
        timer = new CountdownTimer(roundTime);
        Text timerText = timer.getTimerText();
        timerText.getStyleClass().add("timer");
        timer.start();

        // Add them to the top bar
        hboxTop.getChildren().addAll(vboxTopLeft, timerText, vboxTopRight);
        layout.setTop(hboxTop);

        // Text indicating who is it
        itText = new Text();
        itText.getStyleClass().add("it-indicator");
        hboxBottom.getChildren().add(itText);
        layout.setBottom(hboxBottom);

        // Create players
        playerR = new PlayerObject();
        playerR.setXmin(0);
        playerR.setXmax(arena.getWidth() - playerRadius);
        playerR.setYmin(0);
        playerR.setYmax(arena.getHeight() - playerRadius);

        playerB = new PlayerObject();
        playerB.setXmin(0);
        playerB.setXmax(arena.getWidth() - playerRadius);
        playerB.setYmin(0);
        playerB.setYmax(arena.getHeight() - playerRadius);

        // Position them in the center
        resetRoundPositions();

        // Decide randomly who is “it” and update display
        chooseRandomIt();

        // Creating the obstacles for first map (placeholder examples)
        RectangleObstacle obstacle1 = new RectangleObstacle(new Point(23,3), new Point(15,5));
        CircleObstacle obstacle2 = new CircleObstacle(new Point(500, 300), 50);

        // create array of obstacles for the map
        Obstacle[] obstacles = {obstacle1, obstacle2};

        // Create the Scene
        Scene gameScene = new Scene(layout);
        gameScene.getStylesheets().add(getClass().getResource("game-theme.css").toExternalForm());

        // Handle key presses
        gameScene.setOnKeyPressed((KeyEvent ke) -> {
            KeyCode kc = ke.getCode();
            // Red
            if (kc == KeyCode.Q) { playerR.dash(redMoving); }
            if (kc == KeyCode.W) { redMoving[0] = 1; }
            if (kc == KeyCode.S) { redMoving[1] = 1; }
            if (kc == KeyCode.A) { redMoving[2] = 1; }
            if (kc == KeyCode.D) { redMoving[3] = 1; }

            // Blue
            if (kc == KeyCode.SHIFT) { playerB.dash(blueMoving); }
            if (kc == KeyCode.UP)    { blueMoving[0] = 1; }
            if (kc == KeyCode.DOWN)  { blueMoving[1] = 1; }
            if (kc == KeyCode.LEFT)  { blueMoving[2] = 1; }
            if (kc == KeyCode.RIGHT) { blueMoving[3] = 1; }
        });

        // Handle key releases
        gameScene.setOnKeyReleased((KeyEvent kev) -> {
            KeyCode kd = kev.getCode();
            // Red
            if (kd == KeyCode.W) { redMoving[0] = 0; }
            if (kd == KeyCode.S) { redMoving[1] = 0; }
            if (kd == KeyCode.A) { redMoving[2] = 0; }
            if (kd == KeyCode.D) { redMoving[3] = 0; }
            // Blue
            if (kd == KeyCode.UP)    { blueMoving[0] = 0; }
            if (kd == KeyCode.DOWN)  { blueMoving[1] = 0; }
            if (kd == KeyCode.LEFT)  { blueMoving[2] = 0; }
            if (kd == KeyCode.RIGHT) { blueMoving[3] = 0; }
        });

        // Main game loop
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Clear & re-draw the background
                gc.clearRect(0, 0, arena.getWidth(), arena.getHeight());
                gc.setFill(Color.LIGHTGRAY);
                gc.fillRect(0, 0, arena.getWidth(), arena.getHeight());

                // Move players
                playerR.advArrayMove(redMoving);
                playerB.advArrayMove(blueMoving);


                // check object collisions
                for (Obstacle obstacle : obstacles) {
                    if (obstacle instanceof CircleObstacle) {
                        // circle collision logic here
                    } else if (obstacle instanceof RectangleObstacle) {
                        // rectangle collision logic here
                    }
                }

                // Draw red
                gc.setFill(Color.RED);
                gc.fillOval(playerR.getXpos(), playerR.getYpos(), playerRadius, playerRadius);

                // Draw blue
                gc.setFill(Color.BLUE);
                gc.fillOval(playerB.getXpos(), playerB.getYpos(), playerRadius, playerRadius);

                // check for tagging and handle score, timer
                checkTagging();



                // Check if timer hit 0
                if (timer.getTimeRemaining() <= 0) {
                    if (redIsIt) {
                        blueScore++;
                    } else {
                        redScore++;
                    }
                    resetRoundPositions();
                    redIsIt = !redIsIt;
                    timer.reset(roundTime);
                    timer.start();
                }

                // update text for the displays
                actualBlueScore.setText(String.valueOf(blueScore));
                actualRedScore.setText(String.valueOf(redScore));
                if (redIsIt) {
                    itText.setText("Red is it!");
                } else {
                    itText.setText("Blue is it!");
                }

                // end game if someone wins
                if (blueScore >= winScore) {
                    stop();
                    new EndGameScene(stage, false);
                    return;
                }
                if (redScore >= winScore) {
                    stop();
                    new EndGameScene(stage, true);
                }
            }
        };
        gameLoop.start();

        return gameScene;
    }

    /**
     * check collisions between players for logic (tagging)
     */
    private void checkTagging() {
        double dist = Math.sqrt(Math.pow(playerR.getXpos() - playerB.getXpos(), 2)
                + Math.pow(playerR.getYpos() - playerB.getYpos(), 2));
        if (dist <= 2 * playerRadius) {
            resetRoundPositions();
            if (redIsIt) {
                redScore++;
            } else {
                blueScore++;
            }
            // toggle it
            redIsIt = !redIsIt;
            // reset and start timer
            timer.reset(roundTime);
            timer.start();
        }
    }

    /*
     * check a player's collision with a rectangle object (placeholder)
     */
    private void checkRectCollide(PlayerObject player, RectangleObstacle rect){
        double px = player.getXpos();
        double py = player.getYpos();
        double xl = rect.getBottomLeft().getX();
        double yb = rect.getBottomLeft().getY();
        double xr = rect.getTopRight().getX();
        double yt = rect.getTopRight().getY();
        if (py - playerRadius <= yt && py + playerRadius >= yb && px + playerRadius >= xl && px - playerRadius <= xr) {
            if (px < xl) { player.setXvel(0); }
            if (px > xr) { player.setXvel(0); }
            if (py > yt) { player.setYvel(0); }
            if (py < yb) { player.setXvel(0); }
        }
    }

    /**
     * Randomly choose which player is "it" (50/50).
     */
    private void chooseRandomIt() {
        redIsIt = (Math.random() < 0.5);
        if (redIsIt) {
            itText.setText("Red is it!");
        } else {
            itText.setText("Blue is it!");
        }
    }

    /**
     * Reset the player positions to the center again (start of a new round).
     */
    private void resetRoundPositions() {
        // reset moving array
        for (int i = 0; i < 4; i++) {
            redMoving[i] = 0;
            blueMoving[i] = 0;
        }
        // reset velocities
        playerR.setXvel(0);
        playerR.setYvel(0);
        playerB.setXvel(0);
        playerB.setYvel(0);
        // reset positions
        playerR.setXpos(700 - playerRadius / 2.0);
        playerR.setYpos(300 - playerRadius / 2.0);
        playerB.setXpos(400 - playerRadius / 2.0);
        playerB.setYpos(300 - playerRadius / 2.0);
    }
}

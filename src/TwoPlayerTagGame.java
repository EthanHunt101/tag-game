// File: TwoPlayerTagGame.java
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TwoPlayerTagGame extends Application {

    // Keep track of the level selected by the user
    private int mapSelected = 1;

    // Keep track of scores across rounds
    private int redScore = 0;
    private int blueScore = 0;

    // Tracks who is "it" for the current round
    private boolean redIsIt;

    // Simple references to UI text we need to update
    private Text actualRedScore;
    private Text actualBlueScore;
    private Text itText; // shows who is it
    private CountdownTimer timer;

    // Player radius and objects
    private final int playerRadius = 25;
    private PlayerObject playerR;
    private PlayerObject playerB;

    // round time in seconds
    private final int roundTime = 10;
    // score to win
    private final int winScore = 5;

    // Movement arrays [up, dwn, L, R]
    private final int[] redMoving = {0, 0, 0, 0};
    private final int[] blueMoving = {0, 0, 0, 0};

    @Override
    public void start(Stage primaryStage) {
        Scene menuScene = createMenuScene(primaryStage);
        primaryStage.setTitle("Two Player Tag - Menu");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    /**
     * -----------------------------
     *     MENU SCENE
     * -----------------------------
     */
    private Scene createMenuScene(Stage stage) {
        // We'll use a StackPane to layer a background rectangle behind our content
        StackPane menuLayout = new StackPane();
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setPadding(new Insets(30));
        menuLayout.getStyleClass().add("menu-root");

        // Make a full-size rectangle that matches the scene's size
        Rectangle backgroundRect = new Rectangle(400, 300); // initial size
        backgroundRect.widthProperty().bind(menuLayout.widthProperty());
        backgroundRect.heightProperty().bind(menuLayout.heightProperty());

        // Color transition on the rectangle
        FillTransition bgFill = new FillTransition(Duration.seconds(3), backgroundRect,
                Color.DARKBLUE, Color.DARKRED);
        bgFill.setCycleCount(Animation.INDEFINITE);
        bgFill.setAutoReverse(true);
        bgFill.play();

        // The main title text (styled via CSS)
        Text menuTitle = new Text("Welcome to 2 Player Tag!");
        menuTitle.getStyleClass().add("title");

        // Pulse animation (scale up/down) on the title text
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), menuTitle);
        scaleTransition.setFromX(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToY(1.2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.play();

        // The "Play Game" button
        Button playGameButton = new Button("Play Game");
        playGameButton.getStyleClass().add("primary-btn");
        playGameButton.setOnAction(e -> {
            redScore = 0;
            blueScore = 0;
            stage.setScene(createGameScene(stage));
            stage.setTitle("Two Player Tag - Game");
        });

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(menuTitle, playGameButton);

        menuLayout.getChildren().addAll(backgroundRect, vbox);

        Scene scene = new Scene(menuLayout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("game-theme.css").toExternalForm());
        return scene;
    }

    private Scene createMapSelectionScene(Stage stage) {
        VBox mapLayout = new VBox(20);
        mapLayout.setAlignment(Pos.CENTER);
        mapLayout.setPadding(new Insets(30));
        mapLayout.getStyleClass().add("card");

        // creating the title for each map/level
        Text mapTitle = new Text("Select a Map");
        mapTitle.getStyleClass().add("title");

        ToggleGroup mapGroup = new ToggleGroup();

        RadioButton map1 = new RadioButton("Dual");
        map1.setToggleGroup(mapGroup);
        map1.setSelected(true);

        RadioButton map2 = new RadioButton("2");
        map2.setToggleGroup(mapGroup);

        Button confirmButton = new Button("Start Game");
        confirmButton.getStyleClass().add("primary-btn");
        confirmButton.setOnAction(e -> {
            if (map2.isSelected()) {
                mapSelected = 2;
            } else {
                mapSelected = 1;
            }
            Scene gameScene = createGameScene(stage);
            stage.setScene(gameScene);
            stage.setTitle("Two Player Tag - Map: " + mapSelected);
        });

        mapLayout.getChildren().addAll(mapTitle, map1, map2, confirmButton);

        Scene scene = new Scene(mapLayout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("game-theme.css").toExternalForm());
        return scene;
    }

    /**
     * -----------------------------
     *     GAME SCENE
     * -----------------------------
     */
    private Scene createGameScene(Stage stage) {
        // BorderPane for layout
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));
        layout.getStyleClass().add("game-root");

        // Canvas + GraphicsContext
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

                // check object collisions (placeholders for your logic)
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
                    getEndGame(stage, false);
                    return;
                }
                if (redScore >= winScore) {
                    stop();
                    getEndGame(stage, true);
                }
            }
        };
        gameLoop.start();

        return gameScene;
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
     * Once a player wins (reaches 5 points), switch to the end-game screen.
     */
    private void getEndGame(Stage stage, boolean redWon) {
        Scene endScene = createEndGameScene(stage, redWon);
        stage.setScene(endScene);
        stage.setTitle("Two Player Tag - Game Over");
    }

    /**
     * -----------------------------
     *    END-OF-GAME SCENE
     * -----------------------------
     */
    private Scene createEndGameScene(Stage stage, boolean redWon) {
        VBox endLayout = new VBox(20);
        endLayout.setPadding(new Insets(30));
        endLayout.setAlignment(Pos.CENTER);
        endLayout.getStyleClass().add("card");

        String winnerText = (redWon) ? "Red" : "Blue";
        Text congrats = new Text("Congratulations, " + winnerText + "!");
        congrats.getStyleClass().add("title");

        Button returnToTitle = new Button("Return to Title Screen");
        returnToTitle.getStyleClass().add("secondary-btn");
        returnToTitle.setOnAction(e -> {
            Scene menuScene = createMenuScene(stage);
            stage.setScene(menuScene);
            stage.setTitle("Two Player Tag - Menu");
        });

        endLayout.getChildren().addAll(congrats, returnToTitle);
        Scene scene = new Scene(endLayout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("game-theme.css").toExternalForm());
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

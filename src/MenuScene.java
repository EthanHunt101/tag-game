import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

public class MenuScene {
    /**
     * -----------------------------
     *     MENU SCENE
     * -----------------------------
     */
    public Scene createMenuScene(Stage stage) {
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
            //
            stage.setScene(new MapSelectionScene(stage).getMapScene());
            //stage.setScene(new GameScene(stage).getScene());
            // stage.setTitle("Two Player Tag - Game");
        });

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(menuTitle, playGameButton);

        menuLayout.getChildren().addAll(backgroundRect, vbox);

        Scene scene = new Scene(menuLayout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("game-theme.css").toExternalForm());
        return scene;
    }
}

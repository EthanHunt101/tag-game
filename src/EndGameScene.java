import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EndGameScene {

    /**
     * Once a player wins (reaches 5 points), switch to the end-game screen.
     */
    public EndGameScene(Stage stage, boolean redWon) {
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
            Scene menuScene = new MenuScene().createMenuScene(stage);
            stage.setScene(menuScene);
            stage.setTitle("Two Player Tag - Menu");

        });

        endLayout.getChildren().addAll(congrats, returnToTitle);
        Scene scene = new Scene(endLayout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("game-theme.css").toExternalForm());
        return scene;
    }

}

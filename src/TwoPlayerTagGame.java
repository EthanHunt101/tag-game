// File: TwoPlayerTagGame.java

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TwoPlayerTagGame extends Application {

    // Keep track of the level selected by the user
    private int mapSelected = 1;

    @Override
    public void start(Stage primaryStage) {
        Scene menuScene = new MenuScene().createMenuScene(primaryStage);
        primaryStage.setTitle("Two Player Tag - Menu");
        primaryStage.setScene(menuScene);
        primaryStage.show();
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

        RadioButton map1 = new RadioButton("Dul");
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
            Scene gameScene = (new GameScene(stage)).getScene();
            stage.setScene(gameScene);
            stage.setTitle("Two Player Tag - Map: " + mapSelected);
        });

        mapLayout.getChildren().addAll(mapTitle, map1, map2, confirmButton);

        Scene scene = new Scene(mapLayout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("game-theme.css").toExternalForm());
        return scene;
    }


    public static void main(String[] args) {
        launch(args);
    }
}

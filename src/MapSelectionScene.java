import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class MapSelectionScene {

    private Scene mapScene;
    // Keep track of the level selected by the user
    private int mapSelected = 1;


    public MapSelectionScene(Stage stage) {
        Scene mapScene = createMapSelectionScene(stage);
        stage.setScene(mapScene);
        stage.setTitle("Map Selection Screen");
        this.mapScene = mapScene;
    }

    private Scene createMapSelectionScene(Stage stage) {
        VBox mapLayout = new VBox(20);
        mapLayout.setAlignment(Pos.CENTER);
        mapLayout.setPadding(new Insets(30));
        mapLayout.getStyleClass().add("card");

        HBox titleRow = new HBox(10);
        HBox mapRow = new HBox(10);
        HBox confirmRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER);
        mapRow.setAlignment(Pos.CENTER);
        confirmRow.setAlignment(Pos.CENTER);

        // creating the title for each map/level
        Text mapTitle = new Text("Select a Map");
        mapTitle.getStyleClass().add("title");

        ToggleGroup mapGroup = new ToggleGroup();

        RadioButton map1 = new RadioButton("None");
        map1.setToggleGroup(mapGroup);
        map1.setSelected(true);

        RadioButton map2 = new RadioButton("Actual");
        map2.setToggleGroup(mapGroup);

        Button confirmButton = new Button("Start Game");
        confirmButton.getStyleClass().add("primary-btn");
        confirmButton.setOnAction(e -> {
            if (map2.isSelected()) {
                mapSelected = 2;
                Scene gameScene = (new GameScene(stage)).getScene();
                stage.setScene(gameScene);
                stage.setTitle("Two Player Tag - Map: " + mapSelected);
                // will set a certain scene depending on which radio button this will be
            } else {
                // will set a certain scene depending on which radio button this will be
                mapSelected = 1;
            }
        });

        titleRow.getChildren().addAll(mapTitle);

        // The map row will contain Vboxes that contain both the image of the map and the map name will add later
        VBox mapTemp = new VBox(10);
        mapTemp.setAlignment(Pos.CENTER);
        Image fortnite = new Image("fortnite.jpg");
        ImageView ivFortnite = new ImageView(fortnite);
        ivFortnite.setFitHeight(300);
        ivFortnite.setPreserveRatio(true);
        ivFortnite.setSmooth(true);
        mapTemp.getChildren().addAll(ivFortnite, map1);


        VBox mapArena = new VBox(10);
        Image fortnite2 = new Image("fortnite2.jpg");
        ImageView ivFortnite2 = new ImageView(fortnite2);
        ivFortnite2.setFitHeight(300);
        ivFortnite2.setPreserveRatio(true);
        ivFortnite2.setSmooth(true);
        mapArena.getChildren().addAll(ivFortnite2, map2);
        mapArena.setAlignment(Pos.CENTER);

        mapRow.getChildren().addAll(mapTemp, mapArena);
        confirmRow.getChildren().addAll(confirmButton);

        mapLayout.getChildren().addAll(titleRow, mapRow, confirmRow);

        Scene scene = new Scene(mapLayout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("game-theme.css").toExternalForm());
        return scene;
    }

    public Scene getMapScene() {
        return this.mapScene;
    }
}

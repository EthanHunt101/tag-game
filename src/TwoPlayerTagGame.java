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

    @Override
    public void start(Stage primaryStage) {
        Scene menuScene = new MenuScene().createMenuScene(primaryStage);
        primaryStage.setTitle("Two Player Tag - Menu");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

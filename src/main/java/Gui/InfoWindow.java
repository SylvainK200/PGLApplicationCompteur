package Gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * This class implement a window that show a message
 */
public class InfoWindow {

    public static void display(String message){
        Stage window = new Stage();

        // layout
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10,10,10,10));
        layout.setVgap(10);

        // Message
        Label text = new Label(message);
        text.setFont(new Font("Arial",20));
        GridPane.setConstraints(text,0,0);

        // Button
        Button button = new Button("OK");
        button.setMinSize(50,25);
        button.setOnAction(e-> window.close());
        GridPane.setConstraints(button,0,1);

        layout.getChildren().addAll(text,button);
        Scene scene = new Scene(layout,250,100);
        window.setScene(scene);
        window.showAndWait();
    }
}

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        TextField inputField = new TextField();
        Button submitButton = new Button("Submit");
        Label outputLabel = new Label("Output akan muncul di sini");

        submitButton.setOnAction(e -> {
            String input = inputField.getText();
            outputLabel.setText("Anda mengetik: " + input);
        });

        VBox layout = new VBox(10, inputField, submitButton, outputLabel);
        stage.setScene(new Scene(layout, 500, 500));
        stage.setTitle("Input-Output JavaFX");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

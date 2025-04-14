import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DecimalToBinaryFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label labelInput = new Label("Masukkan bilangan desimal:");
        TextField textField = new TextField();
        Button convertButton = new Button("Konversi");
        Label resultLabel = new Label("Biner: ");
        
        convertButton.setOnAction(e -> {
            try {
                int num = Integer.parseInt(textField.getText());
                resultLabel.setText("Biner: " + decimalToBinary(num));
            } catch (NumberFormatException ex) {
                resultLabel.setText("Masukkan angka yang valid!");
            }
        });
        
        VBox layout = new VBox(10, labelInput, textField, convertButton, resultLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Konversi Desimal ke Biner");
        primaryStage.show();
    }
    
    private String decimalToBinary(int n) {
        if (n == 0) return "0";
        
        StringBuilder binary = new StringBuilder();
        while (n > 0) {
            binary.insert(0, n % 2);
            n /= 2;
        }
        return binary.toString();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

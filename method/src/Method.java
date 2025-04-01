import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Method extends Application {

    // Method untuk menjumlahkan dua angka
    private int tambah(int a, int b) {
        return a + b;
    }

    @Override
    public void start(Stage primaryStage) {
        Label label1 = new Label("Angka 1:");
        TextField input1 = new TextField();
        Label label2 = new Label("Angka 2:");
        TextField input2 = new TextField();
        Button btnHitung = new Button("Hitung");
        Label hasil = new Label("Hasil: ");

        // Event handler untuk tombol
        btnHitung.setOnAction(e -> {
            int angka1 = Integer.parseInt(input1.getText());
            int angka2 = Integer.parseInt(input2.getText());
            int hasilTambah = tambah(angka1, angka2); // Memanggil method
            hasil.setText("Hasil: " + hasilTambah);
        });

        VBox layout = new VBox(10, label1, input1, label2, input2, btnHitung, hasil);
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setTitle("Penjumlahan dengan Method");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {

    private TableView<Siswa> table = new TableView<>();
    private List<Siswa> siswaList = new ArrayList<>();

    private TextField namaField = new TextField();
    private TextField nimField = new TextField();
    private TextField nilaiField = new TextField();
    private TextField cariField = new TextField();

    private Label rataLabel = new Label("Rata-rata:");
    private Label totalLabel = new Label("Total siswa:");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Aplikasi Nilai Siswa");

        // Kolom tabel
        TableColumn<Siswa, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Siswa, String> nimCol = new TableColumn<>("NIM");
        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));

        TableColumn<Siswa, Double> nilaiCol = new TableColumn<>("Nilai");
        nilaiCol.setCellValueFactory(new PropertyValueFactory<>("nilai"));

        table.getColumns().addAll(namaCol, nimCol, nilaiCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Input fields
        HBox inputBox = new HBox(5, namaField, nimField, nilaiField, new Button("Tambah") {{
            setOnAction(e -> tambahData());
        }});

        namaField.setPromptText("Nama");
        nimField.setPromptText("NIM");
        nilaiField.setPromptText("Nilai");

        // Search & tombol
        HBox buttonBox = new HBox(5,
            cariField,
            new Button("Cari (Binary)") {{
                setOnAction(e -> cariNama());
            }},
            new Button("Urut Nilai Desc") {{
                setOnAction(e -> urutkanNilai());
            }},
            new Button("Hitung Rata2 & Total") {{
                setOnAction(e -> hitungRataTotal());
            }}
        );
        cariField.setPromptText("Cari Nama");

        VBox root = new VBox(10, table, inputBox, buttonBox, rataLabel, totalLabel);
        root.setStyle("-fx-padding: 10;");

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    private void tambahData() {
        try {
            String nama = namaField.getText();
            String nim = nimField.getText();
            double nilai = Double.parseDouble(nilaiField.getText());
            Siswa s = new Siswa(nama, nim, nilai);
            siswaList.add(s);
            table.getItems().add(s);

            namaField.clear();
            nimField.clear();
            nilaiField.clear();
        } catch (Exception e) {
            showAlert("Input tidak valid!");
        }
    }

    private void urutkanNilai() {
        siswaList.sort((a, b) -> Double.compare(b.getNilai(), a.getNilai()));
        table.getItems().setAll(siswaList);
    }

    private void cariNama() {
        String namaDicari = cariField.getText();
        siswaList.sort(Comparator.comparing(Siswa::getNama));
        int index = binarySearch(namaDicari);
        if (index >= 0) {
            table.getSelectionModel().select(siswaList.get(index));
            showAlert("Ditemukan: " + siswaList.get(index).getNama());
        } else {
            showAlert("Nama tidak ditemukan.");
        }
    }

    private int binarySearch(String nama) {
        int low = 0, high = siswaList.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = siswaList.get(mid).getNama().compareToIgnoreCase(nama);
            if (cmp == 0) return mid;
            else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }

    private void hitungRataTotal() {
        double total = hitungTotalRekursif(siswaList, 0);
        int count = siswaList.size();
        double rata = count > 0 ? total / count : 0;
        rataLabel.setText("Rata-rata: " + rata);
        totalLabel.setText("Total siswa: " + count);
    }

    private double hitungTotalRekursif(List<Siswa> list, int index) {
        if (index == list.size()) return 0;
        return list.get(index).getNilai() + hitungTotalRekursif(list, index + 1);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class Siswa {
        private String nama;
        private String nim;
        private double nilai;

        public Siswa(String nama, String nim, double nilai) {
            this.nama = nama;
            this.nim = nim;
            this.nilai = nilai;
        }

        public String getNama() { return nama; }
        public String getNim() { return nim; }
        public double getNilai() { return nilai; }
    }
}

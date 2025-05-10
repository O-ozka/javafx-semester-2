import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class midtermexamAlgo extends Application {
    private static final String NAMA_FILE = "siswa.txt";
    private File fileTerakhirDipilih = new File(NAMA_FILE);

    private TableView<Siswa> table = new TableView<>();
    private ObservableList<Siswa> data = FXCollections.observableArrayList();

    private TextField namaField = new TextField();
    private TextField nimField = new TextField();
    private TextField matField = new TextField();
    private TextField pemField = new TextField();
    private TextField sbdField = new TextField();

    private TextField cariNamaField = new TextField();
    private TextField cariNimField = new TextField();

    private Label rataLabel = new Label("Rata-rata:");
    private Label totalLabel = new Label("Total siswa:");
    private Label fileAktifLabel = new Label("File aktif: " + NAMA_FILE);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Grades of Elmore Junior High students");

        // Kolom tabel
        TableColumn<Siswa, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Siswa, String> nimCol = new TableColumn<>("NIM");
        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));

        TableColumn<Siswa, Double> matCol = new TableColumn<>("Matematika");
        matCol.setCellValueFactory(new PropertyValueFactory<>("nilaiMat"));

        TableColumn<Siswa, Double> pemCol = new TableColumn<>("Pemrograman");
        pemCol.setCellValueFactory(new PropertyValueFactory<>("nilaiPem"));

        TableColumn<Siswa, Double> sbdCol = new TableColumn<>("Sistem Basis Data");
        sbdCol.setCellValueFactory(new PropertyValueFactory<>("nilaiSbd"));

        TableColumn<Siswa, Double> rataCol = new TableColumn<>("Rata-rata");
        rataCol.setCellValueFactory(new PropertyValueFactory<>("rataRata"));

        table.setItems(data);
        table.getColumns().addAll(namaCol, nimCol, matCol, pemCol, sbdCol, rataCol);

        // Input siswa
        namaField.setPromptText("Nama");
        nimField.setPromptText("NIM");
        matField.setPromptText("Nilai Matematika");
        pemField.setPromptText("Nilai Pemrograman");
        sbdField.setPromptText("Nilai Sistem Basis Data");

        Button tambahBtn = new Button("Tambah");
        tambahBtn.setOnAction(e -> tambahData());

        Button lihatIsiBtn = new Button("Lihat Isi File");
        lihatIsiBtn.setOnAction(e -> lihatIsiFile());

        Button muatUlangBtn = new Button("Muat Ulang File");
        muatUlangBtn.setOnAction(e -> muatUlangFile());

        HBox inputBox = new HBox(10, namaField, nimField, matField, pemField, sbdField, tambahBtn, lihatIsiBtn, muatUlangBtn);

        // Tombol perhitungan
        Button hitungBtn = new Button("Hitung Total & Rata-rata");
        hitungBtn.setOnAction(e -> hitungRataTotal());

        // Pencarian dan pengurutan
        cariNamaField.setPromptText("Cari Nama");
        cariNimField.setPromptText("Cari NIM");

        Button cariNamaBtn = new Button("Cari Nama");
        cariNamaBtn.setOnAction(e -> cariByNamaBinary());

        Button cariNimBtn = new Button("Cari NIM");
        cariNimBtn.setOnAction(e -> cariByNimBinary());

        Button urutBtn = new Button("Urutkan Rata-rata");
        urutBtn.setOnAction(e -> urutkanByNilai());

        Button resetBtn = new Button("Reset");
        resetBtn.setOnAction(e -> table.setItems(data));

        HBox searchBox = new HBox(10, cariNamaField, cariNamaBtn, cariNimField, cariNimBtn, urutBtn, resetBtn);

        VBox root = new VBox(10, table, fileAktifLabel, inputBox, searchBox, hitungBtn, rataLabel, totalLabel);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 1100, 550));
        stage.show();

        bacaDefault();
    }

    private void bacaDefault() {
        Path path = Paths.get(NAMA_FILE);
        if (!Files.exists(path)) return;

        data.clear();
        fileTerakhirDipilih = path.toFile();
        fileAktifLabel.setText("File aktif: " + fileTerakhirDipilih.getName());

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String baris;
            while ((baris = reader.readLine()) != null) {
                String[] bagian = baris.split(";");
                if (bagian.length == 5) {
                    String nama = bagian[0];
                    String nim = bagian[1];
                    double mat = Double.parseDouble(bagian[2]);
                    double pem = Double.parseDouble(bagian[3]);
                    double sbd = Double.parseDouble(bagian[4]);
                    data.add(new Siswa(nama, nim, mat, pem, sbd));
                }
            }
        } catch (IOException | NumberFormatException e) {
            showAlert("Gagal membaca data: " + e.getMessage());
        }
    }

    private void lihatIsiFile() {
        File file = (fileTerakhirDipilih != null) ? fileTerakhirDipilih : new File(NAMA_FILE);

        if (!file.exists()) {
            showAlert("File belum ada atau belum dipilih.");
            return;
        }

        try {
            StringBuilder isi = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    isi.append(line).append("\n");
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Isi File");
            alert.setHeaderText("Isi dari " + file.getName());
            TextArea area = new TextArea(isi.toString());
            area.setEditable(false);
            area.setWrapText(true);
            area.setMaxWidth(Double.MAX_VALUE);
            area.setMaxHeight(Double.MAX_VALUE);
            alert.getDialogPane().setContent(area);
            alert.showAndWait();
        } catch (IOException e) {
            showAlert("Gagal membaca isi file: " + e.getMessage());
        }
    }

    private void muatUlangFile() {
        if (!fileTerakhirDipilih.exists()) {
            showAlert("File belum ada atau belum dipilih.");
            return;
        }

        data.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileTerakhirDipilih))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bagian = line.split(";");
                if (bagian.length == 5) {
                    String nama = bagian[0];
                    String nim = bagian[1];
                    double mat = Double.parseDouble(bagian[2]);
                    double pem = Double.parseDouble(bagian[3]);
                    double sbd = Double.parseDouble(bagian[4]);
                    data.add(new Siswa(nama, nim, mat, pem, sbd));
                }
            }
            table.setItems(data);
            showAlert("Data berhasil dimuat ulang dari file: " + fileTerakhirDipilih.getName());
        } catch (IOException | NumberFormatException e) {
            showAlert("Gagal memuat ulang file: " + e.getMessage());
        }
    }

    private void tambahData() {
        try {
            String nama = namaField.getText();
            String nim = nimField.getText();
            double mat = Double.parseDouble(matField.getText());
            double pem = Double.parseDouble(pemField.getText());
            double sbd = Double.parseDouble(sbdField.getText());

            if (nama.isEmpty() || nim.isEmpty()) {
                showAlert("Nama dan NIM wajib diisi.");
                return;
            }

            data.add(new Siswa(nama, nim, mat, pem, sbd));
            namaField.clear(); nimField.clear();
            matField.clear(); pemField.clear(); sbdField.clear();

            simpanKeFile();
        } catch (NumberFormatException e) {
            showAlert("Semua nilai harus berupa angka!");
        }
    }

    private void simpanKeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileTerakhirDipilih))) {
            for (Siswa s : data) {
                String baris = String.join(";", s.getNama(), s.getNim(),
                        String.valueOf(s.getNilaiMat()),
                        String.valueOf(s.getNilaiPem()),
                        String.valueOf(s.getNilaiSbd()));
                writer.write(baris);
                writer.newLine();
            }
        } catch (IOException e) {
            showAlert("Gagal menyimpan data: " + e.getMessage());
        }
    }

    private void hitungRataTotal() {
        if (data.isEmpty()) {
            rataLabel.setText("Rata-rata: -");
            totalLabel.setText("Total siswa: 0");
            return;
        }

        double totalSemua = hitungTotalRekursif(data, 0);
        int totalSiswa = data.size();
        double rata = totalSemua / totalSiswa;

        rataLabel.setText("Rata-rata: " + String.format("%.2f", rata));
        totalLabel.setText("Total siswa: " + totalSiswa);
    }

    private double hitungTotalRekursif(ObservableList<Siswa> list, int index) {
        if (index == list.size()) return 0;
        return list.get(index).getRataRata() + hitungTotalRekursif(list, index + 1);
    }

    private void cariByNamaBinary() {
        String cari = cariNamaField.getText().trim().toLowerCase();
        if (cari.isEmpty()) {
            showAlert("Masukkan nama yang ingin dicari.");
            return;
        }

        List<Siswa> dataCopy = new ArrayList<>(data);
        dataCopy.sort(Comparator.comparing(s -> s.getNama().toLowerCase()));

        int low = 0, high = dataCopy.size() - 1;
        boolean found = false;

        while (low <= high) {
            int mid = (low + high) / 2;
            String midNama = dataCopy.get(mid).getNama().toLowerCase();

            if (midNama.contains(cari)) {
                table.setItems(FXCollections.observableArrayList(dataCopy.get(mid)));
                found = true;
                break;
            }

            if (midNama.compareTo(cari) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (!found) {
            showAlert("Siswa dengan nama \"" + cari + "\" tidak ditemukan.");
        }
    }

    private void cariByNimBinary() {
        ObservableList<Siswa> salinan = FXCollections.observableArrayList(data);
        FXCollections.sort(salinan, Comparator.comparing(Siswa::getNim));

        String target = cariNimField.getText().trim();
        int low = 0, high = salinan.size() - 1;
        boolean ketemu = false;

        while (low <= high) {
            int mid = (low + high) / 2;
            Siswa midSiswa = salinan.get(mid);
            int cmp = midSiswa.getNim().compareTo(target);

            if (cmp == 0) {
                table.setItems(FXCollections.observableArrayList(midSiswa));
                ketemu = true;
                break;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (!ketemu) {
            showAlert("Siswa dengan NIM " + target + " tidak ditemukan.");
        }
    }

    private void urutkanByNilai() {
        FXCollections.sort(data, Comparator.comparingDouble(Siswa::getRataRata).reversed());
        table.setItems(data);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Peringatan");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class Siswa {
        private final String nama;
        private final String nim;
        private final double nilaiMat, nilaiPem, nilaiSbd;

        public Siswa(String nama, String nim, double mat, double pem, double sbd) {
            this.nama = nama;
            this.nim = nim;
            this.nilaiMat = mat;
            this.nilaiPem = pem;
            this.nilaiSbd = sbd;
        }

        public String getNama() { return nama; }
        public String getNim() { return nim; }
        public double getNilaiMat() { return nilaiMat; }
        public double getNilaiPem() { return nilaiPem; }
        public double getNilaiSbd() { return nilaiSbd; }
        public double getRataRata() {
            return (nilaiMat + nilaiPem + nilaiSbd) / 3;
        }
    }
}

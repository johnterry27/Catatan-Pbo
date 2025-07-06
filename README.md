# Catatan-Pbo

## Benner.java
```java
import javax.swing.*;
import javax.swing.table.DefaultTableModel; // CATATAN: Import tambahan untuk JTable
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import com.google.gson.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ProductForm extends JFrame {
    private JTextField tfName = new JTextField();
    private JTextField tfPrice = new JTextField();
    private JTextField tfCategory = new JTextField();

    // CATATAN: Variabel baru untuk tabel
    private JTable productTable;
    private DefaultTableModel tableModel;
    private String[] columnNames = {"ID", "Name", "Price", "Category"};

    public ProductForm() {
        setTitle("GraphQL Product Form");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel input
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(tfName);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(tfPrice);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(tfCategory);

        JButton btnAdd = new JButton("Add Product");
        JButton btnFetch = new JButton("Show All");
        inputPanel.add(btnAdd);
        inputPanel.add(btnFetch);

        add(inputPanel, BorderLayout.NORTH); // Posisi form input

        // CATATAN: Tabel ditambahkan menggantikan JTextArea
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> tambahProduk());
        btnFetch.addActionListener(e -> ambilSemuaProduk());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // CATATAN: Diubah untuk menampilkan pesan popup dan refresh tabel
    private void tambahProduk() {
        try {
            String query = String.format(
                "mutation { addProduct(name: \"%s\", price: %s, category: \"%s\") { id name } }",
                tfName.getText(),
                tfPrice.getText(),
                tfCategory.getText()
            );

            String jsonRequest = new Gson().toJson(new GraphQLQuery(query));
            String response = sendGraphQLRequest(jsonRequest);

            JOptionPane.showMessageDialog(this, "Product added successfully!");
            ambilSemuaProduk(); // CATATAN: otomatis refresh tabel

            // Kosongkan input form
            tfName.setText("");
            tfPrice.setText("");
            tfCategory.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // CATATAN: Diubah untuk mengisi JTable, bukan lagi JTextArea
    private void ambilSemuaProduk() {
        try {
            String query = "query { allProducts { id name price category } }";
            String jsonRequest = new Gson().toJson(new GraphQLQuery(query));
            String response = sendGraphQLRequest(jsonRequest);

            JsonObject data = JsonParser.parseString(response)
                .getAsJsonObject().getAsJsonObject("data");
            JsonArray products = data.getAsJsonArray("allProducts");

            tableModel.setRowCount(0); // Bersihkan isi tabel sebelum isi baru

            for (JsonElement el : products) {
                JsonObject prod = el.getAsJsonObject();
                Object[] row = {
                    prod.get("id").getAsLong(),
                    prod.get("name").getAsString(),
                    prod.get("price").getAsDouble(),
                    prod.get("category").getAsString()
                };
                tableModel.addRow(row); // Tambahkan baris ke tabel
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private String sendGraphQLRequest(String json) throws Exception {
        URL url = new URL("http://localhost:4567/graphql");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductForm::new);
    }

    // Class bantu untuk query GraphQL
    class GraphQLQuery {
        String query;
        GraphQLQuery(String query) {
            this.query = query;
        }
    }
}


```





























## Benner.java
```java
package org.yourcompany.yourproject;

import java.awt.*; // 📌 digunakan untuk layout
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; // ✅ Ditambahkan: untuk tabel

import com.google.gson.*; // ✅ Ditambahkan: untuk parsing JSON

public class ProductForm extends JFrame {
    private JTextField tfName = new JTextField();
    private JTextField tfPrice = new JTextField();
    private JTextField tfCategory = new JTextField();

    // ❌ Diganti: outputArea dihapus
    // private JTextArea outputArea = new JTextArea(10, 30);

    // ✅ Ditambahkan: untuk menampilkan data di tabel
    private JTable productTable;
    private DefaultTableModel tableModel;
    private String[] columnNames = {"ID", "Name", "Price", "Category"};
    private Long selectedId = null; // ✅ Ditambahkan: simpan ID untuk update/delete

    public ProductForm() {
        setTitle("GraphQL Product Form");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ==== Panel Form Input ====
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(tfName);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(tfPrice);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(tfCategory);

        // ==== Tombol ====
        JButton btnAdd = new JButton("Add");         // ✅ Ditambahkan
        JButton btnShow = new JButton("Show All");   // ✅ Ditambahkan
        JButton btnUpdate = new JButton("Update");   // ✅ Ditambahkan
        JButton btnDelete = new JButton("Delete");   // ✅ Ditambahkan

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnShow);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Gabungkan input dan tombol
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ==== Tabel Produk ====
        tableModel = new DefaultTableModel(columnNames, 0); // ✅ Tabel model
        productTable = new JTable(tableModel);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        // ==== Event Tombol ====
        btnAdd.addActionListener(e -> tambahProduk());
        btnShow.addActionListener(e -> ambilSemuaProduk());
        btnUpdate.addActionListener(e -> updateProduk());
        btnDelete.addActionListener(e -> deleteProduk());

        // ==== Klik Baris Tabel ====
        productTable.getSelectionModel().addListSelectionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                selectedId = Long.valueOf(tableModel.getValueAt(row, 0).toString());
                tfName.setText(tableModel.getValueAt(row, 1).toString());
                tfPrice.setText(tableModel.getValueAt(row, 2).toString());
                tfCategory.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void tambahProduk() {
        try {
            String query = String.format(
                "mutation { addProduct(name: \"%s\", price: %s, category: \"%s\") { id name } }",
                tfName.getText(), tfPrice.getText(), tfCategory.getText()
            );

            String jsonRequest = new Gson().toJson(new GraphQLQuery(query));
            sendGraphQLRequest(jsonRequest);

            // ❌ Diganti: outputArea.setText(...)
            // ✅ Ganti jadi popup + refresh tabel
            JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan.");
            ambilSemuaProduk();
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error tambah: " + e.getMessage());
        }
    }

    // ✅ Ditambahkan: Fitur Update
    private void updateProduk() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Pilih produk dari tabel dulu.");
            return;
        }
        try {
            String deleteQuery = String.format("mutation { deleteProduct(id: %d) }", selectedId);
            sendGraphQLRequest(new Gson().toJson(new GraphQLQuery(deleteQuery)));

            tambahProduk(); // Tambah ulang setelah hapus
            selectedId = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error update: " + e.getMessage());
        }
    }

    // ✅ Ditambahkan: Fitur Delete
    private void deleteProduk() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Pilih produk dari tabel dulu.");
            return;
        }
        try {
            String query = String.format("mutation { deleteProduct(id: %d) }", selectedId);
            sendGraphQLRequest(new Gson().toJson(new GraphQLQuery(query)));

            JOptionPane.showMessageDialog(this, "Produk berhasil dihapus.");
            ambilSemuaProduk();
            resetForm();
            selectedId = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error hapus: " + e.getMessage());
        }
    }

    // ✅ Diubah: sebelumnya hanya tampilkan raw JSON di outputArea
    private void ambilSemuaProduk() {
        try {
            String query = "query { allProducts { id name price category } }";
            String jsonRequest = new Gson().toJson(new GraphQLQuery(query));
            String response = sendGraphQLRequest(jsonRequest);

            JsonObject data = JsonParser.parseString(response)
                .getAsJsonObject().getAsJsonObject("data");
            JsonArray products = data.getAsJsonArray("allProducts");

            tableModel.setRowCount(0); // Bersihkan tabel
            for (JsonElement el : products) {
                JsonObject p = el.getAsJsonObject();
                Object[] row = {
                    p.get("id").getAsLong(),
                    p.get("name").getAsString(),
                    p.get("price").getAsDouble(),
                    p.get("category").getAsString()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error ambil data: " + e.getMessage());
        }
    }

    private String sendGraphQLRequest(String json) throws Exception {
        URL url = new URL("http://localhost:4567/graphql");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            return sb.toString();
        }
    }

    // ✅ Ditambahkan: agar setelah tambah/update/delete, form kosong lagi
    private void resetForm() {
        tfName.setText("");
        tfPrice.setText("");
        tfCategory.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductForm::new);
    }

    class GraphQLQuery {
        String query;
        GraphQLQuery(String query) {
            this.query = query;
        }
    }
}


```

## BannerServer.java
```java
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import javax.swing.*;

public class BannerServer {
    public static void main(String[] args) throws IOException {
        Banner banner = new Banner();

        JFrame frame = new JFrame("Banner Animation");
        frame.add(banner);
        frame.setSize(10000000, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

      
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/setname", new NameHandler(banner));
        server.setExecutor(null); 
        server.start();
        System.out.println("Server started on port 8080");
    }

    static class NameHandler implements HttpHandler {
        private final Banner banner;

        public NameHandler(Banner banner) {
            this.banner = banner;
        }

        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                banner.setText(body);  

                String response = "Name updated to: " + body;
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); 
            }
        }
    }
}

```

## Mahasiswa.java
```java
package org.yourcompany.yourproject;

public class Mahasiswa {
    private String nim;
    private String nama;
    private String prodi;

    public Mahasiswa() {}

    public Mahasiswa(String nim, String nama, String prodi) {
        this.nim = nim;
        this.nama = nama;
        this.prodi = prodi;
    }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getProdi() { return prodi; }
    public void setProdi(String prodi) { this.prodi = prodi; }
}



```


## MahasiswaService.java
```java
package org.yourcompany.yourproject;

import java.util.*;

public class MahasiswaService {
    private final Map<String, Mahasiswa> mahasiswaMap = new HashMap<>();

    public List<Mahasiswa> getAll() {
        return new ArrayList<>(mahasiswaMap.values());
    }

    public Mahasiswa get(String nim) {
        return mahasiswaMap.get(nim);
    }

    public Mahasiswa add(Mahasiswa mhs) {
        mahasiswaMap.put(mhs.getNim(), mhs);
        return mhs;
    }

    public Mahasiswa update(String nim, Mahasiswa mhs) {
        mhs.setNim(nim);
        mahasiswaMap.put(nim, mhs);
        return mhs;
    }

    public boolean delete(String nim) {
        return mahasiswaMap.remove(nim) != null;
    }
}


```
## MahasiswaApp.java
```java
package org.yourcompany.yourproject;

import static spark.Spark.*;
import com.google.gson.Gson;

public class MahasiswaApp {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();
        MahasiswaService mhsService = new MahasiswaService();

        path("/api", () -> {
            get("/mahasiswa", (req, res) -> {
                res.type("application/json");
                return mhsService.getAll();
            }, gson::toJson);

            get("/mahasiswa/:nim", (req, res) -> {
                res.type("application/json");
                String nim = req.params("nim");
                Mahasiswa mhs = mhsService.get(nim);
                if (mhs == null) {
                    res.status(404);
                    return "Mahasiswa not found";
                }
                return mhs;
            }, gson::toJson);

            post("/mahasiswa", (req, res) -> {
                res.type("application/json");
                Mahasiswa mhs = gson.fromJson(req.body(), Mahasiswa.class);
                return mhsService.add(mhs);
            }, gson::toJson);

            put("/mahasiswa/:nim", (req, res) -> {
                res.type("application/json");
                String nim = req.params("nim");
                Mahasiswa mhs = gson.fromJson(req.body(), Mahasiswa.class);
                return mhsService.update(nim, mhs);
            }, gson::toJson);

            delete("/mahasiswa/:nim", (req, res) -> {
                String nim = req.params("nim");
                if (mhsService.delete(nim)) {
                    res.status(204);
                    return "";
                } else {
                    res.status(404);
                    return "Mahasiswa not found";
                }
            });
        });
    }
}


```
 ## MahasiswaForm.java
 ```java
 package org.yourcompany.yourproject;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MahasiswaForm extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private final String[] columnNames = {"NIM", "Nama", "Prodi"};
    private JTextField nimField, namaField, prodiField;

    public MahasiswaForm() {
        setTitle("Manajemen Mahasiswa");
        setSize(750, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        nimField = new JTextField(10);
        namaField = new JTextField(15);
        prodiField = new JTextField(15);
        inputPanel.add(new JLabel("NIM:")); inputPanel.add(nimField);
        inputPanel.add(new JLabel("Nama:")); inputPanel.add(namaField);
        inputPanel.add(new JLabel("Prodi:")); inputPanel.add(prodiField);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Tambah");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Hapus");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> tambahMahasiswa());
        editBtn.addActionListener(e -> editMahasiswa());
        deleteBtn.addActionListener(e -> hapusMahasiswa());
        refreshBtn.addActionListener(e -> loadMahasiswa());

        buttonPanel.add(addBtn); buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn); buttonPanel.add(refreshBtn);

        JPanel controlPanel = new JPanel(new GridLayout(2,1));
        controlPanel.add(inputPanel);
        controlPanel.add(buttonPanel);
        add(controlPanel, BorderLayout.SOUTH);

        loadMahasiswa();
    }

    private void tambahMahasiswa() {
        String nim = nimField.getText().trim();
        String nama = namaField.getText().trim();
        String prodi = prodiField.getText().trim();

        if (nim.isEmpty() || nama.isEmpty() || prodi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try {
            URL url = new URL("http://localhost:4567/api/mahasiswa");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = new Gson().toJson(new Mahasiswa(nim, nama, prodi));
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            if (conn.getResponseCode() == 200) {
                JOptionPane.showMessageDialog(this, "Berhasil ditambah!");
                clearFields();
                loadMahasiswa();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal tambah data");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void editMahasiswa() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris dahulu.");
            return;
        }

        String nim = tableModel.getValueAt(row, 0).toString();
        String nama = namaField.getText().trim();
        String prodi = prodiField.getText().trim();

        if (nama.isEmpty() || prodi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan Prodi tidak boleh kosong.");
            return;
        }

        try {
            URL url = new URL("http://localhost:4567/api/mahasiswa/" + nim);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = new Gson().toJson(new Mahasiswa(nim, nama, prodi));
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            if (conn.getResponseCode() == 200) {
                JOptionPane.showMessageDialog(this, "Berhasil diubah!");
                loadMahasiswa();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal ubah data.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void hapusMahasiswa() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris untuk dihapus.");
            return;
        }

        String nim = tableModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            URL url = new URL("http://localhost:4567/api/mahasiswa/" + nim);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            if (conn.getResponseCode() == 204) {
                JOptionPane.showMessageDialog(this, "Data dihapus.");
                loadMahasiswa();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal hapus data.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadMahasiswa() {
        try {
            URL url = new URL("http://localhost:4567/api/mahasiswa");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String json = in.lines().collect(Collectors.joining());
            in.close();

            List<Mahasiswa> list = new Gson().fromJson(json, new TypeToken<List<Mahasiswa>>() {}.getType());

            tableModel.setRowCount(0);
            for (Mahasiswa m : list) {
                tableModel.addRow(new Object[]{ m.getNim(), m.getNama(), m.getProdi() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
        }
    }

    private void clearFields() {
        nimField.setText("");
        namaField.setText("");
        prodiField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MahasiswaForm().setVisible(true));
    }
}


```







#  CATATAN UTS PBO — UBAH APLIKASI CRUD JAVA GUI (REST API)

##  1. Ganti Nama Class dan File
| Sebelumnya (contoh: Buku) | Ubah Menjadi (contoh: Mahasiswa) |
|---------------------------|----------------------------------|
| `Book.java`               | `Mahasiswa.java`                |
| `BookService.java`        | `MahasiswaService.java`         |
| `LibraryApp.java`         | `MahasiswaApp.java`             |
| `BookForm.java`           | `MahasiswaForm.java`            |

---

##  2. Ganti Nama Atribut / Field
| Atribut Buku       | Atribut Mahasiswa |
|--------------------|-------------------|
| `id` → `long`      | `nim` → `String`  |
| `title` → `String` | `nama` → `String` |
| `author` → `String`| `prodi` → `String`|

---

##  3. Ganti Struktur Data di Service
```java
// Lama:
Map<Long, Book> bookMap;

// Baru:
Map<String, Mahasiswa> mahasiswaMap;
```

---

##  4. Ubah Endpoint REST API
| Operasi | URL Lama              | URL Baru                 |
|---------|------------------------|--------------------------|
| Tampil  | `/api/books`          | `/api/mahasiswa`         |
| Detail  | `/api/books/:id`      | `/api/mahasiswa/:nim`    |
| Tambah  | `POST /api/books`     | `POST /api/mahasiswa`    |
| Edit    | `PUT /api/books/:id`  | `PUT /api/mahasiswa/:nim`|
| Hapus   | `DELETE /api/books/:id`| `DELETE /api/mahasiswa/:nim`|

---

##  5. Ubah Label di GUI
- Kolom tabel: `{"NIM", "Nama", "Prodi"}`
- Text field: `nimField`, `namaField`, `prodiField`
- Tombol: `Tambah`, `Edit`, `Hapus`, `Refresh`

---

##  6. Gunakan Class Mahasiswa untuk JSON
```java
new Gson().toJson(new Mahasiswa(...));
new Gson().fromJson(json, Mahasiswa.class);
```

Pastikan semua `Book` diubah jadi `Mahasiswa`.




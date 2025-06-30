# Catatan-Pbo

## Benner.java
```java
package com.mycompany.mavenproject3;

import java.awt.BorderLayout;                       // ✅ DITAMBAHKAN
import java.awt.GridLayout;                         // ✅ DITAMBAHKAN
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;                     // ✅ DITAMBAHKAN
import javax.swing.JPanel;
import javax.swing.JScrollPane;                     // ✅ DITAMBAHKAN
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ProductForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField codeField;
    private JTextField nameField;
    private JComboBox<String> categoryField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton saveButton;

    // ✅ DITAMBAHKAN untuk tombol Edit dan Hapus
    private JButton editButton;
    private JButton deleteButton;

    public ProductForm() {
        // Data awal
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "P001", "Americano", "Coffee", 18000, 10));
        products.add(new Product(2, "P002", "Pandan Latte", "Coffee", 15000, 8));

        setTitle("WK. Cuan | Stok Barang");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());               // ✅ DITAMBAHKAN

        // === Form Panel ===
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5)); // ✅ DITAMBAHKAN agar layout rapi

        formPanel.add(new JLabel("Kode Barang"));
        codeField = new JTextField(15);              // ✅ Tambah ukuran field
        formPanel.add(codeField);

        formPanel.add(new JLabel("Nama Barang:"));
        nameField = new JTextField(15);
        formPanel.add(nameField);

        formPanel.add(new JLabel("Kategori:"));
        categoryField = new JComboBox<>(new String[]{"Coffee", "Dairy", "Juice", "Soda", "Tea"});
        formPanel.add(categoryField);

        formPanel.add(new JLabel("Harga Jual:"));
        priceField = new JTextField(15);
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stok Tersedia:"));
        stockField = new JTextField(15);
        formPanel.add(stockField);

        // Tombol CRUD
        saveButton = new JButton("Simpan");
        editButton = new JButton("Edit");            // ✅ DITAMBAHKAN
        deleteButton = new JButton("Hapus");         // ✅ DITAMBAHKAN

        formPanel.add(saveButton);
        formPanel.add(editButton);                   // ✅ DITAMBAHKAN
        formPanel.add(deleteButton);                 // ✅ DITAMBAHKAN

        add(formPanel, BorderLayout.NORTH);          // ✅ Tambahkan panel form ke frame

        // === Tabel ===
        tableModel = new DefaultTableModel(new String[]{"Kode", "Nama", "Kategori", "Harga Jual", "Stok"}, 0);
        drinkTable = new JTable(tableModel);
        add(new JScrollPane(drinkTable), BorderLayout.CENTER); // ✅ Tambah scroll ke tabel

        // Load data awal
        loadProductData(products);

        // === Action Listener ===

        // CREATE
        saveButton.addActionListener(e -> {
            try {
                String code = codeField.getText();
                String name = nameField.getText();
                String category = (String) categoryField.getSelectedItem();
                int price = Integer.parseInt(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());

                tableModel.addRow(new Object[]{code, name, category, price, stock});
                clearForm(); // ✅ Reset input
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid.");
            }
        });

        // UPDATE
        editButton.addActionListener(e -> {
            int row = drinkTable.getSelectedRow();
            if (row != -1) {
                try {
                    tableModel.setValueAt(codeField.getText(), row, 0);
                    tableModel.setValueAt(nameField.getText(), row, 1);
                    tableModel.setValueAt(categoryField.getSelectedItem(), row, 2);
                    tableModel.setValueAt(Integer.parseInt(priceField.getText()), row, 3);
                    tableModel.setValueAt(Integer.parseInt(stockField.getText()), row, 4);
                    clearForm(); // ✅ Reset input
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Input tidak valid.");
                }
            }
        });

        // DELETE
        deleteButton.addActionListener(e -> {
            int row = drinkTable.getSelectedRow();
            if (row != -1) {
                tableModel.removeRow(row);
                clearForm(); // ✅ Reset input
            }
        });

        // READ – saat baris di klik, tampilkan data ke form
        drinkTable.getSelectionModel().addListSelectionListener(e -> {
            int row = drinkTable.getSelectedRow();
            if (row != -1) {
                codeField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                categoryField.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                priceField.setText(tableModel.getValueAt(row, 3).toString());
                stockField.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        // Tampilkan frame
        setVisible(true);                             // ✅ DITAMBAHKAN
    }

    private void loadProductData(List<Product> productList) {
        for (Product product : productList) {
            tableModel.addRow(new Object[]{
                product.getCode(), product.getName(), product.getCategory(), product.getPrice(), product.getStock()
            });
        }
    }

    // ✅ Fungsi reset input form
    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        categoryField.setSelectedIndex(0);
        priceField.setText("");
        stockField.setText("");
    }
}

```





























## Benner.java
```java
import java.awt.*;
import javax.swing.*;

public class Banner extends JPanel implements Runnable {
    private String baseText = "Your name here";
    private String scrollingText = "";
    private Thread t;
    private boolean stopFlag;

    public Banner() {
        setBackground(Color.CYAN);
        setForeground(Color.RED);
        t = new Thread(this);
        t.start();
    }

    public synchronized void setText(String text) {
        this.baseText = text;
        rebuildScrollingText();
    }

    private void rebuildScrollingText() {
       
        StringBuilder sb = new StringBuilder();
        int panelWidth = getWidth() > 0 ? getWidth() : 400; 
        int repeat = panelWidth / (baseText.length() * 10); 
        for (int i = 0; i < repeat + 10; i++) {
            sb.append(baseText).append(" ");
        }
        scrollingText = sb.toString();
    }

    public void run() {
        rebuildScrollingText();
        while (!stopFlag) {
            repaint();
            synchronized (this) {
                char ch = scrollingText.charAt(0);
                scrollingText = scrollingText.substring(1) + ch;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(scrollingText, 10, 30);
    }

    public void stop() {
        stopFlag = true;
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




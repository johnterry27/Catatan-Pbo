# Catatan-Pbo

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

---

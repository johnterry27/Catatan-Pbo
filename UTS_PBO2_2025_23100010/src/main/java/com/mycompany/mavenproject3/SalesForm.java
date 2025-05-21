package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class SalesForm extends JFrame {
    private JComboBox<Product> productDropdown;
    private JTextField qtyField;
    private JButton sellButton;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private List<Product> products;
    private Mavenproject3 parent;

    public SalesForm(List<Product> products, Mavenproject3 parent) {
        this.products = products;
        this.parent = parent;

        setTitle("Form Penjualan");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel Input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Pilih Produk:"), gbc);

        gbc.gridx = 1;
        productDropdown = new JComboBox<>(products.toArray(new Product[0]));
        productDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product) {
                    setText(((Product) value).getCode()); // Hanya menampilkan kode barang
                }
                return this;
            }
        });

        inputPanel.add(productDropdown, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Stok:"), gbc);

        gbc.gridx = 1;
        qtyField = new JTextField(10);
        inputPanel.add(qtyField, gbc);

        sellButton = new JButton("Proses Penjualan");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        inputPanel.add(sellButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Tabel Produk
        tableModel = new DefaultTableModel(new String[]{"Kode", "Nama", "Stok Tersedia", "Harga Jual"}, 0);
        productTable = new JTable(tableModel);
        loadProductData();
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        sellButton.addActionListener(e -> processSale());

        setVisible(true);
    }

    private void loadProductData() {
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{p.getCode(), p.getName(), p.getStock(), p.getPrice()});
        }
    }

    private void processSale() {
        Product selectedProduct = (Product) productDropdown.getSelectedItem();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih produk terlebih dahulu.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyField.getText());
            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.");
            return;
        }

        if (selectedProduct.getStock() < qty) {
            JOptionPane.showMessageDialog(this, "Stok tidak mencukupi.");
            return;
        }

        selectedProduct.setStock(selectedProduct.getStock() - qty);
        refreshProductTable();
        JOptionPane.showMessageDialog(this, "Penjualan berhasil!");
        parent.refreshBanner();
    }

    private void refreshProductTable() {
        loadProductData();
    }
}
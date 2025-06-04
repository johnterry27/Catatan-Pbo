/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author ASUS
 */
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SellForm extends JFrame {
    private JComboBox<String> productField;
    private JTextField stockField;
    private JTextField priceField;
    private JTextField qtyField;
    private JButton processButton;
    private List<Product> products;
    private Mavenproject3 mainApp;

    public SellForm(Mavenproject3 mainApp) {
        this.mainApp = mainApp;
        this.products = mainApp.getProductList();

        setTitle("WK. Cuan | Jual Barang");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel sellPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dropdown produk
        gbc.gridx = 0; gbc.gridy = 0;
        sellPanel.add(new JLabel("Barang:"), gbc);

        productField = new JComboBox<>();
        for (Product p : products) {
            productField.addItem(p.getName());
        }
        gbc.gridx = 1;
        sellPanel.add(productField, gbc);

        // Stok
        gbc.gridx = 0; gbc.gridy = 1;
        sellPanel.add(new JLabel("Stok Tersedia:"), gbc);

        stockField = new JTextField(10);
        stockField.setEditable(false);
        gbc.gridx = 1;
        sellPanel.add(stockField, gbc);

        // Harga
        gbc.gridx = 0; gbc.gridy = 2;
        sellPanel.add(new JLabel("Harga Jual:"), gbc);

        priceField = new JTextField(10);
        priceField.setEditable(false);
        gbc.gridx = 1;
        sellPanel.add(priceField, gbc);

        // Qty
        gbc.gridx = 0; gbc.gridy = 3;
        sellPanel.add(new JLabel("Qty:"), gbc);

        qtyField = new JTextField(10);
        gbc.gridx = 1;
        sellPanel.add(qtyField, gbc);

        // Tombol proses
        processButton = new JButton("Proses");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        sellPanel.add(processButton, gbc);

        add(sellPanel);

        // Listener: Update stok dan harga saat produk dipilih
        productField.addActionListener(e -> updateFields());

        // Listener: Tombol Proses
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = productField.getSelectedIndex();
                Product selectedProduct = products.get(selectedIndex);
                try {
                    int qty = Integer.parseInt(qtyField.getText());

                    if (qty <= 0) {
                        JOptionPane.showMessageDialog(SellForm.this, "Qty harus lebih dari 0.");
                        return;
                    }

                    if (qty > selectedProduct.getStock()) {
                        JOptionPane.showMessageDialog(SellForm.this, "Stok tidak mencukupi!");
                        return;
                    }

                    selectedProduct.setStock(selectedProduct.getStock() - qty);
                    JOptionPane.showMessageDialog(SellForm.this, "Transaksi berhasil!\nSisa stok: " + selectedProduct.getStock());

                    updateFields();
                    qtyField.setText("");

                    mainApp.refreshBanner();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SellForm.this, "Qty harus berupa angka.");
                }
            }
        });

        // Set nilai awal dari produk pertama
        if (!products.isEmpty()) {
            productField.setSelectedIndex(0);
            updateFields();
        }
    }

    private void updateFields() {
        int selectedIndex = productField.getSelectedIndex();
        if (selectedIndex != -1) {
            Product selectedProduct = products.get(selectedIndex);
            stockField.setText(String.valueOf(selectedProduct.getStock()));
            priceField.setText(String.valueOf(selectedProduct.getPrice()));
        }
    }
}

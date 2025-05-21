package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Mavenproject3 extends JFrame implements Runnable {
    private String text;
    private int x;
    private int width;
    private BannerPanel bannerPanel;
    private JButton addProductButton;
    private List<Product> productList = new ArrayList<>();

    
    public Mavenproject3() {
        setTitle("WK. STI Chill");
        setSize(600, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        productList.add(new Product(1, "P001", "Americano", "Coffee", 10000, 10));
        productList.add(new Product(2, "P002", "Pandan Latte", "Coffee", 20000, 10));
       
        this.text = getBannerTextFromProducts();
        this.x = -getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(text);

        // Panel teks berjalan
        bannerPanel = new BannerPanel();
        add(bannerPanel, BorderLayout.CENTER);

        // Tombol "Kelola Produk"
        JPanel bottomPanel = new JPanel();
        addProductButton = new JButton("Kelola Produk");
        bottomPanel.add(addProductButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        addProductButton.addActionListener(e -> {
            new ProductForm(this).setVisible(true);
        });

        setVisible(true);

        Thread thread = new Thread(this);
        thread.start();
        // … kode sebelumnya …

        JButton salesFormButton = new JButton("Form Penjualan");
        bottomPanel.add(salesFormButton);

        salesFormButton.addActionListener(e -> {

      new SalesForm(productList, this).setVisible(true);
});

    }

    
    class BannerPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString(text, x, getHeight() / 2);
        }
    }

    public void setBannerText(String newText) {
        this.text = newText;
        this.x = -getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(text);
    }
    
 public String getBannerTextFromProducts() {
    StringBuilder sb = new StringBuilder("Menu yang tersedia: ");
    boolean first = true;
    for (Product p : productList) {
        if (p.getStock() > 0) {
            if (!first) {
                sb.append(" | ");
            }
            sb.append(p.getName());
            first = false;
        }
    }
    if (first) {
        sb.append("Tidak ada produk tersedia");
    }
    return sb.toString();
}


    public void refreshBanner() {
        setBannerText(getBannerTextFromProducts());
    }

    public List<Product> getProductList() {
        return productList;
    }


    @Override
    public void run() {
        width = getWidth();
        while (true) {
            x += 5;
            if (x > width) {
                x = -getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(text);
            }
            bannerPanel.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    

    public static void main(String[] args) {
        new Mavenproject3();
    }
}


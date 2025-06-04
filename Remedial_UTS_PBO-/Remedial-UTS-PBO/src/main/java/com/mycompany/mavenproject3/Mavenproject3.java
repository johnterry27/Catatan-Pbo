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
    private JButton sellProductButton;
    private List<Product> productList = new ArrayList<>();

    public Mavenproject3() {
        setTitle("WK. STI Chill");
        setSize(600, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        productList.add(new Product(1, "P001", "Americano", "Coffee", 10000, 10));
        productList.add(new Product(2, "P002", "Pandan Latte", "Coffee", 20000, 10));
        productList.add(new Product(3, "P003", "Aren Latte", "Coffee", 15000, 10));
        productList.add(new Product(4, "P004", "Matcha Frappucino", "Tea", 28000, 10));
        productList.add(new Product(5, "P005", "Jus Apel", "Juice", 17000, 10));
        
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
        
        sellProductButton = new JButton("Jual Produk");
        bottomPanel.add(sellProductButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        JButton customerButton = new JButton("Customer");
        customerButton.addActionListener(e -> new CustomerForm().setVisible(true));
        bottomPanel.add(customerButton);

        JButton adminButton = new JButton("Admin");
        adminButton.addActionListener(e -> new AdminForm().setVisible(true));
        bottomPanel.add(adminButton);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> new LoginForm().setVisible(true)); // Buat kelas LoginForm
        bottomPanel.add(loginButton);
        
        JButton userButton = new JButton("User");
        userButton.addActionListener(e -> new userForm(this).setVisible(true));
        bottomPanel.add(userButton);


   
        addProductButton.addActionListener(e -> {
            new ProductForm(this).setVisible(true);
        });

        sellProductButton.addActionListener(e -> {
            new SellForm(this).setVisible(true);
        });

        setVisible(true);

        Thread thread = new Thread(this);
        thread.start();
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
        for (int i = 0; i < productList.size(); i++) {
            sb.append(productList.get(i).getName());
            if (i < productList.size() - 1) {
                sb.append(" | ");
            }
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

// Perubahan Nama Commit
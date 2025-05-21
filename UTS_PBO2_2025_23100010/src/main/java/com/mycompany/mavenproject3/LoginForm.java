package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private List<User> users;

    public LoginForm() {
        setTitle("Login Pengguna");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inisialisasi user
        users = new ArrayList<>();
        users.add(new User("admin", "admin123", "admin"));
        users.add(new User("kasir", "kasir123", "kasir"));

        // Panel utama
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Silakan Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        // Username
        JPanel userPanel = new JPanel(new BorderLayout(5, 5));
        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();
        userPanel.add(userLabel, BorderLayout.WEST);
        userPanel.add(usernameField, BorderLayout.CENTER);
        panel.add(userPanel);
        panel.add(Box.createVerticalStrut(10));

        // Password
        JPanel passPanel = new JPanel(new BorderLayout(5, 5));
        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passPanel.add(passLabel, BorderLayout.WEST);
        passPanel.add(passwordField, BorderLayout.CENTER);
        panel.add(passPanel);
        panel.add(Box.createVerticalStrut(20));

        // Tombol login
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loginButton);

        loginButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            for (User u : users) {
                if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + u.getRole());
                    new Mavenproject3().setVisible(true);
                    dispose(); // tutup login form
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Username atau password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}

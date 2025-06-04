package com.mycompany.mavenproject3;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginForm extends JFrame {
    public LoginForm() {
        setTitle("Login");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5)); // 4 baris, 2 kolom, spasi antar komponen

        panel.add(new JLabel("Username:"));
        panel.add(new JTextField(15));

        panel.add(new JLabel("Password:"));
        panel.add(new JPasswordField(15));

        panel.add(new JLabel("Email:"));
        panel.add(new JTextField(15)); // Ganti ke JTextField (bukan password field)

        
        panel.add(new JLabel("Email:"));
        panel.add(new JTextField(15)); // Ganti ke JTextField (bukan password field)

        panel.add(new JLabel()); // Kosong untuk membuat tombol berada di sebelah kanan
        panel.add(new JButton("Login"));

        add(panel);
    }
}

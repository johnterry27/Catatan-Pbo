package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class AdminForm extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private java.util.List<Admin> adminList;

    public AdminForm() {
        adminList = new ArrayList<>();

        setTitle("Admin Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Username", "Role"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addAdmin());
        updateButton.addActionListener(e -> updateAdmin());
        deleteButton.addActionListener(e -> deleteAdmin());
    }

    private void addAdmin() {
        JTextField idField = new JTextField();
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField roleField = new JTextField();

        Object[] fields = {
                "ID:", idField,
                "Username:", usernameField,
                "Password:", passwordField,
                "Role:", roleField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Admin", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Admin admin = new Admin(
                    Integer.parseInt(idField.getText()),
                    usernameField.getText(),
                    passwordField.getText(),
                    roleField.getText()
            );
            adminList.add(admin);
            tableModel.addRow(new Object[]{admin.getId(), admin.getUsername(), admin.getRole()});
        }
    }

    private void updateAdmin() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            Admin admin = adminList.get(selected);

            JTextField idField = new JTextField(String.valueOf(admin.getId()));
            JTextField usernameField = new JTextField(admin.getUsername());
            JTextField passwordField = new JTextField(admin.getPassword());
            JTextField roleField = new JTextField(admin.getRole());

            Object[] fields = {
                    "ID:", idField,
                    "Username:", usernameField,
                    "Password:", passwordField,
                    "Role:", roleField
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Update Admin", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                admin.setId(Integer.parseInt(idField.getText()));
                admin.setUsername(usernameField.getText());
                admin.setPassword(passwordField.getText());
                admin.setRole(roleField.getText());

                tableModel.setValueAt(admin.getId(), selected, 0);
                tableModel.setValueAt(admin.getUsername(), selected, 1);
                tableModel.setValueAt(admin.getRole(), selected, 2);
            }
        }
    }

    private void deleteAdmin() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            adminList.remove(selected);
            tableModel.removeRow(selected);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.langspeakapp2;
/**
 *
 * @author ACER
 */


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LoginApp extends JFrame {

    private final Map<String, String> userDB = new HashMap<>();

    public LoginApp() {
        setTitle("Login - LangSpeak");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(registerBtn);
        panel.add(loginBtn);

        add(panel);

        // Tombol Register
        registerBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (!username.isBlank() && !password.isBlank()) {
                String hashed = CryptoUtil.hashBCrypt(password);
                userDB.put(username, hashed);
                JOptionPane.showMessageDialog(this, "✅ Registrasi berhasil!");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Username dan password tidak boleh kosong.");
            }
        });

        // Tombol Login
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (userDB.containsKey(username)) {
                String hashed = userDB.get(username);
                if (CryptoUtil.validateBCrypt(password, hashed)) {
                    JOptionPane.showMessageDialog(this, "✅ Login sukses!");
                    dispose(); // Tutup form login
                    new LangSpeakApp2(username); // Lanjut ke menu utama dengan username
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Password salah.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ Username tidak ditemukan.");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginApp::new);
    }
}

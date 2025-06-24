package com.mycompany.langspeakapp2;

import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class LangSpeakApp2 extends JFrame {

    private final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private final MongoDatabase database = mongoClient.getDatabase("langspeak");
    private final MongoCollection<Document> collection = database.getCollection("progress");
    private JTabbedPane tabbedPane;
    private final String username;

    public LangSpeakApp2(String username) {
        this.username = username;
        setTitle("LangSpeak - Aplikasi Latihan Bahasa");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));

        tabbedPane.addTab("\uD83D\uDCDA Latihan", createLatihanPanel());
        tabbedPane.addTab("\uD83D\uDCC8 Progress", createProgressPanel());
        tabbedPane.addTab("\uD83D\uDCDD Riwayat", createRiwayatPanel());
        tabbedPane.addTab("\uD83C\uDF10 Pengaturan", createPengaturanPanel());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                mongoClient.close();
            }
        });

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(34, 49, 63));
        JLabel label = new JLabel("\uD83C\uDFA7 Selamat Datang, " + username);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(label);
        return panel;
    }

    private JPanel createLatihanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        DefaultListModel<String> latihanModel = new DefaultListModel<>();
        latihanModel.addElement("Perkenalan Diri");
        latihanModel.addElement("Menyebutkan Hari");
        latihanModel.addElement("Menyebutkan Jam");
        latihanModel.addElement("Menanyakan Arah");

        JList<String> latihanList = new JList<>(latihanModel);
        latihanList.setBorder(BorderFactory.createTitledBorder("Pilih Latihan"));
        latihanList.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel latihanText = new JLabel("Teks latihan akan ditampilkan di sini.");
        latihanText.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel statusLabel = new JLabel("\uD83D\uDD0D Status: Menunggu perintah...");
        AudioRecorder recorder = new AudioRecorder("recorded.wav");

        JButton rekamBtn = new JButton("\uD83D\uDD34 Rekam");
        JButton stopBtn = new JButton("\u23F9\uFE0F Stop");
        JButton putarBtn = new JButton("\u25B6\uFE0F Putar");

        rekamBtn.addActionListener(e -> recorder.startRecording(
                () -> statusLabel.setText("\uD83C\uDF99\uFE0F Merekam..."),
                () -> statusLabel.setText("\u274C Gagal merekam.")));

        stopBtn.addActionListener(e -> {
            recorder.stopRecording();
            statusLabel.setText("\u2705 Rekaman selesai.");
        });

        putarBtn.addActionListener(e -> recorder.playRecording(
                () -> statusLabel.setText("\u25B6\uFE0F Memutar..."),
                () -> statusLabel.setText("\u2705 Selesai memutar."),
                () -> statusLabel.setText("\u274C Gagal memutar.")));

        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        latihanList.addListSelectionListener(e -> {
            String selected = latihanList.getSelectedValue();
            if (selected != null) {
                switch (selected) {
                    case "Perkenalan Diri" ->
                        latihanText.setText("Hello, my name is...");
                    case "Menyebutkan Hari" ->
                        latihanText.setText("Today is Monday...");
                    case "Menyebutkan Jam" ->
                        latihanText.setText("It is now 3 o'clock...");
                    case "Menanyakan Arah" ->
                        latihanText.setText("How do I get to the station?");
                }
            }
        });

        JButton nilaiBtn = new JButton("\u2705 Nilai");
        nilaiBtn.addActionListener(e -> {
            String latihan = latihanText.getText();
            String input = JOptionPane.showInputDialog(this, "Masukkan skor untuk latihan ini (0–100):");
            if (input != null && !input.isBlank()) {
                try {
                    int score = Integer.parseInt(input);
                    if (score < 0 || score > 100) {
                        JOptionPane.showMessageDialog(this, "\u26A0\uFE0F Skor harus antara 0 dan 100.");
                    } else {
                        Document doc = new Document("user", username)
                                .append("latihan", latihan)
                                .append("skor", score)
                                .append("timestamp", System.currentTimeMillis());
                        collection.insertOne(doc);
                        JOptionPane.showMessageDialog(this, "\u2705 Skor tersimpan: " + score);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "\u274C Input tidak valid.");
                }
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.add(rekamBtn);
        buttonPanel.add(stopBtn);
        buttonPanel.add(putarBtn);
        buttonPanel.add(nilaiBtn);
        buttonPanel.add(latihanText);
        buttonPanel.add(statusLabel);

        panel.add(new JScrollPane(latihanList), BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProgressPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel scoreLabel = new JLabel("Skor Terakhir: 0");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton muatBtn = new JButton("\uD83D\uDCC2 Muat dari MongoDB");
        muatBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        muatBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            FindIterable<Document> docs = collection.find(new Document("user", username)).sort(Sorts.descending("timestamp"));
            for (Document doc : docs) {
                sb.append(doc.getString("latihan"))
                        .append(" - ").append(doc.getInteger("skor"))
                        .append("\n");
            }
            scoreLabel.setText("<html>Skor Terakhir:<br>" + sb.toString().replace("\n", "<br>") + "</html>");
        });

        panel.add(scoreLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(muatBtn);

        return panel;
    }

    private JPanel createRiwayatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setEditable(false);

        JButton refreshBtn = new JButton("\uD83D\uDD04 Lihat Riwayat");
        refreshBtn.addActionListener((ActionEvent e) -> {
            StringBuilder sb = new StringBuilder();
            FindIterable<Document> docs = collection.find(new Document("user", username)).sort(Sorts.ascending("timestamp"));
            for (Document doc : docs) {
                sb.append(doc.getString("latihan"))
                        .append(" - ").append(doc.getInteger("skor"))
                        .append("\n");
            }
            textArea.setText(sb.toString());
        });

        panel.add(refreshBtn, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPengaturanPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel label = new JLabel("\uD83C\uDF10 Pilih Bahasa:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] languages = {"\uD83C\uDDEE\uD83C\uDDE9 Bahasa Indonesia", "\uD83C\uDDEC\uD83C\uDDE7 English"};
        JComboBox<String> langBox = new JComboBox<>(languages);
        langBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton applyBtn = new JButton("\uD83D\uDD01 Terapkan");
        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        applyBtn.addActionListener(e -> {
            String selected = (String) langBox.getSelectedItem();
            if (selected.contains("English")) {
                System.out.println("berhasill ke englishh");
                Locale.setDefault(Locale.ENGLISH);
            } else {
                System.out.println("berhasill ke ind");

                Locale.setDefault(new Locale("id", "ID"));
            }
//            SwingUtilities.invokeLater(() -> {
//                dispose();
//                new LangSpeakApp2(username);
//            });
        });

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(langBox);
        panel.add(Box.createVerticalStrut(20));
        panel.add(applyBtn);

        return panel;
    }

    public static void main(String[] args) {
        // Set bahasa ke Indonesia sebelum GUI dibentuk
        I18nUtil.setLocale(new Locale("id", "ID"));  // atau Locale.ENGLISH
        SwingUtilities.invokeLater(() -> new LoginApp().setVisible(true));
        System.out.println("Jalankan aplikasi melalui LoginApp.java");
    }

}

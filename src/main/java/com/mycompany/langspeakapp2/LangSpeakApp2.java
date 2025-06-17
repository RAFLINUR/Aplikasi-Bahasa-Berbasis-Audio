/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.langspeakapp2;

/**
 *
 * @author ACER
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class LangSpeakApp2 extends JFrame {

    private JTabbedPane tabbedPane;

    public LangSpeakApp2() {
        setTitle("LangSpeak - Aplikasi Latihan Bahasa");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));

        tabbedPane.addTab("📚 Latihan", createLatihanPanel());
        tabbedPane.addTab("📈 Progress", createProgressPanel());
        tabbedPane.addTab("📝 Riwayat", createRiwayatPanel());
        tabbedPane.addTab("🌐 Pengaturan", createPengaturanPanel());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(34, 49, 63));
        JLabel label = new JLabel("🎧 Selamat Datang di LangSpeak");
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

        JLabel statusLabel = new JLabel("🔍 Status: Menunggu perintah...");
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

        // Rekaman
        AudioRecorder recorder = new AudioRecorder("recorded.wav");

        JButton rekamBtn = new JButton(I18nUtil.getString("  🔴 Rekam"));
        JButton stopBtn = new JButton("⏹️ Stop");
        JButton putarBtn = new JButton("▶️ Putar");
        JButton nilaiBtn = new JButton("✅ Nilai");

        rekamBtn.addActionListener(e -> recorder.startRecording(
                () -> statusLabel.setText("🎙️ Merekam..."),
                () -> statusLabel.setText("❌ Gagal merekam.")
        ));

        stopBtn.addActionListener(e -> {
            recorder.stopRecording();
            statusLabel.setText("✅ Rekaman selesai.");
        });

        putarBtn.addActionListener(e -> recorder.playRecording(
                () -> statusLabel.setText("▶️ Memutar..."),
                () -> statusLabel.setText("✅ Selesai memutar."),
                () -> statusLabel.setText("❌ Gagal memutar.")
        ));

        nilaiBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel, "🎯 Penilaian belum diimplementasikan.");
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

        JButton simpanBtn = new JButton("💾 Simpan");
        JButton muatBtn = new JButton("📂 Muat");

        simpanBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        muatBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Dummy skor untuk disimpan
        Map<String, Integer> dummyScores = new HashMap<>();
        dummyScores.put("Perkenalan Diri", 85);
        dummyScores.put("Menyebutkan Hari", 90);

        simpanBtn.addActionListener(e -> {
            ProgressManager.saveProgress(dummyScores);
            JOptionPane.showMessageDialog(this, "Progress berhasil disimpan!");
        });

        muatBtn.addActionListener(e -> {
            Map<String, Integer> loaded = ProgressManager.loadProgress();
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Integer> entry : loaded.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            scoreLabel.setText("<html>Skor Terakhir:<br>" + sb.toString().replace("\n", "<br>") + "</html>");
        });

        panel.add(scoreLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(simpanBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(muatBtn);

        return panel;
    }

    private JPanel createRiwayatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setEditable(false);

        JButton refreshBtn = new JButton("🔄 Lihat Riwayat");
        refreshBtn.addActionListener((ActionEvent e) -> {
            textArea.setText("Latihan 1: 78\nLatihan 2: 90\nLatihan 3: 67"); // Dummy
        });

        panel.add(refreshBtn, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPengaturanPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel label = new JLabel("🌐 Pilih Bahasa:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] languages = {"🇮🇩 Bahasa Indonesia", "🇬🇧 English"};
        JComboBox<String> langBox = new JComboBox<>(languages);
        langBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton applyBtn = new JButton("🔁 Terapkan");
        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(langBox);
        panel.add(Box.createVerticalStrut(20));
        panel.add(applyBtn);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LangSpeakApp2::new);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.langspeakapp2;

/**
 *
 * @author ACER
 */
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Kelas untuk menyimpan dan memuat skor kemajuan pengguna menggunakan serialization.
 */
public class ProgressManager {

    private static final String FILE_PATH = "progress.ser";

    /**
     * Menyimpan skor ke file.
     * @param scores Map yang berisi nama latihan dan skor
     */
    public static void saveProgress(Map<String, Integer> scores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(scores);
            System.out.println("Progress berhasil disimpan.");
        } catch (IOException e) {
            System.err.println("Gagal menyimpan progress: " + e.getMessage());
        }
    }

    /**
     * Memuat skor dari file.
     * @return Map skor latihan, atau Map kosong jika gagal
     */
    public static Map<String, Integer> loadProgress() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                return (Map<String, Integer>) obj;
            } else {
                System.err.println("Data progress tidak valid.");
                return new HashMap<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Gagal memuat progress: " + e.getMessage());
            return new HashMap<>();
        }
    }
}

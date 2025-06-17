/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.langspeakapp2;

/**
 *
 * @author ACER
 */
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

/**
 * GenericScoreServer adalah kelas yang fleksibel untuk menyimpan dan mengambil
 * skor pengguna menggunakan tipe data generik.
 * @param <K> Tipe kunci (misalnya nama pengguna)
 * @param <V> Tipe nilai skor (misalnya Integer, Double, atau custom class)
 */
public class GenericScoreServer<K, V> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<K, V> scoreMap;

    public GenericScoreServer() {
        this.scoreMap = new HashMap<>();
    }

    /**
     * Menambahkan atau memperbarui skor untuk pengguna tertentu.
     */
    public void addScore(K user, V score) {
        scoreMap.put(user, score);
    }

    /**
     * Mengambil skor untuk pengguna tertentu.
     */
    public V getScore(K user) {
        return scoreMap.get(user);
    }

    /**
     * Mengecek apakah data pengguna tersedia.
     */
    public boolean containsUser(K user) {
        return scoreMap.containsKey(user);
    }

    /**
     * Menghapus data pengguna dari sistem skor.
     */
    public void removeUser(K user) {
        scoreMap.remove(user);
    }

    /**
     * Mengambil seluruh skor pengguna.
     */
    public Map<K, V> getAllScores() {
        return scoreMap;
    }

    /**
     * Membersihkan seluruh data skor.
     */
    public void clearAllScores() {
        scoreMap.clear();
    }

    /**
     * Mendapatkan jumlah total pengguna yang tersimpan.
     */
    public int totalUsers() {
        return scoreMap.size();
    }
}

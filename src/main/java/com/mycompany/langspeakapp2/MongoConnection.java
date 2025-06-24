package com.mycompany.langspeakapp2;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.*;

public class MongoConnection {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DB_NAME = "langspeak";
    private static final String COLLECTION_NAME = "progress";

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public MongoConnection() {
        mongoClient = MongoClients.create(CONNECTION_STRING);
        database = mongoClient.getDatabase(DB_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    /**
     * Menyimpan data latihan ke MongoDB
     *
     * @param username nama pengguna
     * @param latihan  nama latihan
     * @param skor     nilai latihan
     */
    public void insertProgress(String username, String latihan, int skor) {
        Document doc = new Document("user", username)
                .append("latihan", latihan)
                .append("skor", skor)
                .append("timestamp", System.currentTimeMillis());
        collection.insertOne(doc);
        System.out.println("✅ Skor berhasil disimpan ke MongoDB.");
    }

    /**
     * Mengambil semua data progress (tanpa filter)
     *
     * @return daftar dokumen progress
     */
    public List<Document> getAllProgress() {
        List<Document> result = new ArrayList<>();
        FindIterable<Document> docs = collection.find().sort(Sorts.ascending("timestamp"));
        for (Document doc : docs) {
            result.add(doc);
        }
        return result;
    }

    /**
     * Mendapatkan skor terakhir untuk tiap latihan user
     *
     * @param username nama user
     * @return map namaLatihan → skorTerakhir
     */
    public Map<String, Integer> getLatestProgressPerLatihan(String username) {
        Map<String, Integer> latestScores = new HashMap<>();

        // Ambil semua latihan unik
        List<String> latihanList = collection.distinct("latihan", Filters.eq("user", username), String.class).into(new ArrayList<>());

        for (String latihan : latihanList) {
            Document doc = collection.find(
                    Filters.and(Filters.eq("user", username), Filters.eq("latihan", latihan))
            ).sort(Sorts.descending("timestamp")).limit(1).first();

            if (doc != null) {
                latestScores.put(latihan, doc.getInteger("skor", 0));
            }
        }

        return latestScores;
    }

    /**
     * Menutup koneksi MongoDB
     */
    public void close() {
        mongoClient.close();
    }

    // -------------------- CONTOH PENGGUNAAN --------------------
    public static void main(String[] args) {
        MongoConnection mongo = new MongoConnection();

        // Simpan data
        mongo.insertProgress("rafli123", "Perkenalan Diri", 85);
        mongo.insertProgress("rafli123", "Menyebutkan Hari", 92);

        // Ambil semua data
        List<Document> all = mongo.getAllProgress();
        for (Document doc : all) {
            System.out.println(doc.toJson());
        }

        // Ambil skor terakhir per latihan
        Map<String, Integer> skorTerakhir = mongo.getLatestProgressPerLatihan("rafli123");
        System.out.println("Skor Terakhir:");
        skorTerakhir.forEach((latihan, skor) ->
                System.out.println(" - " + latihan + ": " + skor));

        mongo.close();
    }
}

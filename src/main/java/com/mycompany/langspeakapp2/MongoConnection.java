/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.langspeakapp2;

/**
 *
 * @author ACER
 */
import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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
    }

    /**
     * Mengambil semua data progress
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
     * Menutup koneksi ke MongoDB
     */
    public void close() {
        mongoClient.close();
    }
}

package com.orbix.database;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class CreateConnection {

  public static void main(String[] args) {
    // Replace the uri string with your MongoDB deployment's connection string
    String uri =
      "mongodb+srv://orbixMember:orbixTeam@cluster0.fma2x.mongodb.net/test";
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase database = mongoClient.getDatabase("ORBIX");
      MongoCollection<Document> collection = database.getCollection("records");
      Document doc = collection.find(eq("User", "Simion Inisconi")).first();
      System.out.println(doc.toJson());
    }
  }
}

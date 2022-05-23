package com.orbix.database;

import com.mongodb.MongoException;
import com.mongodb.client.*;

import static com.mongodb.client.model.Sorts.*;

import org.bson.Document;


import java.util.*;

public class DatabaseParser {
  private static final String uri = "mongodb+srv://orbixMember:orbixTeam@cluster0.fma2x.mongodb.net/test";
  private static MongoClient mongoClient;

  public DatabaseParser() throws MongoException {
    mongoClient = MongoClients.create(uri);
  }

  public void close() {
    try
    {
        mongoClient.close();
    }
    catch(MongoException mongoException)
    {
        mongoException.printStackTrace();
    }
  }

  public ArrayList<Database> parseAscending() {
    ArrayList<Database> elements = new ArrayList<Database>();
    List<Document> collection = new ArrayList<Document>() ;
    mongoClient.getDatabase("ORBIX").getCollection("records").find().sort(ascending("DateTime", "User","GPU", "Benchmark", "Score")).into(collection);
    for (Document element: collection) {
        Database tmp = new Database();
        tmp.setTime(element.getString("DateTime"));
        tmp.setUser(element.getString("User"));
        tmp.setGpu(element.getString("GPU"));
        tmp.setBench(element.getString("Benchmark"));
        tmp.setScore(element.getLong("Score"));
        elements.add(tmp);
    }
    return elements;
  }
}

package com.orbix.database;

import static com.mongodb.client.model.Sorts.*;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.orbix.logging.BenchResult;
import java.util.*;
import org.bson.Document;

public class DatabaseParser {

  private static final String uri =
    "mongodb+srv://orbixMember:orbixTeam@cluster0.fma2x.mongodb.net/test";
  private static MongoClient mongoClient;

  public DatabaseParser() throws MongoException {
    mongoClient = MongoClients.create(uri);
  }

  public void close() {
    try {
      mongoClient.close();
    } catch (MongoException mongoException) {
      mongoException.printStackTrace();
    }
  }

  public ArrayList<BenchResult> parseDescending() {
    ArrayList<BenchResult> elements = new ArrayList<BenchResult>();
    List<Document> collection = new ArrayList<Document>();
    mongoClient
      .getDatabase("ORBIX")
      .getCollection("records")
      .find()
      .sort(descending("DateTime", "User", "GPU", "Benchmark", "Score"))
      .into(collection);
    for (Document element : collection) {
      elements.add(
        new BenchResult(
          element.getString("DateTime"),
          element.getString("User"),
          element.getString("GPU"),
          element.getString("Benchmark"),
          element.getLong("Score")
        )
      );
    }
    return elements;
  }
}

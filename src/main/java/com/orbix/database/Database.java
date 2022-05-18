package com.orbix.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public final class Database {
  private static final String uri = "mongodb+srv://orbixMember:orbixTeam@cluster0.fma2x.mongodb.net/test";
  public static MongoClient mongoClient;
  public static MongoDatabase database;

  private Database(){

  }
}

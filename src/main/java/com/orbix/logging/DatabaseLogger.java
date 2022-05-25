package com.orbix.logging;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.orbix.gui.AlertDisplayer;
import java.io.IOException;
import org.bson.Document;
import org.bson.types.ObjectId;

public class DatabaseLogger implements ILogger {

  private static final String uri =
    "mongodb+srv://orbixMember:orbixTeam@cluster0.fma2x.mongodb.net/test";
  private final MongoClient mongoClient;

  public DatabaseLogger() throws MongoException {
    mongoClient = MongoClients.create(uri);
  }

  @Override
  public void close() {
    try {
      mongoClient.close();
    } catch (MongoException mongoException) {
      mongoException.printStackTrace();
    }
  }

  @Override
  public void write(BenchResult benchResult) {
    try {
      mongoClient
        .getDatabase("ORBIX")
        .getCollection("records")
        .insertOne(
          new Document()
            .append("_id", new ObjectId())
            .append("DateTime", benchResult.utcdateTime)
            .append("User", benchResult.getUserName())
            .append("GPU", benchResult.getGPUName())
            .append("Benchmark", benchResult.getBenchName())
            .append("Score", benchResult.getScore())
        );
    } catch (MongoException mongoException) {
      try {
        new FileLogger("logs").write(benchResult);
      } catch (IOException e) {
        new ConsoleLogger().write(benchResult);
        AlertDisplayer.displayWarning(
          "File Write Warning",
          null,
          "Can not write to the log file. Will write to the console instead."
        );
        e.printStackTrace();
      }
      mongoException.printStackTrace();
    }
  }
}

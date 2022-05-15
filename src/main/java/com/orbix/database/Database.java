package com.orbix.database;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.result.InsertOneResult;
import com.orbix.logging.BenchResult;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;

public class Database {

    private static final String uri = "mongodb+srv://orbixMember:orbixTeam@cluster0.fma2x.mongodb.net/test";
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    /*
    public static void createConnection(){
        //try ( {

            //Document doc = collection.find(eq("Benchmark", "Matrix Multiplication")).first();
            //System.out.println(doc.toJson());
        }
    }*/

    public static void insertRecord(BenchResult result)
    {
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("ORBIX");
        collection = database.getCollection("records");
        InsertOneResult oneResult = collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("Time", result.utcDateTime)
                    .append("GPU", result.GPUName)
                    .append("Benchmark", result.benchName)
                    .append("Runtime", result.runTime)
                    .append("Score", result.score)
                    .append("User", result.userName)
                );
    }
}

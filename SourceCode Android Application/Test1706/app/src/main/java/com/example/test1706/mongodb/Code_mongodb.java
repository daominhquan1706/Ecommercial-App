package com.example.test1706.mongodb;

import android.util.Log;

import com.example.test1706.model.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertManyResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Code_mongodb {
    StitchAppClient client;
    RemoteMongoClient mongoClient;
    RemoteMongoDatabase db;
    RemoteMongoCollection<Document> collection;
    String String_collection;

    public Code_mongodb(String collection) {
        this.String_collection = collection;
        cauhinh();
    }

    public void cauhinh() {
        client = Stitch.getDefaultAppClient();
        mongoClient = client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        db = mongoClient.getDatabase("PowerR_database");
        collection = db.getCollection(String_collection);
        client.getAuth().loginWithCredential(new AnonymousCredential()).continueWithTask(
                new Continuation<StitchUser, Task<RemoteUpdateResult>>() {

                    @Override
                    public Task<RemoteUpdateResult> then(@com.mongodb.lang.NonNull Task<StitchUser> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e("STITCH", "Login failed!");
                            throw task.getException();
                        }

                        final Document updateDoc = new Document(
                                "owner_id",
                                task.getResult().getId()
                        );

                        updateDoc.put("number", 42);
                        return collection.updateOne(null, updateDoc, new RemoteUpdateOptions().upsert(true)
                        );
                    }
                }
        ).continueWithTask(new Continuation<RemoteUpdateResult, Task<List<Document>>>() {
            @Override
            public Task<List<Document>> then(@com.mongodb.lang.NonNull Task<RemoteUpdateResult> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.e("STITCH", "Update failed!");
                    throw task.getException();
                }
                List<Document> docs = new ArrayList<>();
                return collection
                        .find(new Document("owner_id", client.getAuth().getUser().getId()))
                        .limit(100)
                        .into(docs);
            }
        }).addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {
                if (task.isSuccessful()) {
                    Log.d("STITCH", "Found docs: " + task.getResult().toString());
                    return;
                }
                Log.e("STITCH", "Error: " + task.getException().toString());
                task.getException().printStackTrace();
            }
        });
    }

    public void ThemDulieu(List<Product> productList) {

        List<Document> documentList = Collections.emptyList();
        for (Product product :productList){
            Document document = new Document();
            document.append("Product_Name", product.getProduct_Name());
            document.append("Price", product.getPrice());
            document.append("category", product.getCategory());
            document.append("Image", product.getImage());
            document.append("Image_Night", product.getImage_Night());
            document.append("Description", product.getDescription());
            document.append("Quantity", product.getQuantity());
            documentList.add(document);
        }





        final Task<RemoteInsertManyResult> insertTask = collection.insertMany(documentList);
        insertTask.addOnCompleteListener(new OnCompleteListener<RemoteInsertManyResult>() {
            @Override
            public void onComplete(@NonNull Task<RemoteInsertManyResult> task) {
                if (task.isSuccessful()) {
                    Log.d("app",
                            String.format("successfully inserted %d items with ids: %s",
                                    task.getResult().getInsertedIds().size(),
                                    task.getResult().getInsertedIds().toString()));
                } else {
                    Log.e("app", "failed to insert document with: ", task.getException());
                }
            }
        });
    }

    public List<Product> layDanhSachDongHo() {

        List<Product> productList = new ArrayList<Product>();

        Document filterDoc = new Document()
                .append("reviews.0", new Document().append("$exists", true));

        RemoteFindIterable findResults = collection
                .find()
                .projection(new Document().append("_id", 0))
                .sort(new Document().append("name", 1));

        // One way to iterate through
        findResults.forEach(item -> {

            productList.add((Product)item);
            Log.d("app", String.format("successfully found:  %s", item.toString()));
        });


        return productList;
    }
}

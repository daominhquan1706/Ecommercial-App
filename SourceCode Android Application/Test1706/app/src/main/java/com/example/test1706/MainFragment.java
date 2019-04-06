package com.example.test1706;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.test1706.model.Product;
import com.example.test1706.mongodb.Code_mongodb;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    List<Product> productList;
    List<String> mkey;
    private StorageReference mStorageRef;
    DatabaseReference myRef;
    ProductAdapter productadapter;
    ProgressBar progressBar;
    MyGridView listView;
    int listheight = 0;
    Product_Recycle_Adapter hori_Adapter;
    RecyclerView recyclerView;
    ViewPager v_page_massage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mStorageRef = FirebaseStorage.getInstance().getReference();

        init();

        //SLIDE SHOW

        ImageAdapter adapter = new ImageAdapter(getActivity());
        v_page_massage.setAdapter(adapter);
        //v_page_massage.setVisibility(View.GONE);

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);


        productList = new ArrayList<Product>();
        mkey = new ArrayList<String>();
        productadapter = new ProductAdapter(getActivity(), productList);

        listView.setAdapter(productadapter);
        //DATABASE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        listView.setNumColumns(2);
        listView.setHorizontalSpacing(20);
        listView.setVerticalSpacing(20);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), productadapter.getItem(position).getProduct_Name(), Toast.LENGTH_SHORT).show();
            }
        });

        //getProductdata();


        // Read from the database
        myRef.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product itemproduct = dataSnapshot.getValue(Product.class);
                productList.add(itemproduct);
                productadapter.notifyDataSetChanged();
                hori_Adapter.notifyDataSetChanged();
                mkey.add(dataSnapshot.getKey());
                Log.d("dữ liệu Firebase", "đã lấy thành công đồng hồ " + itemproduct.getProduct_Name());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                productList.set(mkey.indexOf(dataSnapshot.getKey()), dataSnapshot.getValue(Product.class));
                productadapter.notifyDataSetChanged();
                hori_Adapter.notifyDataSetChanged();
                Log.d("UPDATE dữ liệu ", dataSnapshot.getValue(Product.class).getProduct_Name() + s);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.dismiss();

        initRecycleView();



    }

    private void cauhinhMongo() {
        final StitchAppClient client =
                Stitch.initializeDefaultAppClient("powerr-ntmjm");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("PowerR_database").getCollection("PowerR_collection");

        client.getAuth().loginWithCredential(new AnonymousCredential()).continueWithTask(
                new Continuation<StitchUser, Task<RemoteUpdateResult>>() {

                    @Override
                    public Task<RemoteUpdateResult> then(@NonNull Task<StitchUser> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e("STITCH", "Login failed!");
                            throw task.getException();
                        }

                        final Document updateDoc = new Document(
                                "owner_id",
                                task.getResult().getId()
                        );

                        updateDoc.put("number", 42);
                        return coll.updateOne(
                                null, updateDoc, new RemoteUpdateOptions().upsert(true)
                        );
                    }
                }
        ).continueWithTask(new Continuation<RemoteUpdateResult, Task<List<Document>>>() {
            @Override
            public Task<List<Document>> then(@NonNull Task<RemoteUpdateResult> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.e("STITCH", "Update failed!");
                    throw task.getException();
                }
                List<Document> docs = new ArrayList<>();
                return coll
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


    private void getProductdata() {
        List<Product> list_data = new ArrayList<Product>();
        Product dongho1 = new Product("Đồng hồ sồ 1", 300, "Sport", "dongho_1");
        Product dongho2 = new Product("Đồng hồ sồ 2", 300, "Fashion", "dongho_2");
        Product dongho3 = new Product("Đồng hồ sồ 3", 300, "Bussiness", "dongho_3");
        myRef.push().setValue(dongho1);
        myRef.push().setValue(dongho2);
        myRef.push().setValue(dongho3);
    }

    private void init() {
        listView = (MyGridView) getView().findViewById(R.id.listView_product_smartwatch);
        v_page_massage = (ViewPager) getView().findViewById(R.id.v_pager_fragment_message);
    }

    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleview_horizontal);
        recyclerView.setLayoutManager(layoutManager);
        hori_Adapter = new Product_Recycle_Adapter(getActivity(), productList);
        recyclerView.setAdapter(hori_Adapter);

    }
}

package com.example.test1706;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.test1706.model.CartSqliteHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Admin_Message_Activity extends AppCompatActivity {
    CartSqliteHelper cartSqliteHelper;
    MyListView lv_user_chat_pick;
    private static final String TAG = "Admin_Message_Activity";
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<ChatMessage> chatMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__message_);

        chatMessageList = new ArrayList<ChatMessage>();
    }

    public void init() {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        cartSqliteHelper = new CartSqliteHelper(this);
        lv_user_chat_pick = (MyListView) findViewById(R.id.lv_user_chat_pick);
    }
}

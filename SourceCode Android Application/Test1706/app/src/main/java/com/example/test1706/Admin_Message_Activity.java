package com.example.test1706;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.test1706.model.CartSqliteHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Message_Activity extends AppCompatActivity {
    CartSqliteHelper cartSqliteHelper;
    MyListView lv_user_chat_pick;
    private static final String TAG = "Admin_Message_Activity";
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<ChatMessage> chatMessageList;
    Admin_message_account_adapter adapter;
    List<String> userUID_List;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__message_);
        init();
        chatMessageList = new ArrayList<ChatMessage>();
        displayChatMessage();
        adapter = new Admin_message_account_adapter(this, chatMessageList);
        displayChatMessage();
        lv_user_chat_pick.setAdapter(adapter);
    }

    public void init() {
        userUID_List = new ArrayList<String>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        cartSqliteHelper = new CartSqliteHelper(this);
        lv_user_chat_pick = (MyListView) findViewById(R.id.lv_user_chat_pick);
    }

    private void displayChatMessage() {
        username = "";
        myRef.child("chat_message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String userUID = (String) dataSnapshot.getKey();
                if (!isExistsInlist(userUID_List, userUID)) {
                    userUID_List.add(userUID);
                    if (userUID != null) {
                        myRef.child("chat_message").child(userUID).child("emailCustomer").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                username = dataSnapshot.getValue().toString();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        myRef.child("chat_message").child(userUID).child("message").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    ChatMessage chatMessage1 = dataSnapshot1.getValue(ChatMessage.class);
                                    if (chatMessage1.getMessageUser().equals("admin")) {
                                        chatMessage1.setMessageText("admin: " + chatMessage1.getMessageText());
                                    }
                                    chatMessage1.setMessageUser(username);
                                    chatMessageList.add(chatMessage1);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Handle possible errors.
                            }
                        });

                    }


                }
                Log.d(TAG, "đếm child: " + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
    }

    @Override
    protected void onResume() {

        adapter.notifyDataSetChanged();
        super.onResume();
    }

    public boolean isExistsInlist(List<String> userUID_List, String UserUID) {
        boolean isExistsInlist = false;
        for (String item : userUID_List) {
            if (item.equals(UserUID)) {
                isExistsInlist = true;
            }
        }
        return isExistsInlist;
    }


}

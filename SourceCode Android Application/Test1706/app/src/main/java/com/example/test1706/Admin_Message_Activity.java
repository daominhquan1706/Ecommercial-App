package com.example.test1706;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.ChatMessage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class Admin_Message_Activity extends AppCompatActivity {
    CartSqliteHelper cartSqliteHelper;
    ListView lv_user_chat_pick;
    private static final String TAG = "Admin_Message_Activity";
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<ChatMessage> chatMessageList;
    Admin_message_account_adapter adapter;
    List<String> userUID_List, mkey;
    String username;
    GifImageView loadingscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_message_);

        Slidr.attach(this);
        init();
        chatMessageList = new ArrayList<ChatMessage>();
        displayChatMessage();
        adapter = new Admin_message_account_adapter(this, chatMessageList);
        displayChatMessage();
        lv_user_chat_pick.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return true;
            }
        });
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void init() {
        loadingscreen = (GifImageView) findViewById(R.id.loadingscreen);
        mkey = new ArrayList<String>();
        userUID_List = new ArrayList<String>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        cartSqliteHelper = new CartSqliteHelper(this);
        lv_user_chat_pick = (ListView) findViewById(R.id.lv_user_chat_pick);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                                if (dataSnapshot.getValue() != null) {
                                    username = dataSnapshot.getValue().toString();
                                }

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
                                    mkey.add(dataSnapshot1.getKey());

                                    Collections.sort(chatMessageList);
                                    adapter.notifyDataSetChanged();
                                    loadingscreen.setVisibility(View.GONE);


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}

package com.example.test1706;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1706.model.Product;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ChatBoxMainActivity extends AppCompatActivity {
    List<ChatMessage> chatMessageList;
    private static int SIGN_IN_REQUEST_CODE = 1;
    RelativeLayout activity_chatboxmain;
    FloatingActionButton btn_send_message;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText input;
    ListView listOfMessage;
    private static final String TAG = "ChatBoxMainActivity";
    CustomListAdapter customListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatboxmainactivity);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        activity_chatboxmain = (RelativeLayout) findViewById(R.id.activity_chatboxmainactivity);
        btn_send_message = (FloatingActionButton) findViewById(R.id.fab);
        input = (EditText) findViewById(R.id.input);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        } else {
            displayChatMessage();
        }
        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                FirebaseDatabase.getInstance().getReference().child("chat_message").push().setValue(new ChatMessage(
                        input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        System.currentTimeMillis(),
                        "admin"));
                input.setText("");
                customListAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_chatboxmain, "You have been signed out", Snackbar.LENGTH_SHORT).show();
                    finish();

                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_chatbox, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_chatboxmain, "Succesfully sign in", Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            } else {
                Snackbar.make(activity_chatboxmain, "TRY AGAIN !!!!", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void displayChatMessage() {
        listOfMessage = (ListView) findViewById(R.id.list_of_message);
        chatMessageList = new ArrayList<ChatMessage>();
        customListAdapter = new CustomListAdapter(this, chatMessageList);
        listOfMessage.setAdapter(customListAdapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("chat_message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage itemProduct = dataSnapshot.getValue(ChatMessage.class);
                chatMessageList.add(itemProduct);
                Log.d(TAG, "onChildAdded: " + itemProduct.getMessageUser());
                customListAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
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
    private void scrollMyListViewToBottom() {
        listOfMessage.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listOfMessage.setSelection(customListAdapter.getCount() - 1);
            }
        });
    }
}


package com.example.test1706;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.test1706.UserModel.AccountUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Profile_Account_Activity extends AppCompatActivity {
    CircleImageView img_user_avatar;
    Toolbar toolbar;
    AccountUser accountUser;
    EditText tv_name_profile, tv_phonenumber_profile, tv_address_profile;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        init();
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Glide.with(this).load(R.drawable.ba_cai_dong_ho).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    collapsingToolbarLayout.setBackground(resource);
                }
            }
        });

    }


    public void getCurrentUser() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userUID = firebaseUser.getUid();
        if (firebaseUser != null) {
            databaseReference.child("Account").child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AccountUser accountUser1 = dataSnapshot.getValue(AccountUser.class);
                    tv_name_profile.setText(accountUser1.getName());
                    tv_phonenumber_profile.setText(accountUser1.getSDT());
                    tv_address_profile.setText(accountUser1.getDiachi());
                    Glide.with(User_Profile_Account_Activity.this)
                            .load("https://api.adorable.io/avatars/" + accountUser1.getUID() + "@adorable.png")
                            .apply(new RequestOptions().centerCrop())
                            .into(img_user_avatar);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void init() {
        tv_name_profile = (EditText) findViewById(R.id.tv_name_profile);
        tv_phonenumber_profile = (EditText) findViewById(R.id.tv_phonenumber_profile);
        tv_address_profile = (EditText) findViewById(R.id.tv_address_profile);
        img_user_avatar = (CircleImageView) findViewById(R.id.img_user_avatar);

        accountUser = new AccountUser();



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(accountUser.getEmail());
        }


        getCurrentUser();
    }
}

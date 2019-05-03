package com.example.test1706;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;

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
    Toolbar toolbar_profile;
    AccountUser accountUser;
    EditText tv_name_profile, tv_phonenumber_profile, tv_address_profile;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    boolean isEditing;
    Button btn_save_profile;
    FirebaseUser firebaseUser;
    String userUID;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //loading
        pd = new ProgressDialog(User_Profile_Account_Activity.this);
        pd.setMessage("Đang lấy dữ liệu");
        pd.show();


        isEditing = false;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditing) {
                    Snackbar.make(view, "đã mở chế độ sửa thông tin", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    btn_save_profile.setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(view, "đã tắt chế độ sửa thông tin", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getCurrentUser();
                    btn_save_profile.setVisibility(View.GONE);
                }
                isEditing = !isEditing;
                tv_address_profile.setEnabled(isEditing);
                tv_name_profile.setEnabled(isEditing);
                tv_phonenumber_profile.setEnabled(isEditing);
                btn_save_profile.setClickable(isEditing);

            }
        });
        init();
        getCurrentUser();
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Glide.with(this).load(R.drawable.ba_cai_dong_ho).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    collapsingToolbarLayout.setBackground(resource);
                }
            }
        });

        btn_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountUser1 != null) {
                    databaseReference.child("Account").child(userUID).child("email").setValue(accountUser1.getEmail());
                    databaseReference.child("Account").child(userUID).child("name").setValue(tv_name_profile.getText().toString());
                    databaseReference.child("Account").child(userUID).child("sdt").setValue(tv_phonenumber_profile.getText().toString());
                    databaseReference.child("Account").child(userUID).child("diachi").setValue(tv_address_profile.getText().toString());
                    databaseReference.child("Account").child(userUID).child("uid").setValue(userUID);
                    Snackbar.make(v, "đã lưu thông tin thành công", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getCurrentUser();
                } else {
                    Snackbar.make(v, "thất bại", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getCurrentUser();
                }
                isEditing = !isEditing;
                tv_address_profile.setEnabled(isEditing);
                tv_name_profile.setEnabled(isEditing);
                tv_phonenumber_profile.setEnabled(isEditing);
                btn_save_profile.setClickable(isEditing);
                btn_save_profile.setVisibility(View.GONE);
            }
        });

    }

    AccountUser accountUser1;

    public void getCurrentUser() {

        if (firebaseUser != null) {
            databaseReference.child("Account").child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    accountUser1 = dataSnapshot.getValue(AccountUser.class);
                    tv_name_profile.setText(accountUser1.getName());
                    tv_phonenumber_profile.setText(accountUser1.getSDT());
                    tv_address_profile.setText(accountUser1.getDiachi());
                    Glide.with(User_Profile_Account_Activity.this)
                            .load("https://api.adorable.io/avatars/" + accountUser1.getUID() + "@adorable.png")
                            .apply(new RequestOptions().centerCrop())
                            .into(img_user_avatar);
                    pd.cancel();
                    toolbar_profile.setTitle(accountUser1.getEmail());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void init() {
        toolbar_profile = (Toolbar) findViewById(R.id.toolbar_profile);
        tv_name_profile = (EditText) findViewById(R.id.tv_name_profile);
        tv_phonenumber_profile = (EditText) findViewById(R.id.tv_phonenumber_profile);
        tv_address_profile = (EditText) findViewById(R.id.tv_address_profile);
        img_user_avatar = (CircleImageView) findViewById(R.id.img_user_avatar);
        btn_save_profile = (Button) findViewById(R.id.btn_save_profile);
        accountUser = new AccountUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(accountUser.getEmail());
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userUID = firebaseUser.getUid();

    }
}

package com.example.shiper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    Button btn_dangxuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        btn_dangxuat = (Button) findViewById(R.id.btn_dangxuat);
        checkLogin();

        btn_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangXuat();
            }
        });
    }

    private void checkLogin() {
        if (firebaseUser == null) {
            Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(MainActivity.this, TrangChinhActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void dangXuat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có muốn đăng xuất không ?");
        builder.setCancelable(false);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent troVeDangNhap = new Intent(MainActivity.this, DangNhapActivity.class);
                startActivity(troVeDangNhap);
                finish();
                Toast.makeText(MainActivity.this, "Đăng xuất thành công !", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

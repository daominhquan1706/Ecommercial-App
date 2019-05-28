package com.example.shiper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DangNhapActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    EditText email, password;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    Button btn_dangnhap, btn_dangky;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        init();
        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    DangNhap();
                }
            }
        });
    }

    private void DangNhap() {

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent i = new Intent(DangNhapActivity.this, TrangChinhActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(DangNhapActivity.this, "Đăng nhập thất bại" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    public boolean isValid() {
        String sEmail = email.getText().toString().trim();
        String sPassword = password.getText().toString().trim();

        if (sEmail.equals("")) {
            email.setError("Thiếu email");
            return false;
        } else if (sPassword.equals("")) {
            password.setError("Thiếu mật khẩu");
            return false;
        } else if (!sEmail.contains("@")) {
            email.setError("Sai định dạng email");
            return false;
        } else if (sEmail.length() < 6) {
            email.setError("Sai định dạng email");
            return false;
        } else if (sEmail.split("@")[1].length() < 6) {
            email.setError("Sai định dạng email");
            return false;
        }

        return true;
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Shipper");
        //EditText
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        //Button
        btn_dangnhap = (Button) findViewById(R.id.btn_dangnhap);
        btn_dangky = (Button) findViewById(R.id.btn_dangky);

    }
}

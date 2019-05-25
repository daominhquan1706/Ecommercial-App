package com.example.shiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import static android.support.constraint.Constraints.TAG;

public class DangKyActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    EditText email, password, repassword;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    Button btn_dangky, btn_dangnhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        init();
        checkLogin();
        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    DangKy();
                }
            }
        });


    }


    public void DangKy() {
        String sEmail = email.getText().toString().trim();
        String sPassword = password.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(DangKyActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(DangKyActivity.this, "Đăng ký thất bại : "+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void checkLogin() {
        if (firebaseUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Shipper");
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        btn_dangky = (Button) findViewById(R.id.btn_dangky);
        btn_dangnhap = (Button) findViewById(R.id.btn_dangnhap);
    }

    public boolean isValid() {
        String sEmail = email.getText().toString().trim();
        String sPassword = password.getText().toString().trim();
        String sRepassword = repassword.getText().toString().trim();

        if (sEmail.equals("")) {
            email.setError("Thiếu email");
            return false;
        } else if (sPassword.equals("")) {
            password.setError("Thiếu mật khẩu");
            return false;
        } else if (!sEmail.contains("@")) {
            password.setError("Sai định dạng email");
            return false;
        } else if (sEmail.length() < 6) {
            password.setError("Sai định dạng email");
            return false;
        } else if (sEmail.split("@")[1].length() < 6) {
            password.setError("Sai định dạng email");
            return false;
        } else if (sRepassword.equals("")) {
            repassword.setError("Thiếu nhập lại mật khẩu");
            return false;
        } else if (!sPassword.equals(sRepassword)) {
            repassword.setError("nhập lại mật khẩu sai");
            return false;
        }

        return true;
    }

}

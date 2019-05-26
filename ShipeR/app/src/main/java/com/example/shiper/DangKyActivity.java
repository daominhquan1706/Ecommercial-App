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

import com.example.shiper.Model.AccountUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DangKyActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    EditText email, password, repassword;
    Button btn_dangky, btn_dangnhap;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private List<AccountUser> accountUserList;


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
                            //nếu thành công , lưu user vừa tạo vào csdl
                            FirebaseUser user = mAuth.getCurrentUser();
                            addShipperToFirebase(user);
                        } else {
                            // nếu thất bại hiện thông báo cho user
                            Toast.makeText(DangKyActivity.this, "Đăng ký thất bại : " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addShipperToFirebase(FirebaseUser currentUser) {
        if (currentUser != null) {
            AccountUser accountUser = new AccountUser();
            accountUser.setEmail(currentUser.getEmail());
            accountUser.setUID(currentUser.getUid());
            databaseReference.child(currentUser.getUid()).setValue(accountUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DangKyActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }


    private void checkLogin() {
        if (firebaseUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void init() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Shipper");
        mAuth = FirebaseAuth.getInstance();
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
            email.setError("Sai định dạng email");
            return false;
        } else if (sEmail.length() < 6) {
            email.setError("Sai định dạng email");
            return false;
        } else if (sEmail.split("@")[1].length() < 6) {
            email.setError("Sai định dạng email");
            return false;
        } else if (sRepassword.equals("")) {
            repassword.setError("Thiếu nhập lại mật khẩu");
            return false;
        } else if (!sPassword.equals(sRepassword)) {
            password.setError("nhập lại mật khẩu sai");
            return false;
        }

        return true;
    }

}

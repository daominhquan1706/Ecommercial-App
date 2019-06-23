package com.example.adminr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.r0adkll.slidr.Slidr;

public class LoginActivity_ForgotPassword extends AppCompatActivity {

    TextView mEmailView;
    Button btn_forgotpassword;
    FirebaseAuth firebaseAuth;
    private View relativelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        //Slidr.attach(this);
        mEmailView = (TextView) findViewById(R.id.email);
        btn_forgotpassword = (Button) findViewById(R.id.accept_forgot_password);
        firebaseAuth = FirebaseAuth.getInstance();
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return true;
            }
        });

        btn_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                checkEmail();

            }
        });

        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.accept_forgot_password), "Hướng dẫn sử dụng", "Click để nhận lại mật khẩu")
                        .tintTarget(false)
                        .outerCircleColor(R.color.MoneyColor));
    }

    private void checkEmail() {
        String email = mEmailView.getText().toString().trim();
        if (email.isEmpty()) {
            mEmailView.setError("Email is required");
            mEmailView.requestFocus();
            return;
        }
        if (!email.contains("@")) {
            mEmailView.setError("This is not valid email");
            mEmailView.requestFocus();
            return;
        }


        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity_ForgotPassword.this, getString(R.string.thanhconghaykiemtraemailcuaban), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity_ForgotPassword.this, LoginActivity.class));
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(LoginActivity_ForgotPassword.this, error, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
package com.example.test1706;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword_Activity extends AppCompatActivity {

    TextView tvemail;
    Button btn_forgotpassword;
    FirebaseAuth firebaseAuth;
    private View relativelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        tvemail = (TextView) findViewById(R.id.email_forgotpassword);
        btn_forgotpassword = (Button) findViewById(R.id.accept_forgot_password);
        firebaseAuth = FirebaseAuth.getInstance();


        btn_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                String email = tvemail.getText().toString().trim();
                if (email.equals("")) {
                    Snackbar snackbar = Snackbar
                            .make(relativelayout, "Email are required", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassword_Activity.this, "Success ! Please Check your email", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ForgotPassword_Activity.this,LoginActivity.class));
                            }
                            else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ForgotPassword_Activity.this, error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
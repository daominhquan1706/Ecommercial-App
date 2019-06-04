package com.example.test1706;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test1706.model.AccountUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.support.constraint.Constraints.TAG;

public class Login_RegisterActivity extends AppCompatActivity {
    private FirebaseUser mAuthTask = null;

    // UI references.
    private EditText mPasswordView, mEmailView, mRetypePassword;
    private View mProgressView;
    private FirebaseAuth mAuth;
    private ImageView mImage;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Slidr.attach(this);
        // Set up the login form.
        mProgressView = findViewById(R.id.login_progress);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRetypePassword = (EditText) findViewById(R.id.retype_password);

        mEmailView = (EditText) findViewById(R.id.email);
        mImage = (ImageView) findViewById(R.id.img_account);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        findViewById(R.id.relativeLayout).setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return true;
        });


        Button mEmailSignUpButton = (Button) findViewById(R.id.btn_register);
        mEmailSignUpButton.setOnClickListener(v -> DangKy());
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    public void DangKy() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        String retypepassword = mRetypePassword.getText().toString().trim();
        if (email.isEmpty()) {
            mEmailView.setError("Email is required");
            mEmailView.requestFocus();
            return;
        } else if (!email.contains("@") || !email.split("@")[1].contains(".") || !(email.split("@")[1].length() > 3)) {
            mEmailView.setError("This is not valid email    ");
            mEmailView.requestFocus();
            return;
        } else if (password.isEmpty()) {
            mPasswordView.setError("Password is required");
            mPasswordView.requestFocus();
            return;
        } else if (password.length() < 5) {
            mPasswordView.setError("Password more than 6 characters");
            mPasswordView.requestFocus();
            return;
        } else if (!password.equals(retypepassword)) {
            mPasswordView.setError("please check password again");
            mRetypePassword.setError("please check password again");
            mPasswordView.requestFocus();
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        AccountUser accountUser;
                        accountUser = new AccountUser();
                        accountUser.update_firebaseAccount();

                        Intent i = new Intent(Login_RegisterActivity.this, User_Profile_Account_Activity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(Login_RegisterActivity.this, getString(R.string.chungthucthatbaihaythulai),
                                Toast.LENGTH_SHORT).show();
                    }

                    // ...
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

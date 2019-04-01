package com.example.test1706;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.support.constraint.Constraints.TAG;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int MENU_ITEM_ITEM1 = 1;
    private FirebaseUser mAuthTask = null;

    // UI references.
    private EditText mPasswordView, mEmailView;
    private View mProgressView;
    private ImageView mImage;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialogdialog;
    private View relativelayout;
    private TextInputLayout minputLayout_email, minputLayout_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialogdialog = ProgressDialog.show(LoginActivity.this, "", "Loading. Please wait...", true);
        // Set up the login form.
        mProgressView = findViewById(R.id.login_progress);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView = (EditText) findViewById(R.id.email);
        mImage = (ImageView) findViewById(R.id.img_account);

        minputLayout_email = (TextInputLayout) findViewById(R.id.inputlayout_email);
        minputLayout_password = (TextInputLayout) findViewById(R.id.inputlayout_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        relativelayout = (View) findViewById(R.id.relativeLayout);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                DangNhap();

            }
        });
        TextView btn_forgotpassword = (TextView) findViewById(R.id.btn_open_forgotpassword);
        btn_forgotpassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openforgotpassword = new Intent(LoginActivity.this, ForgotPassword_Activity.class);
                startActivity(openforgotpassword);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return true;
            }
        });


        Button mEmailSignUpButton = (Button) findViewById(R.id.btn_register);
        mEmailSignUpButton.setVisibility(View.GONE);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityAnimation();
            }
        });
        progressDialogdialog.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem create_new_account = menu.add(0, MENU_ITEM_ITEM1, 0, "Create New Account");
        create_new_account.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        create_new_account.setTitle(Html.fromHtml("<font color='#ff3824'>New Account</font>"));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case MENU_ITEM_ITEM1:
                openActivityAnimation();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void DangNhap() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
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

        if (password.isEmpty()) {
            mPasswordView.setError("Password is required");
            mPasswordView.requestFocus();
            return;
        }
        if (password.length() <= 6) {
            mPasswordView.setError("Password more than 5 characters");
            mPasswordView.requestFocus();
            return;
        }


        if (email.equals("admin") && password.equals("admin")) {
            Intent intent = new Intent(LoginActivity.this, Admin.class);
            startActivity(intent);
            return;
        } else if (email.length() > 6 && password.length() > 6) {
            Snackbar snackbar = Snackbar
                    .make(relativelayout, "Chechking information", Snackbar.LENGTH_LONG);
            snackbar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LoginActivity.this, "LOGIN SUCESS", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                mEmailView.setError("Email or Password is incorrect");
                                mPasswordView.setError("Email or Password is incorrect");
                                mPasswordView.requestFocus();
                            }

                            // ...
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "invalid format email or password.",
                    Toast.LENGTH_LONG).show();
        }
        if (email.length() < 6) {

            minputLayout_email.setError("email must include @ character");
        } else {
            minputLayout_email.setErrorEnabled(false);
        }

        if (password.length() < 6) {
            minputLayout_password.setError("password need to have at least 6 character");
        } else {
            minputLayout_password.setErrorEnabled(false);
        }
    }


    public void openActivityAnimation() {
        Intent intent = new Intent(this, Register_Menu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}

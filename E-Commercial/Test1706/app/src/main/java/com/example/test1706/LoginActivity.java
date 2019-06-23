package com.example.test1706;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import com.bumptech.glide.Glide;
import com.example.test1706.Config.Session;
import com.example.test1706.model.AccountUser;
import com.firebase.ui.auth.AuthUI;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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
    Button btn_normal, btn_facebook, btn_phonenumber, btn_normal_account, btn_google_plus;
    private static int SIGN_IN_REQUEST_CODE_GOOGLE = 1;
    FirebaseDatabase db;
    DatabaseReference myref;
    List<String> listaccount;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Slidr.attach(this);
        ImageView image_ba = (ImageView) findViewById(R.id.image_ba_cai_dong_ho);
        Glide
                .with(this)
                .load(getResources().getDrawable(R.drawable.ba_cai_dong_ho))
                .into(image_ba);
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
                Intent openforgotpassword = new Intent(LoginActivity.this, LoginActivity_ForgotPassword.class);
                startActivity(openforgotpassword);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }


        findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return true;
            }
        });


        progressDialogdialog.dismiss();
        session = new Session(getApplicationContext());
        if (session.getSwitchHuongDan()) {
            Huongdan();
        }
        setupButtonResgister();

        getAlluser();
    }

    private void getAlluser() {
        listaccount = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        myref = db.getReference("Account");
        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listaccount.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupButtonResgister() {
        btn_google_plus = (Button) findViewById(R.id.btn_google_plus);
        btn_facebook = (Button) findViewById(R.id.btn_facebook);
        btn_normal = (Button) findViewById(R.id.btn_normal_account);
        btn_phonenumber = (Button) findViewById(R.id.btn_phone_number);

        btn_phonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, Login_Register_Menu_PhoneNumber.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        btn_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, Login_RegisterActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        btn_google_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE_GOOGLE);
                } else {
                    finish();
                }
            }
        });
    }

    private Session session;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE_GOOGLE) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = mAuth.getCurrentUser();
                AccountUser accountUser;
                accountUser = new AccountUser();
                if (mAuth.getCurrentUser() != null) {
                    if (!listaccount.contains(user.getUid())) {
                        accountUser.update_firebaseAccount();
                    }
                }
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thất bại vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Huongdan() {
        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.email_sign_in_button), "Hướng dẫn sử dụng", "Đăng nhập vào phần mềm để có trải nghiệm tốt hơn")
                                .tintTarget(false)

                                .id(1),
                        TapTarget.forView(findViewById(R.id.btn_open_forgotpassword), "Hướng dẫn sử dụng", "Lấy lại mật khẩu đã quên ,bạn phãi cung cấp email, chúng tôi sẽ giúp bạn lấy lại mật khẩu đã quên")
                                .tintTarget(false)

                                .id(2))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Executes when sequence of instruction get completes.
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        final AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Uh oh")
                                .setMessage("You canceled the sequence")
                                .setPositiveButton("OK", null).show();
                        TapTargetView.showFor(dialog,
                                TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())
                                        .cancelable(false)
                                        .tintTarget(false), new TapTargetView.Listener() {
                                    @Override
                                    public void onTargetClick(TapTargetView view) {
                                        super.onTargetClick(view);
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
        sequence.start();
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

    ProgressDialog pd;

    public void DangNhap() {
        pd = new ProgressDialog(this);
        pd.setMessage("Đang kiểm tra...");
        pd.show();
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (email.equals("admin") && password.equals("admin")) {
            Intent intent = new Intent(LoginActivity.this, Admin.class);
            startActivity(intent);
            return;
        }


        if (email.isEmpty()) {
            mEmailView.setError("Email phãi được điền đầy đủ");
            mEmailView.requestFocus();
            pd.cancel();
        } else if (!email.contains("@")) {
            mEmailView.setError("định dạng email sai");
            mEmailView.requestFocus();
            pd.cancel();
        } else if (password.isEmpty()) {
            mPasswordView.setError("Mật khẩu phãi được điền đầy đủ");
            mPasswordView.requestFocus();
            pd.cancel();
        } else if (password.length() < 5) {
            mPasswordView.setError("Mật Khẩu nhiều hơn 5 ký tự");
            mPasswordView.requestFocus();
            pd.cancel();
        } else if (email.length() > 6 && password.length() > 6) {
            Snackbar snackbar = Snackbar
                    .make(relativelayout, "Chechking information", Snackbar.LENGTH_LONG);
            snackbar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.cancel();
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                AccountUser accountUser;
                                accountUser = new AccountUser();
                                if (mAuth.getCurrentUser() != null) {
                                    if (!listaccount.contains(user.getUid())) {
                                        accountUser.update_firebaseAccount();
                                    }
                                }

                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LoginActivity.this, getString(R.string.dangnhapthanhcong), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                mEmailView.setError(getString(R.string.khonghople));
                                mPasswordView.setText("");
                                mPasswordView.requestFocus();
                            }

                            // ...
                        }

                    });
        }
    }


    public void openActivityAnimation() {
        Intent intent = new Intent(this, Login_Register_Menu.class);
        startActivity(intent);
        finish();
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

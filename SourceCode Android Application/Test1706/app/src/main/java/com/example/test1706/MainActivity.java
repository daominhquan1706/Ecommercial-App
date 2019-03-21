package com.example.test1706;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1706.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentUser;
    private NavigationView navigationView;
    private TextView tv_email_nav_header;
    Button btn_login, btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_mainpage);
        }


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_email_nav_header = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_username_nav_header);
        btn_login = (Button) navigationView.getHeaderView(0).findViewById(R.id.btn_login);
        btn_logout = (Button) navigationView.getHeaderView(0).findViewById(R.id.btn_logout);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() == null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MainFragment()).commit();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Sign out successfully", Toast.LENGTH_SHORT).show();
                tv_email_nav_header.setText("Unknow_Account");
                btn_logout.setVisibility(View.INVISIBLE);
            }
        });
        updateUI();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_mainpage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                break;
            case R.id.nav_NiteWatch:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NiteWatchFragment()).commit();
                break;

            case R.id.nav_register:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                Intent intent = new Intent(getApplicationContext(), Launcher3DViewActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_setting:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_buoi1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Buoi1Fragment()).commit();
                break;

            case R.id.nav_shop:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show();
                break;

        }
        navigationView.setCheckedItem(menuItem.getItemId());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateUI();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_mainpage);
    }

    public void updateUI() {

        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("UPDATE UI ACCOUNT", "UpdateUI:  " + currentUser.getEmail());
            tv_email_nav_header.setText(currentUser.getEmail());
            Toast.makeText(this, "chào mừng user" + currentUser.getEmail(), Toast.LENGTH_SHORT);
            btn_logout.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
        } else {
            tv_email_nav_header.setText("Unknow_Account");
            btn_logout.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
        }

    }

}

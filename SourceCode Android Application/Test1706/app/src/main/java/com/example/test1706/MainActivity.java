package com.example.test1706;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
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
    private ViewPager v_pager;
    private TextView tv_email_nav_header;
    private TextView tvt_nav_acoount_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v_pager = (ViewPager) findViewById(R.id.v_pager);
        ImageAdapter adapter = new ImageAdapter(this);
        v_pager.setAdapter(adapter);
        tv_email_nav_header = (TextView) findViewById(R.id.txt_email_nav_header);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView .setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_mainpage);
        }


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_mainpage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                break;
            case R.id.nav_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).commit();*/
                break;
            case R.id.nav_register:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                Intent intent = new Intent(getApplicationContext(),Launcher3DViewActivity.class);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            tv_email_nav_header = (TextView) findViewById(R.id.txt_username_nav_header);
            tv_email_nav_header.setText(user.getEmail());
            Toast.makeText(this,"chào mừng user",Toast.LENGTH_SHORT);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_mainpage);
        }
    }
}

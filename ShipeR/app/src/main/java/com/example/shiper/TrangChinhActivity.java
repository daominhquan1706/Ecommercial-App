package com.example.shiper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class TrangChinhActivity extends AppCompatActivity {

    Fragment fragment_hoadon;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chinh);
        fragment_hoadon= new HoaDonFragment();
        if (savedInstanceState == null) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container,
                    fragment_hoadon).commit();
        }



        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.fragment_container,fragment_hoadon);
                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.fragment_container,fragment_hoadon);
                    return true;
                case R.id.navigation_notifications:
                    transaction.replace(R.id.fragment_container,fragment_hoadon);
                    return true;
            }
            return false;
        }
    };



}

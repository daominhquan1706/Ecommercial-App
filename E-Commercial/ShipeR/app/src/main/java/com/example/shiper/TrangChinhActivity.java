package com.example.shiper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class TrangChinhActivity extends AppCompatActivity {

    Fragment fragment_hoadon,fragment_nhandon;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chinh);
        fragment_hoadon = new HoaDonFragment();
        fragment_nhandon = new NhanDonFragment();
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
                case R.id.nav_nhandon:
                    transaction.replace(R.id.fragment_container, fragment_nhandon);
                    return true;
                case R.id.nav_danglam:
                    transaction.replace(R.id.fragment_container, fragment_hoadon);
                    return true;
                case R.id.nav_dalam:
                    transaction.replace(R.id.fragment_container, fragment_hoadon);
                    return true;
                case R.id.nav_account:
                    transaction.replace(R.id.fragment_container, fragment_hoadon);
                    return true;

            }
            return false;
        }
    };


}

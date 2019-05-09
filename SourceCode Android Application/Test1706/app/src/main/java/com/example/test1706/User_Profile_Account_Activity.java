package com.example.test1706;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.test1706.UserModel.AccountUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Profile_Account_Activity extends AppCompatActivity {
    CircleImageView img_user_avatar;
    Toolbar toolbar_profile;
    AccountUser accountUser;
    EditText tv_name_profile, tv_phonenumber_profile, tv_address_profile;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    boolean isEditing;
    Button btn_save_profile;
    FirebaseUser firebaseUser;
    String userUID;
    LatLng location;
    Bundle savedInstance_bundle;
    TextView tv_lng_location, tv_lat_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance_bundle = savedInstanceState;
        setContentView(R.layout.activity_user_profile__account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //loading


        isEditing = false;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditing) {
                    Snackbar.make(view, "đã mở chế độ sửa thông tin", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    btn_save_profile.setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(view, "đã tắt chế độ sửa thông tin", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getCurrentUser();
                    btn_save_profile.setVisibility(View.GONE);
                }
                isEditing = !isEditing;
                tv_name_profile.setEnabled(isEditing);
                tv_phonenumber_profile.setEnabled(isEditing);
                btn_save_profile.setClickable(isEditing);

            }
        });
        init();
        getCurrentUser();
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Glide.with(this).load(R.drawable.ba_cai_dong_ho).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    collapsingToolbarLayout.setBackground(resource);
                }
            }
        });

        btn_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountUser1 != null) {
                    databaseReference.child("Account").child(userUID).child("email").setValue(accountUser1.getEmail());
                    databaseReference.child("Account").child(userUID).child("name").setValue(tv_name_profile.getText().toString());
                    databaseReference.child("Account").child(userUID).child("sdt").setValue(tv_phonenumber_profile.getText().toString());
                    databaseReference.child("Account").child(userUID).child("diachi").setValue(tv_address_profile.getText().toString());
                    databaseReference.child("Account").child(userUID).child("uid").setValue(userUID);
                    Snackbar.make(v, "đã lưu thông tin thành công", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getCurrentUser();
                } else {
                    Snackbar.make(v, "thất bại", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getCurrentUser();
                }
                isEditing = !isEditing;
                tv_name_profile.setEnabled(isEditing);
                tv_phonenumber_profile.setEnabled(isEditing);
                btn_save_profile.setClickable(isEditing);
                btn_save_profile.setVisibility(View.GONE);
            }
        });
        huflit = new LatLng(10.776663, 106.667445);
        //loadMap(huflit);
    }

    LatLng huflit;

    private void changeLocation(LatLng latLng) {
        // Move map camera to the selected location
        map.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(14)
                        .build()), 2000);
    }


    boolean isLoadMap_Success;

    private void loadMap(LatLng latLng) {
        Mapbox.getInstance(this, getString(R.string.access_token));
        SupportMapFragment mapFragment;
        if (savedInstance_bundle == null) {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MapboxMapOptions options = new MapboxMapOptions();
            options.camera(new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15)
                    .build());
            mapFragment = SupportMapFragment.newInstance(options);
            transaction.add(R.id.mapbox_cardview, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }

        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                map = mapboxMap;

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Add the marker image to map
                        style.addImage("marker-icon-id",
                                BitmapFactory.decodeResource(
                                        User_Profile_Account_Activity.this.getResources(), R.drawable.mapbox_marker_icon_default));

                        GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                                Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
                        style.addSource(geoJsonSource);

                        SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
                        symbolLayer.withProperties(
                                PropertyFactory.iconImage("marker-icon-id")
                        );
                        style.addLayer(symbolLayer);
                    }
                });


            }
        });

    }

    MapboxMap map;
    AccountUser accountUser1;

    public void getCurrentUser() {
        isLoadMap_Success = false;
        toolbar_profile.setTitle(firebaseUser.getEmail());
        if (firebaseUser != null) {
            databaseReference.child("Account").child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    accountUser1 = dataSnapshot.getValue(AccountUser.class);
                    if (accountUser != null) {
                        tv_name_profile.setText(accountUser1.getName());
                        tv_phonenumber_profile.setText(accountUser1.getSDT());
                        tv_address_profile.setText(accountUser1.getDiachi());
                        Glide.with(User_Profile_Account_Activity.this)
                                .load("https://api.adorable.io/avatars/" + accountUser1.getUID() + "@adorable.png")
                                .apply(new RequestOptions().centerCrop())
                                .into(img_user_avatar);
                        if (accountUser1.getLat_Location() != null && accountUser1.getLong_Location() != null) {
                            tv_lng_location.setText(String.valueOf(accountUser1.getLong_Location()));
                            tv_lat_location.setText(String.valueOf(accountUser1.getLat_Location()));


                            location = new LatLng(accountUser1.getLat_Location(), accountUser1.getLong_Location());
                            // loadMap(location);
                            if (!isLoadMap_Success) {
                                loadMap(location);
                                isLoadMap_Success = true;
                            }

                        }
                    } else {
                        update_firebaseAccount();
                        getCurrentUser();
                    }
                    findViewById(R.id.loadingscreen).setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void update_firebaseAccount() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            AccountUser accountUser = new AccountUser(currentUser.getUid(), currentUser.getEmail());
            databaseReference.child("Account").child(currentUser.getUid()).setValue(accountUser);
        }
    }

    Button pickNewLocation;

    public void init() {
        pickNewLocation = (Button) findViewById(R.id.pickNewLocation);
        pickNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(User_Profile_Account_Activity.this, MapBox_Picker.class);
                startActivity(i);
                finish();
            }
        });
        toolbar_profile = (Toolbar) findViewById(R.id.toolbar_profile);
        tv_name_profile = (EditText) findViewById(R.id.tv_name_profile);
        tv_phonenumber_profile = (EditText) findViewById(R.id.tv_phonenumber_profile);
        tv_address_profile = (EditText) findViewById(R.id.tv_address_profile);
        img_user_avatar = (CircleImageView) findViewById(R.id.img_user_avatar);
        btn_save_profile = (Button) findViewById(R.id.btn_save_profile);
        accountUser = new AccountUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(accountUser.getEmail());
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userUID = firebaseUser.getUid();


        tv_lng_location = (TextView) findViewById(R.id.tv_lng_location);
        tv_lat_location = (TextView) findViewById(R.id.tv_lat_location);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUser();
    }
}

package com.example.test1706;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.test1706.Adapter.Cart_Recycle_Adapter_NiteWatch;
import com.example.test1706.Config.Config;
import com.example.test1706.Config.Session;
import com.example.test1706.model.AccountUser;
import com.example.test1706.model.Cart;
import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.Orders;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class Checkout_activity extends AppCompatActivity {
    public static final int PAYPAL_REQUEST_CODE = 7171;
    TextView tv_total_price;
    CartSqliteHelper cartSqliteHelper;
    EditText name_order, address_order, sdt_order;
    ImageView btnPaynow;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // su dung sandbox de test
            .clientId(Config.PAYPAL_CLIENT_ID);
    String amount = "";
    Cart_Recycle_Adapter_NiteWatch cart_recycle_adapter_niteWatch;
    RecyclerView lv_checkout;
    CardView mapbox_cardview;

    FirebaseDatabase db;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Bundle savedInstance_bundle;
    FloatingActionButton search_location_button_mapbox;
    ProgressDialog pd;
    TextView tv_tienship, tv_tienhang, tv_khoangcach;
    Point huflit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance_bundle = savedInstanceState;
        setContentView(R.layout.activity_checkout_activity);
        //Slidr.attach(this);
        init();
        layDataUser();

        tv_tienhang.setText(String.valueOf(cartSqliteHelper.getCartPriceCount() + " USD"));

        // bat dau severvice paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Glide.with(this).load(R.drawable.ba_cai_dong_ho).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    collapsingToolbarLayout.setBackground(resource);
                }
            }
        });
        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_order.getText().toString().equals("")) {
                    name_order.setError(getString(R.string.thieuten));
                    name_order.requestFocus();
                } else if (sdt_order.getText().toString().equals("")) {
                    sdt_order.setError(getString(R.string.thieusodienthoai));
                    sdt_order.requestFocus();
                } else if (address_order.getText().toString().equals("")) {
                    address_order.setError(getString(R.string.thieudiachi));
                    address_order.requestFocus();
                } else if (tv_total_price.getText().toString().equals("")) {
                    Toast.makeText(Checkout_activity.this, getString(R.string.chuathethanhtoanhaythulai), Toast.LENGTH_SHORT).show();
                } else {
                    ProcessPayment();
                }
            }
        });


        cart_recycle_adapter_niteWatch = new Cart_Recycle_Adapter_NiteWatch(this, cartSqliteHelper.getAllCarts(), R.layout.item_checkout_item);
        lv_checkout.setAdapter(cart_recycle_adapter_niteWatch);


        if (session.getSwitchHuongDan()) {
            HuongDan();
        }
        mapbox_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Checkout_activity.this, MapBox_Picker.class);
                startActivity(i);
            }
        });
        search_location_button_mapbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Checkout_activity.this, MapBox_Picker.class);
                startActivity(i);
            }
        });
    }

    AccountUser userAccount;

    private void layDataUser() {
        isLoadMap_Success = false;
        if (currentUser != null) {
            myRef.child("Account").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userAccount = dataSnapshot.getValue(AccountUser.class);
                    if (userAccount != null) {
                        name_order.setText(userAccount.getName());
                        sdt_order.setText(userAccount.getSDT());
                        address_order.setText(userAccount.getDiachi());
                        if (userAccount.getLat_Location() != null && userAccount.getLong_Location() != null) {
                            LatLng location = new LatLng(userAccount.getLat_Location(), userAccount.getLong_Location());
                            if (!isLoadMap_Success) {
                                loadMap(location);
                                isLoadMap_Success = true;
                            } else if (map != null) {
                                changeLocation(location);
                            }
                        }
                    }
                    pd.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            if (!session.getLocation_Lat().equals("0") && !session.getLocation_Lng().equals("0")) {
                loadMap(new LatLng(Double.parseDouble(session.getLocation_Lat()), Double.parseDouble(session.getLocation_Lng())));
                address_order.setText(session.getLocation_Name());
                pd.dismiss();
            }
        }

    }

    private Session session;

    private void changeLocation(LatLng latLng) {
        // Move map camera to the selected location
        map.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(14)
                        .build()), 2000);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("địa chỉ của bạn");
        markerOptions.position(latLng);
        map.clear();
        map.addMarker(markerOptions);
        Point userAddress = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
        getRoute(style_mapbox, huflit, userAddress);
    }

    private void HuongDan() {
        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.btnPaynow), getString(R.string.paypalthanhtoan), getString(R.string.motahuongdanpaypal))
                        .tintTarget(false));
    }

    public void init() {
        tv_khoangcach = (TextView) findViewById(R.id.tv_khoangcach);
        tv_tienhang = (TextView) findViewById(R.id.tv_tienhang);
        huflit = Point.fromLngLat(106.667445, 10.776663);
        tv_tienship = (TextView) findViewById(R.id.tv_tienship);
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.danglaydulieu));
        pd.show();
        search_location_button_mapbox = (FloatingActionButton) findViewById(R.id.search_location_button_mapbox);
        session = new Session(getApplicationContext());
        mapbox_cardview = (CardView) findViewById(R.id.mapbox_cardview);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        lv_checkout = (RecyclerView) findViewById(R.id.lv_checkout);
        cartSqliteHelper = new CartSqliteHelper(this);
        btnPaynow = (ImageView) findViewById(R.id.btnPaynow);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        name_order = (EditText) findViewById(R.id.name_order);
        address_order = (EditText) findViewById(R.id.address_order);
        sdt_order = (EditText) findViewById(R.id.sdt_order);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(Checkout_activity.this, PayPalService.class));
        super.onDestroy();
    }

    private void ProcessPayment() {
        amount = tv_total_price.getText().toString().split(" ")[0];
        PayPalPayment PaypalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), getString(R.string.menhgiatien), getString(R.string.titlepaypal), PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(Checkout_activity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, PaypalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        AddCart_to_Order(paymentDetails);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, getString(R.string.dahuy), Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, R.string.khonghople, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("HardwareIds")
    public void AddCart_to_Order(String paymentDetails) {
        try {
            JSONObject jsonObject = new JSONObject(paymentDetails);
            JSONObject response = jsonObject.getJSONObject("response");
            String id = response.getString("id");
            Orders orders = new Orders();
            orders.setCreationTime(System.currentTimeMillis());
            orders.setCustomerAddress(address_order.getText().toString());
            orders.setCustomerName(name_order.getText().toString());
            orders.setCustomerPhoneNumber(sdt_order.getText().toString());
            orders.setStatus("Chờ xác nhận");
            orders.setTotal(cartSqliteHelper.getCartPriceCount());
            orders.setPaymentid(id);


            String tinhtrang = System.currentTimeMillis() + "_" + "Đang chờ xác nhận đơn hàng";
            List<String> newTimeline = orders.getTimeline();
            if (newTimeline == null) {
                newTimeline = new ArrayList<>();
            }
            newTimeline.add(tinhtrang);
            orders.setTimeline(newTimeline);


            if (currentUser != null) {
                orders.setUserID(currentUser.getUid());
                orders.setAddress_Lat(userAccount.getLat_Location());
                orders.setAddress_Lng(userAccount.getLong_Location());
            } else {
                orders.setUserID(Secure.getString(this.getContentResolver(),
                        Secure.ANDROID_ID));

                orders.setAddress_Lat(Double.parseDouble(session.getLocation_Lat()));
                orders.setAddress_Lng(Double.parseDouble(session.getLocation_Lng()));
            }
            List<Cart> orderDetails = new ArrayList<Cart>();
            for (Cart item : cartSqliteHelper.getAllCarts()) {
                orderDetails.add(item);
                cartSqliteHelper.deleteCart(item);
            }
            orders.setOrderDetails(orderDetails);

            myRef.child("Orders").child(id).setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(Checkout_activity.this, Checkout_PaymentDetails_activity.class)
                            .putExtra("Checkout_PaymentDetails_activity", paymentDetails)
                            .putExtra("PaymentAmount", amount)
                            .putExtra("tennguoinhan", name_order.getText().toString())
                            .putExtra("sodienthoai", sdt_order.getText().toString())
                            .putExtra("diachi", address_order.getText().toString())

                    );
                    finish();


                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    boolean isLoadMap_Success;
    MapboxMap map;
    Style style_mapbox;

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
            try {
                transaction.commit();
            } catch (IllegalStateException e) {
                Toast.makeText(this, "vấn đề :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                recreate();
            }
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
                        style_mapbox = style;
                        // Add the marker image to map
                        style.addImage("marker-icon-id",
                                BitmapFactory.decodeResource(
                                        Checkout_activity.this.getResources(), R.drawable.mapbox_marker_icon_default));

                        GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                                Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
                        style.addSource(geoJsonSource);

                        SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
                        symbolLayer.withProperties(
                                PropertyFactory.iconImage("marker-icon-id")
                        );
                        style.addLayer(symbolLayer);


                        Point userAddress = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
                        getRoute(style, huflit, userAddress);
                    }
                });


            }
        });

    }

    private DirectionsRoute currentRoute;

    private void getRoute(@NonNull final Style style, Point origin, Point destination) {


        MapboxDirections client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.access_token))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<DirectionsResponse> call, @NotNull Response<DirectionsResponse> response) {

                if (response.body() == null) {
                    Timber.e("No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.e("No routes found");
                    return;
                }

                // Retrieve the directions route from the API response
                currentRoute = response.body().routes().get(0);
                tv_khoangcach.setText((currentRoute.distance().intValue() / 1000) + " km");
                double tienship = TinhTienShip(Objects.requireNonNull(currentRoute.distance()), address_order.getText().toString());
                tv_tienship.setText(tienship + " USD");
                tv_total_price.setText((cartSqliteHelper.getCartPriceCount() + tienship) + " USD");


            }

            @Override
            public void onFailure(@NotNull Call<DirectionsResponse> call, @NotNull Throwable throwable) {
                Timber.e("Error: %s", throwable.getMessage());

            }
        });
    }

    public static double TinhTienShip(Double km, String diaChi) {
        int distance = km.intValue() / 1000;

        if (diaChi.contains("Vietnam")) {
            if (diaChi.contains("Hồ Chí Minh")) {
                if (0 <= distance && distance <= 5) {
                    return 1.99;
                } else if (5 <= distance && distance <= 10) {
                    return 2.99;
                } else if (10 <= distance && distance <= 20) {
                    return 3.99;
                } else if (20 <= distance) {
                    return 5.99;
                }
            } else if (diaChi.contains("Hanoi")) {
                return 20.99;
            } else {
                return 25.99;
            }
        } else {
            return 50.99;
        }


        return 0.99;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (currentUser == null) {
            address_order.setText(session.getLocation_Name());
            changeLocation(new LatLng(Double.parseDouble(session.getLocation_Lat()), Double.parseDouble(session.getLocation_Lng())));
        }

    }

}

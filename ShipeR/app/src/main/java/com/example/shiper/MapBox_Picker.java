package com.example.shiper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiper.Adapter.Cart_Recycle_Adapter_NiteWatch;
import com.example.shiper.Adapter.TimelinesAdapter;
import com.example.shiper.model.CartSqliteHelper;
import com.example.shiper.model.Orders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.ramotion.foldingcell.FoldingCell;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

/**
 * Drop a marker at a specific location and then perform
 * reverse geocoding to retrieve and display the location's address
 */
public class MapBox_Picker extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MapBox_Picker";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    String geojsonSourceLayerId = "geojsonSourceLayerId";
    MapView mapView;
    MapboxMap mapboxMap;
    PermissionsManager permissionsManager;
    ImageView hoveringMarker;
    CarmenFeature home;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    TextView tv_place_name, tv_lat_location, tv_lng_location;
    FloatingActionButton btn_search_picker;
    public Point huflitLocation;
    Style mapStyle;
    FirebaseUser user;
    NavigationView navigationView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map_box__picker);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        init();


        btn_search_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchActivity();
            }
        });

    }

    private void init() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Initialize the mapboxMap view

        mapView.getMapAsync(this);
        huflitLocation = Point.fromLngLat(106.667445, 10.776663);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        tv_place_name = (TextView) findViewById(R.id.tv_place_name);
        tv_lat_location = (TextView) findViewById(R.id.tv_lat_location);
        tv_lng_location = (TextView) findViewById(R.id.tv_lng_location);
        btn_search_picker = (FloatingActionButton) findViewById(R.id.search_location_button_mapbox);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //set up Navigation bar (side bar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MapBox_Picker.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull final Style style) {
                mapStyle = style;
                enableLocationPlugin(style);
                addUserLocations();
                // Toast instructing user to tap on the mapboxMap
                Toast.makeText(
                        MapBox_Picker.this,
                        getString(R.string.move_map_instruction), Toast.LENGTH_SHORT).show();

                // When user is still picking a location, we hover a marker above the mapboxMap in the center.
                // This is done by using an image view with the default marker found in the SDK. You can
                // swap out for your own marker image, just make sure it matches up with the dropped marker.
                hoveringMarker = new ImageView(MapBox_Picker.this);
                hoveringMarker.setVisibility(View.INVISIBLE);
                hoveringMarker.setImageResource(R.drawable.mapbox_marker_icon_default);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                hoveringMarker.setLayoutParams(params);
                mapView.addView(hoveringMarker);

                // Initialize, but don't show, a SymbolLayer for the marker icon which will represent a selected location.
                initDroppedMarker(style);

                // Button for user to drop marker or to pick marker back up.

                getAllHoaDon();
            }
        });
    }

    List<String> ordersList;

    private void getAllHoaDon() {
        ordersList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Orders");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Orders orders = dataSnapshot.getValue(Orders.class);
                if (orders.getStatus().equals("Chờ lấy hàng")) {
                    Point thispoint = Point.fromLngLat(orders.getAddress_Lng(), orders.getAddress_Lat());
                    double khoangcach = 1000000;
                    getRoute(mapStyle, thispoint, orders, khoangcach);
                    ordersList.add(orders.getPaymentid());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Orders orders = dataSnapshot.getValue(Orders.class);
                if (!ordersList.contains(orders.getPaymentid()) && orders.getStatus().equals("Chờ lấy hàng")) {
                    Point thispoint = Point.fromLngLat(orders.getAddress_Lng(), orders.getAddress_Lat());
                    double khoangcach = 1000000;
                    getRoute(mapStyle, thispoint, orders, khoangcach);
                    Toast.makeText(MapBox_Picker.this, "vừa thêm hóa đơn tại" + orders.getCustomerAddress(), Toast.LENGTH_SHORT).show();
                }
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

    private void addMarktoMap(double longitude, double latitude, final String ID, final Orders orders) {
        // Add the marker image to map
        mapStyle.addImage("marker-icon-id" + ID,
                BitmapFactory.decodeResource(
                        MapBox_Picker.this.getResources(), R.drawable.markser_hoadon));

        GeoJsonSource geoJsonSource = new GeoJsonSource("source-id" + ID, Feature.fromGeometry(
                Point.fromLngLat(longitude, latitude)));
        mapStyle.addSource(geoJsonSource);

        final SymbolLayer symbolLayer = new SymbolLayer("layer-id" + ID, "source-id" + ID);
        symbolLayer.withProperties(PropertyFactory.iconImage("marker-icon-id" + ID));

        mapStyle.addLayer(symbolLayer.withProperties(iconAllowOverlap(true)));


        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {
                PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
                List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, "layer-id" + ID);
                if (!features.isEmpty()) {
                    Feature selectedFeature = features.get(0);
                    openDialog_HoaDon(orders, symbolLayer);
                    //Toast.makeText(getApplicationContext(), "You selected " + orders.getCustomerName(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void setupMakiLayer(@NonNull Style loadedMapStyle, String MAKI_LAYER_ID, String SOURCE_ID) {
        loadedMapStyle.addLayer(new SymbolLayer(MAKI_LAYER_ID, SOURCE_ID)
                .withProperties(iconAllowOverlap(true)
                ));
    }

    private void openDialog_HoaDon(final Orders orders, final SymbolLayer symbolLayer) {
        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = ((Activity) this).getLayoutInflater();
        View layout = inflater.inflate(R.layout.click_marker_dialog, null);
        dialog.setContentView(layout);
        dialog.setTitle("Hóa Đơn " + orders.getPaymentid());


        TextView
                tv_position,
                tv_customer_sdt,
                tv_total_price_orders,
                tv_creation_time,
                tv_status,
                tv_payid,
                tv_customer_name,
                tv_customer_address;
        RelativeLayout layout_admin_hoadon_item;
        final FoldingCell folding_cell;
        RecyclerView list_prodcut_hoadon_fold, timeline_recycle;
        TextView tv_total_price;
        TextView name_order, address_order, sdt_order, tv_Xac_nhan;
        RecyclerView lv_checkout;
        FirebaseDatabase db;
        final DatabaseReference myRef;
        CartSqliteHelper cartSqliteHelper;
        FirebaseAuth mAuth;
        ActionBar actionBar;
        String paymentId;
        CardView btn_Xac_nhan, btn_TuChoi;
        RelativeLayout admin_details_hoadon_inputlayout_sdt_order, btn_close_fold, rlt_address;
        Button dialog_comment_btn_huy, dialog_comment_btn_danhgia;
        EditText edt_binhluan;
        edt_binhluan = (EditText) dialog.findViewById(R.id.edt_binhluan);

        timeline_recycle = (RecyclerView) dialog.findViewById(R.id.timeline_recycle);
        rlt_address = (RelativeLayout) dialog.findViewById(R.id.rlt_address);
        btn_close_fold = (RelativeLayout) dialog.findViewById(R.id.btn_close_fold);
        tv_position = (TextView) dialog.findViewById(R.id.tv_position);
        tv_customer_sdt = (TextView) dialog.findViewById(R.id.tv_customer_sdt);
        tv_creation_time = (TextView) dialog.findViewById(R.id.tv_creation_time);
        tv_status = (TextView) dialog.findViewById(R.id.tv_status);
        tv_payid = (TextView) dialog.findViewById(R.id.tv_payid);
        tv_customer_name = (TextView) dialog.findViewById(R.id.tv_customer_name);
        tv_customer_address = (TextView) dialog.findViewById(R.id.tv_customer_address);
        tv_total_price_orders = (TextView) dialog.findViewById(R.id.tv_total_price_orders_item);
        layout_admin_hoadon_item = (RelativeLayout) dialog.findViewById(R.id.layout_admin_hoadon_item);
        folding_cell = (FoldingCell) dialog.findViewById(R.id.folding_cell);


        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        btn_TuChoi = (CardView) dialog.findViewById(R.id.btn_dialog_tuchoidonhang);
        admin_details_hoadon_inputlayout_sdt_order = (RelativeLayout) dialog.findViewById(R.id.cv_phone_admin_order);
        lv_checkout = (RecyclerView) dialog.findViewById(R.id.admin_details_hoadon_lv_checkout);
        btn_Xac_nhan = (CardView) dialog.findViewById(R.id.btn_dialog_nhandonhang);
        tv_Xac_nhan = (TextView) dialog.findViewById(R.id.tv_Xac_nhan);
        tv_total_price = (TextView) dialog.findViewById(R.id.admin_details_hoadon_tv_total_price);
        name_order = (TextView) dialog.findViewById(R.id.admin_details_hoadon_name_order);
        address_order = (TextView) dialog.findViewById(R.id.admin_details_hoadon_address_order);
        sdt_order = (TextView) dialog.findViewById(R.id.admin_details_hoadon_sdt_order);

        //tv_position.setText(String.valueOf("#" + (position + 1)));
        tv_total_price_orders.setText(String.valueOf("$" + orders.getTotal()));
        tv_payid.setText(orders.getPaymentid());
        tv_status.setText(orders.getStatus());
        //tv_creation_time.setText(ThoiGian(orders.getCreationTime()));
        tv_customer_name.setText(orders.getCustomerName());
        tv_customer_address.setText(orders.getCustomerAddress());
        tv_customer_sdt.setText(orders.getCustomerPhoneNumber());
        folding_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folding_cell.toggle(true);
            }
        });
        tv_total_price.setText(String.valueOf(orders.getTotal()));
        name_order.setText(orders.getCustomerName());
        address_order.setText(orders.getCustomerAddress());
        sdt_order.setText(orders.getCustomerPhoneNumber());


        Cart_Recycle_Adapter_NiteWatch adapter;
        adapter = new Cart_Recycle_Adapter_NiteWatch(this, orders.getOrderDetails(), R.layout.item_checkout_item_slider_card);
        lv_checkout.setAdapter(adapter);
        TimelinesAdapter timelineAdapter;
        timelineAdapter = new TimelinesAdapter(this, orders.getTimeline());
        timeline_recycle.setAdapter(timelineAdapter);


        btn_close_fold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folding_cell.toggle(true);
            }
        });
        tv_Xac_nhan.setText(this.getString(R.string.tinhtrang_layhang));
        btn_Xac_nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tinhtrang = System.currentTimeMillis() + "_" + "Đơn hàng đã được nhân viên giao hàng [" + user.getEmail() + "] lấy hàng, chuẩn bị giao cho khách hàng.";
                List<String> newTimeline = orders.getTimeline();
                if (newTimeline == null) {
                    newTimeline = new ArrayList<>();
                }
                newTimeline.add(tinhtrang);
                myRef.child("Orders").child(orders.getPaymentid()).child("timeline").setValue(newTimeline);
                myRef.child("Orders").child(orders.getPaymentid()).child("shiper_uid").setValue(user.getUid());
                myRef.child("Orders").child(orders.getPaymentid()).child("shiper_email").setValue(user.getEmail());


                myRef.child("Orders").child(orders.getPaymentid()).child("status").setValue("Đang giao").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mapStyle.removeLayer(symbolLayer);
                        sendNotification(orders.getUserID(), "Đơn hàng đã được nhân viên giao hàng [" + user.getEmail() + "] lấy hàng, chuẩn bị giao cho khách hàng.");
                        dialog.dismiss();
                        Toast.makeText(MapBox_Picker.this, "đã lấy hàng thành công, hãy giao cho khách sớm nhất có thể", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btn_TuChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void sendNotification(final String userUID, final String noiDung) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MWIwNDQxNGQtMzE0Mi00MGY5LThmNzgtYTJhM2RjYTJkODE0");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"054e65ad-ff00-43a1-8615-48de2e56cc4f\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + userUID + "\"}],"

                                + "\"data\": {\"activity\": \"User_HoaDon_Activity\"},"
                                + "\"contents\": {\"en\": \"" + noiDung + "\", \"es\": \"Tình trạng đơn hàng\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }





    private void initDroppedMarker(@NonNull Style loadedMapStyle) {
        // Add the marker image to map
        loadedMapStyle.addImage("dropped-icon-image", BitmapFactory.decodeResource(
                getResources(), R.drawable.mapbox_marker_icon_default));
        loadedMapStyle.addSource(new GeoJsonSource("dropped-marker-source-id"));
        loadedMapStyle.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id").withProperties(
                iconImage("dropped-icon-image"),
                visibility(NONE),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        ));
    }


    private void addUserLocations() {
        home = CarmenFeature.builder().text("Đại Học Ngoại Ngữ Tin Học HUFLIT")
                .geometry(huflitLocation)
                .placeName("155 Sư Vạn Hạnh (nd), Phường 13, Quận 10, TP.HCM")
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();
    }

    private void openSearchActivity() {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken())
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .addInjectedFeature(home)
                        .build(PlaceOptions.MODE_CARDS))
                .build(MapBox_Picker.this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    // Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 1000);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted && mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                enableLocationPlugin(style);
            }
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }



    double currentLat;
    double currentLng;

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationPlugin(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component. Adding in LocationComponentOptions is also an optional
            // parameter
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);
            //get current lat lng
            currentLat = locationComponent.getLastKnownLocation().getLatitude();
            currentLng = locationComponent.getLastKnownLocation().getLongitude();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }


    private DirectionsRoute currentRoute;

    private void getRoute(@NonNull final Style style, Point destination, final Orders orders, final double khoangcach) {
        Point origin = Point.fromLngLat(currentLng, currentLat);

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
                double km = currentRoute.distance() / 1000;
                if (km < khoangcach) {
                    addMarktoMap(orders.getAddress_Lng(), orders.getAddress_Lat(), orders.getPaymentid(), orders);
                }

            }

            @Override
            public void onFailure(@NotNull Call<DirectionsResponse> call, @NotNull Throwable throwable) {
                Timber.e("Error: %s", throwable.getMessage());

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_profile:
                /*Intent intention = new Intent(getApplicationContext(), Admin.class);
                startActivity(intention);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                break;
            case R.id.nav_giaohang:
                Intent intention = new Intent(getApplicationContext(), Admin_HoaDon_Activity.class);
                startActivity(intention);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.dang_xuat);
                builder.setMessage(R.string.question_dang_xuat);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.answer_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton(R.string.answer_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MapBox_Picker.this, getString(R.string.dangxuat_thanh_cong), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MapBox_Picker.this, DangNhapActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
        navigationView.setCheckedItem(menuItem.getItemId());
        return true;
    }
}

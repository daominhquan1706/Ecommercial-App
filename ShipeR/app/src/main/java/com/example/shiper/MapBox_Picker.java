package com.example.shiper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import java.util.List;

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
public class MapBox_Picker extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback {

    private static final String TAG = "MapBox_Picker";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    String geojsonSourceLayerId = "geojsonSourceLayerId";
    MapView mapView;
    MapboxMap mapboxMap;
    Button selectLocationButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map_box__picker);

        // Initialize the mapboxMap view
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        huflitLocation = Point.fromLngLat(106.667445, 10.776663);

        tv_place_name = (TextView) findViewById(R.id.tv_place_name);
        tv_lat_location = (TextView) findViewById(R.id.tv_lat_location);
        tv_lng_location = (TextView) findViewById(R.id.tv_lng_location);
        btn_search_picker = (FloatingActionButton) findViewById(R.id.search_location_button_mapbox);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btn_search_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchActivity();
            }
        });

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
                hoveringMarker.setImageResource(R.drawable.mapbox_marker_icon_default);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                hoveringMarker.setLayoutParams(params);
                mapView.addView(hoveringMarker);

                // Initialize, but don't show, a SymbolLayer for the marker icon which will represent a selected location.
                initDroppedMarker(style);

                // Button for user to drop marker or to pick marker back up.
                selectLocationButton = findViewById(R.id.btn_pick_location);
                selectLocationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hoveringMarker.getVisibility() == View.VISIBLE) {
                            updateUI_dachon(style);
                        } else {
                            updateUI_ChuaChon(style);
                        }
                    }
                });


                getAllHoaDon();
            }
        });
    }

    private void getAllHoaDon() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Orders");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Orders orders = dataSnapshot.getValue(Orders.class);
                Point thispoint = Point.fromLngLat(orders.getAddress_Lng(), orders.getAddress_Lat());
                double khoangcach = 10;
                getRoute(mapStyle, thispoint, orders, khoangcach);
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

    private void addMarktoMap(double longitude, double latitude, final String ID, final Orders orders) {
        // Add the marker image to map
        mapStyle.addImage("marker-icon-id" + ID,
                BitmapFactory.decodeResource(
                        MapBox_Picker.this.getResources(), R.drawable.mapbox_marker_icon_default));

        GeoJsonSource geoJsonSource = new GeoJsonSource("source-id" + ID, Feature.fromGeometry(
                Point.fromLngLat(longitude, latitude)));
        mapStyle.addSource(geoJsonSource);

        SymbolLayer symbolLayer = new SymbolLayer("layer-id" + ID, "source-id" + ID);
        symbolLayer.withProperties(PropertyFactory.iconImage("marker-icon-id" + ID));
        mapStyle.addLayer(symbolLayer);


        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {
                PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
                List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, "layer-id" + ID);
                if (!features.isEmpty()) {
                    Feature selectedFeature = features.get(0);
                    openDialog_HoaDon(orders);
                    Toast.makeText(getApplicationContext(), "You selected " + orders.getCustomerName(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void openDialog_HoaDon(final Orders orders) {
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
                folding_cell.toggle(false);
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
                folding_cell.fold(false);
            }
        });


        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void updateUI_ChuaChon(Style style) {
        // Switch the button appearance back to select a location.
        selectLocationButton.setBackgroundColor(
                ContextCompat.getColor(MapBox_Picker.this, R.color.colorPrimary));
        selectLocationButton.setText(getString(R.string.ch_n_m_t_v_tr));

        // Show the red hovering ImageView marker
        hoveringMarker.setVisibility(View.VISIBLE);

        // Hide the selected location SymbolLayer
        if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
            style.getLayer(DROPPED_MARKER_LAYER_ID).setProperties(visibility(NONE));
        }
    }

    ProgressDialog pd_danglaydulieuvitri;

    private void updateUI_dachon(Style style) {
        pd_danglaydulieuvitri = new ProgressDialog(this);
        pd_danglaydulieuvitri.setMessage("Đang lấy thông tin vị trí này");
        pd_danglaydulieuvitri.show();

        // Use the map target's coordinates to make a reverse geocoding search
        final LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;

        // Hide the hovering red hovering ImageView marker
        hoveringMarker.setVisibility(View.INVISIBLE);

        // Transform the appearance of the button to become the cancel button
        selectLocationButton.setBackgroundColor(
                ContextCompat.getColor(MapBox_Picker.this, R.color.colorAccent));
        selectLocationButton.setText(getString(R.string.location_picker_select_location_button_cancel));

        // Show the SymbolLayer icon to represent the selected map location
        if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
            GeoJsonSource source = style.getSourceAs("dropped-marker-source-id");
            if (source != null) {
                source.setGeoJson(Feature.fromGeometry(Point.fromLngLat(
                        mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude())));
            }
            style.getLayer(DROPPED_MARKER_LAYER_ID).setProperties(visibility(VISIBLE));
        }

        // Use the map camera target's coordinates to make a reverse geocoding search
        reverseGeocode(style, Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));

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

    /**
     * This method is used to reverse geocode where the user has dropped the marker.
     *
     * @param style style
     * @param point The location to use for the search
     */

    private void reverseGeocode(@NonNull final Style style, final Point point) {
        try {
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.access_token))
                    .query(Point.fromLngLat(point.longitude(), point.latitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_LOCALITY)
                    .build();

            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                    List<CarmenFeature> results = response.body().features();
                    if (results.size() > 0) {
                        CarmenFeature feature = results.get(0);

                        // If the geocoder returns a result, we take the first in the list and show a Toast with the place name.
                        if (style.isFullyLoaded() && style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                            Toast.makeText(MapBox_Picker.this,
                                    getString(R.string.location_picker_place_name_result) + ": " +
                                            feature.placeName(), Toast.LENGTH_SHORT).show();
                            tv_place_name.setText(String.valueOf(feature.placeName()));
                            tv_lat_location.setText(String.valueOf(point.latitude()));
                            tv_lng_location.setText(String.valueOf(point.longitude()));

                        }
                    } else {
                        Toast.makeText(MapBox_Picker.this,
                                getString(R.string.location_picker_dropped_marker_snippet_no_results), Toast.LENGTH_SHORT).show();
                    }
                    pd_danglaydulieuvitri.dismiss();
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    Timber.e("Geocoding Failure: %s", throwable.getMessage());
                }
            });
        } catch (ServicesException servicesException) {
            Timber.e("Error geocoding: %s", servicesException.toString());
            servicesException.printStackTrace();
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


}

package com.example.test1706;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.Product;
import com.example.test1706.model.ProductSqliteHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

public class User_Viewed_Product extends AppCompatActivity {
    ProductSqliteHelper productSqliteHelper;
    List<Product> productList;
    RecyclerView recyclerView;
    Product_Recycle_Adapter_NiteWatch product_recycle_adapter_niteWatch;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_viewed_product);
        productSqliteHelper = new ProductSqliteHelper(this);
        productList = new ArrayList<>();
        productList = productSqliteHelper.getAllProducts();

        recyclerView = (RecyclerView) findViewById(R.id.listView_product_nitewatch);
        product_recycle_adapter_niteWatch = new Product_Recycle_Adapter_NiteWatch(
                this,
                productList,
                R.layout.item_layout_watch_nitewatch);
        recyclerView.setAdapter(product_recycle_adapter_niteWatch);
        setUpToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        product_recycle_adapter_niteWatch.notifyDataSetChanged();
    }


    LinearLayout btn_enable_night_view;
    TextView tv_NightView;
    ImageView icon_buttonNightView;
    DatabaseReference myRef;
    TextView textCartItemCount;
    CartSqliteHelper cartSqliteHelper;
    FrameLayout frame_container;
    ListView listView_search;
    AppBarLayout appBarLayout;
    Adapter_Search_Product productadapter;
    FirebaseDatabase database;
    ArrayList<Product> list_data;
    List<String> mkey;
    ScrollView scrollView;
    private static final String TAG = "User_Viewed_Product";

    private void setupBadge(int mCartItemCount) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setUpToolbar() {
        icon_buttonNightView = (ImageView) findViewById(R.id.icon_buttonNightView);
        tv_NightView = (TextView) findViewById(R.id.tv_NightView);
        scrollView = (ScrollView) findViewById(R.id.scrollview_nitewatch);
        btn_enable_night_view = (LinearLayout) findViewById(R.id.btn_enable_night_view);
        btn_enable_night_view.setVisibility(View.VISIBLE);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        listView_search = (ListView) findViewById(R.id.listview_search);
        frame_container = (FrameLayout) findViewById(R.id.fragment_container);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);

        mkey = new ArrayList<String>();
        list_data = new ArrayList<Product>();
        getProductdata();
        listView_search.setDividerHeight(10);
        productadapter = new Adapter_Search_Product(this, list_data);
        listView_search.setAdapter(productadapter);


        final Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        final Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        final Animation slideDown_toolbar = AnimationUtils.loadAnimation(this, R.anim.toolbar_slidedown);
        final Animation slideUp_toolbar = AnimationUtils.loadAnimation(this, R.anim.toolbar_slideup);

        btn_enable_night_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_recycle_adapter_niteWatch.setNight(!product_recycle_adapter_niteWatch.isNight());
                product_recycle_adapter_niteWatch.notifyDataSetChanged();
                boolean isNight = product_recycle_adapter_niteWatch.isNight();
                if (isNight) {
                    tv_NightView.setText(getString(R.string.disable_night_view));
                    icon_buttonNightView.setImageResource(R.drawable.ic_power_settings_new_red_24dp);
                    scrollView.setBackgroundColor(getResources().getColor(R.color.clearblack));

                } else {
                    tv_NightView.setText(getString(R.string.enable_night_view));
                    icon_buttonNightView.setImageResource(R.drawable.ic_power_settings_new_blue_24dp);
                    scrollView.setBackgroundColor(getResources().getColor(R.color.black));

                }

            }
        });


        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 10 && btn_enable_night_view.getVisibility() == View.INVISIBLE) {
                    btn_enable_night_view.setVisibility(View.VISIBLE);
                    btn_enable_night_view.startAnimation(slideUp);

                } else if (scrollY - oldScrollY < -10 && btn_enable_night_view.getVisibility() == View.VISIBLE) {
                    btn_enable_night_view.setVisibility(View.INVISIBLE);
                    btn_enable_night_view.startAnimation(slideDown);
                }

                if (scrollY - oldScrollY > 10 && appBarLayout.getVisibility() == View.VISIBLE) {
                    appBarLayout.setVisibility(View.INVISIBLE);
                    appBarLayout.startAnimation(slideUp_toolbar);


                } else if (scrollY - oldScrollY < -10 && appBarLayout.getVisibility() == View.INVISIBLE) {

                    appBarLayout.setVisibility(View.VISIBLE);
                    appBarLayout.startAnimation(slideDown_toolbar);
                }
            }


        });
    }

    private void getProductdata() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("NiteWatch").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Product itemProduct = item.getValue(Product.class);
                    list_data.add(itemProduct);
                    mkey.add(item.getKey());
                    productadapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    list_data.set(mkey.indexOf(item.getKey()), item.getValue(Product.class));
                    productadapter.notifyDataSetChanged();
                    Log.d("UPDATE dữ liệu ", dataSnapshot.getValue(Product.class).getProduct_Name() + s);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Here");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //thiết lập badge cart count
        cartSqliteHelper = new CartSqliteHelper(this);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge(cartSqliteHelper.getCartQuantityCount());
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        //end-thiết lập badge cart count
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d(TAG, "onMenuItemActionExpand: title" + item.getTitle() + "Icon" + item.getIcon());
                frame_container.setVisibility(View.GONE);
                listView_search.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
                btn_enable_night_view.setVisibility(View.INVISIBLE);
                return true; // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                frame_container.setVisibility(View.VISIBLE);
                listView_search.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.VISIBLE);
                btn_enable_night_view.setVisibility(View.VISIBLE);
                return true; // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: đã gọi được hàm nhập dữ liệu searchview");
                productadapter.getFilter().filter(newText);
                return false;
            }
        });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                frame_container.setVisibility(View.GONE);
                listView_search.setVisibility(View.VISIBLE);
                return true;
            case R.id.action_cart:
                Intent intentCart = new Intent(User_Viewed_Product.this, Cart_Activity.class);
                startActivity(intentCart);
                return true;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return true;
    }
}

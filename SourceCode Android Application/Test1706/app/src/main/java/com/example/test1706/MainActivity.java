package com.example.test1706;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FrameLayout frame_container;


    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentUser;
    private NavigationView navigationView;
    private TextView tv_email_nav_header, textCartItemCount;
    Button btn_login, btn_profile, btn_logout;
    private static final String TAG = "MainActivity";
    Context mContext;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ListView listView_search;
    Search_Adapter productadapter;
    ArrayList<Product> list_data;
    List<String> mkey;
    ActionBarDrawerToggle toggle;
    FloatingActionButton fab;
    Toolbar toolbar;
    Fragment fragmentnitewatch;


    LinearLayout btn_enable_night_view;

    int mStartX, mStartY, mEndX, mEndY;
    AppBarLayout appBarLayout;
    CartSqliteHelper cartSqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //set up Navigation bar (side bar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    fragmentnitewatch).commit();
            navigationView.setCheckedItem(R.id.nav_NiteWatch);
        }

        mkey = new ArrayList<String>();
        list_data = new ArrayList<Product>();
        getProductdata();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        listView_search.setAdapter(productadapter);
        listView_search.setDividerHeight(10);


        productadapter = new Search_Adapter(this, list_data);
        listView_search.setAdapter(productadapter);
        btn_enable_night_view.bringToFront();


    }


    private void init() {
        cartSqliteHelper = new CartSqliteHelper(this);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        fragmentnitewatch = new NiteWatchFragment();
        frame_container = (FrameLayout) findViewById(R.id.fragment_container);
        listView_search = (ListView) findViewById(R.id.listview_search);
        btn_enable_night_view = (LinearLayout) findViewById(R.id.btn_enable_night_view);
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
                Intent intentCart = new Intent(MainActivity.this, Cart_Activity.class);
                startActivity(intentCart);
                return true;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_email_nav_header = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_username_nav_header);
        btn_login = (Button) navigationView.getHeaderView(0).findViewById(R.id.btn_login);
        btn_profile = (Button) navigationView.getHeaderView(0).findViewById(R.id.btn_profile);
        btn_logout = (Button) navigationView.getHeaderView(0).findViewById(R.id.btn_logout);
        setupBadge(cartSqliteHelper.getCartQuantityCount());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() == null) {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Toast.makeText(MainActivity.this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Sign out successfully", Toast.LENGTH_SHORT).show();
                tv_email_nav_header.setText(getString(R.string.unknow_account));
                btn_profile.setVisibility(View.INVISIBLE);
                btn_login.setVisibility(View.VISIBLE);*/

                Intent intent_profile = new Intent(MainActivity.this, Profile_Account_Activity.class);
                startActivity(intent_profile);

            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Sign out successfully", Toast.LENGTH_SHORT).show();
                tv_email_nav_header.setText(getString(R.string.unknow_account));
                btn_profile.setVisibility(View.INVISIBLE);
                btn_logout.setVisibility(View.INVISIBLE);
                btn_login.setVisibility(View.VISIBLE);
            }
        });
        updateUI();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_mainpage:
                Intent testAdmin = new Intent(getApplicationContext(), Test_mongodb.class);
                startActivity(testAdmin);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_NiteWatch:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NiteWatchFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_admin:
                Intent intention = new Intent(getApplicationContext(), Admin.class);
                startActivity(intention);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_user_chat:
                Intent intention_chat = new Intent(getApplicationContext(), ChatBoxMainActivity.class);
                startActivity(intention_chat);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

        }

        navigationView.setCheckedItem(menuItem.getItemId());


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

    private void updateUI() {
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("UPDATE UI ACCOUNT", "UpdateUI:  " + currentUser.getEmail());
            tv_email_nav_header.setText(currentUser.getEmail());
            Toast.makeText(this, "chào mừng user" + currentUser.getEmail(), Toast.LENGTH_SHORT);
            btn_profile.setVisibility(View.VISIBLE);
            btn_logout.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
        } else {
            tv_email_nav_header.setText(getString(R.string.unknow_account));
            btn_profile.setVisibility(View.GONE);
            btn_logout.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


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

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, NotificationService.class));
    }
}

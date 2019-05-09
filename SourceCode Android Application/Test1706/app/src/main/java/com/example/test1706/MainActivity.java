package com.example.test1706;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.Config.Session;
import com.example.test1706.UserModel.AccountUser;
import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.Product;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FrameLayout frame_container;

    private DrawerLayout drawer;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private NavigationView navigationView;
    private TextView tv_email_nav_header, textCartItemCount;
    MenuItem nav_login, nav_profile, nav_logout, nav_advertisement, nav_tutorial, nav_changelang;
    private static final String TAG = "MainActivity";
    Context mContext;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ListView listView_search;
    Adapter_Search_Product productadapter;
    ArrayList<Product> list_data;
    List<String> mkey;
    ActionBarDrawerToggle toggle;
    FloatingActionButton fab;
    Toolbar toolbar;

    Fragment fragmentnitewatch;

    Button btn_close_quangcao;
    LinearLayout rlt_ad;

    LinearLayout btn_enable_night_view;

    int mStartX, mStartY, mEndX, mEndY;
    AppBarLayout appBarLayout;
    CartSqliteHelper cartSqliteHelper;
    CircleImageView img_user_avatar_chat;

    RelativeLayout rlt_image_ad;
    ImageView banner_advertisement_night, banner_advertisement_light, img_tag_sale;

    SwitchCompat switch_quangcao, switch_huongdan;
    Session session;
    MenuItem searchItem;

    Spinner spinner;
    Locale myLocale;
    String currentLanguage = "en", currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        loadLangguage();

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
        }

        mkey = new ArrayList<String>();
        list_data = new ArrayList<Product>();
        getProductdata();

        listView_search.setAdapter(productadapter);
        listView_search.setDividerHeight(10);


        productadapter = new Adapter_Search_Product(this, list_data);
        listView_search.setAdapter(productadapter);
        btn_enable_night_view.bringToFront();


        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_201_listing_front_day.png?alt=media")
                .apply(new RequestOptions().fitCenter())
                .into(banner_advertisement_light);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_201_listing_front_night.png?alt=media")
                .apply(new RequestOptions().fitCenter())
                .into(banner_advertisement_night);
        Glide.with(this)
                .load("http://www.pngpix.com/wp-content/uploads/2016/10/PNGPIX-COM-Circle-Label-Tag-PNG-Transparent-Image-500x484.png")
                .apply(new RequestOptions().fitCenter())
                .into(img_tag_sale);


        long time = 3000;
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(time);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(50);
        fadeOut.setDuration(time);

        banner_advertisement_night.setAnimation(fadeOut);
        banner_advertisement_night.setVisibility(View.GONE);
        banner_advertisement_light.setVisibility(View.VISIBLE);
        banner_advertisement_light.setAnimation(fadeIn);

        rlt_image_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (banner_advertisement_night.getVisibility() == View.VISIBLE) {
                    banner_advertisement_night.setAnimation(fadeOut);
                    banner_advertisement_night.setVisibility(View.GONE);
                    banner_advertisement_light.setVisibility(View.VISIBLE);
                    banner_advertisement_light.setAnimation(fadeIn);

                } else {
                    banner_advertisement_night.setAnimation(fadeIn);
                    banner_advertisement_night.setVisibility(View.VISIBLE);
                    banner_advertisement_light.setVisibility(View.GONE);
                    banner_advertisement_light.setAnimation(fadeOut);

                }
            }
        });

        btn_close_quangcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlt_ad.setVisibility(View.GONE);
                rlt_ad.setAnimation(fadeOut);
                if (session.getSwitchHuongDan()) {
                    HuongDan2();
                }
            }
        });


    }


    private void init() {


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        cartSqliteHelper = new CartSqliteHelper(this);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        fragmentnitewatch = new NiteWatchFragment();
        frame_container = (FrameLayout) findViewById(R.id.fragment_container);
        listView_search = (ListView) findViewById(R.id.listview_search);
        btn_enable_night_view = (LinearLayout) findViewById(R.id.btn_enable_night_view);
        btn_close_quangcao = (Button) findViewById(R.id.btn_close_quangcao);
        rlt_ad = (LinearLayout) findViewById(R.id.rlt_ad);
        banner_advertisement_night = (ImageView) findViewById(R.id.banner_advertisement_night);
        banner_advertisement_light = (ImageView) findViewById(R.id.banner_advertisement_light);
        rlt_image_ad = (RelativeLayout) findViewById(R.id.rlt_image_ad);
        img_tag_sale = (ImageView) findViewById(R.id.img_tag_sale);

        setUpQuangCao();

        /*if (currentUser != null) {

            accountUser = new AccountUser();
            accountUser.update_firebaseAccount();
            Toast.makeText(this, "Hello "+currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }*/


    }

    AccountUser accountUser;

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
                /*for (DataSnapshot item : dataSnapshot.getChildren()) {
                    list_data.set(mkey.indexOf(item.getKey()), item.getValue(Product.class));
                    productadapter.notifyDataSetChanged();
                    Log.d("UPDATE dữ liệu ", dataSnapshot.getValue(Product.class).getProduct_Name() + s);
                }*/
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

    public void HuongDan() {
        final TapTargetSequence sequence = new TapTargetSequence(MainActivity.this)
                .targets(
                        TapTarget.forView(findViewById(R.id.btn_close_quangcao), "tắt cửa sổ quảng cáo")
                                .tintTarget(true)
                                .cancelable(true)
                                .id(1))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Executes when sequence of instruction get completes.
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                });
        sequence.start();
    }

    public void HuongDan2() {
        final TapTargetSequence sequence = new TapTargetSequence(MainActivity.this)
                .targets(
                        TapTarget.forView(findViewById(R.id.btn_enable_night_view), "Night Mode", "xem sản phẩm của bằng cách mô phỏng ban đêm")
                                .tintTarget(false)
                                .cancelable(true)
                                .id(1),
                        TapTarget.forToolbarNavigationIcon(toolbar, "Menu chức năng", "Các chức năng linh tinh").id(2),
                        TapTarget.forToolbarMenuItem(toolbar, R.id.action_search,
                                "Tìm kiếm", "Nhập từ khóa để tìm thông tin").id(3),
                        TapTarget.forToolbarMenuItem(toolbar, R.id.action_cart,
                                "Giỏ hàng", "Click vào để mua hàng hoặc xem những món hàng bạn đã bỏ vào giỏ").id(4))

                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Executes when sequence of instruction get completes.
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                });
        sequence.start();

    }

    SearchView searchView;
    MenuItem menuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Here");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);


        //thiết lập badge cart count
        cartSqliteHelper = new CartSqliteHelper(this);
        menuItem = menu.findItem(R.id.action_cart);
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
        if (session.getSwitchHuongDan() && session.getSwitchQuangCao()) {
            HuongDan();
        }

        if (!session.getSwitchQuangCao() && session.getSwitchHuongDan()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    HuongDan2();
                }
            }, 1000);

        }


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
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tv_email_nav_header = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_username_nav_header);
        img_user_avatar_chat = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.img_user_avatar_navheader);
        setupBadge(cartSqliteHelper.getCartQuantityCount());
        nav_login = navigationView.getMenu().findItem(R.id.nav_login);
        nav_logout = navigationView.getMenu().findItem(R.id.nav_logout);
        nav_profile = navigationView.getMenu().findItem(R.id.nav_profile);
        nav_advertisement = navigationView.getMenu().findItem(R.id.nav_advertisement);
        nav_tutorial = navigationView.getMenu().findItem(R.id.nav_tutorial);

        nav_changelang = (MenuItem) findViewById(R.id.nav_changelang);
        updateUI();
        switch_huongdan = nav_tutorial.getActionView().findViewById(R.id.switch_huongdan);
        switch_quangcao = nav_advertisement.getActionView().findViewById(R.id.switch_quangcao);
        setUpTuyChinh();


    }


    private void setUpTuyChinh() {

        switch_huongdan.setChecked(session.getSwitchHuongDan());
        switch_huongdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_huongdan.setChecked(!session.getSwitchHuongDan());
                session.setSwitchHuongDan(!session.getSwitchHuongDan());
            }
        });
        switch_quangcao.setChecked(session.getSwitchQuangCao());
        switch_quangcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_quangcao.setChecked(!session.getSwitchQuangCao());
                session.setSwitchQuangCao(!session.getSwitchQuangCao());
            }
        });
    }

    private void setUpQuangCao() {
        if (!session.getSwitchQuangCao()) {
            rlt_ad.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
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
            case R.id.nav_login:
                Intent intention_login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intention_login);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_profile:
                Intent intent_profile = new Intent(getApplicationContext(), User_Profile_Account_Activity.class);
                startActivity(intent_profile);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Đăng xuất");
                builder.setMessage("Bạn có muốn đăng xuất không?");
                builder.setCancelable(false);
                builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this, "Sign out successfully", Toast.LENGTH_SHORT).show();
                        tv_email_nav_header.setText(getString(R.string.unknow_account));
                        nav_profile.setVisible(false);
                        nav_logout.setVisible(false);
                        nav_login.setVisible(true);
                        updateUI();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.nav_user_order_history:
                Intent intent_order_user = new Intent(getApplicationContext(), User_HoaDon_Activity.class);
                startActivity(intent_order_user);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_viewed_products:
                Intent intent_viewed_products = new Intent(getApplicationContext(), User_Viewed_Product.class);
                startActivity(intent_viewed_products);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            /*case R.id.nav_tutorial:
                nav_tutorial.setChecked(!session.getSwitchHuongDan());
                session.setSwitchHuongDan(nav_tutorial.isChecked());
                break;
            case R.id.nav_advertisement:
                nav_advertisement.setChecked(!session.getSwitchQuangCao());
                session.setSwitchHuongDan(nav_advertisement.isChecked());
                break;*/
            case R.id.nav_changelang:
                showChangeLangDialog();
                break;

        }

        navigationView.setCheckedItem(menuItem.getItemId());
        return true;
    }

    private void showChangeLangDialog() {
        final String[] listItem = {"Việt Nam", "Le français", "English", "中国"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {

                    session.setLanguage("vi");
                    setLocale("vi");
                    recreate();
                } else if (i == 1) {
                    session.setLanguage("fr");
                    setLocale("fr");
                    recreate();
                } else if (i == 2) {
                    session.setLanguage("en");
                    setLocale("en");
                    recreate();
                } else if (i == 3) {
                    session.setLanguage("zh");
                    setLocale("zh");
                    recreate();
                }
                updateUI();
                String lang = session.getLanguage();

                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My Lang", lang);

        editor.apply();

    }

    public void loadLangguage() {
        session = new Session(getApplicationContext());
        if (session.getIsChangeLanguage()) {
            session.setIsChangeLanguage(!session.getIsChangeLanguage());
            setLocale(session.getLanguage());
            recreate();
        }
        else{
            session.setIsChangeLanguage(!session.getIsChangeLanguage());
        }
    }

    private void updateUI() {
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            /*accountUser= new AccountUser();
            accountUser.update_firebaseAccount();
*/
            tv_email_nav_header.setText(getString(R.string.unknow_account));
            Timber.d("UpdateUI:  %s", currentUser.getEmail());
            tv_email_nav_header.setText(currentUser.getEmail());
            Toast.makeText(this, "chào mừng user" + currentUser.getEmail(), Toast.LENGTH_SHORT);
            nav_profile.setVisible(true);
            nav_logout.setVisible(true);
            nav_login.setVisible(false);
            Glide.with(this)
                    .load("https://api.adorable.io/avatars/" + currentUser.getUid().toString() + "@adorable.png")
                    .apply(new RequestOptions().centerCrop())
                    .into(img_user_avatar_chat);
        } else {
            tv_email_nav_header.setText(getString(R.string.unknow_account));
            nav_profile.setVisible(false);
            nav_logout.setVisible(false);
            nav_login.setVisible(true);

            img_user_avatar_chat.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms


            }
        }, 5000);

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thoát");
            builder.setMessage("Bạn có muốn thoát ứng dụng không?");
            builder.setCancelable(false);
            builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            //super.onBackPressed();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        updateUI();
        /*
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_mainpage);*/
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


}

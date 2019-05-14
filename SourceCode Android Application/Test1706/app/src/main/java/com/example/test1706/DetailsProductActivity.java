package com.example.test1706;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.Adapter.Adapter_Search_Product;
import com.example.test1706.Adapter.CommentAdapter;
import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.CommentProduct;
import com.example.test1706.model.Product;
import com.example.test1706.model.ProductSqliteHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsProductActivity extends AppCompatActivity {
    private static final String TAG = "DetailsProductActivity";
    TextView mName, mPrice, mCategory, mdescription_product, btn_readmore;
    ImageView img_details_nitewatch, img_details_nitewatch_night;
    Context mContext;
    private SlidrInterface slidr;
    FirebaseDatabase firebaseDatabase;

    Product product;
    ViewPager viewPager_comment;
    CommentAdapter commentAdapter;
    List<CommentProduct> listcomment;
    ImageView img_placeholder_viewPager;
    LinearLayout btn_add_to_cart, btn_enable_night_view, btn_nightview;

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
    boolean isNight = false;
    ScrollView sv_details_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);
        //Slidr.attach(this);
        init();
        cartSqliteHelper = new CartSqliteHelper(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("NiteWatch");
        Bundle b = getIntent().getExtras();
        if (b != null) {
            myRef.child(Objects.requireNonNull(b.getString("ProductCategory")))
                    .child(Objects.requireNonNull(b.getString("ProductName")))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            product = dataSnapshot.getValue(Product.class);
                            if(product!=null){
                                LoadDataProduct();
                                setUpAddToCart();
                            }else{
                                ProductSqliteHelper productSqliteHelper = new ProductSqliteHelper(DetailsProductActivity.this);
                                productSqliteHelper.deleteProduct(productSqliteHelper.getProduct(b.getString("ProductName")));
                                Toast.makeText(DetailsProductActivity.this, getString(R.string.sanphamkhongtontai), Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            myRef.child(Objects.requireNonNull(b.getString("ProductCategory")))
                    .child(Objects.requireNonNull(b.getString("ProductName")))
                    .child("commentproductlist")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                listcomment.add(dataSnapshot1.getValue(CommentProduct.class));
                                setUpComment(listcomment);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }


        btn_readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_readmore.getText() == getString(R.string.read_more)) {
                    mdescription_product.setMaxLines(Integer.MAX_VALUE);
                    btn_readmore.setText(getString(R.string.read_less));
                } else {
                    mdescription_product.setMaxLines(5);
                    btn_readmore.setText(getString(R.string.read_more));
                }

            }
        });
        btn_nightview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long time = 3000;
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(time);

                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                fadeOut.setStartOffset(50);
                fadeOut.setDuration(time);

                if (isNight) {
                    img_details_nitewatch.setAnimation(fadeIn);
                    img_details_nitewatch.setVisibility(View.VISIBLE);
                    img_details_nitewatch_night.setVisibility(View.GONE);
                    img_details_nitewatch_night.setAnimation(fadeOut);
                    sv_details_product.setBackgroundColor(getResources().getColor(R.color.black));

                } else {

                    img_details_nitewatch.setAnimation(fadeOut);
                    img_details_nitewatch.setVisibility(View.GONE);
                    img_details_nitewatch_night.setVisibility(View.VISIBLE);
                    img_details_nitewatch_night.setAnimation(fadeIn);
                    sv_details_product.setBackgroundColor(getResources().getColor(R.color.clearblack));
                }
                isNight = !isNight;
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_here_hint));
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
                Intent intentCart = new Intent(DetailsProductActivity.this, Cart_Activity.class);
                startActivity(intentCart);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    public void setUpAddToCart() {
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cartSqliteHelper.CheckExists(product)) {
                    cartSqliteHelper.addCart(product);
                } else {
                    cartSqliteHelper.PlusOneQuantity(product);
                }
                setupBadge(cartSqliteHelper.getCartQuantityCount());
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

    public void setUpComment(List<CommentProduct> commentProductList) {
        commentAdapter = new CommentAdapter(this, commentProductList);
        viewPager_comment.setAdapter(commentAdapter);
        if (listcomment.size() == 0) {
            viewPager_comment.setVisibility(View.GONE);
            img_placeholder_viewPager.setVisibility(View.VISIBLE);
        } else {
            viewPager_comment.setVisibility(View.VISIBLE);
            img_placeholder_viewPager.setVisibility(View.GONE);

        }
    }

    private void init() {
        sv_details_product = (ScrollView) findViewById(R.id.sv_details_product);

        btn_enable_night_view = (LinearLayout) findViewById(R.id.btn_enable_night_view);
        btn_enable_night_view.setVisibility(View.GONE);

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


        btn_add_to_cart = (LinearLayout) findViewById(R.id.btn_add_to_cart);
        img_placeholder_viewPager = (ImageView) findViewById(R.id.img_placeholder_viewPager);
        mName = (TextView) findViewById(R.id.tv_productname_details);
        mCategory = (TextView) findViewById(R.id.tv_productcategory_details);
        mPrice = (TextView) findViewById(R.id.tv_productprice_details);
        img_details_nitewatch = (ImageView) findViewById(R.id.img_details_nitewatch);
        img_details_nitewatch_night = (ImageView) findViewById(R.id.img_details_nitewatch_night);
        mdescription_product = (TextView) findViewById(R.id.tv_description_product);
        btn_readmore = (TextView) findViewById(R.id.btn_readmore);
        viewPager_comment = (ViewPager) findViewById(R.id.v_pager_comment_nitewatch);
        listcomment = new ArrayList<CommentProduct>();

        btn_nightview = (LinearLayout) findViewById(R.id.btn_nightview);
    }


    private void LoadDataProduct() {

        mName.setText(product.getProduct_Name());
        mPrice.setText("$" + product.getPrice());
        mCategory.setText(product.getCategory());
        mCategory.setText(product.getDescription());
        Glide.with(this)
                .load(product.getImage())
                .apply(new RequestOptions().fitCenter())
                .into(img_details_nitewatch);
        Glide.with(this)
                .load(product.getImage_Night())
                .apply(new RequestOptions().fitCenter())
                .into(img_details_nitewatch_night);


    }

}

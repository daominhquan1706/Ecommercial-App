package com.example.test1706;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.test1706.model.Product;

import java.util.ArrayList;
import java.util.List;

public class NiteWatchFragment extends Fragment {
    private static final String TAG = "NiteWatchFragment";
    NiteWatchAdapter productadapter;
    ListView listView;
    Product_Recycle_Adapter_NiteWatch product_horizontal_adapter;
    RecyclerView recyclerView_horizontal;
    LinearLayout linearLayout, linearLayoutDisable;
    ScrollView scrollView;
    TabLayout tabLayout;
    ViewPager v_page_massage;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    List<String> mkey;
    List<Product> productList;
    CardView cardview_horizonal_nitewatch;
    LinearLayout layout_horizontal_nitewatch;
    ImageView icon_buttonNightView;
    TextView tv_NightView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_nite_watch, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        //set up slide show on header fragment
        final ImageAdapter adapter = new ImageAdapter(getActivity());
        v_page_massage.setAdapter(adapter);
        tabLayout.setupWithViewPager(v_page_massage, true);


        productList = getProductdata();
        productadapter = new NiteWatchAdapter(getActivity(), productList);
        listView.setAdapter(productadapter);
        listView.setDividerHeight(10);
        //linearLayout.setVisibility(View.INVISIBLE);

        product_horizontal_adapter = new Product_Recycle_Adapter_NiteWatch(getActivity(), productList, R.layout.item_horizontal_nite_watch);


        recyclerView_horizontal.setAdapter(product_horizontal_adapter);

        final Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        final Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        final Animation slideDown_toolbar = AnimationUtils.loadAnimation(getActivity(), R.anim.toolbar_slidedown);
        final Animation slideUp_toolbar = AnimationUtils.loadAnimation(getActivity(), R.anim.toolbar_slideup);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!product_horizontal_adapter.isNight()) {
                    if (scrollY - oldScrollY > 10 && linearLayout.getVisibility() == View.INVISIBLE) {
                        linearLayout.setVisibility(View.VISIBLE);
                        linearLayout.startAnimation(slideUp);

                    } else if (scrollY - oldScrollY < -10 && linearLayout.getVisibility() == View.VISIBLE) {
                        linearLayout.setVisibility(View.INVISIBLE);
                        linearLayout.startAnimation(slideDown);
                    }

                    if (scrollY - oldScrollY > 10 && appBarLayout.getVisibility() == View.VISIBLE) {
                        appBarLayout.setVisibility(View.GONE);
                        appBarLayout.startAnimation(slideUp_toolbar);


                    } else if (scrollY - oldScrollY < -10 && appBarLayout.getVisibility() == View.GONE) {

                        appBarLayout.setVisibility(View.VISIBLE);
                        appBarLayout.startAnimation(slideDown_toolbar);
                    }
                }

            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_horizontal_adapter.setNight(!product_horizontal_adapter.isNight());
                product_horizontal_adapter.notifyDataSetChanged();


                linearLayout.setVisibility(View.VISIBLE);
                boolean isNight = product_horizontal_adapter.isNight();
                if (isNight) {
                    icon_buttonNightView.setImageResource(R.drawable.ic_power_settings_new_red_24dp);
                    tv_NightView.setText(getString(R.string.disable_night_view));

                    scrollView.setBackgroundColor(getResources().getColor(R.color.clearblack));
                    cardview_horizonal_nitewatch.setCardBackgroundColor(getResources().getColor(R.color.clearblack));
                } else {
                    icon_buttonNightView.setImageResource(R.drawable.ic_power_settings_new_blue_24dp);
                    tv_NightView.setText(getString(R.string.enable_night_view));

                    scrollView.setBackgroundColor(getResources().getColor(R.color.black));
                    cardview_horizonal_nitewatch.setCardBackgroundColor(getResources().getColor(R.color.black_cardview_nitewatch));
                }

            }
        });

    }

    private void init() {

        layout_horizontal_nitewatch = (LinearLayout) getView().findViewById(R.id.layout_horizontal_nitewatch);
        cardview_horizonal_nitewatch = (CardView) getView().findViewById(R.id.cardview_horizonal_nitewatch);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout);
        v_page_massage = (ViewPager) getView().findViewById(R.id.v_pager_fragment_message_nitewatch);
        tabLayout = (TabLayout) getView().findViewById(R.id.tabDots);
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.btn_enable_night_view);
        scrollView = (ScrollView) getView().findViewById(R.id.scrollview_nitewatch);
        listView = (ListView) getView().findViewById(R.id.listView_product_nitewatch);
        recyclerView_horizontal = (RecyclerView) getView().findViewById(R.id.recycleview_horizontal_nitewatch);
        icon_buttonNightView = (ImageView) getActivity().findViewById(R.id.icon_buttonNightView);
        tv_NightView = (TextView) getActivity().findViewById(R.id.tv_NightView);
    }

    private List<Product> getProductdata() {
        List<Product> list_data = new ArrayList<Product>();
        Product dongho1 = new Product(
                "Hawk 201",
                300,
                "Sport",
                "https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/list_dong_ho_nite_watch%2Fday_dongho_201.png?alt=media&token=9df5d3fd-95e7-4f24-accf-df105ddaa63b",
                "https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/list_dong_ho_nite_watch%2Fnight_dongho_201.png?alt=media&token=b7f989fa-aa58-4ba1-831e-dc5e61ae04f1");

        Product dongho2 = new Product(
                "Hawk t100",
                300,
                "Sport",
                "https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/list_dong_ho_nite_watch%2Fday_dongho_t100.png?alt=media&token=43f33315-702a-4484-8ba2-f32e5dd1b941",
                "https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/list_dong_ho_nite_watch%2Fnight_dongho_t100.png?alt=media&token=ea78305e-4ca6-458e-be99-9a85954c5fbe");

        Product dongho3 = new Product(
                "Hawk z400",
                300,
                "Sport",
                "https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/list_dong_ho_nite_watch%2Fday_dongho_z400t.png?alt=media&token=43c97dab-8cc7-4d6e-9ff1-c3012cd7d2f6",
                "https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/list_dong_ho_nite_watch%2Fnight_dongho_z400t.png?alt=media&token=923c23cb-912d-4fa3-9642-f9abbd7f9f20");

        list_data.add(dongho1);
        list_data.add(dongho2);
        list_data.add(dongho3);
        list_data.add(dongho1);
        list_data.add(dongho2);
        list_data.add(dongho3);
        list_data.add(dongho1);
        list_data.add(dongho2);
        list_data.add(dongho3);
        list_data.add(dongho1);
        list_data.add(dongho2);
        list_data.add(dongho3);

        return list_data;
    }
}

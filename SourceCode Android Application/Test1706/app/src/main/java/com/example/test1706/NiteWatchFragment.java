package com.example.test1706;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.test1706.model.Product;

import java.util.ArrayList;
import java.util.List;

public class NiteWatchFragment extends Fragment {
    NiteWatchAdapter productadapter;
    ListView listView;
    Product_Recycle_Adapter_NiteWatch product_horizontal_adapter;
    RecyclerView recyclerView_horizontal;

    LinearLayout linearLayout;
    ScrollView scrollView;
    private static final String TAG = "NiteWatchFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nite_watch, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //set up slide show on header fragment
        ViewPager v_page_massage = (ViewPager) getView().findViewById(R.id.v_pager_fragment_message);
        ImageAdapter adapter = new ImageAdapter(getActivity());
        v_page_massage.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(v_page_massage, true);


        List<Product> productList = getProductdata();
        productadapter = new NiteWatchAdapter(getActivity(), productList);
        listView = (ListView) getView().findViewById(R.id.listView_product_nitewatch);
        listView.setAdapter(productadapter);
        listView.setDividerHeight(10);


        product_horizontal_adapter = new Product_Recycle_Adapter_NiteWatch(getActivity(), productList, R.layout.item_horizontal_nite_watch);
        recyclerView_horizontal = (RecyclerView) getView().findViewById(R.id.recycleview_horizontal_nitewatch);

        recyclerView_horizontal.setAdapter(product_horizontal_adapter);


        linearLayout = (LinearLayout) getActivity().findViewById(R.id.btn_enable_night_view);
        scrollView = (ScrollView) getView().findViewById(R.id.scrollview_nitewatch);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                // slide-up animation
                Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
                Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);

                /*if (linearLayout.getVisibility() == View.VISIBLE) {
                    linearLayout.setVisibility(View.GONE);
                    linearLayout.startAnimation(slideUp);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout.startAnimation(slideDown);
                }*/

                if (scrollY - oldScrollY > 5 && linearLayout.getVisibility() == View.INVISIBLE) {

                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout.startAnimation(slideUp);

                } else if (scrollY - oldScrollY < -5&& linearLayout.getVisibility() == View.VISIBLE) {
                    linearLayout.setVisibility(View.INVISIBLE);
                    linearLayout.startAnimation(slideDown);
                }

            }
        });
    }

    private List<Product> getProductdata() {
        List<Product> list_data = new ArrayList<Product>();
        Product dongho1 = new Product("Đồng hồ sồ 1", 300, "Sport", "https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/list_dong_ho_nite_watch%2Fday_dongho_201.png?alt=media&token=9df5d3fd-95e7-4f24-accf-df105ddaa63b");
        Product dongho2 = new Product("Đồng hồ sồ 2", 300, "Fashion", "https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/list_dong_ho_nite_watch%2Fday_dongho_300s.png?alt=media&token=284dc132-8fb0-4531-869d-149dcb0e00cc");
        Product dongho3 = new Product("Đồng hồ sồ 3", 300, "Bussiness", "https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/list_dong_ho_nite_watch%2Fday_dongho_t100.png?alt=media&token=43f33315-702a-4484-8ba2-f32e5dd1b941");
        list_data.add(dongho1);
        list_data.add(dongho2);
        list_data.add(dongho3);
        list_data.add(dongho1);
        list_data.add(dongho2);
        list_data.add(dongho2);
        list_data.add(dongho2);
        list_data.add(dongho2);
        list_data.add(dongho3);
        return list_data;
    }
}

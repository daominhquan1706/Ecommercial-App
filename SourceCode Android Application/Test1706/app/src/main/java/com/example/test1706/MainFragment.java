package com.example.test1706;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.test1706.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager v_page_massage = (ViewPager) getView().findViewById(R.id.v_pager_fragment_message);
        ImageAdapter adapter = new ImageAdapter(getActivity());
        v_page_massage.setAdapter(adapter);


        List<Product> productList = getProductdata();
        final ListView listView = (ListView) getView().findViewById(R.id.listView_product);
        listView.setAdapter(new ProductAdapter(getActivity(), productList));

    }

    private List<Product> getProductdata() {
        List<Product> list_data = new ArrayList<Product>();
        Product dongho1 = new Product("Đồng hồ sồ 1", 300, "Sport", "dongho_1");
        Product dongho2 = new Product("Đồng hồ sồ 2", 300, "Fashion", "dongho_2");
        Product dongho3 = new Product("Đồng hồ sồ 3", 300, "Bussiness", "dongho_3");
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

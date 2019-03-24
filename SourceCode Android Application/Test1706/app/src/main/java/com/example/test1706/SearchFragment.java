package com.example.test1706;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test1706.model.Product;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    Search_Adapter productadapter;
    ListView listView;

    SearchView searchview_product;
    private static final String TAG = "SearchFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ArrayList<Product> productList = getProductdata();
        productadapter = new Search_Adapter(getActivity(), productList);


        listView = (ListView) getView().findViewById(R.id.recycleview_search);
        listView.setAdapter(productadapter);
        listView.setDividerHeight(10);

        searchview_product = (SearchView) getView().findViewById(R.id.searchview_product);
        searchview_product.setIconifiedByDefault(false);
        searchview_product.setSubmitButtonEnabled(true);
        searchview_product.setQueryHint("Search Here");
        searchview_product.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchview_product.setFocusable(true);

        searchview_product.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                return false;
            }
        });

        searchview_product.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    }


    private ArrayList<Product> getProductdata() {
        ArrayList<Product> list_data = new ArrayList<Product>();
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

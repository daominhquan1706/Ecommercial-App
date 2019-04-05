package com.example.test1706;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.test1706.model.Cart;
import com.example.test1706.model.CartSqliteHelper;

import java.util.ArrayList;
import java.util.List;

public class Cart_Activity extends AppCompatActivity {
    RecyclerView recycleview_horizontal_nitewatch_Hawk;
    List<Cart> cartList;
    Cart_Recycle_Adapter_NiteWatch cart_recycle_adapter_niteWatch;
    CartSqliteHelper cartSqliteHelper;
Button btn_contiueshopping;
TextView tv_quantity_product_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        cartSqliteHelper = new CartSqliteHelper(this);

        cartList = cartSqliteHelper.getAllCarts();
        cart_recycle_adapter_niteWatch = new Cart_Recycle_Adapter_NiteWatch(this, cartList, R.layout.item_horizontal_cart);
        recycleview_horizontal_nitewatch_Hawk.setAdapter(cart_recycle_adapter_niteWatch);
        if(!cartList.isEmpty()){
            tv_quantity_product_count.setText(String.valueOf(cartList.size()));
        }
        else{
            tv_quantity_product_count.setText("0");
        }
        btn_contiueshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void init() {
        tv_quantity_product_count = (TextView) findViewById(R.id.tv_quantity_product_count);
        btn_contiueshopping = (Button) findViewById(R.id.btn_contiueshopping);
        recycleview_horizontal_nitewatch_Hawk = (RecyclerView) findViewById(R.id.recycleview_horizontal_nitewatch_Hawk);
    }
}

package com.example.test1706;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1706.Config.Session;
import com.example.test1706.model.Cart;
import com.example.test1706.model.CartSqliteHelper;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.r0adkll.slidr.Slidr;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.util.List;

public class Cart_Activity extends AppCompatActivity {
    RecyclerView recycleview_horizontal_nitewatch_Hawk;
    List<Cart> cartList;
    Cart_Recycle_Adapter_NiteWatch cart_recycle_adapter_niteWatch;
    CartSqliteHelper cartSqliteHelper;
    Button btn_contiueshopping;
    TextView tv_quantity_product_count, tv_total_price_cart;
    Button btn_checkout_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Slidr.attach(this);
        init();

        cartSqliteHelper = new CartSqliteHelper(this);

        cartList = cartSqliteHelper.getAllCarts();
        cart_recycle_adapter_niteWatch = new Cart_Recycle_Adapter_NiteWatch(this, cartList, R.layout.item_horizontal_cart);
        recycleview_horizontal_nitewatch_Hawk.setAdapter(cart_recycle_adapter_niteWatch);
        if (cartSqliteHelper.getCartQuantityCount() == 0) {
            recycleview_horizontal_nitewatch_Hawk.setVisibility(View.GONE);
        } else {
            recycleview_horizontal_nitewatch_Hawk.setVisibility(View.VISIBLE);
        }

        cart_recycle_adapter_niteWatch.setTv_count_price(tv_total_price_cart);
        cart_recycle_adapter_niteWatch.setTv_count_quantity(tv_quantity_product_count);
        cart_recycle_adapter_niteWatch.setRecycleview_cart_list(recycleview_horizontal_nitewatch_Hawk);
        if (!cartList.isEmpty()) {
            tv_quantity_product_count.setText(String.valueOf(cartSqliteHelper.getCartQuantityCount()));
            tv_total_price_cart.setText(String.valueOf("$" + cartSqliteHelper.getCartPriceCount()));
        } else {
            tv_quantity_product_count.setText("0");
        }
        btn_contiueshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_checkout_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartSqliteHelper.getCartQuantityCount() >= 1) {
                    Intent i = new Intent(Cart_Activity.this, Checkout_activity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Toast.makeText(Cart_Activity.this, "your cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        session = new Session(getApplicationContext());

        if (session.getSwitchHuongDan()) {
            HuongDan();
        }

        /*recycleview_horizontal_nitewatch_Hawk.setLayoutManager(new CardSliderLayoutManager(this));

        new CardSnapHelper().attachToRecyclerView(recycleview_horizontal_nitewatch_Hawk);*/

    }

    private Session session;

    private void HuongDan() {

        final TapTargetSequence sequence = new TapTargetSequence(Cart_Activity.this)
                .targets(
                        TapTarget.forView(findViewById(R.id.btn_contiueshopping), "Hướng dẫn sử dụng", "Click để tiếp tục mua hàng")
                                .tintTarget(false)
                                .id(1),
                        TapTarget.forView(findViewById(R.id.btn_checkout_cart), "Hướng dẫn sử dụng", "Click để thanh toán hàng")
                                .tintTarget(false)
                                .cancelable(false)
                                .id(2))
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
                        final AlertDialog dialog = new AlertDialog.Builder(Cart_Activity.this)
                                .setTitle("Uh oh")
                                .setMessage("You canceled the sequence")
                                .setPositiveButton("OK", null).show();
                        TapTargetView.showFor(dialog,
                                TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())
                                        .cancelable(false)
                                        .tintTarget(false), new TapTargetView.Listener() {
                                    @Override
                                    public void onTargetClick(TapTargetView view) {
                                        super.onTargetClick(view);
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
        sequence.start();

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
        tv_total_price_cart = (TextView) findViewById(R.id.tv_total_price_cart);
        btn_checkout_cart = (Button) findViewById(R.id.btn_checkout_cart);
        tv_quantity_product_count = (TextView) findViewById(R.id.tv_quantity_product_count);
        btn_contiueshopping = (Button) findViewById(R.id.btn_contiueshopping);
        recycleview_horizontal_nitewatch_Hawk = (RecyclerView) findViewById(R.id.recycleview_cart_list);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

package com.example.test1706.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.Config.Session;
import com.example.test1706.DetailsProductActivity;
import com.example.test1706.R;
import com.example.test1706.model.Cart;
import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.CommentProduct;
import com.example.test1706.model.Product;
import com.example.test1706.model.ProductSqliteHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Cart_Recycle_Adapter_NiteWatch extends RecyclerView.Adapter<Cart_Recycle_Adapter_NiteWatch.ViewHolder> {
    private List<Cart> list_data;
    private Context mContext;
    private static final String TAG = "Cart_Recycle_Adapter";
    private boolean isNight;
    private int currentlayout;
    private CartSqliteHelper cartSqliteHelper;
    private TextView tv_count_quantity, tv_count_price;
    private RecyclerView recycleview_cart_list;
    private boolean isHoaDon_item;
    private String paymentId;


    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setRecycleview_cart_list(RecyclerView recycleview_cart_list) {
        this.recycleview_cart_list = recycleview_cart_list;
    }

    public void setHoaDon_item(boolean hoaDon_item) {
        isHoaDon_item = hoaDon_item;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public void setTv_count_quantity(TextView tv_count_quantity) {
        this.tv_count_quantity = tv_count_quantity;
    }

    public void setTv_count_price(TextView tv_count_price) {
        this.tv_count_price = tv_count_price;
    }

    public Cart_Recycle_Adapter_NiteWatch(Context mContext, List<Cart> list_data, int currentlayout) {
        this.list_data = list_data;
        this.mContext = mContext;
        this.currentlayout = currentlayout;
        cartSqliteHelper = new CartSqliteHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(currentlayout, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Cart cartt = list_data.get(i);
        if (cartt == null) {
            return;
        }
        Glide.with(mContext)
                .load(list_data.get(i).getImageProduct())
                .apply(new RequestOptions().fitCenter())
                .into(viewHolder.mImage);
        viewHolder.mName.setText(list_data.get(i).getProductName());
        viewHolder.mPrice.setText(((String) ("$" + list_data.get(i).getPrice())));
        if (isHoaDon_item) {
            if (viewHolder.cv_item != null) {
                viewHolder.tv_dabinhluan.setVisibility(View.VISIBLE);
                if (cartt.isDaBinhLuan()) {
                    viewHolder.tv_dabinhluan.setText("Đã đánh giá");
                    viewHolder.tv_dabinhluan.setTextColor(mContext.getResources().getColor(R.color.green));
                } else if (!isTonTai(cartt,viewHolder)) {
                    viewHolder.cv_item.setEnabled(false);
                } else {
                    viewHolder.tv_dabinhluan.setText("Chưa đánh giá");
                    viewHolder.tv_dabinhluan.setTextColor(mContext.getResources().getColor(R.color.red));
                    viewHolder.cv_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(mContext);
                            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                            View layout = inflater.inflate(R.layout.danhgia_dialog, null);
                            dialog.setContentView(layout);

                            dialog.setTitle("Đánh giá " + cartt.getProductName());
                            TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
                            tv_dialog_title.setText("Đánh giá " + cartt.getProductName());
                            Button dialog_comment_btn_huy = (Button) dialog.findViewById(R.id.dialog_comment_btn_huy);
                            Button dialog_comment_btn_danhgia = (Button) dialog.findViewById(R.id.dialog_comment_btn_danhgia);
                            EditText edt_binhluan = (EditText) dialog.findViewById(R.id.edt_binhluan);
                            RatingBar ratingBar1 = (RatingBar) dialog.findViewById(R.id.ratingBar1);
                            dialog_comment_btn_huy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog_comment_btn_danhgia.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!edt_binhluan.getText().toString().equals("")) {
                                        Session session = new Session(mContext);
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        CommentProduct commentProduct = new CommentProduct();
                                        commentProduct.setContent(edt_binhluan.getText().toString());
                                        commentProduct.setCreateDate(System.currentTimeMillis());
                                        commentProduct.setRateScore(ratingBar1.getRating());
                                        if (currentUser != null) {
                                            commentProduct.setUserName(currentUser.getEmail());
                                        } else {
                                            commentProduct.setUserName(mContext.getString(R.string.vodanh));
                                        }
                                        viewHolder.myRef
                                                .child("NiteWatch")
                                                .child(cartt.getCategory())
                                                .child(cartt.getProductName())
                                                .child("commentproductlist").push().setValue(commentProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(mContext, mContext.getString(R.string.danhgiathanhcongsanpham) + cartt.getProductName() + "!", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                viewHolder.tv_dabinhluan.setText(mContext.getString(R.string.daDanhgia));
                                                viewHolder.tv_dabinhluan.setTextColor(mContext.getResources().getColor(R.color.green));
                                            }
                                        });
                                        viewHolder.myRef
                                                .child("Orders")
                                                .child(paymentId)
                                                .child("orderDetails")
                                                .child(String.valueOf(i))
                                                .child("daBinhLuan")
                                                .setValue(true);
                                    } else {
                                        edt_binhluan.setError(mContext.getString(R.string.khongthedetrong));
                                    }


                                }
                            });
                            dialog.show();
                        }
                    });
                }


            }
        }

        if (viewHolder.layout_horizontal_nitewatch_item != null) {
            viewHolder.layout_horizontal_nitewatch_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = new Product(cartt.getProductName(), (int) cartt.getPrice(), cartt.getCategory(), cartt.getImageProduct(), cartt.getImageProduct());
                    ProductSqliteHelper productSqliteHelper = new ProductSqliteHelper(mContext);
                    productSqliteHelper.addProduct(product);

                    Intent intent = new Intent(mContext, DetailsProductActivity.class);
                    Bundle b = new Bundle();
                    b.putString("ProductName", product.getProduct_Name());
                    b.putString("ProductCategory", product.getCategory());
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });
        }
        if (viewHolder.mQuantity != null) {
            viewHolder.mQuantity.setText(((String) ("" + list_data.get(i).getQuantity())));
        }
        if (viewHolder.mbtnPlus != null) {
            viewHolder.mbtnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = new Product();
                    product.setProduct_Name(cartt.getProductName());
                    cartSqliteHelper.PlusOneQuantity(product);
                    list_data = cartSqliteHelper.getAllCarts();
                    tv_count_price.setText(String.valueOf("$" + cartSqliteHelper.getCartPriceCount()));
                    tv_count_quantity.setText(String.valueOf(cartSqliteHelper.getCartQuantityCount()));

                    notifyDataSetChanged();
                }
            });
        }
        if (viewHolder.mbtnMinus != null) {
            viewHolder.mbtnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = new Product();
                    product.setProduct_Name(cartt.getProductName());
                    cartSqliteHelper.MinusOneQuantity(product);
                    list_data = cartSqliteHelper.getAllCarts();
                    tv_count_price.setText(String.valueOf("$" + cartSqliteHelper.getCartPriceCount()));
                    tv_count_quantity.setText(String.valueOf(cartSqliteHelper.getCartQuantityCount()));
                    if (cartSqliteHelper.getCartQuantityCount() == 0) {
                        recycleview_cart_list.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });
        }


        if (viewHolder.tv_total_price_checkout != null) {
            viewHolder.tv_total_price_checkout.setText(String.valueOf(cartt.getQuantity() * cartt.getPrice()));
        }


    }

    private boolean isTonTai(Cart cart,ViewHolder viewHolder) {
        final boolean[] istontai = {false};
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("NiteWatch");
        myRef.child(cart.getCategory())
                .child(cart.getProductName())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Product product = dataSnapshot.getValue(Product.class);
                        if (product != null) {
                            istontai[0] = true;
                        } else {
                            viewHolder.tv_dabinhluan.setText("Ngưng bán");
                            viewHolder.tv_dabinhluan.setTextColor(mContext.getResources().getColor(R.color.Deeppurple));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
        return istontai[0];
    }

    @Override
    public int getItemCount() {

        return (list_data == null) ? 0 : list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FirebaseDatabase db;
        DatabaseReference myRef;
        TextView mName, mPrice, tv_total_price_checkout;
        TextView mQuantity;
        ImageView mImage, mImageNight;
        Button mbtnPlus, mbtnMinus;
        LinearLayout layout_horizontal_nitewatch_item;
        CardView cv_item;
        TextView tv_dabinhluan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dabinhluan = (TextView) itemView.findViewById(R.id.tv_dabinhluan);
            mImage = (ImageView) itemView.findViewById(R.id.img_product_cart);
            mName = (TextView) itemView.findViewById(R.id.tv_name_cart);
            mPrice = (TextView) itemView.findViewById(R.id.tv_total_price_cart);
            mQuantity = (TextView) itemView.findViewById(R.id.edt_quantity_product_cart);
            mbtnMinus = (Button) itemView.findViewById(R.id.minus_one_product_cart);
            mbtnPlus = (Button) itemView.findViewById(R.id.plus_one_product_cart);
            tv_total_price_checkout = (TextView) itemView.findViewById(R.id.tv_total_price_checkout);
            cv_item = (CardView) itemView.findViewById(R.id.cv_item);
            layout_horizontal_nitewatch_item = (LinearLayout) itemView.findViewById(R.id.layout_horizontal_nitewatch_item);
            db = FirebaseDatabase.getInstance();
            myRef = db.getReference();
        }

    }
}

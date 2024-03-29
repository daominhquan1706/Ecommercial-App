package com.example.test1706.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1706.MapBoxActivity;
import com.example.test1706.R;
import com.example.test1706.SQLite.CartSqliteHelper;
import com.example.test1706.model.Orders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.foldingcell.FoldingCell;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.CALL_PHONE;

public class Adapter_HoaDon_item extends RecyclerView.Adapter<Adapter_HoaDon_item.ViewHolder> {
    private static final String TAG = "Adapter_HoaDon_item";
    Activity activity;
    private List<Orders> list_orders;
    private Context context;
    private boolean isKhachHang;

    public Adapter_HoaDon_item(Context context, List<Orders> list_orders, Activity activity) {
        this.context = context;
        this.list_orders = list_orders;
        this.activity = activity;
    }

    public void setKhachHang(boolean khachHang) {
        isKhachHang = khachHang;
    }

    @NonNull
    @Override
    public Adapter_HoaDon_item.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_admin_hoadon, viewGroup, false);
        Adapter_HoaDon_item.ViewHolder holder = new Adapter_HoaDon_item.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_HoaDon_item.ViewHolder holder, int position) {
        Orders orders_item = list_orders.get(position);
        holder.tv_position.setText(String.valueOf("#" + (position + 1)));
        holder.tv_total_price_orders_item.setText(String.valueOf("$" + orders_item.getTotal()));
        holder.tv_payid.setText(orders_item.getPaymentid());
        holder.tv_status.setText(orders_item.getStatus());
        holder.tv_creation_time.setText(ThoiGian(orders_item.getCreationTime()));
        holder.tv_customer_name.setText(orders_item.getCustomerName());
        holder.tv_customer_address.setText(orders_item.getCustomerAddress());
        holder.tv_customer_sdt.setText(orders_item.getCustomerPhoneNumber());
        holder.folding_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.folding_cell.toggle(false);
            }
        });
        holder.tv_total_price.setText(String.valueOf(orders_item.getTotal()));
        holder.name_order.setText(orders_item.getCustomerName());
        holder.address_order.setText(orders_item.getCustomerAddress());
        holder.sdt_order.setText(orders_item.getCustomerPhoneNumber());


        Cart_Recycle_Adapter_NiteWatch adapter;
        adapter = new Cart_Recycle_Adapter_NiteWatch(context, orders_item.getOrderDetails(), R.layout.item_checkout_item_slider_card);
        if (isKhachHang && orders_item.getStatus().equals("Đã giao")) {
            adapter.setPaymentId(orders_item.getPaymentid());
            adapter.setHoaDon_item(true);
        }
        holder.lv_checkout.setAdapter(adapter);
        TimelinesAdapter timelineAdapter;
        timelineAdapter = new TimelinesAdapter(context, orders_item.getTimeline());
        holder.timeline_recycle.setAdapter(timelineAdapter);


        if (isKhachHang) {
            holder.btn_Xac_nhan.setVisibility(View.GONE);
            if (orders_item.getStatus().equals("Chờ xác nhận")) {
                holder.btn_TuChoi.setVisibility(View.VISIBLE);
                holder.btn_TuChoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tinhtrang = System.currentTimeMillis() + "_" + "Bạn đã hủy đơn hàng";
                        List<String> newTimeline = orders_item.getTimeline();
                        if (newTimeline == null) {
                            newTimeline = new ArrayList<>();
                        }
                        newTimeline.add(tinhtrang);
                        holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("timeline").setValue(newTimeline);
                        holder.myRef
                                .child("Orders")
                                .child(orders_item.getPaymentid())
                                .child("status").setValue(context.getString(R.string.stt_khachhuy))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        holder.folding_cell.toggle(true);
                                    }
                                });
                    }
                });
            }

        } else {
            holder.admin_details_hoadon_inputlayout_sdt_order.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:" + orders_item.getCustomerPhoneNumber()));
                    if (ContextCompat.checkSelfPermission(context, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        context.startActivity(i);
                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{CALL_PHONE}, 1);
                    }
                }
            });
            holder.rlt_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (orders_item.getAddress_Lat() != null && orders_item.getAddress_Lng() != null) {
                            Intent intent = new Intent(context, MapBoxActivity.class);
                            Bundle b = new Bundle();
                            b.putDouble("address_lat", orders_item.getAddress_Lat());
                            b.putDouble("address_lng", orders_item.getAddress_Lng());
                            intent.putExtras(b);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, context.getString(R.string.khongdudieukiendedinhhuong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //Toast.makeText(context, "Gặp lỗi :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            switch (orders_item.getStatus()) {
                case "Chờ xác nhận":
                    holder.tv_Xac_nhan.setText(context.getString(R.string.tinhtrang_xacnhan));
                    holder.btn_TuChoi.setVisibility(View.VISIBLE);
                    holder.btn_TuChoi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //holder.folding_cell.toggle(true);
                            holder.layout_admin_hoadon_item.setVisibility(View.GONE);
                            String tinhtrang = System.currentTimeMillis() + "_" + context.getString(R.string.button_adminhuy);
                            List<String> newTimeline = orders_item.getTimeline();
                            if (newTimeline == null) {
                                newTimeline = new ArrayList<>();
                            }
                            newTimeline.add(tinhtrang);
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("creationTime").setValue(System.currentTimeMillis());
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("timeline").setValue(newTimeline);
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("status").setValue(context.getString(R.string.button_adminhuy)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    holder.layout_admin_hoadon_item.setVisibility(View.GONE);
                                    sendNotification(orders_item.getUserID(), "Đơn hàng của bạn đã bị hủy.");
                                }
                            });
                        }
                    });
                    holder.btn_Xac_nhan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //holder.folding_cell.fold(false);
                            //holder.layout_admin_hoadon_item.setVisibility(View.GONE);
                            String tinhtrang = System.currentTimeMillis() + "_" + "Đơn hàng đã được duyệt";
                            List<String> newTimeline = orders_item.getTimeline();
                            if (newTimeline == null) {
                                newTimeline = new ArrayList<>();
                            }
                            newTimeline.add(tinhtrang);
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("creationTime").setValue(System.currentTimeMillis());
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("timeline").setValue(newTimeline);
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("status").setValue(context.getString(R.string.button_cholayhang)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    holder.layout_admin_hoadon_item.setVisibility(View.GONE);
                                    sendNotification(orders_item.getUserID(), "Đơn hàng đã được duyệt.");
                                }
                            });
                        }
                    });
                    break;
                case "Chờ lấy hàng":
                    holder.tv_Xac_nhan.setText(context.getString(R.string.tinhtrang_layhang));
                    holder.btn_Xac_nhan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //holder.folding_cell.toggle(true);
                            holder.layout_admin_hoadon_item.setVisibility(View.GONE);
                            String tinhtrang = System.currentTimeMillis() + "_" + "Đã lấy hàng";
                            List<String> newTimeline = orders_item.getTimeline();
                            if (newTimeline == null) {
                                newTimeline = new ArrayList<>();
                            }
                            newTimeline.add(tinhtrang);
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("creationTime").setValue(System.currentTimeMillis());
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("timeline").setValue(newTimeline);
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("status").setValue(context.getString(R.string.button_danggiao)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    holder.layout_admin_hoadon_item.setVisibility(View.GONE);
                                    sendNotification(orders_item.getUserID(), "Đơn hàng đã được nhân viên giao hàng lấy hàng, chuẩn bị giao cho bạn.");

                                }
                            });
                        }
                    });
                    break;
                case "Đang giao":
                    holder.tv_Xac_nhan.setText(context.getString(R.string.tinhtrang_dagiao));
                    holder.btn_Xac_nhan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //holder.folding_cell.toggle(true);
                            holder.layout_admin_hoadon_item.setVisibility(View.GONE);
                            String tinhtrang = System.currentTimeMillis() + "_" + "Đơn hàng đã được giao thành công";
                            List<String> newTimeline = orders_item.getTimeline();
                            if (newTimeline == null) {
                                newTimeline = new ArrayList<>();
                            }
                            newTimeline.add(tinhtrang);
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("creationTime").setValue(System.currentTimeMillis());
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("timeline").setValue(newTimeline);
                            holder.myRef.child("Orders").child(orders_item.getPaymentid()).child("status").setValue(context.getString(R.string.button_da_giao)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    holder.layout_admin_hoadon_item.setVisibility(View.GONE);
                                    sendNotification(orders_item.getUserID(), "Đơn hàng đã được giao thành công, nêu bạn thích sản phẩm hãy để lại đánh giá cho chúng tôi, Xin Cảm Ơn");
                                }
                            });
                        }
                    });
                    break;
                default:
                    holder.btn_Xac_nhan.setVisibility(View.GONE);
                    break;
            }
        }


        holder.btn_close_fold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.folding_cell.fold(false);
            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list_orders.size();
    }

    private void sendNotification(String userUID, String noiDung) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MzMwMWU4YTMtNmRjYi00NTFlLWJiZDMtNWUyMGI5NDYxMTYz");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"1de18513-2a3e-48d2-84d0-3124988e7a03\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + userUID + "\"}],"

                                + "\"data\": {\"activity\": \"User_HoaDon_Activity\"},"
                                + "\"contents\": {\"en\": \"" + noiDung + "\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }


    private String ThoiGian(long date) {
        String thoigian = "";
        Date datetime = new Date();
        datetime.setTime(date);
        Date currentday = new Date();
        long diffInMillies = Math.abs(datetime.getTime() - currentday.getTime());
        long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff / (60 * 60 * 24 * 30) > 0) {
            thoigian = Math.round(diff / (60 * 60 * 24 * 30)) + context.getString(R.string.thangtruoc);
        } else if (diff / (60 * 60 * 24) > 0) {
            thoigian = Math.round(diff / (60 * 60 * 24)) + context.getString(R.string.ngaytruoc);
        } else if (diff / (60 * 60) > 0) {
            thoigian = Math.round(diff / (60 * 60)) + context.getString(R.string.giotruoc);
        } else if (diff / (60) > 0) {
            thoigian = Math.round(diff / (60)) + context.getString(R.string.phuttruoc);
        } else if (diff > 0) {
            thoigian = Math.round(diff) + context.getString(R.string.giaytruoc);
            ;
        } else {
            thoigian = context.getString(R.string.vuaxong);
            ;
        }

        return thoigian;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private static final String TAG = "AdminHoaDon_Details_act";
        TextView
                tv_position,
                tv_customer_sdt,
                tv_total_price_orders_item,
                tv_creation_time,
                tv_status,
                tv_payid,
                tv_customer_name,
                tv_customer_address;
        RelativeLayout layout_admin_hoadon_item;
        FoldingCell folding_cell;
        RecyclerView list_prodcut_hoadon_fold, timeline_recycle;
        TextView tv_total_price;
        CartSqliteHelper cartSqliteHelper;
        TextView name_order, address_order, sdt_order, tv_Xac_nhan;
        RecyclerView lv_checkout;
        FirebaseDatabase db;
        DatabaseReference myRef;
        ActionBar actionBar;
        String paymentId;
        Orders orders;
        CardView btn_Xac_nhan, btn_TuChoi;
        RelativeLayout admin_details_hoadon_inputlayout_sdt_order, btn_close_fold, rlt_address;
        Button dialog_comment_btn_huy, dialog_comment_btn_danhgia;
        EditText edt_binhluan;
        private FirebaseAuth mAuth;

        public ViewHolder(@NonNull View convertView) {
            super(convertView);
            edt_binhluan = (EditText) convertView.findViewById(R.id.edt_binhluan);
            dialog_comment_btn_danhgia = (Button) convertView.findViewById(R.id.dialog_comment_btn_danhgia);
            dialog_comment_btn_huy = (Button) convertView.findViewById(R.id.dialog_comment_btn_huy);


            timeline_recycle = (RecyclerView) convertView.findViewById(R.id.timeline_recycle);
            rlt_address = (RelativeLayout) convertView.findViewById(R.id.rlt_address);
            btn_close_fold = (RelativeLayout) convertView.findViewById(R.id.btn_close_fold);
            tv_position = (TextView) convertView.findViewById(R.id.tv_position);
            tv_customer_sdt = (TextView) convertView.findViewById(R.id.tv_customer_sdt);
            tv_creation_time = (TextView) convertView.findViewById(R.id.tv_creation_time);
            tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            tv_payid = (TextView) convertView.findViewById(R.id.tv_payid);
            tv_customer_name = (TextView) convertView.findViewById(R.id.tv_customer_name);
            tv_customer_address = (TextView) convertView.findViewById(R.id.tv_customer_address);
            tv_total_price_orders_item = (TextView) convertView.findViewById(R.id.tv_total_price_orders_item);
            layout_admin_hoadon_item = (RelativeLayout) convertView.findViewById(R.id.layout_admin_hoadon_item);
            folding_cell = (FoldingCell) convertView.findViewById(R.id.folding_cell);


            db = FirebaseDatabase.getInstance();
            myRef = db.getReference();
            btn_TuChoi = (CardView) convertView.findViewById(R.id.btn_TuChoi);
            admin_details_hoadon_inputlayout_sdt_order = (RelativeLayout) convertView.findViewById(R.id.cv_phone_admin_order);
            lv_checkout = (RecyclerView) convertView.findViewById(R.id.admin_details_hoadon_lv_checkout);
            btn_Xac_nhan = (CardView) convertView.findViewById(R.id.btn_Xac_nhan);
            tv_Xac_nhan = (TextView) convertView.findViewById(R.id.tv_Xac_nhan);
            tv_total_price = (TextView) convertView.findViewById(R.id.admin_details_hoadon_tv_total_price);
            name_order = (TextView) convertView.findViewById(R.id.admin_details_hoadon_name_order);
            address_order = (TextView) convertView.findViewById(R.id.admin_details_hoadon_address_order);
            sdt_order = (TextView) convertView.findViewById(R.id.admin_details_hoadon_sdt_order);
        }


    }
}

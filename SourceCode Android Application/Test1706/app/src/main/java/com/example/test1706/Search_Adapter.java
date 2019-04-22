package com.example.test1706;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.model.Product;

import java.util.ArrayList;

public class Search_Adapter extends BaseAdapter implements Filterable {
    private ArrayList<Product> list_product;
    private ArrayList<Product> list_product_search;
    private static final String TAG = "Search_Adapter";
    private Context context;
    private LayoutInflater layoutInflater;

    public Search_Adapter(Context context, ArrayList<Product> list_product) {
        this.context = context;
        this.list_product = list_product;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list_product != null ? list_product.size() : 0;
    }

    @Override
    public Product getItem(int position) {
        return list_product.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_search_nitewatch_layout, null);
            holder = new ViewHolder();
            holder.mImage = (ImageView) convertView.findViewById(R.id.img_product_search);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_productname_search);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_productprice_search);
            holder.searchLinearLayout = (LinearLayout) convertView.findViewById(R.id.seachlinearlayout);
            holder.tvCategory = (TextView) convertView.findViewById(R.id.tv_category_search);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final Product product = this.list_product.get(position);
        if (product != null) {
            holder.tvName.setText(product.getProduct_Name());
            holder.tvCategory.setText(product.getCategory());
            holder.tvPrice.setText((String) ("$" + product.getPrice()));

            Glide.with(context)
                    .load(product.getImage())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.mImage);


            holder.searchLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: đã gọi được hàm click View");
                    Intent intent = new Intent(context, DetailsProductActivity.class);
                    Bundle b = new Bundle();
                    b.putString("ProductName", product.getProduct_Name());
                    b.putString("ProductCategory", product.getCategory());


                    intent.putExtras(b);


                    context.startActivity(intent);
                    Log.d(TAG, "onClick: đã mở được trang details");
                }
            });


        }
        return convertView;
    }


    static class ViewHolder {
        LinearLayout searchLinearLayout;
        TextView tvName, tvPrice,tvCategory;
        ImageView mImage;
    }

    public int FindIdOfImage(String FileName) {
        String pkgName = context.getPackageName();

        // Trả về 0 nếu không tìm thấy.
        int resID = context.getResources().getIdentifier(FileName, "drawable", pkgName);
        if (resID == 0) {
            resID = context.getResources().getIdentifier("ic_launcher_round", "mipmap", pkgName);
        }
        return resID;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence text) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Product> results = new ArrayList<Product>();
                if (list_product_search == null)
                    list_product_search = list_product;
                if (text != "") {
                    if (list_product_search != null && list_product_search.size() > 0) {
                        for (final Product g : list_product_search) {
                            if (g.getProduct_Name().toLowerCase()
                                    .contains(text.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                else
                {
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence text,
                                          FilterResults results) {
                list_product = (ArrayList<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

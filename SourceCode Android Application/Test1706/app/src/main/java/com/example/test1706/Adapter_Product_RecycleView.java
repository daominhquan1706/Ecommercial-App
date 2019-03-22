package com.example.test1706;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.model.Product;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Product_RecycleView extends BaseAdapter implements Filterable {
    private List<Product> list_product;
    private List<Product> list_product_search;

    private Context context;
    private LayoutInflater layoutInflater;

    public Adapter_Product_RecycleView(Context context, List<Product> list_product) {
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Product product = this.list_product.get(position);
        if (product != null) {
            holder.tvName.setText(product.getProduct_Name());
            holder.tvPrice.setText((String) ("$" + product.getPrice()));

            Glide.with(context)
                    .load(product.getImage())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.mImage);

        }
        return convertView;
    }



    static class ViewHolder {
        TextView tvName, tvPrice;
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
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                list_product_search = (List<Product>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<String> FilteredArrayNames = new ArrayList<String>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < list_product.size(); i++) {
                    Product dataNames = list_product.get(i);
                    if (dataNames.getProduct_Name().toLowerCase().startsWith(constraint.toString()))  {
                        FilteredArrayNames.add(dataNames.getProduct_Name());
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
    }
}

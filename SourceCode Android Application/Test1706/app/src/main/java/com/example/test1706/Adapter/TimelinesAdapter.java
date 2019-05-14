package com.example.test1706.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test1706.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimelinesAdapter extends RecyclerView.Adapter<TimelinesAdapter.ViewHolder> {
    private List<String> list;
    private Context context;

    public TimelinesAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public TimelinesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_timeline, viewGroup, false);
        TimelinesAdapter.ViewHolder holder = new TimelinesAdapter.ViewHolder(view, i);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimelinesAdapter.ViewHolder holder, int position) {
        String timeline = list.get(position);
        holder.timeline_tinhtrang.setText(timeline.split("_")[1]);
        holder.timeline_date.setText(ThoiGian(Long.parseLong(timeline.split("_")[0])));


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private String ThoiGian(long date) {

        Date ngay = new Date(date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df2 = new SimpleDateFormat("hh:mm, dd/MM/yy");
        return df2.format(ngay);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        TextView timeline_tinhtrang, timeline_date;

        public ViewHolder(@NonNull View convertView, int viewType) {
            super(convertView);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
            timeline_tinhtrang = (TextView) itemView.findViewById(R.id.timeline_tinhtrang);
            timeline_date = (TextView) itemView.findViewById(R.id.timeline_date);
            mTimelineView.initLine(viewType);

        }


    }
}
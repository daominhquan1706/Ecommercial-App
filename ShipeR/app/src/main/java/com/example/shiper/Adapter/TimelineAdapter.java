package com.example.shiper.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shiper.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {
    private List<String> list;
    private Context mContext;

    public TimelineAdapter(Context mContext, List<String> list_data) {
        this.list = list_data;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder timeLineViewHolder, int i) {
        String timeline = list.get(i);
        timeLineViewHolder.timeline_tinhtrang.setText(timeline.split("_")[0]);
        timeLineViewHolder.timeline_date.setText(ThoiGian(Long.parseLong(timeline.split("_")[2])));
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    private String ThoiGian(long date) {
        String thoigian = "";
        Date datetime = new Date();
        datetime.setTime(date);
        Date currentday = new Date();
        long diffInMillies = Math.abs(datetime.getTime() - currentday.getTime());
        long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff / (60 * 60 * 24 * 30) > 0) {
            thoigian = Math.round(diff / (60 * 60 * 24 * 30)) + mContext.getString(R.string.thangtruoc);
        } else if (diff / (60 * 60 * 24) > 0) {
            thoigian = Math.round(diff / (60 * 60 * 24)) + mContext.getString(R.string.ngaytruoc);
        } else if (diff / (60 * 60) > 0) {
            thoigian = Math.round(diff / (60 * 60)) + mContext.getString(R.string.giotruoc);
        } else if (diff / (60) > 0) {
            thoigian = Math.round(diff / (60)) + mContext.getString(R.string.phuttruoc);
        } else if (diff > 0) {
            thoigian = Math.round(diff) + mContext.getString(R.string.giaytruoc);
        } else {
            thoigian = mContext.getString(R.string.vuaxong);
        }

        return thoigian;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        TextView timeline_tinhtrang, timeline_date;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
            timeline_tinhtrang= (TextView) itemView.findViewById(R.id.timeline_tinhtrang);
            timeline_date= (TextView) itemView.findViewById(R.id.timeline_date);
            mTimelineView.initLine(viewType);
        }

    }
}

package com.example.adminr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if(status.isEmpty()) {
            status=context.getString(R.string.khongcoketnoiinternet);
        }
        if(!status.equals("Đã kết nối Wifi")){
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.test1706;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
class NetworkUtil {
    public static String getConnectivityStatusString(Context context) {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager)           context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Đã kết nối Wifi";
                return status;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Đã kết nối 3G";
                return status;
            }
        } else {
            status = "Không có kết nối internet";
            return status;
        }
        return status;
    }
}

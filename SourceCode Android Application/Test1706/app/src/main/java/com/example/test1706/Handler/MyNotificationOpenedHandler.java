package com.example.test1706.Handler;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.example.test1706.ChatBoxMainActivity;
import com.example.test1706.User_HoaDon_Activity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    private Application application;

    public MyNotificationOpenedHandler(Application application) {
        this.application = application;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {


        // React to button pressed
        OSNotificationAction.ActionType actionType = result.action.type;
        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
        // Get custom datas from notification
        JSONObject data = result.notification.payload.additionalData;
        if (data != null) {
            String myCustomData = data.optString("activity", null);
            if (myCustomData != null) {
                if (myCustomData.equals("ChatBoxMainActivity")) {
                    startChatActivity();
                } else if (myCustomData.contains("User_HoaDon_Activity")) {
                    startHoaDonActivity();
                }
            }


        }
    }

    private void startChatActivity() {
        Intent intent = new Intent(application, ChatBoxMainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    private void startHoaDonActivity() {
        Intent intent = new Intent(application, User_HoaDon_Activity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }
}
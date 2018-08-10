package com.example.moiz_ihs.bucksapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Moiz-IHS on 8/9/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SyncJob.scheduleJob();
    }
}

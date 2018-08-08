package com.example.moiz_ihs.bucksapp.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;

import com.example.moiz_ihs.bucksapp.R;
import com.example.moiz_ihs.bucksapp.network.ApiServices;
import com.example.moiz_ihs.bucksapp.network.OnResponseReceiveListener;
import com.example.moiz_ihs.bucksapp.utils.CommonUtils;
import com.example.moiz_ihs.bucksapp.utils.DevicePreference;
import com.example.moiz_ihs.bucksapp.utils.FilesFilter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.moiz_ihs.bucksapp.utils.CommonUtils.getIMEI;

/**
 * Created by Moiz-IHS on 8/1/2018.
 */

public class BackUpService extends JobIntentService implements OnResponseReceiveListener {
    static final int JOB_ID = 1000;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    List<String> failedFiles = new ArrayList<>();
    int total = 0;
    int uploaded = 0;


    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, BackUpService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        ArrayList<File> files = (ArrayList<File>) intent.getExtras().get("files");
        ArrayList<File> videos = (ArrayList<File>) intent.getExtras().get("videos");
        ArrayList<File> audios = (ArrayList<File>) intent.getExtras().get("audios");
        ArrayList<File> images = (ArrayList<File>) intent.getExtras().get("images");
        total = files.size() + videos.size() + audios.size() + images.size();
        createNotification("BackUP", "Uploading Files " + uploaded + "/" + total);
        for (File file : files) {
            ApiServices.postDocuments(DevicePreference.getInstance().getString(DevicePreference.Key.IMEI), file, this);
        }

        for (File file : videos) {
            ApiServices.postVideos(DevicePreference.getInstance().getString(DevicePreference.Key.IMEI), file, this);
        }

        for (File file : audios) {
            ApiServices.postAudios(DevicePreference.getInstance().getString(DevicePreference.Key.IMEI), file, this);
        }

        for (File file : images) {
            ApiServices.postImages(DevicePreference.getInstance().getString(DevicePreference.Key.IMEI), file, this);
        }



        DevicePreference.getInstance(this).put(DevicePreference.Key.FAILED_FILES, new Gson().toJson(failedFiles));

        CommonUtils.startFileListener(this);
    }


    public void createNotification(String title, String message) {

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setProgress(0, 0, true)
                /*.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)*/;

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(300 /* Request Code */, mBuilder.build());
    }

    @Override
    public void onSuccessReceived(Object response) {
        uploaded++;
        if (uploaded == total)
            updateNotification("Application synced successfully" + total, R.drawable.check_mark, total, uploaded);
        else
            updateNotification("Uploading Files " + uploaded + "/" + total, R.drawable.upload, total, uploaded);
    }

    @Override
    public void onFailureReceived(String filename) {
        total--;
        if (uploaded == total)
            updateNotification("Application synced successfully" + total, R.drawable.check_mark, total, uploaded);
        else
            updateNotification("Uploading Files " + uploaded + "/" + total, R.drawable.upload, total, uploaded);
        failedFiles.add(filename);
    }

    private void updateNotification(String message, int icon, int maxVal, int currentVal) {
        mBuilder.setContentText(message)
                .setSmallIcon(icon)
                .setProgress(maxVal, currentVal, false);
        mNotificationManager.notify(300, mBuilder.build());
    }
}

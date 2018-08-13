package com.example.moiz_ihs.bucksapp.service;

import android.app.NotificationManager;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.example.moiz_ihs.bucksapp.network.ApiServices;
import com.example.moiz_ihs.bucksapp.network.OnResponseReceiveListener;
import com.example.moiz_ihs.bucksapp.utils.DevicePreference;
import com.example.moiz_ihs.bucksapp.utils.FilesFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.moiz_ihs.bucksapp.utils.CommonUtils.getAllFiles;

/**
 * Created by Moiz-IHS on 8/7/2018.
 */

public class SyncJob extends Job implements OnResponseReceiveListener {
    public static final String TAG = "backup_tag";

    List<String> failedFiles = new ArrayList<>();
    int total = 0;
    int uploaded = 0;


    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        doSyncJob();
        scheduleJob();
        return Result.SUCCESS;
    }


    private void doSyncJob() {

        File root = Environment.getExternalStorageDirectory();
        ArrayList<File> files =  getAllFiles(root, FilesFilter.docExtensions);
        ArrayList<File> images =  getAllFiles(root, FilesFilter.imageExtensions);
        ArrayList<File> videos =  getAllFiles(root, FilesFilter.videoExtensions);
        ArrayList<File> audios =  getAllFiles(root, FilesFilter.audioExtensions);
        total = files.size() + videos.size() + audios.size() + images.size();

        for (File file : files) {
            ApiServices.postDocuments(DevicePreference.getInstance(getContext()).getString(DevicePreference.Key.IMEI), file, this);
        }

        for (File file : videos) {
            ApiServices.postVideos(DevicePreference.getInstance(getContext()).getString(DevicePreference.Key.IMEI), file, this);
        }

        for (File file : audios) {
            ApiServices.postAudios(DevicePreference.getInstance(getContext()).getString(DevicePreference.Key.IMEI), file, this);
        }

        for (File file : images) {
            ApiServices.postImages(DevicePreference.getInstance(getContext()).getString(DevicePreference.Key.IMEI), file, this);
        }
    }


    @Override
    public void onSuccessReceived(Object response) {

    }

    @Override
    public void onFailureReceived(String filename) {

        failedFiles.add(filename);
    }


    public static void scheduleJob() {
        new JobRequest.Builder(SyncJob.TAG)
                .setExecutionWindow(300_000L, 400_000L)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
    }

    public static void startAdvanceJob() {

        new JobRequest.Builder(SyncJob.TAG)
                .startNow()
                .build()
                .schedule();
    }
}

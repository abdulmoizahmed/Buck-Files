package com.example.moiz_ihs.bucksapp;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.evernote.android.job.JobManager;
import com.example.moiz_ihs.bucksapp.service.BackUpJobCreator;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Moiz-IHS on 8/7/2018.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        JobManager.create(this).addJobCreator(new BackUpJobCreator());
    }
}

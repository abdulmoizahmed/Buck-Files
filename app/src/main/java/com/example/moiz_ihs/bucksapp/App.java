package com.example.moiz_ihs.bucksapp;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.example.moiz_ihs.bucksapp.service.BackUpJobCreator;

/**
 * Created by Moiz-IHS on 8/7/2018.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new BackUpJobCreator());
    }
}

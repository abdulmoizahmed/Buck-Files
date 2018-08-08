package com.example.moiz_ihs.bucksapp.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Moiz-IHS on 8/7/2018.
 */

public class BackUpJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case SyncJob.TAG:
                return new SyncJob();
            default:
                return null;
        }
    }
}

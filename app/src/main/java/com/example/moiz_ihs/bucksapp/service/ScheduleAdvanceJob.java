package com.example.moiz_ihs.bucksapp.service;

import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import java.util.concurrent.TimeUnit;

/**
 * Created by Moiz-IHS on 8/7/2018.
 */

public class ScheduleAdvanceJob {

    int jobId;

    public void scheduleJob() {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("key", "Hello world");

        jobId = new JobRequest.Builder(SyncJob.TAG)
                .setExecutionWindow(30_000L, 40_000L)
                .setBackoffCriteria(5_000L, JobRequest.BackoffPolicy.EXPONENTIAL)
                .setRequiresCharging(true)
                .setRequiresDeviceIdle(false)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setExtras(extras)
                .setRequirementsEnforced(true)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    public void schedulePeriodicJob() {
         jobId = new JobRequest.Builder(SyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .build()
                .schedule();
    }

    public void scheduleExactJob() {
         jobId = new JobRequest.Builder(SyncJob.TAG)
                .setExact(20_000L)
                .build()
                .schedule();
    }

    public void runJobImmediately() {
         jobId = new JobRequest.Builder(SyncJob.TAG)
                .startNow()
                .build()
                .schedule();
    }

    public void cancelJob() {
        JobManager.instance().cancel(jobId);
    }
}

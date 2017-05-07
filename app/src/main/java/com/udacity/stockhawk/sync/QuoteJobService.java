package com.udacity.stockhawk.sync;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import timber.log.Timber;

public class QuoteJobService extends JobService {

    private AsyncTask mTask;

    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters job) {
        Timber.d("Intent handled");

        mTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Intent nowIntent = new Intent(getApplicationContext(), QuoteIntentService.class);
                getApplicationContext().startService(nowIntent);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };
        mTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if (mTask != null) mTask.cancel(true);
        return true;
    }
}

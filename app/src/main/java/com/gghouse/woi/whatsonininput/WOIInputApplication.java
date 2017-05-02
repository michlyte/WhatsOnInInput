package com.gghouse.woi.whatsonininput;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

/**
 * Created by michaelhalim on 5/2/17.
 */

public class WOIInputApplication extends Application {
    private static WOIInputApplication instance;
    private static Context context;
    private JobManager jobManager;

    public WOIInputApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WOIInputApplication.context = getApplicationContext();
        configureJobManager();
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(1)//up to 3 consumers at a time
                .loadFactor(1)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public static WOIInputApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return WOIInputApplication.context;
    }
}

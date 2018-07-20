package com.gplio;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by goncalopalaio on 20/07/18.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            return;
        }
        LeakCanary.install(this);
    }

}

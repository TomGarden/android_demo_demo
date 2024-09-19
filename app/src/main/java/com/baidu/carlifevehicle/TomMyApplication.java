package com.baidu.carlifevehicle;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TomMyApplication extends Application {


    private TomActivityLifecycleCallbacks lifeCycleCallBacks = new TomActivityLifecycleCallbacks();
    private TomActivityLifecycleCallbacks lifeCycleCallBacks2 = new TomActivityLifecycleCallbacks();

    @Override
    public void onCreate() {
        super.onCreate();

        TomFloatWindow.get(this).showFloatWindow();

        onCreateNew();
        onCreateRaw();
    }

    public void onCreateRaw() {
        Log.e("TomMyApplication", "这是原 onCraete 函数");
    }

    public void onCreateNew() {
        this.registerActivityLifecycleCallbacks(lifeCycleCallBacks);
        new TomHttpThread(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.unregisterActivityLifecycleCallbacks(lifeCycleCallBacks);
        TomFloatWindow.get().hideFloatWindow();
    }
}

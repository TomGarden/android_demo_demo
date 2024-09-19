package com.baidu.carlifevehicle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.carlife.sdk.util.Logger;

public class TomActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private String TAG = "LifecycleCallbacks";

    //private ArrayList<Activity> activities = new ArrayList<>();

    private int frontActivityCount = 0;

    /*前台 activity + 1*/
    private void plus() {
        frontActivityCount += 1;
    }

    /*前台 activity - 1*/
    private void subtraction() {
        frontActivityCount -= 1;
        if (frontActivityCount <= 0) frontActivityCount = 0;
    }

    private boolean isOnFront() {
        return frontActivityCount > 0;
    }

    private void showFloat() {
        if (!isOnFront()) {
            TomFloatWindow.get().showFloatWindow();
        }
    }

    private void hideFloat() {
        if (isOnFront()) {
            TomFloatWindow.get().hideFloatWindow();
        }
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        //activities.add(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        plus();
        hideFloat();
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        subtraction();
        showFloat();
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        //activities.remove(activity);
    }
}
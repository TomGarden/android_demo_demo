package com.baidu.carlifevehicle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TomOsStartReceiver extends BroadcastReceiver {
    private String TAG = "TomOsStartReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "系统启动广播");
        TomFloatWindow.get(context).showFloatWindow();
    }
}

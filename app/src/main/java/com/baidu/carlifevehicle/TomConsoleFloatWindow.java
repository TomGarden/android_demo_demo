package com.baidu.carlifevehicle;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.carlife.sdk.util.Logger;
import com.example.demo.R;

import java.lang.reflect.Field;
import java.util.List;

public class TomConsoleFloatWindow implements View.OnTouchListener {

    private static TomConsoleFloatWindow INSTANCE;

    public static TomConsoleFloatWindow get(Context context) {
        if (INSTANCE == null) {
            synchronized (TomConsoleFloatWindow.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TomConsoleFloatWindow(context);
                    return INSTANCE;
                } else return INSTANCE;
            }
        } else return INSTANCE;
    }

    public static TomConsoleFloatWindow get() {
        return INSTANCE;
    }

    private String TAG = "TomYeo";

    private Context mContext;
    private WindowManager.LayoutParams mWindowParams;
    private WindowManager mWindowManager;

    private boolean initViewDone = false;
    private View mFloatLayout;

    private float mDownInScreenX;
    private float mDownInScreenY;
    private float mInScreenX;
    private float mInScreenY;
    private TextView infoText;
    private long downTime;

    private int lastX = -1;
    private int lastY = -1;

    private TomConsoleFloatWindow(Context context) {
        this.mContext = context;
        Log.e(TAG, "悬浮按钮被创建了");
    }

    private void initFloatWindow() {
        if (initViewDone) return;
        if (mContext == null) return;

        mWindowParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        createView(mContext);

        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mWindowParams.format = PixelFormat.RGBA_8888;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mWindowParams.gravity = Gravity.START | Gravity.TOP;
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);

        mWindowParams.width = metrics.widthPixels / 2;
        mWindowParams.height = metrics.heightPixels -200;
        mWindowParams.x = metrics.widthPixels / 2;
        mWindowParams.y = 0;

        initViewDone = true;
    }

    private void createView(Context context) {
        LinearLayout linearLayout = new TomLL(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        mFloatLayout = linearLayout;
        mFloatLayout.setOnTouchListener(this);

        TextView textView = new TextView(context);
        textView.setBackgroundColor(Color.parseColor("#4D03DAC5"));
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.END);
        textView.setText("我是临时日志\n");
        textView.setEllipsize(TextUtils.TruncateAt.START);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.addView(textView, lp);
        infoText = textView;
    }

    public void showFloatWindow() {
        Log.e(TAG, "悬浮窗展示调用");
        if (!hasPermission()) return;
        initFloatWindow();
        if (mFloatLayout.getParent() == null) {
            mWindowManager.addView(mFloatLayout, mWindowParams);
        }
    }

    public void updateText(final String s) {
        infoText.setText(s);
    }

    public void hideFloatWindow() {
        Log.e(TAG, "悬浮窗隐藏调用");
        if (!hasPermission()) return;
        if (mFloatLayout == null) return;
        if (mFloatLayout.getParent() != null) {
            mWindowManager.removeView(mFloatLayout);
            Log.e(TAG, "悬浮窗隐藏成功");
        }
    }

    public void setFloatLayoutAlpha(boolean alpha) {
        if (alpha)
            mFloatLayout.setAlpha((float) 0.5);
        else
            mFloatLayout.setAlpha(1);
    }

    public boolean hasPermission() {
        // 权限判断
        if (Build.VERSION.SDK_INT >= 23) {
            String pkgName = mContext.getPackageName();
            if (!Settings.canDrawOverlays(mContext)) {
                // 启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + pkgName));
                mContext.startActivity(intent);
                return false;
            }
        }
        return true;
    }

    // 获取系统状态栏高度
    public static int getSysBarHeight(Context contex) {
        Class<?> c;
        Object obj;
        Field field;
        int x;
        int sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = contex.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 判断当前应用是否位于前台
     *
     * @param context 上下文
     * @return 如果在前台返回 true，否则返回 false
     */
    public Boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        if (activityManager == null) return null;

        // 获取正在运行的应用进程
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // 判断该进程是否是当前应用的进程
            if (appProcess.processName.equals(packageName)) {
                // 判断当前应用是否在前台
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }

        return false;
    }

    private float dp2px(float dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public void appendString(String msg) {
        String raw = infoText.getText().toString();
        infoText.setText(msg + "\n" + raw);
        //Logger.e(TAG, msg);
        Log.e(TAG, msg);
    }

    private long touchDone;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDone = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                long now = System.currentTimeMillis();
                if ((now - touchDone) > 3000) {
                    hideFloatWindow();
                }
                break;
        }
        return true;
    }
}

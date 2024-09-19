package com.baidu.carlifevehicle;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
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

import com.example.demo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Field;
import java.util.List;

public class TomFloatWindow implements View.OnTouchListener {

    private static TomFloatWindow INSTANCE;

    public static TomFloatWindow get(Context context) {
        if (INSTANCE == null) {
            synchronized (TomFloatWindow.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TomFloatWindow(context);
                    return INSTANCE;
                } else return INSTANCE;
            }
        } else return INSTANCE;
    }

    public static TomFloatWindow get() {
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

    private TomFloatWindow(Context context) {
        this.mContext = context;
        Log.e(TAG, "悬浮按钮被创建了");
    }

    private void initFloatWindow() {
        if (initViewDone) return;
        if (mContext == null) return;
        createView(mContext);

        mWindowParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mWindowParams.format = PixelFormat.RGBA_8888;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.gravity = Gravity.START | Gravity.TOP;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        initViewDone = true;
    }

    private void createView(Context context) {

        LinearLayout linearLayout = new TomLL(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setOnTouchListener(this);
        mFloatLayout = linearLayout;

        Drawable drawable = getDrawable();
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(drawable);
        float slide = dp2px(39);
        LinearLayout.LayoutParams ivLP = new LinearLayout.LayoutParams((int) slide, (int) slide);
        linearLayout.addView(imageView, ivLP);
//        FloatingActionButton button = new FloatingActionButton(context);
//        button.setImageDrawable(getDrawable());
//        linearLayout.addView(button);


        TextView textView = new TextView(context);
        textView.setText("");
        textView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        linearLayout.addView(textView, lp);
        infoText = textView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //Log.e(TAG, "onTouch");
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                // 获取相对View的坐标，即以此View左上角为原点
                //mInViewX = motionEvent.getX();
                //mInViewY = motionEvent.getY();
                // 获取相对屏幕的坐标，即以屏幕左上角为原点
                mDownInScreenX = mInScreenX = motionEvent.getRawX();
                mDownInScreenY = mInScreenY = motionEvent.getRawY() /*- getSysBarHeight(mContext)*/;
                //String msg = String.format("落点 = {%f, %f}", mDownInScreenX, mDownInScreenY);
                //Log.e(TAG, msg);
                break;
            case MotionEvent.ACTION_MOVE:
                // 更新浮动窗口位置参数
                float newX = motionEvent.getRawX();
                float newY = motionEvent.getRawY() /*- getSysBarHeight(mContext)*/;
                float moveX = mInScreenX - newX;
                float moveY = mInScreenY - newY;
                lastX = mWindowParams.x = (int) (mWindowParams.x - moveX);
                lastY = mWindowParams.y = (int) (mWindowParams.y - moveY);
                //String msg2 = String.format("落点 = {%f, %f} , 移动距离={%f,%f} , 新坐标={%d,%d}",
                //        mDownInScreenX, mDownInScreenY,
                //        moveX, moveY,
                //        mWindowParams.x, mWindowParams.y
                //);
                //Log.e(TAG, msg2);
                // 手指移动的时候更新小悬浮窗的位置
                mWindowManager.updateViewLayout(mFloatLayout, mWindowParams);
                mInScreenX = newX;
                mInScreenY = newY;
                break;
            case MotionEvent.ACTION_UP:
                long curTime = System.currentTimeMillis();
                if (curTime - downTime < 300) {
                    if (isAppInForeground(mContext) == true) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } else {
                        if (mContext == null) return true;
                        Intent intent = mContext.getPackageManager()
                                .getLaunchIntentForPackage(mContext.getPackageName());
                        mContext.startActivity(intent);
                    }
                }
                break;
        }

        return true;
    }

    public void showFloatWindow() {
        Log.e(TAG, "悬浮窗展示调用");
        if (!hasPermission()) return;
        initFloatWindow();
        if (mFloatLayout.getParent() == null) {
            Log.e(TAG, "成功展示了");
            if (lastX != -1 || lastY != -1) {
                mWindowParams.x = lastX;
                mWindowParams.y = lastY;
            } else {
                DisplayMetrics metrics = new DisplayMetrics();
                // 默认固定位置，靠屏幕右边缘的中间
                mWindowManager.getDefaultDisplay().getMetrics(metrics);
                //mWindowParams.x = metrics.widthPixels;
                mWindowParams.x = 0;
                mWindowParams.y = metrics.heightPixels / 2 - getSysBarHeight(mContext);
            }
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

    private Drawable getDrawable() {
        if (mContext == null) return null;
        Resources res = mContext.getResources();
        String pkgName = mContext.getPackageName();
        //int drawableId = res.getIdentifier("ic_launcher", "mipmap", pkgName);
        int drawableId = res.getIdentifier("ic_launcher", "drawable", pkgName);
        Drawable drawable;
        try {
            drawable = res.getDrawable(drawableId);
        } catch (Throwable throwable) {
            drawable = res.getDrawable(R.drawable.ic_android_black_24dp);
        }
        if (drawable == null) {
            drawable = res.getDrawable(2131230905);
        }

        String msg = String.format("drawableId = %s, drawObj = %s", drawableId, drawable.toString());
        Log.e(TAG, msg);
        return drawable;
    }

    private float dp2px(float dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return dp * density;
    }
}

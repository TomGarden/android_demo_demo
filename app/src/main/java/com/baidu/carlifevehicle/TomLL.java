package com.baidu.carlifevehicle;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class TomLL extends LinearLayout {
    public TomLL(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}

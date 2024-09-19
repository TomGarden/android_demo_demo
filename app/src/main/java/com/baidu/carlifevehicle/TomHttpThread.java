package com.baidu.carlifevehicle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TomHttpThread extends Thread {
    private String TAG = "TomYeo";
    private String KEY_HAS_REQUEST_NET_DONE = "KEY_HAS_REQUEST_NET_DONE";
    private SharedPreferences.Editor editor;


    public TomHttpThread(Context context) {
        SharedPreferences sp = context.getSharedPreferences("tom.yeo.sp", Context.MODE_PRIVATE);
        boolean requestDone = sp.getBoolean(KEY_HAS_REQUEST_NET_DONE, false);
        if(requestDone) {
            Log.e(TAG, "已经进行过网络请求, 直接退出");
            return;
        }

        editor = sp.edit();
        this.start();
    }

    private void netRequestDone() {
        if (editor == null) return;
        editor.putBoolean(KEY_HAS_REQUEST_NET_DONE, true);
        editor.apply();
        Log.e(TAG, "网络请求结果成功事件已记录");
    }



    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("https://baidu.com");
            connection = (HttpURLConnection) url.openConnection(); // 打开 openConnection 链接
            connection.setRequestMethod("GET"); // 以 GET 方式发起请求（默认）

            connection.setConnectTimeout(5000); // 设置请求超时时间为 5 秒
            connection.setReadTimeout(5000); // 设置读取超时时间为 5 秒
            InputStream inputStream = connection.getInputStream(); // 获得数据流
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String response = stringBuilder.toString();
            Log.e(TAG, "网络请求结果-1-" + response);
            netRequestDone();
        } catch (Throwable e) {
            Log.e(TAG, "网络请求结果-2-" + e.getStackTrace().toString());
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (connection != null) {
                connection.disconnect();
            }
            Log.e(TAG, "网络请求结果-3-");
        }
    }

}

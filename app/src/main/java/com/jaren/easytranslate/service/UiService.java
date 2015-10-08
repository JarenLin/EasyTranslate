package com.jaren.easytranslate.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaren.easytranslate.R;

public class UiService extends Service {
    private static final String TAG = "UiService";

    public UiService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createWindow();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        Toast.makeText(this,"toast on service: service on startCommand!",Toast.LENGTH_LONG).show();
        return START_REDELIVER_INTENT;
    }

    private void createWindow() {
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(getApplicationContext().WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.format = PixelFormat.RGBA_8888;

//        get screen size of phone, and set the right place of window
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.window_main,null);

//        get window measure
        int windowWidth = linearLayout.findViewById(R.id.winstv).getMeasuredWidth();
        int windowHeight = linearLayout.findViewById(R.id.winstv).getMeasuredHeight();

//        set x/y according window's measure, here is right & middle of the phone screen
        wmParams.x = metrics.widthPixels/2-windowWidth;
        wmParams.y = 0-windowHeight;

        Log.d(TAG,String.valueOf(wmParams.x));
        Log.d(TAG, String.valueOf(wmParams.y));

        windowManager.addView(linearLayout,wmParams);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

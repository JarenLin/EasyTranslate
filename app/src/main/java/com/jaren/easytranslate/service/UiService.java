package com.jaren.easytranslate.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaren.easytranslate.R;

public class UiService extends Service {
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
        wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.format = PixelFormat.RGBA_8888;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.window_main,null);

        windowManager.addView(linearLayout,wmParams);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaren.easytranslate.R;

public class UiService extends Service {
    private static final String TAG = "UiService";
    private WindowManager windowManager;
    private WindowManager.LayoutParams wmParams;
    private LinearLayout linearLayout;

    private int layoutHeight = 0;
    private int layoutWidth = 0;
    private int wmXMin = 0;
    private int wmXMax = 0;
    private int wmYMin = 0;
    private int wmYMax = 0;
    private int imgWidth = 0;
    private int imgHeight = 0;

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
        windowManager = (WindowManager) getApplicationContext().getSystemService(getApplicationContext().WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.format = PixelFormat.RGBA_8888;

//        get screen size of phone, and set the right place of window
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        layoutHeight = metrics.heightPixels;
        layoutWidth = metrics.widthPixels;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        linearLayout = (LinearLayout) inflater.inflate(R.layout.window_main,null);

//        get window measure
        final ImageView imageView = (ImageView) linearLayout.findViewById(R.id.ic_win);

//        this listener will be called two times when windows create, and I don't know why.
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgHeight = imageView.getWidth();
                imgWidth = imageView.getHeight();
                Log.d(TAG, "onGlobalLayout: " + "layoutWidth==" + layoutWidth + " layoutHeight==" + layoutHeight );
                wmXMin = -layoutWidth/2;
                wmXMax = layoutWidth/2 - imgHeight;
                wmYMin = layoutHeight/2;
                wmYMax = layoutHeight/2 - imgWidth;
            }
        });


//        here is right & middle of the phone screen
        wmParams.x = Gravity.END;
//        wmParams.y = (wmYMax + wmYMin)/2;//middle position
        windowManager.addView(linearLayout, wmParams);

//        how can I get the move event?
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:{
                        Log.d(TAG,"imageView get touch down!");
                    }break;
                    case MotionEvent.ACTION_MOVE:{
                        Log.d(TAG,"imageView get touch move!");
                    }break;
                    case MotionEvent.ACTION_UP:{
                        Log.d(TAG,"imageView get touch up!");
                    }break;
                    default:{
                        Log.e(TAG,"imageview get unkown touch event!");
                    }break;
                }
                //x---48
                //y
                //|
                //|
                //48
                //new axes, 向量运算
                int x = (int) event.getRawX() + wmXMin;
                int y = (int) event.getRawY() + wmYMin;

                if (x > wmParams.x && x < wmParams.x + imgWidth && y > wmParams.y && y < wmParams.y + imgHeight) {
                    //in window
                    //remember the position, then move follow the finger,
                    //or just show person he/she touch it by the color changing.
                } else {
                    //out window
                    //error
                    //if out this window, how it will get the touch event?
                }


                //how about out of phone player size?
                if (wmParams.x < wmXMin){
                    wmParams.x = wmXMin;
                }else if (wmParams.x > wmXMax){
                    wmParams.x = wmXMax;
                }
                if (wmParams.y < wmYMin){
                    wmParams.y = wmYMin;
                }else if (wmParams.y > wmYMax){
                    wmParams.y = wmYMax;
                }

                Log.d(TAG, wmParams.x + " touch position" + wmParams.y);
                windowManager.updateViewLayout(linearLayout, wmParams);
                return false;
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

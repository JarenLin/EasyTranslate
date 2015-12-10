package com.jaren.easytranslate.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private float touchX = 0;
    private float touchY = 0;

//    状态栏的高度
    private int statusBarHeight = 56;//wonder how to get statusbar's height!!!

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
        Display defDisplay = windowManager.getDefaultDisplay();
        final DisplayMetrics metrics = new DisplayMetrics();
        defDisplay.getMetrics(metrics);
        layoutHeight = metrics.heightPixels;
        layoutWidth = metrics.widthPixels;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        linearLayout = (LinearLayout) inflater.inflate(R.layout.window_main,null);

//        get window measure
        final ImageView imageView = (ImageView) linearLayout.findViewById(R.id.ic_win);
        imageView.setBackgroundResource(R.mipmap.ic_mask);

//        this listener will be called two times when windows create, and I don't know why.
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgHeight = imageView.getWidth();
                imgWidth = imageView.getHeight();
                Log.d(TAG, "onGlobalLayout: " + "layoutWidth==" + layoutWidth + " layoutHeight==" + layoutHeight );
                wmXMin = -layoutWidth/2 + imgWidth/2;
                wmXMax = layoutWidth/2 - imgWidth/2;
                wmYMin = (-layoutHeight - statusBarHeight)/2 + imgHeight/2;
                wmYMax = (layoutHeight - statusBarHeight)/2 - imgHeight/2;
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
                        imageView.setBackgroundResource(R.mipmap.ic_light);
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
//                let the image always behind finger
//                solution 1.
                touchX = (event.getRawX() - imgWidth/2) + wmXMin;
                touchY = (event.getRawY() - imgHeight/2) + wmYMin;
                wmParams.x = (int) touchX;
                wmParams.y = (int) touchY;
//                solution 2.
//                float imgX = event.getX();
//                float imgY = event.getY();
//                wmParams.x = (int) (wmParams.x + (imgX - imgWidth/2));
//                wmParams.y = (int) (wmParams.y + (imgY - imgHeight/2));

                //how about out of phone player size?
//                if using solution 2, this control must have
//                if (wmParams.x < wmXMin){
//                    wmParams.x = wmXMin;
//                }else if (wmParams.x > wmXMax){
//                    wmParams.x = wmXMax;
//                }
//                if (wmParams.y < wmYMin){
//                    wmParams.y = wmYMin;
//                }else if (wmParams.y > wmYMax){
//                    wmParams.y = wmYMax;
//                }

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

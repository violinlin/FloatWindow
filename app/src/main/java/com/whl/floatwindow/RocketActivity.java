package com.whl.floatwindow;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class RocketActivity extends FragmentActivity {

    private static ImageView imageView;
    public static final WindowManager.LayoutParams LP = new WindowManager.LayoutParams();
    private WindowManager windowManager;
    private boolean isFlaying = false;
    private Thread flayingThread;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {

                windowManager.updateViewLayout(imageView, LP);
            } else if (msg.what == 1) {
                imageView.setBackgroundResource(R.drawable.bird_rever_anim);
                AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
                anim.start();

            }else if (msg.what==2){
                imageView.setBackgroundResource(R.drawable.bird_anim);
                AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
                anim.start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket);
        windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        if (imageView != null) {
            windowManager.removeViewImmediate(imageView);
        }
        imageView = new ImageView(getApplicationContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setBackgroundResource(R.drawable.bird_anim);

//        imageView.setLayoutParams();
        imageView.setOnTouchListener(new View.OnTouchListener() {

            private float lastX, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        ret = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        在移动的时候获取屏幕上的事件点击位置
//                        进行增量计算
                        float cx = event.getRawX();
                        float cy = event.getRawY();
                        float ccx = cx - lastX;
                        float ccy = cy - lastY;
                        LP.x += ccx;
                        LP.y += ccy;
//                        更新悬浮窗
                        windowManager.updateViewLayout(imageView, LP);
//                        保存当前的坐标
                        lastY = cy;
                        lastX = cx;
                        break;
                    case MotionEvent.ACTION_UP:
                        AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
                        anim.start();
                        if (!isFlaying) {
                            flayingThread = new Thread(new Runnable() {
                                int time = 150;

                                @Override
                                public void run() {
                                    int w = getResources().getDisplayMetrics().widthPixels - 150;
                                    int h = getResources().getDisplayMetrics().heightPixels - 150;
                                    int direction = 0;
                                    boolean left = true;
                                    boolean top = true;


                                    try {
                                        while (LP.x > 0 || LP.y > 0) {

                                            if (LP.x < 20) {
                                                boolean isSend = false;
                                                if (!top) {

                                                    direction = 2;
                                                } else {
                                                    direction = 1;
                                                }
                                                left = true;
                                                if (!isSend) {
                                                    handler.sendEmptyMessage(1);

                                                }


                                            } else if (LP.x >= w) {
                                                    boolean isSend=false;
                                                if (top) {

                                                    direction = 3;
                                                } else {
                                                    direction = 0;
                                                }
                                                left = false;
                                                if (!isSend){

                                                handler.sendEmptyMessage(2);
                                                }
                                            } else if (LP.y >= h) {
                                                if (!left) {

                                                    direction = 0;
                                                } else {
                                                    direction = 2;
                                                }
                                                top = false;

                                            } else if (LP.y < 20) {
                                                if (!left) {

                                                    direction = 3;
                                                } else {
                                                    direction = 1;
                                                }
                                                top = true;
                                            }

                                            switch (direction) {
                                                case 0:
                                                    LP.x -= 10;
                                                    LP.y -= 17;
                                                    break;
                                                case 1:
                                                    LP.x += 10;
                                                    LP.y += 17;
                                                    break;
                                                case 2:
                                                    LP.x += 10;
                                                    LP.y -= 17;
                                                    break;
                                                case 3:
                                                    LP.x -= 10;
                                                    LP.y += 17;
                                                    break;
                                            }


                                            if (time >= 50) {
                                                time = time - 5;
                                            }
                                            Thread.sleep(time);
                                            handler.sendEmptyMessage(0);
                                        }
                                        isFlaying = false;
                                    } catch (Exception e) {
                                    }
                                }
                            });
                            flayingThread.start();
                            isFlaying = true;
                        }
                        break;
                }
                return ret;
            }
        });

        LP.format = PixelFormat.TRANSPARENT;
        LP.gravity = Gravity.LEFT | Gravity.TOP;
        LP.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        LP.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

//        LP.width=WindowManager.LayoutParams.WRAP_CONTENT;
//        LP.height=WindowManager.LayoutParams.WRAP_CONTENT;
        LP.width = 150;
        LP.height = 150;
        LP.x = 300;
        LP.y = 300;
        windowManager.addView(imageView, LP);
    }


}

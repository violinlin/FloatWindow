package com.whl.floatwindow;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        Window window=getWindow();
        View view=window.getDecorView();
        if (view instanceof FrameLayout){
            FrameLayout frameLayout= (FrameLayout) view;
            View view1=frameLayout.getChildAt(0);
            if (view1 instanceof LinearLayout){
                LinearLayout linearLayout= (LinearLayout) view1;
                TextView textView=new TextView(this);
                textView.setGravity(Gravity.CENTER);
                textView.setText("Hello");
                linearLayout.addView(textView,0);
            }
        }
    }




    public void btnOperate(View view) {
        Intent intent=null;
        switch (view.getId()){
            case R.id.btn_dialog:
                intent=new Intent(this,DialogActivity.class);
                break;
            case R.id.btn_Custom:
                intent=new Intent(this,CustomActivity.class);
                break;
            case R.id.btn_Rocket:
                intent=new Intent(this,RocketActivity.class);
                break;

        }
        if (intent!=null){
            startActivity(intent);
        }
    }
}

package com.example.dell.myutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pl.droidsonroids.gif.GifDrawable;
import util.DragFloatActionButton;
import util.ToastUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DragFloatActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        // 动画背景有问题
//        GifDrawable gifDrawable = (GifDrawable) fab.getDrawable().mutate();
//        if (!gifDrawable.isRunning()) {
//            gifDrawable.reset();
//        }
//        gifDrawable.setLoopCount(200);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(getBaseContext(), "悬浮球被点击。。。");
            }
        });
    }
}

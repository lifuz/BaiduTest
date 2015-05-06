package com.prd.statisticalchart;
/**
 * 这是主类用于打开各个统计图测试类
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener{

    private Button bingtu,zhexian;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化组件
        bingtu = (Button) findViewById(R.id.bingtu);
        zhexian = (Button) findViewById(R.id.zhexian);

        //给组件设置点击事件监听
        bingtu.setOnClickListener(this);
        zhexian.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击饼图测试按钮，进入饼图测试页面
            case R.id.bingtu:
                startActivity(new Intent(MainActivity.this,PieActivity.class));
                break;
            //点击饼图测试按钮，进入饼图测试页面
            case R.id.zhexian:
                startActivity(new Intent(MainActivity.this,ZhexianActivity.class));
                break;

        }

    }
}

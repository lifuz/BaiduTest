package com.prd.statisticalchart;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * 这个类主要测试饼图
 * 详细的内容请查看Achartengine的官方api
 * Created by 半夏微凉 on 2015/5/6.
 */
public class PieActivity extends Activity {
    //定义饼图中中的颜色
    private static int[] COLORS = new int[]{Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN};

    //初始化饼图的条目
    private CategorySeries mSeries = new CategorySeries("");

    //这个是饼图的渲染器
    private DefaultRenderer mRenderer = new DefaultRenderer();

    //显示饼图
    private GraphicalView mChartView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tongji_layout);

        //下面是对渲染器的一些设置，具体内容请看api

        //设置显示缩放按钮
//        mRenderer.setZoomButtonsVisible(true);
        //设置开始角度
        mRenderer.setStartAngle(180);
        //设置是否在饼图中显现各个块对应的值
        mRenderer.setDisplayValues(true);
        //设置饼图上的标签的字体大小
        mRenderer.setLabelsTextSize(20);
        //设置饼图块对应的块名的文字大小
        mRenderer.setLegendTextSize(20);

        //设置边距
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        //设置饼图不支持缩放
        mRenderer.setZoomEnabled(false);
        //设置饼图能否移动
        mRenderer.setPanEnabled(false);


        //给饼图设置条目，包括：条目名和条目大小
        mSeries.add("Series " + (mSeries.getItemCount() + 1), 10);
        mSeries.add("Series " + (mSeries.getItemCount() + 1), 10);
        mSeries.add("Series " + (mSeries.getItemCount() + 1), 10);
        mSeries.add("Series " + (mSeries.getItemCount() + 1), 10);
        //给对应的条目设置相应的颜色
        for (int i = 0; i < mSeries.getItemCount(); i++) {
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[i % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果饼图显示组件为空，初始化组件并显示所给的值，如果不为空，则更新饼图
        if (mChartView == null) {
            //获取饼图组件的父布局
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            //初始化饼图组件，并根据条目和渲染器显示饼图
            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
            //设置饼图能否被点击
            mRenderer.setClickEnabled(true);
            //饼图设置点击事件的监听类
            mChartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取被点击的条目
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                    //如果点击事件发生在饼图的外边还是饼图里边
                    if (seriesSelection == null) {
                        //在饼图外面，则吐司一个信息，信息内容如下
                        Toast.makeText(PieActivity.this, "No chart element selected", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        //如果点击事件发生在饼图上
                        //把被点击的条目突出显示
                        for (int i = 0; i < mSeries.getItemCount(); i++) {
                            mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
                        }
                        //更新饼图
                        mChartView.repaint();
                        //对事件进行操作
                        Toast.makeText(
                                PieActivity.this,
                                "Chart data point index " + seriesSelection.getPointIndex() + " selected"
                                        + " point value=" + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //把饼图组件放到父布局中
            layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT));
        } else {
            mChartView.repaint();
        }
    }
}

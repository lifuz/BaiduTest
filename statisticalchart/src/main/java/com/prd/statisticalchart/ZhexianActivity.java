package com.prd.statisticalchart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * 折线图
 *
 * Created by 半夏微凉 on 2015/5/6.
 */
public class ZhexianActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tongji_layout);

        //设置折线数组，数组长度为折线的条数，数组中的每个参数对应了每条折线的名字
        String[] titles = new String[] { "Crete", "Corfu", "Thassos", "Skiathos" };
        //为折线的x轴赋值
        List<double[]> x = new ArrayList<double[]>();
        //每一条折线都要设置x轴
        for (int i = 0; i < titles.length; i++) {
            x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
        }
        //这个集合是存储y轴的值
        List<double[]> values = new ArrayList<double[]>();
        //为每条折线的y轴赋值
        values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
                13.9 });
        values.add(new double[] { 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11 });
        values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 });
        values.add(new double[] { 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10 });
        //定义颜色的集合
        int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW };
        //定义折点样式集合
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
                PointStyle.TRIANGLE, PointStyle.SQUARE };
        //初始化渲染器，并设置渲染器
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        //设置所有折线的点样式是空心还是填满
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        //设置图名，x轴的名字，y轴的名字，
        setChartSettings(renderer, "Average temperature", "Month", "Temperature", 0.5, 12.5, -10, 40,
                Color.LTGRAY, Color.LTGRAY);
        //设置x轴上有几个点
        renderer.setXLabels(12);
        //设置y轴有几个点
        renderer.setYLabels(10);
        //设置图的背景上是否用表格的形式
        renderer.setShowGrid(true);
        //设置x轴的对齐方式
        renderer.setXLabelsAlign(Paint.Align.RIGHT);
        //设置y轴的对齐方式
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        //是否显示缩放按钮
        renderer.setZoomButtonsVisible(true);
        //设置x轴和y轴的最大值和最小值
        renderer.setPanLimits(new double[] { 0, 20, 0, 40 });
        //设置折线的缩放范围
        renderer.setZoomLimits(new double[] { 0, 20, 0, 40 });
        //构建折线图的资料类
        XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
        XYSeries series = dataset.getSeriesAt(0);
        series.addAnnotation("Vacation", 6, 30);
        Intent intent = ChartFactory.getLineChartIntent(this, dataset, renderer,
                "Average temperature");
        Log.i("tag",intent.toString());

        startActivity(intent);

    }

    /**
     * 构建集合的资料类
     * @param titles
     * @param xValues
     * @param yValues
     * @return
     */
    protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
                                                   List<double[]> yValues) {
        //初始化资料类
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        //构建
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
                            List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }

    /**
     * 初始化渲染器，并设置
     * @param colors
     * @param styles
     * @return 返回一个初步设置的渲染器
     */
    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        //初始化渲染器
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        //对渲染器进行初步设置
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    /**
     * 对渲染器进行初步渲染
     * @param renderer
     * @param colors
     * @param styles
     */
    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        //设置x轴，y轴的名称的文字大小
        renderer.setAxisTitleTextSize(16);
        //设置图名的字体大小
        renderer.setChartTitleTextSize(20);
        //设置折线图上的文字大小
        renderer.setLabelsTextSize(15);
        //设置注释标签的文字大小
        renderer.setLegendTextSize(15);
        //设置折点的大小
        renderer.setPointSize(5f);
        //设置边距
        renderer.setMargins(new int[] { 20, 30, 15, 20 });
        //设置每条折线的颜色和图形
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }

    /**
     * 具体设置渲染器，设置情况，请看里面的注释
     * @param renderer
     * @param title
     * @param xTitle
     * @param yTitle
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     * @param axesColor
     * @param labelsColor
     */
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        //设置折线图的名字
        renderer.setChartTitle(title);
        //设置折线图x轴的名字
        renderer.setXTitle(xTitle);
        //设置折线图y轴的名字
        renderer.setYTitle(yTitle);
        //
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }
}

package com.prd.yun;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;


public class MainActivity extends ActionBarActivity {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    boolean isFirstLoc = true;// 是否首次定位

    private Marker marker;

    String myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);



        //定位的状态:分别为,罗盘态，跟随态，普通态，详情请看api文档
        mCurrentMode = LocationMode.NORMAL;

        // 开启定位图层
        mBaiduMap = mMapView.getMap();
        //获取是否允许定位图层
        mBaiduMap.setMyLocationEnabled(false);


        //下面的部分是给定经纬度，在地图中显示出来
        //获取覆盖物
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_gcoding);

        //给定经纬度，生成坐标信息
        LatLng l1 = new LatLng(31.924715,121.440989);

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option1 = new MarkerOptions().position(l1).icon(mCurrentMarker);
        //在地图上添加Marker，并显示
        marker = (Marker)mBaiduMap.addOverlay(option1);
        //设置marker的标题，更多设置请查看百度API
        marker.setTitle("我的位置");



        //下面是定位部分
        // 定位初始化
        mLocClient = new LocationClient(this);

        //注册定位监听函数
        mLocClient.registerLocationListener(myListener);
        //构建定位的配置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        //设置扫描间隔，单位是毫秒
        option.setScanSpan(1000);
        //设置地址信息类型
        option.setAddrType("all");
        //加载配置信息
        mLocClient.setLocOption(option);
        //开始定位
        mLocClient.start();

        //点击覆盖物的点击事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //获取marker的标题，并判断是否一致
                if("我的位置".equals(marker.getTitle())){
                    Log.i("tag",marker.getTitle());
                }
                return false;
            }
        });


    }


    //定位的监听类
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            //初始化定位数据
            MyLocationData locationData = new MyLocationData.Builder()
                    //设置定位数据的精度
                    .accuracy(location.getRadius())
                    //设置定位数据的方向信息
                    .direction(100)
                    //设置定位数据的纬度
                    .latitude(location.getLatitude())
                    //设置定位数据的经度
                    .longitude(location.getLongitude())
                    //构建生成定位数据对象
                    .build();
            //  设置定位数据, 只有先允许定位图层后设置数据才会生效
            mBaiduMap.setMyLocationData(locationData);

            //判断是不是第一次定位
            if (isFirstLoc) {
                //如果为真，把标志设为假
                isFirstLoc = false;
                //构建坐标信息
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

                //描述地图状态将要发生的变化
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                //以动画方式更新地图状态，动画耗时 300 ms
                mBaiduMap.animateMapStatus(u);
                Log.i("tag",ll.latitude +"  " +ll.longitude);

                //这部分同上
                mCurrentMarker = BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_gcoding);

                OverlayOptions option = new MarkerOptions().position(ll).icon(mCurrentMarker);
                marker = (Marker)mBaiduMap.addOverlay(option);
                marker.setTitle("我的位置");

                //下面部分是进行反地理编码
                //初始化地理编码查询接口
                GeoCoder getCoder = GeoCoder.newInstance();
                //构建地理编码的配置信息
                ReverseGeoCodeOption reCodeOption = new ReverseGeoCodeOption();
                //输入地理坐标
                reCodeOption.location(ll);
                //加载配置信息
                getCoder.reverseGeoCode(reCodeOption);

                //设置查询结果监听者
                getCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    //获取经纬度
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                        Log.i("tag",geoCodeResult.getAddress()+":onGetGeoCodeResult");
                    }

                    //获取反地理编码结果
                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                        myLocation = reverseGeoCodeResult.getAddress();
                        Log.i("tag",reverseGeoCodeResult.getAddress()+":reverseGeoCodeResult");
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}

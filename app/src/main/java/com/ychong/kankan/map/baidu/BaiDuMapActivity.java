package com.ychong.kankan.map.baidu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.ychong.kankan.R;
import com.ychong.kankan.ui.BaseActivity;
import com.ychong.kankan.ui.MoreActivity;
import com.ychong.kankan.utils.PermissionsChecker;

public class BaiDuMapActivity extends BaseActivity {
    private LinearLayout mapLl;
    private RadioButton satelliteRb;
    private RadioButton ordinaryRb;
    private CheckBox realTrafficCb;
    private ImageView navigationIv;//导航
    private MapView mapView;
    private static final int REQUEST_CODE = 0;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private PermissionsChecker mPermissionsChecker;
    private LatLng currLatLng;
    private BaiduMap Bmap;

    //防止每次定位都重新设置中心点和marker
    private boolean isFirstLocation = true;
    //初始化LocationClient定位类
    private LocationClient mLocationClient = null;
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口
    private BDAbstractLocationListener myListener = new MyLocationListener();
    //经纬度
    private double lat;
    private double lon;
    private LatLng startPt;
    private LatLng endPt;
    private Marker mStartMarker;
    private Marker mEndMarker;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, BaiDuMapActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        ordinaryRb.setOnClickListener(view -> {Bmap.setMapType(BaiduMap.MAP_TYPE_NORMAL);});
        satelliteRb.setOnClickListener(view -> {Bmap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);});
        realTrafficCb.setOnCheckedChangeListener((compoundButton, b) -> Bmap.setTrafficEnabled(b));
        navigationIv.setOnClickListener(view -> {
            startNavigation();
        });


    }

    private void startNavigation() {
    WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {
        @Override
        public void engineInitSuccess() {
            //引擎初始化成功
            Log.e("ddd","引擎初始化成功");
            routeWalkPlanWithParam();

        }

        @Override
        public void engineInitFail() {
            //引擎初始化失败的回调
            Log.e("ddd","引擎初始化失败");

        }
    });
    }

    private void routeWalkPlanWithParam() {
        //起始位置
        //startPt = new LatLng(40.047416,116.312143);
        endPt = new LatLng(currLatLng.latitude+0.005,currLatLng.longitude+0.006);
        WalkRouteNodeInfo startNode = new WalkRouteNodeInfo();
        startNode.setLocation(currLatLng);
        WalkRouteNodeInfo endNode = new WalkRouteNodeInfo();
        endNode.setLocation(endPt);
        //构造WalkNaviLaunchParam
        WalkNaviLaunchParam mParam = new WalkNaviLaunchParam().
                startNodeInfo(startNode)
                .endNodeInfo(endNode);
        //发起算路
        WalkNavigateHelper.getInstance()
                .routePlanWithRouteNode(mParam, new IWRoutePlanListener() {
                    @Override
                    public void onRoutePlanStart() {
                        //开始算路的回调
                        Log.e("ddd","开始算路");

                    }

                    @Override
                    public void onRoutePlanSuccess() {
                        Log.e("ddd","算路成功");
                        //算路成功
                        //跳转至诱导界面
                        startActivity(new Intent(BaiDuMapActivity.this,NavigationActivity.class));

                    }

                    @Override
                    public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {
                        //算路失败
                        Log.e("ddd","算路失败"+walkRoutePlanError);

                    }
                });
    }


    private void initData() {
        mapLl.addView(mapView);
        initPermissions();
        initMap();



    }
    private void initMap(){
        Bmap = mapView.getMap();
        mapView.removeViewAt(1);//去除自带的logo
        //地图上比例尺
        mapView.showScaleControl(false);
        // 隐藏缩放控件
        mapView.showZoomControls(false);
        // 开启定位图层
        Bmap.setMyLocationEnabled(true);
        //声明LocationClient类
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        //开始定位
        mLocationClient.start();
    }

    private void initView() {
        mapLl = findViewById(R.id.map_ll);
        ordinaryRb = findViewById(R.id.ordinary_rb);
        satelliteRb = findViewById(R.id.satellite_rb);
        realTrafficCb = findViewById(R.id.real_traffic_cb);
        navigationIv = findViewById(R.id.navigation_iv);
        mapView = new MapView(this);

    }

    private void initLayout() {
        setContentView(R.layout.activity_baidu_map);
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop();
        // 关闭定位图层
        Bmap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    /**
     * 配置定位参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        int span = 5000;
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    /**
     * 实现定位监听 位置一旦有所改变就会调用这个方法
     * 可以在这个方法里面获取到定位之后获取到的一系列数据
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            location.getTime();    //获取定位时间
            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.getLocType();    //获取定位类型
            location.getLatitude();    //获取纬度信息
            location.getLongitude();    //获取经度信息
            location.getRadius();    //获取定位精准度
            location.getAddrStr();    //获取地址信息
            location.getCountry();    //获取国家信息
            location.getCountryCode();    //获取国家码
            location.getCity();    //获取城市信息
            location.getCityCode();    //获取城市码
            location.getDistrict();    //获取区县信息
            location.getStreet();    //获取街道信息
            location.getStreetNumber();    //获取街道码
            location.getLocationDescribe();    //获取当前位置描述信息
            location.getPoiList();    //获取当前位置周边POI信息

            location.getBuildingID();    //室内精准定位下，获取楼宇ID
            location.getBuildingName();    //室内精准定位下，获取楼宇名称
            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息
            //经纬度
            lat = location.getLatitude();
            lon = location.getLongitude();
            currLatLng = new LatLng(lat,lon);

            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLocation) {
                isFirstLocation = false;
                //设置并显示中心点
                setPosition2Center(Bmap, location, true);
            }
        }
    }
    /**
     * 设置中心点和添加marker
     *
     * @param map
     * @param bdLocation
     * @param isShowLoc
     */
    public void setPosition2Center(BaiduMap map, BDLocation bdLocation, Boolean isShowLoc) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getRadius()).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        map.setMyLocationData(locData);

        if (isShowLoc) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    public void initPermissions() {
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startPermissionsActivity(this, REQUEST_CODE, PERMISSIONS);
        }
    }
}

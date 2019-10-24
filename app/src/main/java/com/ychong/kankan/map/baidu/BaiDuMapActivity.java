package com.ychong.kankan.map.baidu;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.ychong.kankan.R;
import com.ychong.kankan.ui.BaseActivity;
import com.ychong.kankan.utils.PermissionsChecker;

public class BaiDuMapActivity extends BaseActivity {
    private LinearLayout mapLl;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private  boolean isFirstLoc = true;// 是否首次定位

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        initPermissions();
        mapLl.addView(mapView);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        //定位初始化
        mLocationClient = new LocationClient(this);

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        BaiDuLocationListener myLocationListener = new BaiDuLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();

    }

    private void initView() {
        mapLl = findViewById(R.id.map_ll);
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
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    public class BaiDuLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                // MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                // 设置缩放比例,更新地图状态
                float f = mBaiduMap.getMaxZoomLevel();// 19.0
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,
                        f - 2);
                mBaiduMap.animateMapStatus(u);
                //地图位置显示
                Toast.makeText(BaiDuMapActivity.this, location.getAddrStr(),
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void initPermissions(){
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startPermissionsActivity(this,REQUEST_CODE,PERMISSIONS);
        }
    }
}

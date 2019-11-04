package com.ychong.kankan.map.baidu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.bikenavi.params.BikeRouteNodeInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.ychong.kankan.R;
import com.ychong.kankan.map.baidu.adapter.AddressAdapter;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.utils.PermissionsChecker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BaiDuMapActivity extends BaseActivity {
    private static final String TAG = BaiDuMapActivity.class.getSimpleName();
    private LinearLayout mapLl;
    private RadioButton satelliteRb;
    private RadioButton ordinaryRb;
    private CheckBox realTrafficCb;
    private EditText startAddressEt;
    private EditText endAddressEt;
    /**
     * 导航
     */
    private ImageView navigationIv;
    /**
     * 定位
     */
    private ImageView locationIv;
    private MapView mapView;

    //导航类型
    public  int navigationType;
    public static final int WALK_TYPE = 0;
    public static final int RADING_TYPE = 1;
    private static final int REQUEST_CODE = 0;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private PermissionsChecker mPermissionsChecker;
    private LatLng startLatLng;
    private BaiduMap Bmap;
    private RecyclerView addressRv;
    private int inputStartOrEnd=0;

    private AddressAdapter adapter;
    private List<Address> addressList = new ArrayList<>();

    /**
     * 防止每次定位都重新设置中心点和marker
     */
    private boolean isFirstLocation = true;
    /**
     * 初始化LocationClient定位类
     */
    private LocationClient mLocationClient = null;
    /**
     * BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口
     */
    private BDAbstractLocationListener myListener = new MyLocationListener();
    private LatLng endLatLng;
    private LinearLayout navigationTypeLl;
    private LinearLayout navigationInputLl;
    private ImageView walkIv;
    private ImageView radingIv;
    private ImageView carIv;

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
        ordinaryRb.setOnClickListener(view -> {
            Bmap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        });
        satelliteRb.setOnClickListener(view -> {
            Bmap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        });
        realTrafficCb.setOnCheckedChangeListener((compoundButton, b) -> Bmap.setTrafficEnabled(b));
        navigationIv.setOnClickListener(view -> {
            if (!getAddress()){
                return;
            }
            if (navigationType == WALK_TYPE){
                //步行
                startWalkNavigation();
            }else if (navigationType == RADING_TYPE){
                //骑行
                startRadingNavigation();
            }
        });
        startAddressEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputStartOrEnd=0;
                try {
                    addressList.clear();
                    Geocoder geocoder = new Geocoder(BaiDuMapActivity.this, Locale.CHINA);
                    List<Address> startAddressList = geocoder.getFromLocationName(editable.toString(), 3);
                    addressList.addAll(startAddressList);
                    adapter.notifyDataSetChanged();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        endAddressEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputStartOrEnd = 1;
                try {
                    addressList.clear();
                    Geocoder geocoder = new Geocoder(BaiDuMapActivity.this, Locale.CHINA);
                    List<Address> endAddressList = geocoder.getFromLocationName(editable.toString(), 3);
                    addressList.addAll(endAddressList);
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        adapter.setOnClickListener(position -> {
            Address address = addressList.get(position);
            if (inputStartOrEnd==0){
                startAddressEt.setText(address.getAddressLine(0));
            }else if (inputStartOrEnd==1){
                endAddressEt.setText(address.getAddressLine(0));
            }
            addressList.clear();
            adapter.notifyDataSetChanged();
        });
        walkIv.setOnClickListener(view -> walkNavigation());
        radingIv.setOnClickListener(view -> radingNavigation());

        locationIv.setOnClickListener(view -> startLocation());
    }

    /**
     * 选择骑行导航
     */
    private void radingNavigation() {
        navigationType = RADING_TYPE;
        hideNavigationType();
    }
    /**
     * 选择步行导航
     */
    private void walkNavigation() {
        navigationType = WALK_TYPE;
        hideNavigationType();
    }

    /**
     * 隐藏选择导航类型控件  显示导航地点输入框控件
     */
    private void hideNavigationType() {
        navigationTypeLl.setVisibility(View.GONE);
        navigationInputLl.setVisibility(View.VISIBLE);
    }
    /**
     * 显示选择导航类型控件  隐藏导航地点输入框控件
     */
    private void showNavigationType(){
        navigationTypeLl.setVisibility(View.VISIBLE);
        navigationInputLl.setVisibility(View.GONE);
    }

    /**
     * 开始骑行导航
     */
    private void startRadingNavigation(){
        BikeNavigateHelper.getInstance().initNaviEngine(this, new IBEngineInitListener() {
            @Override
            public void engineInitSuccess() {
                Toast.makeText(BaiDuMapActivity.this,"骑行引擎初始化成功",Toast.LENGTH_SHORT).show();
                    routeBikePlanWithParam();
            }

            @Override
            public void engineInitFail() {

            }
        });

    }

    /**
     * 开始步行导航
     */
    private void startWalkNavigation() {
        WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {
            @Override
            public void engineInitSuccess() {
                //引擎初始化成功
                Log.e(TAG, "步行引擎初始化成功");
                routeWalkPlanWithParam();
            }

            @Override
            public void engineInitFail() {
                //引擎初始化失败的回调
                Log.e(TAG, "步行引擎初始化失败");

            }
        });
    }

    /**
     * 获取输入的地址所转换的坐标
     * @return
     */
    private boolean getAddress() {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.CHINA);
            String startAddressName = startAddressEt.getText().toString();
            String endAddressName = endAddressEt.getText().toString();
            if (!TextUtils.isEmpty(startAddressName)) {
                List<Address> startAddressList = geocoder.getFromLocationName(startAddressName, 1);
                Address startAddress = startAddressList.get(0);
                startLatLng = new LatLng(startAddress.getLatitude(), startAddress.getLongitude());
            }
            if (TextUtils.isEmpty(endAddressName)) {
                Toast.makeText(this, "终点不能为空", Toast.LENGTH_SHORT).show();
                return false;
            }
            List<Address> endAddressList = geocoder.getFromLocationName(endAddressName, 10);
            Address endAddress = endAddressList.get(0);

            endLatLng = new LatLng(endAddress.getLatitude(), endAddress.getLongitude());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 骑行算路
     */
    private void routeBikePlanWithParam() {
        BikeRouteNodeInfo startNode = new BikeRouteNodeInfo();
        startNode.setLocation(startLatLng);
        BikeRouteNodeInfo endNode = new BikeRouteNodeInfo();
        endNode.setLocation(endLatLng);
        //构造BikeNaviLaunchParam
        BikeNaviLaunchParam mParam = new BikeNaviLaunchParam().
                startNodeInfo(startNode)
                .endNodeInfo(endNode);
        //发起算路
        BikeNavigateHelper.getInstance()
                .routePlanWithRouteNode(mParam, new IBRoutePlanListener() {
                    @Override
                    public void onRoutePlanStart() {
                        //开始算路的回调
                        Toast.makeText(BaiDuMapActivity.this,"开始算路",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRoutePlanSuccess() {
                       Toast.makeText(BaiDuMapActivity.this, "算路成功",Toast.LENGTH_SHORT).show();
                        //算路成功
                        //跳转至诱导界面
                        Intent intent = new Intent(BaiDuMapActivity.this,NavigationActivity.class);
                        intent.putExtra("navigationType",navigationType);
                        startActivity(intent);
                    }

                    @Override
                    public void onRoutePlanFail(BikeRoutePlanError bikeRoutePlanError) {
                        //算路失败
                        Toast.makeText(BaiDuMapActivity.this, "算路失败"+bikeRoutePlanError.name(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 步行算路
     */
    private void routeWalkPlanWithParam() {
        WalkRouteNodeInfo startNode = new WalkRouteNodeInfo();
        startNode.setLocation(startLatLng);
        WalkRouteNodeInfo endNode = new WalkRouteNodeInfo();
        endNode.setLocation(endLatLng);
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
                        Toast.makeText(BaiDuMapActivity.this,"开始算路",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRoutePlanSuccess() {
                       Toast.makeText(BaiDuMapActivity.this, "算路成功",Toast.LENGTH_SHORT).show();
                        //跳转至诱导界面
                        Intent intent = new Intent(BaiDuMapActivity.this,NavigationActivity.class);
                        intent.putExtra("navigationType",navigationType);
                        startActivity(intent);
                    }

                    @Override
                    public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {
                        //算路失败
                        Toast.makeText(BaiDuMapActivity.this, "算路失败"+walkRoutePlanError.name(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mapLl.addView(mapView);
        adapter = new AddressAdapter(this,addressList);
        addressRv.setLayoutManager(new LinearLayoutManager(this));
        addressRv.setAdapter(adapter);
        initPermissions();
        initMap();

    }

    /**
     * 初始化百度地图
     */
    private void initMap() {
        Bmap = mapView.getMap();
        mapView.removeViewAt(1);//去除自带的logo
        //地图上比例尺
        mapView.showScaleControl(false);
        // 隐藏缩放控件
        mapView.showZoomControls(false);
        startLocation();
    }

    /**
     * 开始定位
     */
    private void startLocation(){
        // 开启定位图层
        Bmap.setMyLocationEnabled(true);
        //声明LocationClient类
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocationConfig();
        //开始定位
        mLocationClient.start();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mapLl = findViewById(R.id.map_ll);
        ordinaryRb = findViewById(R.id.ordinary_rb);
        satelliteRb = findViewById(R.id.satellite_rb);
        realTrafficCb = findViewById(R.id.real_traffic_cb);
        navigationIv = findViewById(R.id.navigation_iv);
        startAddressEt = findViewById(R.id.start_address_et);
        endAddressEt = findViewById(R.id.end_address_et);
        addressRv = findViewById(R.id.address_rv);
        navigationTypeLl = findViewById(R.id.navigation_type_ll);
        navigationInputLl = findViewById(R.id.navigation_input_ll);
        walkIv = findViewById(R.id.walk_iv);
        radingIv = findViewById(R.id.rading_iv);
        carIv = findViewById(R.id.car_iv);
        locationIv = findViewById(R.id.location_iv);
        mapView = new MapView(this);

    }

    /**
     * 初始化布局
     */
    private void initLayout() {
        setContentView(R.layout.activity_baidu_map);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showNavigationType();
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
    private void initLocationConfig() {
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
            startLatLng = new LatLng(location.getLatitude(), location.getLongitude());
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

package com.qiaoyi.secondworker.ui.center.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.LocationUtil;
import com.qiaoyi.secondworker.utlis.MapHandler;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;
import com.qiaoyi.secondworker.view.MyMapView;

import java.util.List;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/24
 *  选择地址
 * @author Spirit
 */

public class GetAddressActivity extends BaseActivity implements View.OnClickListener, AMap.OnMapLoadedListener, AMap.OnMarkerClickListener, AMap.OnCameraChangeListener, AMap.OnMapClickListener, AMapLocationListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_location;
    private EditText et_search;
    private MapView mapview;
    private RecyclerView rv_list;
    private AMap aMap;
    public static final int ZOOM = 17;
    public static final LatLng BEIJING = new LatLng(39.8141895741, 116.4085128613);// 北京市经纬度
    //高德定位
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private double lat;
    private double lng;

    private LocationUtil locationUtil;
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getaddress);
        initView();
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        toStartLocation();
        initMap(savedInstanceState);
    }

    private void initMap(Bundle savedInstanceState) {
        mapview.onCreate(savedInstanceState); // 此方法必须重写
        if (aMap == null) {
            aMap = mapview.getMap();
            setUpMap();
        }
        intLocation();
    }
    void intLocation() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();

//        requestLocationArea(crops_id);
    }
    private void setUpMap() {
        locationUtil = new LocationUtil();
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapClickListener(this);
//        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);//显示定位层并可触发，默认false

        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);//设置是否显示放大缩小按钮
        uiSettings.setScrollGesturesEnabled(true);//是否允许通过手势来移动
        uiSettings.setZoomGesturesEnabled(true);//这个方法设置了地图是否允许通过手势来缩放。
        aMap.addMarker(locationUtil.getMarkerOption("",lat,lng));
    }
    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_location = (TextView) findViewById(R.id.tv_location);
        et_search = (EditText) findViewById(R.id.et_search);
        mapview = (MapView) findViewById(R.id.mapview);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_title_txt.setText("选择地址");

        view_back.setOnClickListener(this);
        tv_location.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_location:

                break;
        }
    }

    private void submit() {
        // validate
        String search = et_search.getText().toString().trim();
        if (TextUtils.isEmpty(search)) {
            Toast.makeText(this, "请您输入服务地址", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try{
            mapview.onSaveInstanceState(outState);
        }catch (Exception e){
            ToastUtils.showLong("定位权限被拒绝，请在设置中打开权限！！");
        }

    }

    // 此方法必须重写
    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
    }
    // 此方法必须重写
    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
//        aMap.clear();
        ALog.e("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        ALog.e("onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁地图
        mapview.onDestroy();
    }
    @Override
    public void onMapLoaded() {
        moveCameraWithMap(new LatLng(lat, lng), ZOOM);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        aMap.clear();//移动后重新添加marker
        aMap.addMarker(locationUtil.getMarkerOption("",cameraPosition.target.latitude,cameraPosition.target.longitude));
        requestLocationPoint();
    }

    /**
     * 请求地址列表
     */
    private void requestLocationPoint() {
        LatLng point = MapHandler.getMapCenterPoint(aMap, mapview);
        ALog.e(point.latitude + "-" + point.longitude);
        CameraPosition cameraPosition = aMap.getCameraPosition();
        ALog.e(cameraPosition.target.latitude + "-----" + cameraPosition.target.longitude);
        ToastUtils.showShort("请求列表");
//        requestData(area[0].latitude, area[0].longitude, area[1].latitude, area[1].longitude, cameraPosition.zoom,lng+","+lat,crops_id);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
    public void moveCameraWithMap(LatLng latLng, int zoom) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
    LatLng loaction = BEIJING;
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0) {
            mlocationClient.stopLocation();
            //可在其中解析amapLocation获取相应内容。
            lat = aMapLocation.getLatitude();
            lng = aMapLocation.getLongitude();
            loaction = new LatLng(lat, lng);
            moveCameraWithMap(new LatLng(lat, lng), ZOOM);
            aMap.clear();

            requestLocationPoint();
        } else {
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                ToastUtils.showShort("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
        }
    }


}


package com.qiaoyi.secondworker.ui.map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flyco.tablayout.SlidingTabLayout;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.WorkerBean;
import com.qiaoyi.secondworker.cache.ACache;
import com.qiaoyi.secondworker.ui.BaseFragment;
import com.qiaoyi.secondworker.utlis.FixSizeLinkedList;
import com.qiaoyi.secondworker.utlis.GlideCircleTransform;
import com.qiaoyi.secondworker.utlis.MapHandler;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;
import com.qiaoyi.secondworker.utlis.UmengUtils;

import java.util.List;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/19
 *
 * @author Spirit
 */

public class MapFragment2 extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, AMapLocationListener, AMap.OnMapLoadedListener, AMap.OnCameraChangeListener {
    private static final int ZOOM = 17;
    private View rootView;
    private ImageView locationButton;
    private TextView tv_location;
    private ImageView iv_msg;
    private SlidingTabLayout tl_10;
    private ImageView iv_search_top;
    private MapView mapView;
    private ViewPager workerGallery;
    private Marker preMarker;//用来记录被点击的marker
    private boolean isScroll = false;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private double lat;
    private double lng;
    public static final LatLng BEIJING = new LatLng(39.8141895741, 116.4085128613);// 北京市经纬度
    private AMap aMap;
    private WorkerGalleryAdapter workerGalleryAdapter;
    private boolean isLoaded = true;
    private int requestNum=0;//请求超过10次刷新地图
    private String service_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(getActivity());
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_map, container, false);
        }
        initView(savedInstanceState);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    // 此方法必须重写
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try{
            mapView.onSaveInstanceState(outState);
        }catch (Exception e){
            ToastUtils.showLong("定位权限被拒绝，请在设置中打开权限！！");
        }

    }
    private void initView(Bundle savedInstanceState) {
        locationButton = (ImageView) rootView.findViewById(R.id.iv_location);
        locationButton.setOnClickListener(this);
        tv_location = (TextView) rootView.findViewById(R.id.tv_location);
        tv_location.setOnClickListener(this);
        iv_msg = (ImageView) rootView.findViewById(R.id.iv_msg);
        iv_msg.setOnClickListener(this);
        tl_10 = (SlidingTabLayout) rootView.findViewById(R.id.tl_10);
        tl_10.setOnClickListener(this);
        iv_search_top = (ImageView) rootView.findViewById(R.id.iv_search_top);
        iv_search_top.setOnClickListener(this);
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
       /* workerGallery = (ViewPager) rootView.findViewById(R.id.vp_adj);
        workerGalleryAdapter = new WorkerGalleryAdapter(getContext());
        workerGallery.setPageMargin(getResources().getDimensionPixelSize(R.dimen.base16dp));
        workerGallery.setAdapter(workerGalleryAdapter);
        workerGallery.addOnPageChangeListener(this);
        workerGallery.setVisibility(View.GONE);
        workerGallery.requestFocus();*/
        intLocation();
    }

    void intLocation() {
        mlocationClient = new AMapLocationClient(this.getContext());
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
//        initLocationMarker();
    }
    //添加事件并开始获取marker
    private void setUpMap() {
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnCameraChangeListener(this);

        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);//设置是否显示放大缩小按钮
        uiSettings.setScrollGesturesEnabled(true);//是否允许通过手势来移动
        uiSettings.setZoomGesturesEnabled(true);//这个方法设置了地图是否允许通过手势来缩放。
        aMap.addMarker(new LocationUtil().getMarkerOption("",lat,lng));
    }
    // 此方法必须重写
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        UmengUtils.startStatistics4Fragment("地图");
    }

    // 此方法必须重写
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
//        aMap.clear();
        ALog.e("onPause");
        UmengUtils.endStatistics4Fragment("地图");
    }

    @Override
    public void onStop() {
        super.onStop();
        ALog.e("onStop");
    }

    // 此方法必须重写
    @Override
    public void onDestroy() {
        super.onDestroy();
        aMap = null;
        mlocationClient.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_location:
            case R.id.tv_location:

                break;
            case R.id.iv_msg:

                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        ALog.e("onPageScrolled");
        isScroll = true;
    }

    @Override
    public void onPageSelected(int position) {
        if (isScroll) {
//            ALog.e("onPageSelected");
//            WorkerBean pathologyBean = pbMarkers.get(workerGallery.getCurrentItem());
//            LatLng latLng = MapHandler.createLatLng(pathologyBean.lat,pathologyBean.lng);
//            Marker marker = MapHandler.getMarkerByLatLng(aMap.getMapScreenMarkers(), latLng);
//            changeMarkerFlag(marker);
            isScroll = false;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        ALog.e("onPageScrollStateChanged:");
    }
    LatLng loaction = BEIJING;
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mlocationClient.stopLocation();
                //可在其中解析amapLocation获取相应内容。
                lat = aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
                loaction = new LatLng(lat, lng);
                moveCameraWithMap(new LatLng(lat, lng), ZOOM);
                aMap.addMarker(new LocationUtil().getMarkerOption("",lat,lng));
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                ToastUtils.showShort("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    private void moveCameraWithMap(LatLng latLng, int zoom) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onMapLoaded() {
        moveCameraWithMap(new LatLng(lat, lng), ZOOM);
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        ALog.e("onCameraChange");
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
    }

}

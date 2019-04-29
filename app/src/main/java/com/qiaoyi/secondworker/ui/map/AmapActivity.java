package com.qiaoyi.secondworker.ui.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.qiaoyi.secondworker.R;

import java.util.List;

import cn.isif.alibs.utils.ALog;

public class AmapActivity extends AppCompatActivity implements LocationSource, PoiSearch.OnPoiSearchListener, AMap.OnCameraChangeListener {
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    private LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        if(aMap == null){
            aMap = mapView.getMap();
        }

        setLocationCallBack();

        //设置定位监听
        aMap.setLocationSource(this);
        aMap.setOnCameraChangeListener(this);
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //显示定位层并可触发，默认false
        aMap.setMyLocationEnabled(true);
    }

    private void setLocationCallBack(){
        locationUtil = new LocationUtil();
        locationUtil.setLocationCallBack(new LocationUtil.ILocationCallBack() {
            @Override
            public void callBack(String str,double lat,double lgt,AMapLocation aMapLocation) {

                //根据获取的经纬度，将地图移动到定位位置
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat,lgt)));
                mListener.onLocationChanged(aMapLocation);
                //添加定位图标
                aMap.addMarker(locationUtil.getMarkerOption(str,lat,lgt));
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    LatLonPoint searchLatlonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                    if (searchLatlonPoint != null) {
                        doSearchQuery(searchLatlonPoint);
                    }
                }
            }
        });
    }

    private void doSearchQuery(LatLonPoint searchLatlonPoint) {
        PoiSearch.Query query = new PoiSearch.Query("", "", "");
        query.setPageSize(5);
        query.setPageNum(0);
        PoiSearch poisearch = new PoiSearch(this, query);
        poisearch.setOnPoiSearchListener(this);
        poisearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 500, true));
        poisearch.searchPOIAsyn();
    }

    //定位激活回调
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;

        locationUtil.startLocate(getApplicationContext());
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新绘制加载地图
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getPois().size() > 0) {
                List<PoiItem> poiItems = poiResult.getPois();
//                datas.addAll(poiItems);
//                adapter.notifyDataSetChanged();
                for (int i = 0; i < poiItems.size(); i++) {
                    ALog.e("第 "+i +"个"+poiItems.get(i)+"");
                }
            } else {
//                ToastUtil.toast("无搜索结果");
            }
        } else {
//            ToastUtil.toast("搜索失败" );
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        aMap.clear();
        double latitude = cameraPosition.target.latitude;
        double longitude = cameraPosition.target.longitude;
        aMap.addMarker(locationUtil.getMarkerOption("",latitude,longitude));
        doSearchQuery(new LatLonPoint(latitude,longitude));
    }
}

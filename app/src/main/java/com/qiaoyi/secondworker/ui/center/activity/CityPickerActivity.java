package com.qiaoyi.secondworker.ui.center.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.qiaoyi.secondworker.BaseActivity;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/28
 *  城市选择
 * @author Spirit
 */

public class CityPickerActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener {

    private String location_city;
    private GeocodeSearch geocodeSearch;
    private LatLonPoint point = new LatLonPoint(36.649816,116.971664);
    private String city_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        Intent intent = getIntent();
        location_city = intent.getStringExtra("location_city");
        initCityPicker();
    }

    private void initCityPicker() {
        List<HotCity> hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
        hotCities.add(new HotCity("济南", "山东", "101120101"));
        CityPicker.from(this) //activity或者fragment
//                .enableAnimation(false)	//启用动画效果，默认无
//                .setAnimationStyle(anim)	//自定义动画
                .setLocatedCity(new LocatedCity(location_city, "", ""))  //APP自身已定位的城市，传null会自动定位（默认）
                .setHotCities(hotCities)	//指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        //地理编码
                        city_name = data.getName();
                        getCityLatlng(city_name);

                    }

                    @Override
                    public void onCancel(){
                        finish();
                    }

                    @Override
                    public void onLocate() {
                        //定位接口，需要APP自身实现，这里模拟一下定位
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //定位完成之后更新数据
                                toStartLocation();
                                CityPicker.from(CityPickerActivity.this)
                                        .locateComplete(new LocatedCity(city, province, ""), LocateState.SUCCESS);
                            }
                        }, 2000);
                    }
                })
                .show();
    }

    /**
     * 根据地理名称查找坐标
     * @param location_city
     */
    public void getCityLatlng(String location_city) {
        //构造 GeocodeSearch 对象，并设置监听。
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        //通过GeocodeQuery设置查询参数,调用getFromLocationNameAsyn(GeocodeQuery geocodeQuery) 方法发起请求。
        //address表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode都ok
        GeocodeQuery query = new GeocodeQuery(location_city,location_city);
        geocodeSearch.getFromLocationNameAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (geocodeResult.getGeocodeAddressList()!=null && geocodeResult.getGeocodeAddressList().size()>0) {
            point = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
            ALog.e(point.getLatitude() + "++++++++++" + point.getLongitude());
            Intent intent = new Intent();
            intent.putExtra("city_lat", point.getLatitude());
            intent.putExtra("city_lng", point.getLongitude());
            intent.putExtra("select_city", city_name);
            setResult(RESULT_OK, intent);
            finish();
        }else {
            ToastUtils.showShort("该城市未开通服务");
            finish();
        }
    }
}

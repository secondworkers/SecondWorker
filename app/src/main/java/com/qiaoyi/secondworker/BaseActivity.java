package com.qiaoyi.secondworker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.qiaoyi.secondworker.utlis.ACache;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;

public class BaseActivity extends AppCompatActivity {
  public Toast toast;
  public int mPage = 1;
  public int mPageSize = 20;
  public String mLimitId = "";
  public ProgressDialog progressDialog;

  public AMapLocationClient mLocationClient = null;
  public double lat,lng;
  public String address;
  private String township;
  private String formatAddress;

  @Override public void onCreate(@Nullable Bundle savedInstanceState,
      @Nullable PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
    SecondWorkerApplication.getInstance().addActivity(this);
    StatusBarUtil.setTranslucentStatus(this);
    StatusBarUtil.setStatusBarDarkTheme(this, true);
/*    //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
    StatusBarUtil.setRootViewFitsSystemWindows(this,true);
    //设置状态栏透明
    StatusBarUtil.setTranslucentStatus(this);
    //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
    //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
    if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
      //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
      //这样半透明+白=灰, 状态栏的文字能看得清
      StatusBarUtil.setStatusBarColor(this,0x55000000);
    }*/
  }

  @Override protected void onResume() {
    super.onResume();
//    MobclickAgent.onResume(this);
  }

  @Override protected void onDestroy() {
    if (mLocationClient != null) {
      mLocationClient.onDestroy();
    }
    super.onDestroy();
  }

  @Override
  public void finish() {
    super.finish();
    SecondWorkerApplication.getInstance().removeActivity(this);
  }

  public void toStartLocation() {
    AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    //mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
    //mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
    //mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
    mLocationOption.setOnceLocation(true);
    mLocationOption.setNeedAddress(true);
    mLocationOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);
    mLocationClient = new AMapLocationClient(getApplicationContext());
    mLocationClient.setLocationOption(mLocationOption);
    mLocationClient.setLocationListener(new AMapLocationListener() {
      @Override public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
          lat = amapLocation.getLatitude();
          lng = amapLocation.getLongitude();
          //详情地址
          address = amapLocation.getAddress();
          //市区
          address = amapLocation.getCity() + amapLocation.getDistrict();
          //逆地理编码通过坐标获取地理位置
          GeocodeSearch geocoderSearch = new GeocodeSearch(getApplication());

          geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
              formatAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
              RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
              township = address.getTownship();
              ALog.e("formatAddress:"+ formatAddress);
              ALog.e("rCode:"+i);
              ALog.e("Township:"+ township);

            }
            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
          });
//          LatLonPoint latLonPoint = new LatLonPoint(38.794666,105.990509);
          LatLonPoint latLonPoint = new LatLonPoint(lat, lng);
          // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
          RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
          geocoderSearch.getFromLocationAsyn(query);
          String districtAddress = amapLocation.getProvince()+","+amapLocation.getCity()+","+amapLocation.getDistrict()+","+township;
          ALog.e("定位：" + address + "," + lat + "," + lng+"---"+amapLocation.getStreet());
          try {
            ACache mCache = ACache.get(getApplication());
            mCache.put("user_loc_lat", lat+"");
            mCache.put("user_loc_lng", lng+"");
            mCache.put("user_loc_address", address);//济南市长清区
            mCache.put("user_loc_districtAddress", districtAddress);//山东省，济南市，长清区
//            mCache.put("user_loc_formatAddress", formatAddress);//山东省济南市长清区大学城356街道

          } catch (Exception e) {
            e.printStackTrace();
          }
          //amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
          //amapLocation.getAccuracy();//获取精度信息
          //amapLocation.getCountry();//国家信息
//          amapLocation.getProvince();//省信息
//          amapLocation.getCity();//城市信息
//          amapLocation.getDistrict();//城区信息
          //amapLocation.getStreet();//街道信息
          //amapLocation.getStreetNum();//街道门牌号信息
          //amapLocation.getCityCode();//城市编码
          //amapLocation.getAdCode();//地区编码
          //amapLocation.getAoiName();//获取当前定位点的AOI信息
          //amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
          //amapLocation.getFloor();//获取当前室内定位的楼层
          //amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
          ////获取定位时间
          //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(
          //    new Date(amapLocation.getTime()));
        } else {
          ALog.d("定位：失败");
        }

      }
    });
    mLocationClient.stopLocation();
    mLocationClient.startLocation();

  }

  public void checkPermission() {
    if (Build.VERSION.SDK_INT >= 23) {
      String[] mPermissionList = new String[] {
          Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS,
          Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW,
          Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS
      };
      String[] pp = checkPermission(mPermissionList);
      if (null != pp && pp.length > 0) {
        ActivityCompat.requestPermissions(this, checkPermission(mPermissionList), 123);
      }
    }
  }

  private String[] checkPermission(String[] permissionList) {
    if (Build.VERSION.SDK_INT >= 23) {
      List<String> pers = new ArrayList<String>();
      for (String per : permissionList) {
        if (checkSelfPermission(per) != PackageManager.PERMISSION_GRANTED) {
          pers.add(per);
        }
      }
      return pers.size() > 0 ? pers.toArray(new String[pers.size()]) : null;
    } else {
      return null;
    }
  }

  public void closeToast() {
    if (toast != null) {
      toast.cancel();
    }
  }

  public void showToast(String txt) {
    //closeToast();
    //if (!isFinishing()) {
    //  if (toast == null) {
    //    toast = Toast.makeText(this, txt, Toast.LENGTH_SHORT);
    //    toast.setGravity(Gravity.CENTER, 0, 0);
    //  } else {
    //    toast.setText(txt);
    //  }
    //  toast.show();
    //}
    ToastUtils.showShort(txt);
  }

  @Override public void onPause() {
    super.onPause();
//    MobclickAgent.onPause(this);
    closeToast();
  }

  public void initTitleBar() {
    if (findViewById(R.id.rl_title_bg) != null) {
      findViewById(R.id.rl_title_bg).setVisibility(View.VISIBLE);
      findViewById(R.id.view_back).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onBackPressed();
        }
      });
    }
  }

  public void initTitleBarNoLIne() {
    if (findViewById(R.id.rl_title_bg) != null) {
      findViewById(R.id.view_title_line).setVisibility(View.GONE);
      findViewById(R.id.rl_title_bg).setVisibility(View.VISIBLE);
      findViewById(R.id.view_back).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onBackPressed();
        }
      });
    }
  }

  /**
   * 该方法会检测当前app拥有的权限
   *
   * @param webUrl 分享的url链接
   * @param title 分享的标题
   * @param des 分享描述
   * @param img 缩略图
   */
  public void share(String webUrl, String title, String des, String img) {
    if (TextUtils.isEmpty(des)) {
      des = title;
    }
    checkPermission();
//    Intent intent = new Intent(this, MyShareActivity.class);
    Intent intent = new Intent(this, MainActivity.class);
    intent.putExtra("webUrl",webUrl);
    intent.putExtra("title",title);
    intent.putExtra("des",des);
    intent.putExtra("img",img);
    startActivity(intent);
    overridePendingTransition(R.anim.push_bottom_in, 0);
//    UmengUtil.shareWeb(this, webUrl, title, des, img);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
//    UmengUtil.onActivityResult(this, requestCode, resultCode, data);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
    return super.shouldShowRequestPermissionRationale(permission);
  }

  public String plusOne(String str) {
    if (TextUtils.isEmpty(str)) {
      str = "0";
    }
    int i = 0;
    try {
      i = Integer.parseInt(str);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return String.valueOf((i + 1) > 0 ? i + 1 : 0);
  }

  public String minusOne(String str) {
    if (TextUtils.isEmpty(str)) {
      str = "0";
    }
    int i = 0;
    try {
      i = Integer.parseInt(str);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return String.valueOf((i - 1) > 0 ? i - 1 : 0);
  }

 /* public void dismissProgressDialog() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }*/

  /*public void showProgressDialog(String text) {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
    }
    if (TextUtils.isEmpty(text)) {
      progressDialog.setMessage("加载中...");
    } else {
      progressDialog.setMessage(text);
    }
    if (!isFinishing()) {
      progressDialog.show();
    }
  }*/
}

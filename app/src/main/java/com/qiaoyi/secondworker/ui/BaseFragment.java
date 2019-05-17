package com.qiaoyi.secondworker.ui;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.Toast;


import com.qiaoyi.secondworker.bean.ServiceTypeBean;
import com.qiaoyi.secondworker.bean.WrapServiceBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiHome;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

public class BaseFragment extends Fragment {
  public static final String ARG_PARAM1 = "param1";
  public static final String ARG_PARAM2 = "param2";
  public static final String ARG_PARAM3 = "mParam3";
  public static final int PAGE_SIZE = 20;
  public boolean hidden;
  public Toast toast;
  public void closeToast() {
    if (toast != null) {
      toast.cancel();
    }
  }

  public void showToast(String txt) {
    //closeToast();
    //if (!hidden) {
    //  if (toast == null) {
    //    toast = Toast.makeText(getActivity(), txt, Toast.LENGTH_SHORT);
    //    toast.setGravity(Gravity.CENTER, 0, 0);
    //  } else {
    //    toast.setText(txt);
    //  }
    //  toast.show();
    //}
    ToastUtils.showShort(txt);
  }


  public void checkPermission(String[] mPermissionList) {
    if (Build.VERSION.SDK_INT >= 23) {
      String[] pp = selfPermission(mPermissionList);
      if (null != pp && pp.length > 0) {
        ActivityCompat.requestPermissions(this.getActivity(), selfPermission(mPermissionList), 123);
      }
    }
  }

  private String[] selfPermission(String[] permissionList) {
    if (Build.VERSION.SDK_INT >= 23) {
      List<String> pers = new ArrayList<String>();
      for (String per : permissionList) {
        if (this.getContext().checkSelfPermission(per) != PackageManager.PERMISSION_GRANTED) {
          pers.add(per);
        }
      }
      return pers.size() > 0 ? pers.toArray(new String[pers.size()]) : null;
    } else {
      return null;
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  public void onResume() {
    super.onResume();
    MobclickAgent.onResume(this.getActivity()); //统计时长
  }
  public void onPause() {
    super.onPause();
    closeToast();
    MobclickAgent.onPause(this.getActivity()); //统计时长
  }
}

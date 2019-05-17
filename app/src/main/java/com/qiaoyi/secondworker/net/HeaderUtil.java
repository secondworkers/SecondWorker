package com.qiaoyi.secondworker.net;


import com.qiaoyi.secondworker.BuildConfig;
import com.qiaoyi.secondworker.local.AccountHandler;

/**
 * Created by Spirit on 2016/12/21-18:11.
 */

public class HeaderUtil {

  /**
   * 设置header信息
   */
  public static Header getHeader() {
    Header header = new Header();
    header.put("token", AccountHandler.getToken());//ios特有android为空\
    header.put("version", BuildConfig.VERSION_NAME);//设备平台 android 固定
    header.put("uid", AccountHandler.getUserId());//设备平台 android 固定
    /**
     * mobile_type
     * 1：小版本更新  2：强更
     * */
    return header;
  }
}

package com.qiaoyi.secondworker.net;


/**
 * Created by dell on 2016/12/21-18:11.
 */

public class HeaderUtil {

  /**
   * 设置header信息
   */
  public static Header getHeader() {
    Header header = new Header();
    header.put("token", "2134rwe32412334erwer324e");//ios特有android为空\
    header.put("version", "1.0");//设备平台 android 固定
    header.put("fromtype", "143242453124234");//app的版本
    header.put("time", "143242453124234");//app的版本
    return header;
  }
}

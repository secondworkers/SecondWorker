package com.qiaoyi.secondworker.net;

public class Contact {
  public static final String DB_NAME = "secondworker";
  ///
//    public static final String BASE_URL = "http://irapi.sndistinguish.com/";//release主机
//    public static final String BASE_URL = "http://irapi-test.sndistinguish.com/";//预发布
//  public static final String BASE_URL = "http://192.168.0.105:8066";//debug主机
  public static final String BASE_URL = "http://103.118.221.46:8066/mg_service";//预发布主机

  public static final String ANDROID = "android";
  public static final String APP_ID = "wx43715d46bea8060f";//微信appid
    /*static {
        switch (BuildConfig.ENV) {
            case "debug":
                BASE_URL = DEBUG_BASE_URL;
                break;
            case "online":
                BASE_URL = RELEASE_BASE_URL;
                break;
            default:
                BASE_URL = RELEASE_BASE_URL;
                break;
        }
    }*/


  public static final String LOGIN = BASE_URL + "/user/login";//登录
  public static final String SEND_SMS = BASE_URL + "/user/code";//发送验证码
  public static final String GETUSERINFO = BASE_URL + "/user/getUserInfo";//发送验证码
  public static final String MODIFY_USERINFO = BASE_URL + "/user/upUserInfo";//修改用户信息
  public static final String BIND_PHONE = BASE_URL + "/user/bindPhone";//三方登录后绑定手机号


  public static final String THIRD_LOGIN = BASE_URL + "/user/loginThird";//第三方登录
  public static final String SHAKE_WORKER = BASE_URL + "/worker/queryTjList";//摇出秒工
  public static final String ADDRESS_LIST = BASE_URL + "/address/selectAddress";//地址列表
  public static final String ADDRESS_UPDATE = BASE_URL + "/address/saveAddress";//修改地址
  public static final String ADDRESS_DELETE = BASE_URL + "/address/modificationAddress";//删除地址

  public static final String CREATE_ORDER = BASE_URL + "/order/createOrder";//生成订单
  public static final String ORDER_INFO = BASE_URL + "/order/toPay";//查询订单时间
  public static final String CANCEL_ORDER = BASE_URL + "/order/cancelOrder";//取消订单
  public static final String UPDATE_ORDER = BASE_URL + "/order/updateStatus";//修改订单状态
  public static final String GET_ORDER_LIST = BASE_URL + "/order/queryMyOrder";//查询订单list
  public static final String GET_ORDER_DETAILS = BASE_URL + "/order/queryOrderDetails";//查询订单详情

  public static final String SERVICE_TYPE = BASE_URL + "/serviceType/getServiceType";//服务类型
  public static final String SERVICE_ITEM = BASE_URL + "/serviceItem/getServiceItem";//服务子类型
  public static final String GET_SERVICE_ITEM = BASE_URL + "/serviceItem/getServiceItemById";//根据服务大类id 查询服务小类
  public static final String GET_SERVICE_DETAIL = BASE_URL + "/serviceItem/getServiceItemDetail";//服务详情

  public static final String SEARCH_QUERY_LIST = BASE_URL + "/worker/queryList";//首页--搜索展示列表
  public static final String GET_WORKER_DETAIL = BASE_URL + "/worker/getWorkerDetail";//首页--搜索展示列表
  public static final String GET_MAP_WORKERS = BASE_URL + "/worker/queryMapList";//查询工作人员详情

  public static final String POST_REQUIREMENT = BASE_URL + "/release/requirement";//发布需求
  public static final String GET_REQUIREMENT = BASE_URL + "/release/queryRequirement";//查询需求
  public static final String UPDATE_REQUIREMENT = BASE_URL + "/release/updateRequirement";//修改需求
  public static final String APPLY_WORKER = BASE_URL + "/worker/saveWorkerInfo";//申请成为秒工人
  public static final String QUERY_APPLY_WORKER_STATUS = BASE_URL + "/worker/selectAuditState";//查询申请状态

  public static final String GET_QINIUTOUKEN = BASE_URL + "/mobile/getUpToken";//申请成为秒工人
  public static final String CHECK_UPDATE = BASE_URL + "/mobile/queryVersion";//检查更新




  public static final String QINIU_UPTOKEN =
      "LzTePBio504g-GHxKU6MNg7_jVxdDSySVHEbN-oo:GenTUQtovVmR8kH7mZicqAa4Zrs=:eyJzY29wZSI6InNlY29uZHdvcmtlciIsImRlYWRsaW5lIjoxNTU3OTA0MDc1fQ==";
  public static final String TEST_IMG = "http://image.lenongdao.com/image/20180318/117.jpg";
  public static final String QN_IMG = "http://pqpmjbn5w.bkt.clouddn.com/";


  public static final String WX_PAY = BASE_URL+"/wxPay/app/tenpay/prepay";
  public static final String DOWNLOAD = "http://103.118.221.46:8066/miaogong.apk";
}

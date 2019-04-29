package com.qiaoyi.secondworker.net;

public class Contact {
  public static final String DB_NAME = "godargo";
///
//    public static final String BASE_URL = "http://irapi.sndistinguish.com/";//release主机
//    public static final String BASE_URL = "http://irapi-test.sndistinguish.com/";//预发布
//  public static final String BASE_URL = "http://192.168.0.104:8090/";//debug主机
  public static final String BASE_URL = "http://192.168.0.103:8066/";//debug主机


  public static final String ANDROID = "android";
  public static final String APP_ID = "wx41aba1402b780e3f";//微信appid
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


  public static final String LOGIN = BASE_URL + "user/login";//登录
  public static final String SEND_SMS = BASE_URL + "user/code";//发送验证码
  public static final String THIRD_LOGIN = BASE_URL + "/thirdLogin";//第三方登录



  public static final String QINIU_UPTOKEN =
      "Ao1LtcZyDdhB_QjlnuyL_FvN64KEeC7cTQTbWm7r:E4eOA8ZNKKpweaI-yZcQY047spI=:eyJjYWxsYmFja1VybCI6IiIsImNhbGxiYWNrQm9keSI6Im5hbWU9JChmbmFtZSkmZnNpemU9JChmc2l6ZSkmbWltZVR5cGU9JChtaW1lVHlwZSkma2V5PSQoa2V5KSZoYXNoPSQoZXRhZykmdz0kKGltYWdlSW5mby53aWR0aCkmaD0kKGltYWdlSW5mby5oZWlnaHQpJnVpZD0zIiwicmV0dXJuQm9keSI6IntcImtleVwiOiAkKGtleSksIFwiaGFzaFwiOiAkKGV0YWcpLCBcIndcIjogJChpbWFnZUluZm8ud2lkdGgpLCBcImhcIjogJChpbWFnZUluZm8uaGVpZ2h0KSxcIm1pbWVUeXBlXCI6JChtaW1lVHlwZSksXCJmc2l6ZVwiOiQoZnNpemUpfSIsInNjb3BlIjoiaW1hZ2VhbmFseXNpcyIsImRlYWRsaW5lIjoxNTIyNDIzOTc1fQ==";
  public static final String QINIU_UPTOKEN_1 =
      "Ao1LtcZyDdhB_QjlnuyL_FvN64KEeC7cTQTbWm7r:2Vm2-eXaVoYVD58KeAkwKzfusu4=:eyJjYWxsYmFja1VybCI6IiIsImNhbGxiYWNrQm9keSI6Im5hbWU9JChmbmFtZSkmZnNpemU9JChmc2l6ZSkmbWltZVR5cGU9JChtaW1lVHlwZSkma2V5PSQoa2V5KSZoYXNoPSQoZXRhZykmdz0kKGltYWdlSW5mby53aWR0aCkmaD0kKGltYWdlSW5mby5oZWlnaHQpJnVpZD0zIiwicmV0dXJuQm9keSI6IntcImtleVwiOiAkKGtleSksIFwiaGFzaFwiOiAkKGV0YWcpLCBcIndcIjogJChpbWFnZUluZm8ud2lkdGgpLCBcImhcIjogJChpbWFnZUluZm8uaGVpZ2h0KSxcIm1pbWVUeXBlXCI6JChtaW1lVHlwZSksXCJmc2l6ZVwiOiQoZnNpemUpfSIsInNjb3BlIjoiaW1hZ2VhbmFseXNpcyIsImRlYWRsaW5lIjoxNTIyMzM4NTc5fQ==";
  public static final String TEST_IMG = "http://image.lenongdao.com/image/20180318/117.jpg";
  public static final String QN_IMG = "http://image.lenongdao.com/";


  public static final String WX_PAY = BASE_URL+"wxpay/wxpay/getTuneUpWxInfo";

}

package cn.isif.umlibs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;


public class UmengUtil {
    private static SHARE_MEDIA[] shareMedias;

    private UmengUtil() {

    }

    public static void setPlatfrom(SHARE_MEDIA... shareMedias) {
        UmengUtil.shareMedias = shareMedias;
    }

    public static void setWeixin(String key, String secert) {
        PlatformConfig.setWeixin(key, secert);
    }

    public static void setSinaWeibo(String key, String secert, String url) {
        PlatformConfig.setSinaWeibo(key, secert, url);

    }

    public static void setQQZone(String key, String secert) {
        PlatformConfig.setQQZone(key, secert);

    }

    public static void shareWeb(final Activity activity, String WebUrl, String title, String description, String imageUrl) {
        UMWeb web = new UMWeb(WebUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if (TextUtils.isEmpty(imageUrl)) {
            web.setThumb(new UMImage(activity, R.drawable.logo));  //本地缩略图
        } else {
            web.setThumb(new UMImage(activity, imageUrl));  //网络缩略图
        }
        new ShareAction(activity)
                .setDisplayList(shareMedias)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (share_media.name().equals("WEIXIN_FAVORITE")) {
                                    Toast.makeText(activity, "收藏成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (throwable != null) {
                            Log.d("throw", "throw:" + throwable.getMessage());
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .open();
    }

    public static void authWeiXin(Activity activity, UMAuthListener listener) {
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, listener);
    }

    public static void authQQ(Activity activity, UMAuthListener listener) {
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.QQ, listener);
    }

    //授权
    private static void authorization(Activity activity, SHARE_MEDIA share_media, UMAuthListener listener) {
        UMShareAPI.get(activity).getPlatformInfo(activity, share_media, listener);
    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
    }

}

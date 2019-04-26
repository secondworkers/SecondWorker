package com.qiaoyi.secondworker.utlis;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * dp、sp 转换为 px 的工具类
 *
 * @author fxsky 2012.11.12
 *
 */
public class DisplayUtil {
  /**
   * 将px值转换为dip或dp值，保证尺寸大小不变
   *
   * @param context
   * @param pxValue
   *            （DisplayMetrics类中属性density）
   * @return
   */
  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  /**
   * 将dip或dp值转换为px值，保证尺寸大小不变
   *
   * @param context
   * @param dipValue
   *            （DisplayMetrics类中属性density）
   * @return
   */
  public static int dip2px(Context context, float dipValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  /**
   * 将px值转换为sp值，保证文字大小不变
   *
   * @param context
   * @param pxValue
   *            （DisplayMetrics类中属性scaledDensity）
   * @return
   */
  public static int px2sp(Context context, float pxValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (pxValue / fontScale + 0.5f);
  }

  /**
   * 将sp值转换为px值，保证文字大小不变
   *
   * @param context
   * @param spValue
   *            （DisplayMetrics类中属性scaledDensity）
   * @return
   */
  public static int sp2px(Context context, float spValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }

  /**
   * 获取签名包sha1
   *
   * @param context
   * @return
   */
  public static String sHA1(Context context) {
    try {
      PackageInfo info = context.getPackageManager().getPackageInfo(
              context.getPackageName(), PackageManager.GET_SIGNATURES);
      byte[] cert = info.signatures[0].toByteArray();
      MessageDigest md = MessageDigest.getInstance("SHA1");
      byte[] publicKey = md.digest(cert);
      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < publicKey.length; i++) {
        String appendString = Integer.toHexString(0xFF & publicKey[i])
                .toUpperCase(Locale.US);
        if (appendString.length() == 1)
          hexString.append("0");
        hexString.append(appendString);
        hexString.append(":");
      }
      String result = hexString.toString();
      return result.substring(0, result.length()-1);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }
}

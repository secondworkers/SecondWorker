package com.qiaoyi.secondworker.utlis;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.qiaoyi.secondworker.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import anet.channel.util.StringUtils;

/**
 * Created by Spirit on 2019-04-17.
 */

public class VwUtils {
  public static final int ROUNDED_CORNERS = 8;

  /**
   * 返回 ***2009
   * @param bankNum
   * @return
   */
  public static String getLast4String(String bankNum){
    if (bankNum.length()>=4){
      return "**** **** **** *** "+bankNum.substring(bankNum.length()-4,bankNum.length());
    }
    return bankNum;
  }

  public static int dp2px(Context ctx, float dpValue) {
    DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
    int px = (int) (dpValue * (metrics.densityDpi / 160f));
    return px;
  }

  public static void viewAnimator(View view) {
    viewAnimator(view, 600);
  }

  /**
   * 小到大动画
   */
  public static void viewAnimator(View view, int duriation) {
    ObjectAnimator ScaleAnim1 = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.0f);
    ScaleAnim1.setDuration(duriation);
    ScaleAnim1.setInterpolator(new DecelerateInterpolator());
    ObjectAnimator ScaleAnim2 = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
    ScaleAnim2.setDuration(duriation);
    ScaleAnim2.setInterpolator(new DecelerateInterpolator());
    ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 0f);
    fadeAnim.setDuration(duriation);
    ObjectAnimator appearAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
    appearAnim.setDuration(duriation);
    appearAnim.setInterpolator(new DecelerateInterpolator());
    AnimatorSet as = new AnimatorSet();
    as.play(appearAnim).with(ScaleAnim1);
    as.play(appearAnim).with(ScaleAnim2);
    as.play(fadeAnim).before(appearAnim);
    as.start();
  }

  /**
   * 文本单位
   */
  public static void txtUnit(Activity act, TextView tv, String num, int colorsize1, int colorsize2,
      String unit) {
    String txt = num + unit;
    int length = txt.length();
    SpannableString spannableString = new SpannableString(txt);
    spannableString.setSpan(new TextAppearanceSpan(act, colorsize1), 0, length - unit.length(),
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    spannableString.setSpan(new TextAppearanceSpan(act, colorsize2), length - unit.length(), length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    tv.setText(spannableString, TextView.BufferType.SPANNABLE);
  }

  public static int setVL(Activity act, View view, int w, int h) {
    DisplayMetrics metrics = new DisplayMetrics();
    act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int width = metrics.widthPixels;
    //int height = metrics.heightPixels;
    ViewGroup.LayoutParams vl = view.getLayoutParams();
    if (w > 0) {
      vl.width = (w * width) / 375;
    }
    if (h > 0) {
      vl.height = (h * width) / 375;
    }
    return vl.width;
  }

  public static int setVLH(Activity act, View view, int w, int h) {
    DisplayMetrics metrics = new DisplayMetrics();
    act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int width = metrics.widthPixels;
    //int height = metrics.heightPixels;
    ViewGroup.LayoutParams vl = view.getLayoutParams();
    if (w > 0) {
      vl.width = (w * width) / 375;
    }
    if (h > 0) {
      vl.height = (h * width) / 375;
    }
    return vl.height;
  }

  public static void setVLWH(Activity act, View view, int w, int h) {
    ViewGroup.LayoutParams vl = view.getLayoutParams();
    vl.width = w;
    vl.height = h;
  }

  public static int getSW(Activity act) {
    DisplayMetrics metrics = new DisplayMetrics();
    act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int width = metrics.widthPixels;
    return width;
  }

  public static int getSH(Activity act) {
    DisplayMetrics metrics = new DisplayMetrics();
    act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int height = metrics.heightPixels;
    return height;
  }

  public static int getSW(Activity act, int w) {
    DisplayMetrics metrics = new DisplayMetrics();
    act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int width = metrics.widthPixels;
    return (w * width) / 375;
  }

  public static int measureLayout(View view) {
    int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    view.measure(w, h);
    int width = view.getMeasuredWidth();
    return width;
  }

  public static String addZero(int number) {
    if (number > 9) {
      return String.valueOf(number);
    }
    return "0" + number;
  }

  public static String getWeek(int week) {
    if (week == 2) {
      return "周一";
    } else if (week == 3) {
      return "周二";
    } else if (week == 4) {
      return "周三";
    } else if (week == 5) {
      return "周四";
    } else if (week == 6) {
      return "周五";
    } else if (week == 7) {
      return "周六";
    } else {
      return "周日";
    }
  }

  public static String getViewTimeText(String time) {
    long l = 0;
    try {
      l = Long.parseLong(time);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(l * 1000);
    StringBuilder builder = new StringBuilder();
    builder.append(addZero(calendar.get(Calendar.MONTH) + 1));
    builder.append("-");
    builder.append(addZero(calendar.get(Calendar.DAY_OF_MONTH)));
    builder.append(" ");
    builder.append(addZero(calendar.get(Calendar.HOUR_OF_DAY)));
    builder.append(":");
    builder.append(addZero(calendar.get(Calendar.MINUTE)));
    return builder.toString();
  }

  public static void setSoftInput(Activity act, boolean isShow) {
    InputMethodManager imm =
        (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm == null) {
      return;
    }
    if (isShow) {
      if (act.getCurrentFocus() == null) {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
      } else {
        imm.showSoftInput(act.getCurrentFocus(), 0);
      }
    } else {
      if (act.getCurrentFocus() != null) {
        imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);
      }
    }
  }

  public static void hideKeyboard(View v) {
    if (v == null) {
      return;
    }
    InputMethodManager imm =
        (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null && imm.isActive()) {
      imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }
  }

  public static String getTime(String p) {
    DateFormat df = new SimpleDateFormat(p, Locale.SIMPLIFIED_CHINESE);
    String time = df.format(new Date());
    return time;
  }

  public static String getTime(String p, long l) {
    DateFormat df = new SimpleDateFormat(p, Locale.SIMPLIFIED_CHINESE);
    String time = df.format(new Date(l));
    return time;
  }

  public static View getNoDataView(Context context,String textDes){
    View view;
    view = View.inflate(context, R.layout.view_no_data,null);
    TextView info = view.findViewById(R.id.tv_noData);
    info.setText(textDes);
    return view;
  }
  public static Bitmap convertViewToBitmap(View view) {

    view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

    view.buildDrawingCache();

    Bitmap bitmap = view.getDrawingCache();

    return bitmap;

  }

  //毫秒转秒
  public static String long2String(long time){

    //毫秒转秒
    int sec = (int) time / 1000 ;
    int min = sec / 60 ;	//分钟
    sec = sec % 60 ;		//秒
    if(min < 10){	//分钟补0
      if(sec < 10){	//秒补0
        return "0"+min+":0"+sec;
      }else{
        return "0"+min+":"+sec;
      }
    }else{
      if(sec < 10){	//秒补0
        return min+":0"+sec;
      }else{
        return min+":"+sec;
      }
    }

  }

  /**
   * mm分ss秒
   * @param l
   * @return
   */
  public static String longToTimeStr(Long l) {
    long day = l / (1000 * 24 * 60 * 60); //单位天
    long hour = (l - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60); //单位时
    long minute = (l - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60); //单位分
    long second = (l - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;//单位秒

    String strtime = minute+"分"+second+"秒";
    return strtime;
  }
  /**
   * 返回当前时间的格式为 yyyy-MM-dd HH:mm:ss
   * @return
   */
  public static String  getCurrentTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    return sdf.format(System.currentTimeMillis());
  }

  /**
   * 适配全面屏 配合抽取的title
   * @param activity
   */
public static void fixScreen(Activity activity){
  StatusBarUtil.setTranslucentStatus(activity);
  StatusBarUtil.setStatusBarDarkTheme(activity, true);
}

  /**
   * 手机号中间4位使用*替换
   * @param phone
   * @return
   */
  public static String midleReplaceStar(String phone){
    String result=null;
    if (!TextUtils.isEmpty(phone)){
      if (phone.length()<7){
        result=phone;
      }else{
        String start = phone.substring(0,3);
        String end = phone.substring(phone.length()-4,phone.length());
        StringBuilder sb=new StringBuilder();
        sb.append(start).append("****").append(end);
        result=sb.toString();
      }
    }
    return result;
  }

  /**
   * 验证手机号
   * @param str
   * @return
   */
  public static boolean isMobile(String str) {
    Pattern p = null;
    Matcher m = null;
    boolean b = false;
    String s2="^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";// 验证手机号
    if(StringUtils.isNotBlank(str)){
      p = Pattern.compile(s2);
      m = p.matcher(str);
      b = m.matches();
    }
    return b;
  }
  /**
   * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
   *
   * @param str 需要处理的字符串
   * @return 处理完之后的字符串
   */
  public static String splitPrice(String str) {
    DecimalFormat decimalFormat = new DecimalFormat(",###");
    return decimalFormat.format(Double.parseDouble(str));
  }

  /**
   * 输入金额分割成单个字符数组
   * @param str 1990.12
   * @return
   */
  public static List<String> splitString(String str){
    String[] s = str.split("\\.");
    String s1 = s[0];
    String s2 = s[1];
    s1 = autoGenericCode(s1, 7);
    ArrayList<String> strings = new ArrayList<>();

    char[] chars = s1.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      strings.add(String.valueOf(chars[i]));
    }
    strings.add("."+s2);
    return strings;
  }
  /**
   * 不够位数的在前面补0，保留num的长度位数字
   * @param code
   * @return
   */
  public static String autoGenericCode(String code, int num) {
    String result = "";
    // 保留num的位数
    // 0 代表前面补充0
    // num 代表长度为4
    // d 代表参数为正数型
    result = String.format("%0" + num + "d", Integer.parseInt(code) + 1);

    return result;
  }
}

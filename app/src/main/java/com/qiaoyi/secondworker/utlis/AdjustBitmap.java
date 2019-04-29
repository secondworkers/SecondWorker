package com.qiaoyi.secondworker.utlis;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import cn.isif.alibs.utils.ALog;

/**
 * Created by Spirit on 2018/4/5.
 */

public class AdjustBitmap {

  /**
   * 将Drawable转换为bitmap
   *
   * @param drawable
   * @return
   */
  public static Bitmap drawableToBitmap(Drawable drawable) {
    int w = drawable.getIntrinsicWidth();
    int h = drawable.getIntrinsicHeight();
    System.out.println("Drawable转Bitmap");
    Bitmap.Config config =
        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
            : Bitmap.Config.RGB_565;
    Bitmap bitmap = Bitmap.createBitmap(w, h, config);
    //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, w, h);
    drawable.draw(canvas);

    return bitmap;
  }

  /**
   * 将bitmap调整到指定大小
   *
   * @param origin
   * @param newWidth
   * @param newHeight
   * @return
   */
  public static Bitmap sizeBitmap(Bitmap origin, int newWidth, int newHeight) {
    if (origin == null) {
      return null;
    }
    int height = origin.getHeight();
    int width = origin.getWidth();
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    Matrix matrix = new Matrix();
    matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
    Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
    if (!origin.isRecycled()) {//这时候origin还有吗？
      origin.recycle();
    }
    return newBM;
  }

  /**
   * 按比例缩放
   *
   * @param origin
   * @param scale
   * @return
   */
  public static Bitmap scaleBitmap(Bitmap origin, float scale) {
    if (origin == null) {
      return null;
    }
    int width = origin.getWidth();
    int height = origin.getHeight();
    Matrix matrix = new Matrix();
    matrix.preScale(scale, scale);
    ALog.e("scale===>  "+  scale+"  , width=====> "+width+" ,  height=====> "+height);
    Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
    if (newBM.equals(origin)) return newBM;
    origin.recycle();
    return newBM;
  }

  /**
   * 从中间截取一个正方形
   * @param bitmap
   * @return
   */
  public static Bitmap cropBitmap(Bitmap bitmap) {
    int w = bitmap.getWidth(); // 得到图片的宽，高
    int h = bitmap.getHeight();
    int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长

    return Bitmap.createBitmap(bitmap, (bitmap.getWidth() - cropWidth) / 2,
        (bitmap.getHeight() - cropWidth) / 2, cropWidth, cropWidth);
  }

  /**
   * 把图片裁剪成圆形
   *
   * @param bitmap
   * @return
   */
  public static Bitmap getCircleBitmap(Bitmap bitmap) {//把图片裁剪成圆形
    if (bitmap == null) {
      return null;
    }
    bitmap = cropBitmap(bitmap);//裁剪成正方形
    try {
      Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),
          bitmap.getHeight(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(circleBitmap);
      final Paint paint = new Paint();
      final Rect rect = new Rect(0, 0, bitmap.getWidth(),
          bitmap.getHeight());
      final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
          bitmap.getHeight()));
      float roundPx = 0.0f;
      roundPx = bitmap.getWidth();
      paint.setAntiAlias(true);
      canvas.drawARGB(0, 0, 0, 0);
      paint.setColor(Color.WHITE);
      canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
      paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
      final Rect src = new Rect(0, 0, bitmap.getWidth(),
          bitmap.getHeight());
      canvas.drawBitmap(bitmap, src, rect, paint);
      return circleBitmap;
    } catch (Exception e) {
      return bitmap;
    }
  }
}

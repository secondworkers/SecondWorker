package com.qiaoyi.secondworker.utlis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.SecondWorkerApplication;


import java.util.List;

import cn.isif.alibs.utils.ALog;

/**
 * Created by Spirit on 2018/4/5.
 */

public class MapHandler {
    public static final float MARKER_WIDTH = 60;
    public static final float MARKER_HEIGHT = 60;

    /**
     * 通过drawable获取一个用来显示marker icon的bitmap
     */
    public static Bitmap getMarkerBitmap(Context context, Drawable drawable,boolean isSelect) {
        Bitmap result;
        Bitmap original = AdjustBitmap.drawableToBitmap(drawable);//获得原始bitmap
        float width = DisplayUtil.dip2px(context, MARKER_WIDTH);
        int m_width = original.getWidth();
        int m_height = original.getHeight();
        int min = Math.min(m_width, m_height);
        result = AdjustBitmap.scaleBitmap(original, width / min);
        ALog.d("scaled result----" + "w:" + result.getWidth() + "----" + "h:" + result.getHeight()+",scale===="+width / min);
        if (isSelect){
            return drawBorderMarkerBitmap(result, context.getResources().getColor(R.color.select_marker));
        }else {
            return drawBorderMarkerBitmap4White(result, Color.WHITE);
        }
    }

    /**
     * 为BitMap添加边框
     */
    public static Bitmap drawBorderMarkerBitmap(Bitmap source, int borderColor) {
        if (source == null) return null;
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        float proportion = 2f;

        Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
//        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / proportion;
        //绘制边框
        Paint mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(VwUtils.dp2px(SecondWorkerApplication.getInstance().getApplicationContext(),3));//画笔宽度为4px
        mBorderPaint.setColor(borderColor);//边框颜色
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        mBorderPaint.setAntiAlias(true);
        float r1 = (size - 2 * 4) / proportion;
        canvas.drawCircle(r, r, r1, paint);
//        result.recycle();
        canvas.drawCircle(r, r, r1, mBorderPaint);//画边框
        return result;
    }
    public static Bitmap drawBorderMarkerBitmap4White(Bitmap source, int borderColor) {
        if (source == null) return null;
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        float proportion = 2f;

        Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / proportion;
        //绘制边框
        Paint mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(VwUtils.dp2px(SecondWorkerApplication.getInstance().getApplicationContext(),3));//画笔宽度为4px
        mBorderPaint.setColor(borderColor);//边框颜色
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        mBorderPaint.setAntiAlias(true);
        float r1 = (size - 2 * 4) / proportion;
        canvas.drawCircle(r, r, r1, paint);
//        squared.recycle();
        canvas.drawCircle(r, r, r1, mBorderPaint);//画边框
        return result;
    }

    /**
     * 设置Marker边框的颜色
     */
    public static void setMarkerSelectFlag(final Marker marker, int colorFlag,boolean isSelected) {
        BitmapDescriptor bitmapDescriptor = marker.getIcons().get(0);
        Bitmap source = bitmapDescriptor.getBitmap();
//        getMarkerBitmap(GadArgoApplication.getInstance().getApplicationContext(),source,isSelected);
        if (isSelected){
            marker.setIcon(
                    BitmapDescriptorFactory.fromBitmap(MapHandler.drawBorderMarkerBitmap(source, colorFlag)));
        }else {
            marker.setIcon(
                    BitmapDescriptorFactory.fromBitmap(MapHandler.drawBorderMarkerBitmap4White(source, colorFlag)));
        }
    }

    /**
     * 通过latlng获取marker对象
     */
    public static Marker getMarkerByLatLng(List<Marker> list, LatLng latLng) {
        Marker marker = null;
        for (Marker m : list) {
            if (m.getPosition().equals(latLng)) {
                marker = m;
                break;
            }
        }
        return marker;
    }

    /**
     * 获取基于屏幕中心位置的地图位置
     */
    public static LatLng getMapCenterPoint(AMap aMap, MapView mv) {
        int left = mv.getLeft();
        int top = mv.getTop();
        int right = mv.getRight();
        int bottom = mv.getBottom();
        // 获得屏幕点击的位置
        int x = (int) (mv.getX() + (right - left) / 2);
        int y = (int) (mv.getY() + (bottom - top) / 2);
        Projection projection = aMap.getProjection();
        LatLng pt = projection.fromScreenLocation(new Point(x, y));
        return pt;
    }


    /**
     * 获取可视区域坐标
     *
     * @param aMap
     * @param mv
     * @return
     */
    public static LatLng[] getMapLocationArea(AMap aMap, MapView mv) {
        int left = mv.getLeft();
        int top = mv.getTop();
        int right = mv.getRight();
        int bottom = mv.getBottom();
        Projection projection = aMap.getProjection();

        LatLng leftTop = projection.fromScreenLocation(new Point(left, top));
        LatLng rightBottom = projection.fromScreenLocation(new Point(right, bottom));

        return new LatLng[]{leftTop, rightBottom};
    }

    public static LatLng createLatLng(String lat, String lng) {
        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
            return new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        }
        return null;
    }

}

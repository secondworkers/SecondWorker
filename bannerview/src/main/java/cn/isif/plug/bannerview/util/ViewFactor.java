package cn.isif.plug.bannerview.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;

import cn.isif.plug.bannerview.R;

/**
 * Created by dell on 2016/6/6-15:31.
 */
public class ViewFactor {
    /**
     * 获取一个简单的视图
     *
     * @param url
     * @return
     */
    public static View getImageView(Context context, String url, @DrawableRes int placeHold, @DrawableRes int error) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.item_layout, null);
        ImageView bannerImg = (ImageView) v.findViewById(R.id.img_banner);
        RequestOptions options = new RequestOptions()
                .transform(new GlideRoundTransform(context))
            .fitCenter()
            .centerCrop()
            .circleCrop();//指定图片的缩放类型为centerCrop （圆形）
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(bannerImg);
        return v;
    }
}

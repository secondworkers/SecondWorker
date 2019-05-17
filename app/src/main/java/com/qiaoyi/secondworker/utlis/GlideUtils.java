package com.qiaoyi.secondworker.utlis;

import android.content.Context;

import com.bumptech.glide.request.RequestOptions;
import com.qiaoyi.secondworker.R;

import cn.isif.plug.bannerview.util.GlideRoundTransform;

/**
 * Created on 2019/5/10
 *
 * @author Spirit
 */

public class GlideUtils {
    /**
     * 圆形头像
     * @return
     */
    public static RequestOptions setCircleAvatar(){
      return   new RequestOptions()
                .placeholder(R.mipmap.avatar)
                .error(R.mipmap.avatar)
                .fitCenter()
                .centerCrop()
                .circleCrop();
    }

    /**
     * 圆角图片
     * @param context
     * @param corners 圆角大小
     * @return
     */
    public static RequestOptions setRoundTransform(Context context ,int corners) {
        return new RequestOptions()
                .placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_placeholder)
                .transform(new GlideRoundTransform(context, corners));
    }
}

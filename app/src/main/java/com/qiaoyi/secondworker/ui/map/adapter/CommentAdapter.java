package com.qiaoyi.secondworker.ui.map.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.CommentBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import cn.isif.alibs.utils.StringUtils;


/**
 * Created on 2019/5/12
 *  评价
 * @author Spirit
 */

public class CommentAdapter extends BaseQuickAdapter<CommentBean,BaseViewHolder> {
    Activity activity;
    public CommentAdapter(int item_msg, Activity activity) {
        super(item_msg);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean item) {
        ImageView iv_user_photo = helper.getView(R.id.iv_user_photo);
        Glide.with(activity).load(item.avatar).apply(GlideUtils.setCircleAvatar()).into(iv_user_photo);
        RatingBar ratingbar = helper.getView(R.id.ratingbar);
        if (!StringUtils.isEmpty(item.score)){

            ratingbar.setRating(Float.valueOf(item.score));
            helper.setText(R.id.tv_score,item.score+"分");
        }
        helper.setText(R.id.tv_user_name,item.screenName);
        helper.setText(R.id.tv_time, VwUtils.getTime("yyyy-MM-dd",item.et));
        helper.setText(R.id.tv_describe,item.evaluation);
    }
}

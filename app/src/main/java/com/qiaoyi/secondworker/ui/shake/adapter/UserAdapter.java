package com.qiaoyi.secondworker.ui.shake.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.OnePlanBean;
import com.qiaoyi.secondworker.bean.OneUserInfo;
import com.qiaoyi.secondworker.ui.shake.activity.OnePlanActivity;
import com.qiaoyi.secondworker.utlis.GlideUtils;

/**
 * Created on 2019/7/27
 *
 * @author Spirit
 */

public class UserAdapter extends BaseQuickAdapter<OneUserInfo,BaseViewHolder>{
    Activity context;
    public UserAdapter(int ranking, Activity context) {
        super(ranking);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OneUserInfo item) {
        helper.setText(R.id.tv_rank,String.valueOf(item.order));
        helper.setText(R.id.tv_nick_name,item.userName);
        helper.setText(R.id.tv_text,"贡献募集金额"+item.mujiMoney+"元");
        ImageView iv_avatar = helper.getView(R.id.iv_head_photo);
        Glide.with(context).load(item.avatar).apply(GlideUtils.setCircleAvatar()).into(iv_avatar);
        ProgressBar progress_bar = helper.getView(R.id.progress_bar);
        progress_bar.setProgress(item.precent);
    }
}

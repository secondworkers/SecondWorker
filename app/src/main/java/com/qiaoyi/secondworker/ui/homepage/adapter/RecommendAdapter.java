package com.qiaoyi.secondworker.ui.homepage.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.RedomListBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.ui.homepage.activity.ServiceDetailsActivity;

/**
 * Created on 2019/5/11
 *
 * @author Spirit
 */

public class RecommendAdapter extends BaseQuickAdapter<RedomListBean,BaseViewHolder>{
    Activity activity;
    public RecommendAdapter(int item_recommended, Activity activity) {
        super(item_recommended);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, RedomListBean item) {
        helper.setText(R.id.tv_service,item.goodsName);
        helper.setText(R.id.tv_price,item.price+item.unit+"起");
        ImageView iv_service_icon = helper.getView(R.id.iv_service_icon);
        RequestOptions options = new RequestOptions().
                placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_placeholder)
                .circleCrop();
        Glide.with(activity).load(item.goodsPhoto).apply(options).into(iv_service_icon);
        helper.getView(R.id.rl_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceDetailsActivity.startDetails(activity,item.goodsId,"");
            }
        });
    }
}

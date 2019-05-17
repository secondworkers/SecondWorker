package com.qiaoyi.secondworker.ui.homepage.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceTypeBean;
import com.qiaoyi.secondworker.net.Contact;

/**
 * Created on 2019/4/26
 *
 * @author Spirit
 */

public class AllServiceAdapter extends BaseQuickAdapter<ServiceTypeBean,BaseViewHolder>{
    Activity context;
    public AllServiceAdapter(int layoutResId, Activity context) {
        super(layoutResId);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceTypeBean item) {
        helper.setText(R.id.tv_service_type,item.serviceType);
        ImageView iv_service_type = helper.getView(R.id.iv_service_type);
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.avatar)
                .error(R.mipmap.avatar)
                .fitCenter()
                .centerCrop()
                .circleCrop();
        Glide.with(context).load(item.icon).apply(options).into(iv_service_type);
    }
}

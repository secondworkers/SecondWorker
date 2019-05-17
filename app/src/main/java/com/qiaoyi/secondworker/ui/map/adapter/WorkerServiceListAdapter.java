package com.qiaoyi.secondworker.ui.map.adapter;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceItemListBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.utlis.GlideUtils;

/**
 * Created on 2019/5/12
 *
 * @author Spirit
 */

public class WorkerServiceListAdapter extends BaseQuickAdapter<ServiceItemListBean,BaseViewHolder>{
    Activity activity;
    public WorkerServiceListAdapter(int item_collect, Activity activity) {
        super(item_collect);
        this.activity = activity;

    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceItemListBean item) {
        ImageView iv_img = helper.getView(R.id.iv_img);
        helper.setText(R.id.tv_service,item.serviceItem);
        helper.setText(R.id.tv_price,item.price+item.unit);
        Glide.with(activity).load(item.image).apply(GlideUtils.setRoundTransform(activity,5)).into(iv_img);
    }
}

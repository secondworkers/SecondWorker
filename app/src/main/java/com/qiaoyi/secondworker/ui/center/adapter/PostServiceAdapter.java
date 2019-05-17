package com.qiaoyi.secondworker.ui.center.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceTypeBean;

/**
 * Created on 2019/5/12
 *
 * @author Spirit
 */

public class PostServiceAdapter extends BaseQuickAdapter<ServiceTypeBean,BaseViewHolder>{
    Activity activity;
    public PostServiceAdapter(int item_map_title, Activity activity) {
        super(item_map_title);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceTypeBean item) {
        TextView tv_title = helper.getView(R.id.tv_service_type);
        tv_title.setText(item.serviceType);
    }
}

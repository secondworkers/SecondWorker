package com.qiaoyi.secondworker.ui.center.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceBean;

/**
 * Created on 2019/5/6
 *
 * @author Spirit
 */

public class ServiceTypeAdapter extends BaseQuickAdapter<ServiceBean,BaseViewHolder> {
    public ServiceTypeAdapter(int item_service_type) {
        super(item_service_type);

    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceBean item) {
        helper.setText(R.id.tv_service_type,item.getServicename());
    }
}

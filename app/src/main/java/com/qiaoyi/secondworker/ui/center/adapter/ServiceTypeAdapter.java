package com.qiaoyi.secondworker.ui.center.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceItemBean;
import com.qiaoyi.secondworker.bean.ServiceItemByIdBean;
import com.qiaoyi.secondworker.bean.ServiceTypeBean;

/**
 * Created on 2019/5/6
 *
 * @author Spirit
 */

public class ServiceTypeAdapter extends BaseQuickAdapter<ServiceItemByIdBean,BaseViewHolder> {
    public ServiceTypeAdapter(int item_service_type) {
        super(item_service_type);

    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceItemByIdBean item) {
        helper.setText(R.id.tv_service_type,item.serviceItem);
    }
}

package com.qiaoyi.secondworker.ui.center.adapter;

import android.app.Activity;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;


/**
 * Created on 2019/4/27
 *
 * @author Spirit
 */

public class PoiAdapter extends BaseQuickAdapter<PoiItem,BaseViewHolder>{
    public PoiAdapter(int item_selectlocation, Activity getAddressActivity) {
        super(item_selectlocation);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiItem item) {
        helper.setText(R.id.tv_location_title,item.getTitle());
        helper.setText(R.id.tv_location_msg,
                item.getProvinceName()+
                        item.getCityName()+
                        item.getAdName()+
                        item.getSnippet());
    }
}

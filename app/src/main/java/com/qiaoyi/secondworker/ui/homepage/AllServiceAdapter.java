package com.qiaoyi.secondworker.ui.homepage;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.bean.ServiceBean;

/**
 * Created on 2019/4/26
 *
 * @author Spirit
 */

public class AllServiceAdapter extends BaseQuickAdapter<ServiceBean,BaseViewHolder>{
    public AllServiceAdapter(int layoutResId, Activity homeBaseFragment) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceBean item) {

    }
}

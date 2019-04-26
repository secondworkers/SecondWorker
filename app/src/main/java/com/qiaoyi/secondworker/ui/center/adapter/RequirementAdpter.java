package com.qiaoyi.secondworker.ui.center.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.bean.RequirementBean;

import java.util.List;

/**
 * create on 2019/4/25
 * ling
 * 我的发布需求管理Adapter
 */


public class RequirementAdpter extends BaseQuickAdapter<RequirementBean,BaseViewHolder> {


    public RequirementAdpter(int layoutResId, @Nullable List<RequirementBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RequirementBean item) {

    }
}

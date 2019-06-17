package com.qiaoyi.secondworker.ui.center.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.BankBean;
import com.qiaoyi.secondworker.utlis.VwUtils;

/**
 * Created on 2019/5/29
 *
 * @author Spirit
 */

public class BankListAdapter extends BaseQuickAdapter<BankBean,BaseViewHolder>{
    Activity activity;
    public BankListAdapter(int item_bank, Activity activity) {
        super(item_bank);
        this.activity =activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, BankBean item) {
            helper.setText(R.id.tv_bank_number, VwUtils.getLast4String(item.bankCardNo));
        ImageView iv_bank = helper.getView(R.id.iv_bank);
        Glide.with(activity).load(item.icon).into(iv_bank);
    }
}

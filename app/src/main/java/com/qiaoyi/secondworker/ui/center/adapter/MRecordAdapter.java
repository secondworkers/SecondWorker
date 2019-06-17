package com.qiaoyi.secondworker.ui.center.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.RewardpointBean;

/**
 * Created on 2019/6/11
 *  积分明细
 * @author Spirit
 */

public class MRecordAdapter extends BaseQuickAdapter<RewardpointBean,BaseViewHolder>{
    Context context;
    public MRecordAdapter(int item_withdrawal_recodrd, Context context) {
        super(item_withdrawal_recodrd);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, RewardpointBean item) {
        helper.setText(R.id.tv_record_time,item.createTime);
        helper.setText(R.id.tv_record_title,item.itemName);
        TextView tv_payment_count = helper.getView(R.id.tv_record_count);
        TextView tv_status = helper.getView(R.id.tv_status);
        tv_status.setVisibility(View.GONE);
        if (item.type == 2){
            tv_payment_count.setTextColor(context.getResources().getColor(R.color.text_red));
            tv_payment_count.setText("-"+item.consume);
        }else {
            tv_payment_count.setTextColor(context.getResources().getColor(R.color.text_blue));
            tv_payment_count.setText("+"+item.consume);
        }
    }
}

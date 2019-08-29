package com.qiaoyi.secondworker.ui.center.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.PaymentDetailsBean;

/**
 * Created on 2019/5/29
 *  账单明细
 * @author Spirit
 */

public class PaymentDetailsAdapter extends BaseQuickAdapter<PaymentDetailsBean,BaseViewHolder>{
    Context context;
    public PaymentDetailsAdapter(int item_payment_details, Context context) {
        super(item_payment_details);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PaymentDetailsBean item) {
        helper.setText(R.id.tv_payment_title,item.itemName);
        helper.setText(R.id.tv_payment_time,item.createTime);
        TextView tv_payment_count = helper.getView(R.id.tv_payment_count);
        tv_payment_count.setText(String.valueOf(item.money));
        if (item.type == 1||item.type ==3||item.type ==6){
            tv_payment_count.setTextColor(context.getResources().getColor(R.color.text_red));
            tv_payment_count.setText("-"+item.money);
        }else {
            tv_payment_count.setTextColor(context.getResources().getColor(R.color.text_blue));
            tv_payment_count.setText("+"+item.money);
        }
    }
}

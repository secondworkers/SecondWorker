package com.qiaoyi.secondworker.ui.center.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.PaymentDetailsBean;

/**
 * Created on 2019/5/29
 *
 * @author Spirit
 */

public class RecordAdapter extends BaseQuickAdapter<PaymentDetailsBean,BaseViewHolder>{
    Context context;
    public RecordAdapter(int item_withdrawal_recodrd, Context context) {
        super(item_withdrawal_recodrd);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PaymentDetailsBean item) {
        helper.setText(R.id.tv_record_time,item.createTime);
        TextView tv_payment_count = helper.getView(R.id.tv_record_count);
        TextView tv_status = helper.getView(R.id.tv_status);
        tv_payment_count.setText("-"+item.money);
        if (item.auditStatus.equals("shenhezhong")){
            tv_status.setText("审核中");
            tv_status.setTextColor(context.getResources().getColor(R.color.text_blue));
        }else if (item.auditStatus.equals("yitongguo")){
            tv_status.setText("审核成功");
            tv_status.setTextColor(context.getResources().getColor(R.color.text_blue));
        }else if (item.auditStatus.equals("weitongguo")){
            tv_status.setText("未通过");
            tv_status.setTextColor(context.getResources().getColor(R.color.text_grey));
        }
    }
}

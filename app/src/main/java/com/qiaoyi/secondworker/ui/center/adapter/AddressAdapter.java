package com.qiaoyi.secondworker.ui.center.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.AddressBean;
import com.qiaoyi.secondworker.ui.center.LocationActivity;
import com.qiaoyi.secondworker.ui.center.SelectLocationActivity;


/**
 * Created on 2019/4/23
 *  地址管理adapter
 * @author Spirit
 */

public class AddressAdapter extends BaseQuickAdapter<AddressBean,BaseViewHolder>{
    Activity context;
    public AddressAdapter(int layoutResId, Activity context) {
        super(layoutResId);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressBean item) {
        helper.setText(R.id.tv_location,"泰山国际大厦");
        ImageView iv_edit = helper.getView(R.id.iv_edit);
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationActivity.startLocationActivity(context,"edit");
            }
        });
    }
}

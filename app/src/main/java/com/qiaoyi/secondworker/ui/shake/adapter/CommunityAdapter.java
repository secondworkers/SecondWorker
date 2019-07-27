package com.qiaoyi.secondworker.ui.shake.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.CommunityBean;

/**
 * Created on 2019/7/26
 *
 * @author Spirit
 */

public class CommunityAdapter extends BaseQuickAdapter<CommunityBean,BaseViewHolder>{
    Activity activity;
    public CommunityAdapter(int item_community, Activity activity) {
        super(item_community);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityBean item) {
        helper.setText(R.id.tv_name,item.shequName);
        TextView tv_name = helper.getView(R.id.tv_name);
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

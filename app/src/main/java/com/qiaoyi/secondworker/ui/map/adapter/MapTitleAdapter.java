package com.qiaoyi.secondworker.ui.map.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceTypeBean;

/**
 * Created on 2019/5/12
 *  
 * @author Spirit
 */

public class MapTitleAdapter extends BaseQuickAdapter<ServiceTypeBean,BaseViewHolder>{
    Activity activity;
    int lastPosition = -1;
    public MapTitleAdapter(int item_map_title, Activity activity) {
        super(item_map_title);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceTypeBean item) {
        TextView tv_title = helper.getView(R.id.tv_title);
        tv_title.setText(item.serviceType);
        if (helper.getPosition() == 0){
            tv_title.setTextColor(activity.getResources().getColor(R.color.text_black));
            lastPosition = 0;
        }
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPosition != helper.getPosition()){
                    tv_title.setTextColor(activity.getResources().getColor(R.color.text_black));
                    lastPosition = -1;
                }else {
                    tv_title.setTextColor(activity.getResources().getColor(R.color.title_grey));
                }
            }
        });
    }
    public interface PriorityListener {
        /**
         * 回调函数，用于在title的监听事件触发后刷新Activity的UI显示
         */
        void refreshPriorityUI(String name);
    }

    private PriorityListener listener;
}

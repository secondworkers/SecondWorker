package com.qiaoyi.secondworker.ui.map.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceTypeBean;

import java.util.List;

/**
 * Created on 2019/5/12
 *  
 * @author Spirit
 */

public class MapTitle2Adapter extends RecyclerView.Adapter<MapTitle2Adapter.ViewHolder>{
    Activity activity;
    int mposition = 0;
    private List<ServiceTypeBean> mList;


    public MapTitle2Adapter(List<ServiceTypeBean> mList,Activity activity) {
        this.mList = mList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_title,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceTypeBean serviceTypeBean = mList.get(position);
        holder.mTitle.setText(serviceTypeBean.serviceType);
/*
        添加选中的打勾显示
         */
        holder.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将点击的位置传出去
                mposition = holder.getAdapterPosition();
                //在点击监听里最好写入setVisibility(View.VISIBLE);这样可以避免效果会闪
                holder.mTitle.setTextColor(activity.getResources().getColor(R.color.text_blue));
                listener.refreshPriorityUI(serviceTypeBean.serviceTypeId);
                //刷新界面 notify 通知Data 数据set设置Changed变化
                //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                notifyDataSetChanged();
            }
        });
        if (position == mposition) {
            holder.mTitle.setTextColor(activity.getResources().getColor(R.color.text_blue));
        } else {
            holder.mTitle.setTextColor(activity.getResources().getColor(R.color.title_grey));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    public interface PriorityListener {
        /**
         * 回调函数，用于在title的监听事件触发后刷新Activity的UI显示
         */
        void refreshPriorityUI(String sTypeId);
    }

    private PriorityListener listener;
    public void setOnItemClickListener(PriorityListener listener){
        this.listener = listener;
    }
}

package com.qiaoyi.secondworker.ui.center.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.AddressBean;
import com.qiaoyi.secondworker.bean.MessageBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/24
 * ling
 * 我的信息管理Adapter
 */
public class MessageAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> implements View.OnClickListener {


    public MessageAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean item) {
        helper.setText(R.id.tv_system_title, "系统消息");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = formatter.format(curDate);
        helper.setText(R.id.tv_system_time, "str");
        ImageView tv_system_time = helper.getView(R.id.tv_system_time);
        tv_system_time.setOnClickListener(this);
        helper.setText(R.id.tv_messgae, "您发布的需求信息已有人接单啦~快去看看");
        TextView view = helper.getView(R.id.tv_messgae);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_system_time:
            case R.id.tv_messgae:
                ToastUtils.showLong("ooooooooo");
                break;
        }
    }
}

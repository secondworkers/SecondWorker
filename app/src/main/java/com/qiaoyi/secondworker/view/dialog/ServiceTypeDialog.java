package com.qiaoyi.secondworker.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.chad.library.adapter.base.BaseViewHolder;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceBean;
import com.qiaoyi.secondworker.ui.ItemDecoration.GridSpacingItemDecoration;
import com.qiaoyi.secondworker.ui.center.adapter.ServiceTypeAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.ArrayList;

/**
 * Created on 2019/5/5
 *
 * @author Spirit
 */

public class ServiceTypeDialog extends Dialog implements View.OnClickListener {

    private TextView tv_title;
    private RecyclerView rv_service_list;
    private TextView tv_cancle;
    private TextView tv_select;
    private Activity context;

    public interface ServiceChooseListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void refreshDialogUI(String service_id,String service_name);
    }

    private ServiceChooseListener listener;

    public ServiceTypeDialog(@NonNull Activity context,ServiceChooseListener listener) {
        super(context, R.style.date_dialog_style);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_service_type);
        computeWeigth();
        initView();
        initData();
    }

    private void initView() {
        rv_service_list = findViewById(R.id.rv_service_list);
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_select = findViewById(R.id.tv_select);
        tv_select.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
    }
    void initData(){
        ArrayList<ServiceBean> serviceBeans = new ArrayList<>();
        ServiceBean bean = new ServiceBean();
        for (int i = 0; i < 10; i++) {
//            bean.setServicename("服务"+ i);
            serviceBeans.add(bean);
        }
        ServiceTypeAdapter listAdapter = new ServiceTypeAdapter(R.layout.item_service_type);
        int spanCount = 4;
        int spacing = VwUtils.getSW(context, 0);//item间隙宽度
        int itemDecorationCount = rv_service_list.getItemDecorationCount();
        if (itemDecorationCount == 0) {
            rv_service_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
            rv_service_list.addOnItemTouchListener(new OnItemClickListener() {
                @Override public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view,
                                                        final int position) {
                    ServiceBean item = (ServiceBean) adapter.getItem(position);
                    listener.refreshDialogUI(item.serviceType,item.id);
                    dismiss();
                }
            });
        }
        GridLayoutManager manager = new GridLayoutManager(context, spanCount);
        rv_service_list.setLayoutManager(manager);
        listAdapter.setEnableLoadMore(false);
        listAdapter.addData(serviceBeans);//
        rv_service_list.setAdapter(listAdapter);
    }
    private void computeWeigth() {
        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.y = 0;
        window.setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancle:
                break;
            case R.id.tv_select:
                break;
        }
    }
}

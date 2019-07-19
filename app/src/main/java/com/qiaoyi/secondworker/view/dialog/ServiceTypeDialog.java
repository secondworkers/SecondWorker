package com.qiaoyi.secondworker.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceTypeBean;
import com.qiaoyi.secondworker.bean.WrapServiceBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiHome;
import com.qiaoyi.secondworker.ui.ItemDecoration.GridSpacingItemDecoration;
import com.qiaoyi.secondworker.ui.center.adapter.PostServiceAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

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
    private List<ServiceTypeBean> result;
    private PostServiceAdapter listAdapter;

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
        initServiceType();
        setCanceledOnTouchOutside(true);
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
        listAdapter = new PostServiceAdapter(R.layout.item_service_type,context);
        int spanCount = 4;
        int spacing = VwUtils.getSW(context, 0);//item间隙宽度
        int itemDecorationCount = rv_service_list.getItemDecorationCount();
        if (itemDecorationCount == 0) {
            rv_service_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
            rv_service_list.addOnItemTouchListener(new OnItemClickListener() {
                @Override public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view,
                                                        final int position) {
                    ServiceTypeBean item = (ServiceTypeBean) adapter.getItem(position);
                    listener.refreshDialogUI(item.id,item.serviceType);
                    dismiss();
                }
            });
        }
        GridLayoutManager manager = new GridLayoutManager(context, spanCount);
        rv_service_list.setLayoutManager(manager);
        listAdapter.setEnableLoadMore(false);
        rv_service_list.setAdapter(listAdapter);
    }
    public void initServiceType() {
        ApiHome.getServiceType("map",new ServiceCallBack<WrapServiceBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapServiceBean> payload) {
                WrapServiceBean body = payload.body();
                result = body.result;
                listAdapter.addData(result);
            }
        });
    }
    private void computeWeigth() {
        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setGravity(Gravity.BOTTOM);
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

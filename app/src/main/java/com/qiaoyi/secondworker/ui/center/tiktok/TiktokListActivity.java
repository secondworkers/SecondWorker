package com.qiaoyi.secondworker.ui.center.tiktok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.TaskDetailBean;
import com.qiaoyi.secondworker.bean.WrapTaskBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/7/7
 *
 * @author Spirit
 */

public class TiktokListActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout view_back;
    private RecyclerView rv_list;
    private TextView tv_title_txt;
    private TiktokAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trill_everyday_task);
        VwUtils.fixScreen(this);
        initView();
        requestData();
        initData();
    }

    private void initView() {
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);

        view_back.setOnClickListener(this);
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_title_txt.setText("每日任务");
    }

    private void initData() {
        adapter = new TiktokAdapter(R.layout.item_trill_everyday_task, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setAdapter(adapter);
        rv_list.setLayoutManager(manager);
        rv_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                TaskDetailBean item = (TaskDetailBean) adapter.getItem(position);
                String id = item.id;
                Intent intent = new Intent(TiktokListActivity.this, TiktokTaskDetailActivity.class);
                intent.putExtra("task_id",id);
                startActivity(intent);
            }
        });
    }

    private void requestData() {
        ApiUserService.queryTask("", new ServiceCallBack<WrapTaskBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapTaskBean> payload) {
                List<TaskDetailBean> result = payload.body().result;
                adapter.addData(result);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
        }
    }
}

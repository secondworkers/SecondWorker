package com.qiaoyi.secondworker.ui.shake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.CommunityBean;
import com.qiaoyi.secondworker.bean.WrapCommunityBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.shake.adapter.CommunityAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.dialog.SelectCommunityDialog;

import java.util.List;

import cn.isif.alibs.utils.SharePreferenceUtils;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/7/25
 *
 * @author Spirit
 */

public class CommunitySearchActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt,tv_done;
    private RelativeLayout view_back;
    private EditText et_search;
    private LinearLayout ll_total;
    private String city_code;
    private RecyclerView rv_list;
    private String communityId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_community);
        VwUtils.fixScreen(this);
        city_code = getIntent().getStringExtra("city_code");
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_done = (TextView) findViewById(R.id.tv_done);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        et_search = (EditText) findViewById(R.id.et_search);
        ll_total = (LinearLayout) findViewById(R.id.ll_total);
        tv_title_txt.setText("搜索小区");
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        view_back.setOnClickListener(this);
        tv_done.setOnClickListener(this);
        tv_done.setClickable(false);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String c_name = et_search.getText().toString().trim();
                    if(TextUtils.isEmpty(c_name)){
                        ToastUtils.showShort("请输入要搜索的社区");
                        return true;
                    }
                    searchCommunity(c_name);
                    return true;
                }
                return false;
            }
        });
    }

    private void searchCommunity(String c_name) {
        ApiUserService.selectShequ(city_code, c_name, new ServiceCallBack<WrapCommunityBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapCommunityBean> payload) {
                List<CommunityBean> result = payload.body().result;
                CommunityAdapter adapter = new CommunityAdapter(R.layout.item_community,CommunitySearchActivity.this);
                adapter.addData(result);
                LinearLayoutManager manager = new LinearLayoutManager(CommunitySearchActivity.this);
                rv_list.setAdapter(adapter);
                rv_list.setLayoutManager(manager);
                rv_list.addOnItemTouchListener(new OnItemClickListener() {
                    @Override
                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                        CommunityBean item = (CommunityBean) adapter.getItem(position);
                        TextView tv_name = view.findViewById(R.id.tv_name);
                        new SelectCommunityDialog(CommunitySearchActivity.this, new SelectCommunityDialog.DoneClickListener() {
                            @Override
                            public void refreshUI() {
                                item.setSelected(true);
                                tv_name.setTextColor(getResources().getColor(R.color.text_blue));
                                view.findViewById(R.id.iv_done).setVisibility(View.VISIBLE);
                                communityId = item.id;
                                ApiUserService.chooseShequ(communityId, new ServiceCallBack() {
                                    @Override
                                    public void failed(String code, String errorInfo, String source) {
                                        ToastUtils.showShort(errorInfo);
                                    }

                                    @Override
                                    public void success(RespBean resp, Response payload) {
                                        SharePreferenceUtils.write("sqID","sqID",communityId);
                                        startActivity(new Intent(CommunitySearchActivity.this,OnePlanActivity.class));
                                        finish();
                                    }
                                });
                            }
                        }, new SelectCommunityDialog.CancelClickListener() {
                            @Override
                            public void refreshUI() {
                                finish();
                            }
                        }).show();
                    }
                });
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_done:
                ApiUserService.chooseShequ(communityId, new ServiceCallBack() {
                    @Override
                    public void failed(String code, String errorInfo, String source) {
                        ToastUtils.showShort(errorInfo);
                    }

                    @Override
                    public void success(RespBean resp, Response payload) {
                        startActivity(new Intent(CommunitySearchActivity.this,OnePlanActivity.class));
                        finish();
                    }
                });
                break;
        }
    }
}

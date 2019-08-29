package com.qiaoyi.secondworker.ui.shake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.MainActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.CommunityBean;
import com.qiaoyi.secondworker.bean.WrapCommunityBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.address.CityPickerActivity;
import com.qiaoyi.secondworker.ui.shake.adapter.CommunityAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.dialog.SelectCommunityDialog;

import java.util.List;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.SharePreferenceUtils;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/7/25
 *
 * @author Spirit
 */

public class CommunitySelectActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_select;
    private TextView skip;
    private RelativeLayout ll_top;
    private TextView cityName;
    private LinearLayout ll_search;
    private TextView tv_title,tv_done,tv_add;
    private RecyclerView rv_list;
    private LinearLayout ll_total;
//    private String city_code;
    private String communityId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_community);
        VwUtils.fixScreen(this);
        toStartLocation();
        initView();
        requestData();
    }

    private void requestData() {
        ApiUserService.selectShequ(cityCode, "", new ServiceCallBack<WrapCommunityBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                    ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapCommunityBean> payload) {
                List<CommunityBean> result = payload.body().result;
                CommunityAdapter adapter = new CommunityAdapter(R.layout.item_community,CommunitySelectActivity.this);
                adapter.addData(result);
                LinearLayoutManager manager = new LinearLayoutManager(CommunitySelectActivity.this);
                rv_list.setAdapter(adapter);
                rv_list.setLayoutManager(manager);
                rv_list.addOnItemTouchListener(new OnItemClickListener() {
                    @Override
                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                        CommunityBean item = (CommunityBean) adapter.getItem(position);
                        TextView tv_name = view.findViewById(R.id.tv_name);
                            new SelectCommunityDialog(CommunitySelectActivity.this, "请确认选择的社区是否正确，选择后不能更改社区！",new SelectCommunityDialog.DoneClickListener() {
                                @Override
                                public void refreshUI() {
                                    item.setSelected(true);
                                    tv_name.setTextColor(getResources().getColor(R.color.text_blue));
                                    view.findViewById(R.id.iv_done).setVisibility(View.VISIBLE);
                                    communityId = item.id;
                                    ApiUserService.chooseShequ(communityId,"","","","","", new ServiceCallBack() {
                                        @Override
                                        public void failed(String code, String errorInfo, String source) {
                                            ToastUtils.showShort(errorInfo);
                                        }

                                        @Override
                                        public void success(RespBean resp, Response payload) {
                                            SharePreferenceUtils.write("sqID","sqID",communityId);
                                            startActivity(new Intent(CommunitySelectActivity.this,OnePlanActivity.class));
                                            finish();
                                        }
                                    });
                                }
                            }, new SelectCommunityDialog.CancelClickListener() {
                                @Override
                                public void refreshUI() {
                                    requestData();
                                }
                            }).show();
                    }
                });
            }
        });
    }

    private void initView() {
        tv_select = (TextView) findViewById(R.id.tv_select);
        tv_add = (TextView) findViewById(R.id.tv_add);
        skip = (TextView) findViewById(R.id.skip);
        ll_top = (RelativeLayout) findViewById(R.id.ll_top);
        cityName = (TextView) findViewById(R.id.city);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_done = (TextView) findViewById(R.id.tv_done);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        ll_total = (LinearLayout) findViewById(R.id.ll_total);
        cityName.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        skip.setOnClickListener(this);
        tv_done.setOnClickListener(this);
        ll_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                break;
            case R.id.skip:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.ll_search:
                Intent intent1 = new Intent(this, CommunitySearchActivity.class);
                intent1.putExtra("city_code",cityCode);
                startActivity(intent1);
                break;
            case R.id.city:
                Intent intent = new Intent(this, CityPickerActivity.class);
                intent.putExtra("location_city",city);
                startActivityForResult(intent,4001);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode){
            if (requestCode == 4001){
                cityName.setText(data.getStringExtra("select_city"));
//                city_code = data.getStringExtra("city_code");
                requestData();
            }
        }
    }
}

package com.qiaoyi.secondworker.ui.homepage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.SearchServiceBean;
import com.qiaoyi.secondworker.bean.ServiceItemBean;
import com.qiaoyi.secondworker.bean.ServiceItemByIdBean;
import com.qiaoyi.secondworker.bean.ServiceTypeBean;
import com.qiaoyi.secondworker.bean.WrapSearchServiceBean;
import com.qiaoyi.secondworker.bean.WrapServiceItemByIdBean;
import com.qiaoyi.secondworker.cache.ACache;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiHome;
import com.qiaoyi.secondworker.ui.ItemDecoration.GridSpacingItemDecoration;
import com.qiaoyi.secondworker.ui.center.adapter.ServiceTypeAdapter;
import com.qiaoyi.secondworker.ui.homepage.adapter.SearchAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.isif.alibs.utils.ALog;

/**
 * Created on 2019/5/6
 *  服务列表
 * @author Spirit
 */

public class ServiceListActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private ImageView iv_msg;
    private RelativeLayout view_right;
    private View view_title_line;
    private TextView tv_sort;
    private ImageView iv_sort;
    private LinearLayout ll_sort;
    private TextView tv_service_type;
    private ImageView iv_service_type;
    private LinearLayout ll_service_type,ll_spinner;
    private RecyclerView rv_list;
    List<String> sortData = new LinkedList<>(Arrays.asList("综合排序", "星级", "销量"));
    private ArrayAdapter testDataAdapter;
    private PopupWindow typeSelectPopup;
    private PopupWindow typeServicePopup;
    private boolean isSelected = false;//item isSelected
    private boolean isSortSelected = false;
    private String s_name;
    private String s_type_id;
    private List<ServiceItemByIdBean> result;
    private String serviceItemId = "";
    private String sort = "";
    private List<SearchServiceBean> searchResult;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_service_list);
        Intent intent = getIntent();
        s_name = intent.getStringExtra("service_name");
        s_type_id = intent.getStringExtra("item_id");
        ACache mCache = ACache.get(getApplication());
        lat = Double.valueOf(mCache.getAsString("user_loc_lat"));
        lng = Double.valueOf(mCache.getAsString("user_loc_lng"));
        initView();
        requestData();
        initData();
    }

    private void requestData() {
//        服务项目数据
        ApiHome.getServiceItemById(s_type_id, new ServiceCallBack<WrapServiceItemByIdBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapServiceItemByIdBean> payload) {
                result = payload.body().result;
                requestList();
            }
        });
    }


    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        iv_msg = (ImageView) findViewById(R.id.iv_msg);
        view_right = (RelativeLayout) findViewById(R.id.view_right);
        view_title_line = (View) findViewById(R.id.view_title_line);
        tv_sort = (TextView) findViewById(R.id.tv_sort);
        iv_sort = (ImageView) findViewById(R.id.iv_sort);
        ll_sort = (LinearLayout) findViewById(R.id.ll_sort);
        tv_service_type = (TextView) findViewById(R.id.tv_service_type);
        iv_service_type = (ImageView) findViewById(R.id.iv_service_type);
        ll_service_type = (LinearLayout) findViewById(R.id.ll_service_type);
        ll_spinner = (LinearLayout) findViewById(R.id.ll_spinner);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);

        view_back.setOnClickListener(this);
        view_right.setOnClickListener(this);
        ll_service_type.setOnClickListener(this);
        ll_sort.setOnClickListener(this);
    }
    private void initData() {
        tv_title_txt.setText(s_name);//TODO:
        iv_msg.setImageDrawable(getResources().getDrawable(R.mipmap.iv_search));
        iv_msg.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchAdapter = new SearchAdapter(R.layout.item_search_service, this);

        rv_list.setLayoutManager(layoutManager);
        rv_list.setAdapter(searchAdapter);
        rv_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchServiceBean item = (SearchServiceBean) adapter.getItem(position);
                ServiceDetailsActivity.startDetails(ServiceListActivity.this,item.serviceItemId);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.view_right:
                startActivity(new Intent(this,SearchActivity.class));
                break;
            case R.id.ll_sort:
                initSelectPopup(sortData);
                if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
                    iv_sort.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_up));
                    typeSelectPopup.showAsDropDown(tv_sort, 0, 10);
                }
                break;
            case R.id.ll_service_type:
                initServiceTypePopup();
                if (typeServicePopup != null) {
                    ALog.e("STATUS = "+typeServicePopup.isShowing());
                    if (typeServicePopup.isShowing()){
                        iv_service_type.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_up));
                        typeServicePopup.showAsDropDown(tv_service_type, 0, 10);
                    }
                }
                break;
        }
    }
    /**
     * 初始化popup窗口
     */
    private void initSelectPopup(List<String> data) {
        ListView mTypeLv = new ListView(this);
        // 设置适配器
        testDataAdapter = new ArrayAdapter<String>(this, R.layout.sort_list_item, data);
        mTypeLv.setDividerHeight(0);
        mTypeLv.setAdapter(testDataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 在这里获取item数据
                String value = data.get(position);
                // 把选择的数据展示对应的TextView上
                tv_sort.setText(value);
                if (value.equals("综合排序")){
                    sort = "";
                }else if (value.equals("星级")){
                    sort = "score";
                }else if (value.equals("销量")){
                    sort = "counts";
                }
                tv_sort.setTextColor(getResources().getColor(R.color.text_blue));
                iv_sort.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_down));
                isSortSelected = true;
                // 选择完后关闭popup窗口
                typeSelectPopup.dismiss();
                requestList();
            }
        });
        typeSelectPopup = new PopupWindow(mTypeLv, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景图片

        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                if (isSortSelected){
                    iv_sort.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_down));
                }else {
                    iv_sort.setImageDrawable(getResources().getDrawable(R.mipmap.iv_location_right));
                }
                typeSelectPopup.dismiss();
            }
        });
    }

    /**
     * 请求列表数据
     */
    private void requestList() {
        ApiHome.searchSearch("", lng, lat, sort, "", serviceItemId, s_type_id,new ServiceCallBack<WrapSearchServiceBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapSearchServiceBean> payload) {
                searchResult = payload.body().result;
                searchAdapter.setNewData(searchResult);
                searchAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initServiceTypePopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popwindow_recyclerview,null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        int spanCount = 3;
        int spacing = VwUtils.getSW(this, 20);//item间隙宽度
        int itemDecorationCount = recyclerView.getItemDecorationCount();
        if (itemDecorationCount == 0) {
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
            recyclerView.addOnItemTouchListener(new OnItemClickListener() {
                @Override public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view,
                                                        final int position) {
                    ServiceItemByIdBean item = (ServiceItemByIdBean) adapter.getItem(position);
                    serviceItemId = item.id;
                    tv_service_type.setText(item.serviceItem);
                    tv_service_type.setTextColor(getResources().getColor(R.color.text_blue));
                    iv_service_type.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_down));
                    isSelected = true;
                    typeServicePopup.dismiss();
                    requestList();
                }
            });
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        ServiceTypeAdapter scoreTeamAdapter = new ServiceTypeAdapter(R.layout.item_service_type);
        scoreTeamAdapter.addData(result);
        recyclerView.setAdapter(scoreTeamAdapter);
        typeServicePopup = new PopupWindow(ll_spinner, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        typeServicePopup.setContentView(view);
        typeServicePopup.setFocusable(true);
        typeServicePopup.showAsDropDown(ll_spinner);
        typeServicePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                if (isSelected){
                    iv_service_type.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_down));
                }else {
                    iv_service_type.setImageDrawable(getResources().getDrawable(R.mipmap.iv_location_right));
                }
                typeServicePopup.dismiss();
            }
        });
    }
}

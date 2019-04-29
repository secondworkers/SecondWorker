package com.qiaoyi.secondworker.ui.center.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.LocationBean;
import com.qiaoyi.secondworker.ui.center.adapter.PoiAdapter;
import com.qiaoyi.secondworker.utlis.UmengUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/28
 *
 * @author Spirit
 */

public class LocationSearchActivity extends BaseActivity implements View.OnClickListener, PoiSearch.OnPoiSearchListener {
    private EditText et_search;
    private TextView tv_cancel;
    private RelativeLayout rl_search;
    private RecyclerView rv_list;
    private PoiAdapter adapter;
    private String from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        toStartLocation();
        initView();
    }

    private void initView() {
        et_search = (EditText) findViewById(R.id.et_search);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);

        tv_cancel.setOnClickListener(this);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == 0) {
                    checkSubmit();
                }
                return true;
            }
        });
        initData();
    }
    private void initData() {
        adapter = new PoiAdapter(R.layout.item_selectlocation,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        rv_list.setAdapter(adapter);

        rv_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                PoiItem item = (PoiItem) adapter.getItem(position);
                if (TextUtils.equals(from,"home")){
                    EventBus.getDefault().post(new LocationBean(item.getLatLonPoint().getLatitude(),
                            item.getLatLonPoint().getLongitude(),
                            item.getTitle(),
                            item.getSnippet()));
                    finish();
                }else {

                }
            }
        });
    }
    private void checkSubmit() {
        String content = et_search.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            VwUtils.hideKeyboard(et_search);
            submit(content);
        } else {
            showToast("请输入正确地点");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                hintKbTwo();
                finish();
                break;
        }
    }
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    private void submit(String search) {
        // validate
        // TODO validate success, do something
        LatLonPoint latLonPoint = new LatLonPoint(lat,lng);
        doSearchQuery(latLonPoint,search);

    }
    private void doSearchQuery(LatLonPoint searchLatlonPoint,String search) {
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        PoiSearch.Query query = new PoiSearch.Query(search, "", city);
        query.setPageSize(mPageSize);
        query.setPageNum(0);
        PoiSearch poisearch = new PoiSearch(this, query);
        poisearch.setOnPoiSearchListener(this);
        poisearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 1000, true));
        poisearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getPois().size() > 0) {
                List<PoiItem> poiItems = poiResult.getPois();
                adapter.setNewData(poiItems);
                adapter.notifyDataSetChanged();
            } else {
                ALog.e("\"无结果\"");
            }
        } else {
            ALog.e("搜索失败");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public void onResume() {
        super.onResume();
        UmengUtils.startStatistics4Activity(this,"搜索页面");
    }
    public void onPause() {
        super.onPause();
        UmengUtils.endStatistics4Activity(this,"搜索页面");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

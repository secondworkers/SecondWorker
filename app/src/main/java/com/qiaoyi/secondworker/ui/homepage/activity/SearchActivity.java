package com.qiaoyi.secondworker.ui.homepage.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.SearchServiceBean;
import com.qiaoyi.secondworker.bean.WrapSearchServiceBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiHome;
import com.qiaoyi.secondworker.ui.homepage.adapter.SearchAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2019/5/6
 *  搜索
 * @author Spirit
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_search;
    private TextView tv_search,tv_sort,tv_distance;
    private RecyclerView rv_list;
    List<String> sortData = new LinkedList<>(Arrays.asList("综合排序", "星级", "销量"));
    List<String> distanceData = new LinkedList<>(Arrays.asList("500m", "1km", "3km", "5km",
            "10km"));
    private PopupWindow typeSelectPopup;
    private ArrayAdapter testDataAdapter;
    private LinearLayout ll_sort;
    private LinearLayout ll_distance;
    private ImageView iv_sort;
    private ImageView iv_distance;
    private boolean isSelect = false;
    private boolean isSortSelected = false;
    private List<SearchServiceBean> searchResult;
    private String sort = "";
    private SearchAdapter searchAdapter;
    private String search;
    private String distance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        toStartLocation();
        setContentView(R.layout.activity_search);
        initView();
        initData();
    }

    private void initView() {
        et_search = (EditText) findViewById(R.id.et_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_sort = findViewById(R.id.tv_sort);
        tv_distance = findViewById(R.id.tv_distance);
        ll_sort = findViewById(R.id.ll_sort);
        ll_distance = findViewById(R.id.ll_distance);
        iv_sort = findViewById(R.id.iv_sort);
        iv_distance = findViewById(R.id.iv_distance);
        tv_search.setOnClickListener(this);
        ll_sort.setOnClickListener(this);
        ll_distance.setOnClickListener(this);
    }
    private void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchAdapter = new SearchAdapter(R.layout.item_search_service, this);
        rv_list.setLayoutManager(layoutManager);
        rv_list.setAdapter(searchAdapter);
        rv_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchServiceBean item = (SearchServiceBean) adapter.getItem(position);
                ServiceDetailsActivity.startDetails(SearchActivity.this,item.goodsId,"");
            }
        });
    }
    /**
     * 请求列表数据
     */
    private void requestList() {
        ApiHome.searchSearch(search, lng, lat, sort, distance, "", "",new ServiceCallBack<WrapSearchServiceBean>() {
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                submit();
                break;
            case R.id.ll_sort:
                initSelectPopup(sortData,true);
                // 使用isShowing()检查popup窗口是否在显示状态
                if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
                    iv_sort.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_up));
                    typeSelectPopup.showAsDropDown(tv_sort, 0, 10);
                }
                break;
            case R.id.ll_distance:
                initSelectPopup(distanceData,false);
                // 使用isShowing()检查popup窗口是否在显示状态
                if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
                    iv_distance.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_up));
                    typeSelectPopup.showAsDropDown(tv_distance, 0, 10);
                }
                break;
        }
    }
    /**
     * 初始化popup窗口
     */
    private void initSelectPopup(List<String> data,boolean isSort) {
        ListView mTypeLv = new ListView(this);
        // 设置适配器
        testDataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list_item, data);
        mTypeLv.setDividerHeight(0);
        mTypeLv.setAdapter(testDataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 在这里获取item数据
                String value = data.get(position);
                // 把选择的数据展示对应的TextView上
                if (isSort){
                    if (value.equals("综合排序")){
                        sort = "";
                    }else if (value.equals("星级")){
                        sort = "score";
                    }else if (value.equals("销量")){
                        sort = "counts";
                    }
                    tv_sort.setText(value);
                    tv_sort.setTextColor(getResources().getColor(R.color.text_blue));
                    iv_sort.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_down));
                    isSortSelected = true;
                }else {
                    if (value.equals("500m")){
                        distance = "0.5";
                    } else if (value.equals("1km")){
                        distance = "1";
                    } else if (value.equals("3km")){
                        distance = "3";
                    } else if (value.equals("5km")){
                        distance = "5";
                    } else if (value.equals("10km")){
                        distance = "10";
                    } else {
                        distance="";
                    }
                    tv_distance.setText(value);
                    tv_distance.setTextColor(getResources().getColor(R.color.text_blue));
                    iv_distance.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_down));
                    isSelect = true;
                }
                // 选择完后关闭popup窗口
                typeSelectPopup.dismiss();
                requestList();
            }
        });
        typeSelectPopup = new PopupWindow(mTypeLv, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景图片
//        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_corner);
//        typeSelectPopup.setBackgroundDrawable(drawable);
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
                if (isSelect){
                    iv_distance.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blue_arrow_down));
                }else {
                    iv_distance.setImageDrawable(getResources().getDrawable(R.mipmap.iv_location_right));
                }
                typeSelectPopup.dismiss();
            }
        });
    }

    private void submit() {
        // validate
        search = et_search.getText().toString().trim();
        if (TextUtils.isEmpty(search)) {
            Toast.makeText(this, "请您输入服务名称", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        requestList();

    }
}

package com.qiaoyi.secondworker.ui.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceBean;
import com.qiaoyi.secondworker.ui.BaseFragment;
import com.qiaoyi.secondworker.ui.ItemDecoration.GridSpacingItemDecoration;
import com.qiaoyi.secondworker.ui.center.activity.GetAddressActivity;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;

/**
 * Created on 2019/4/19
 *  首页
 * @author Spirit
 */

public class HomeBaseFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private ImageView iv_location;
    private TextView tv_location;
    private ImageView iv_msg;
    private TextView tv_search;
    private XBanner xbanner;
    private TextView tv_recommended_more;
    private RecyclerView mRecyclerView;
    private RecyclerView rv_service;
    private AllServiceAdapter listAdapter;
    public ArrayList<ServiceBean> crops = new ArrayList<>();
    public HomeBaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(getActivity());
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        }
        initView(rootView);
        initAllServiceAdapter();
        return rootView;
    }

    private void initAllServiceAdapter() {
        ArrayList<ServiceBean> serviceBeans = new ArrayList<>();
        ServiceBean bean = new ServiceBean();
        for (int i = 0; i < 7; i++) {
           bean.setServicename("服务"+ i);
            serviceBeans.add(bean);
        }

        int spanCount = 4;
        int spacing = VwUtils.getSW(getActivity(), 0);//item间隙宽度
        int itemDecorationCount = mRecyclerView.getItemDecorationCount();
        if (itemDecorationCount == 0) {
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
            mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                @Override public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view,
                                                        final int position) {
//                    CropBean cb = (CropBean) adapter.getItem(position);
//                    Intent intent = new Intent(CropsActivity.this, PictureSelectActivity.class);
//                    intent.putExtra("crop_bean", cb);
//                    intent.putExtra("crop_part", crop_part);
//                    startActivityForResult(intent, 8801);
                }
            });
        }
        GridLayoutManager manager = new GridLayoutManager(getActivity(), spanCount);
        mRecyclerView.setLayoutManager(manager);
        listAdapter = new AllServiceAdapter(R.layout.item_all_service_list, getActivity());
        listAdapter.setEnableLoadMore(false);
        listAdapter.addData(serviceBeans);//       TODO:
        mRecyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View rootView) {

        iv_location = (ImageView) rootView.findViewById(R.id.iv_location);
        iv_location.setOnClickListener(this);
        tv_location = (TextView) rootView.findViewById(R.id.tv_location);
        tv_location.setOnClickListener(this);
        iv_msg = (ImageView) rootView.findViewById(R.id.iv_msg);
        iv_msg.setOnClickListener(this);
        tv_search = (TextView) rootView.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(this);
        xbanner = (XBanner) rootView.findViewById(R.id.xbanner);
        xbanner.setOnClickListener(this);
        tv_recommended_more = (TextView) rootView.findViewById(R.id.tv_recommended_more);
        tv_recommended_more.setOnClickListener(this);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        rv_service = (RecyclerView) rootView.findViewById(R.id.rv_service);
        rv_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_location:
            case R.id.tv_location:
                Intent intent = new Intent(getActivity(), GetAddressActivity.class);
                intent.putExtra("","");
                startActivityForResult(intent,9999);
                break;
            case R.id.iv_msg:
//                new Intent(getActivity(),);
                break;
            case R.id.tv_search:

                break;
            case R.id.tv_recommended_more:

                break;
        }
    }
}
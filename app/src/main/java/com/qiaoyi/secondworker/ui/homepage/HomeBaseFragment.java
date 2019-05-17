package com.qiaoyi.secondworker.ui.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.BannerListBean;
import com.qiaoyi.secondworker.bean.BaseWrapServiceItemBean;
import com.qiaoyi.secondworker.bean.LocationBean;
import com.qiaoyi.secondworker.bean.RedomListBean;
import com.qiaoyi.secondworker.bean.ServiceItemBean;
import com.qiaoyi.secondworker.bean.ServiceTypeBean;
import com.qiaoyi.secondworker.bean.WrapServiceBean;
import com.qiaoyi.secondworker.bean.WrapServiceItemBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiHome;
import com.qiaoyi.secondworker.ui.BaseFragment;
import com.qiaoyi.secondworker.ui.ItemDecoration.GridSpacingItemDecoration;
import com.qiaoyi.secondworker.ui.center.address.GetAddressActivity;
import com.qiaoyi.secondworker.ui.center.center.MessageActivity;
import com.qiaoyi.secondworker.ui.homepage.activity.SearchActivity;
import com.qiaoyi.secondworker.ui.homepage.activity.ServiceListActivity;
import com.qiaoyi.secondworker.ui.homepage.adapter.AllServiceAdapter;
import com.qiaoyi.secondworker.ui.homepage.adapter.RecommendAdapter;
import com.qiaoyi.secondworker.ui.homepage.adapter.ServiceItemAdapter;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;
import com.qiaoyi.secondworker.utlis.VwUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;
import cn.isif.plug.bannerview.BannerView;
import cn.isif.plug.bannerview.exception.ClassTypeException;
import cn.isif.plug.bannerview.listener.OnBannerClickListener;

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
    private TextView tv_recommended_more;
    private RecyclerView mRecyclerView;
    private RecyclerView rv_service,rv_list_recommend;
    private AllServiceAdapter listAdapter;
    private ArrayList<ServiceTypeBean> serviceTypeBeans;
    private List<ServiceTypeBean> result;
    private List<ServiceItemBean> objList;
    private List<BannerListBean> bannerList = new ArrayList<>();
    private List<RedomListBean> redomList;//随机推荐
    private BannerView bannerView;

    public HomeBaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(getActivity());
        EventBus.getDefault().register(this);
    }
    public void initServiceType() {
        ApiHome.getServiceType(new ServiceCallBack<WrapServiceBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapServiceBean> payload) {
                WrapServiceBean body = payload.body();
                result = body.result;
                initAllServiceAdapter();
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        }
        initView(rootView);
//        ShowProgressDialog.showProgressOn(getActivity(),"","",false);
        initServiceType();
        requestData();

        return rootView;
    }

    private void initData() {
        /*********************initBanner***********************************/
        try {
            bannerView.setData(bannerList);
        } catch (ClassTypeException e) {
            e.printStackTrace();
        }
        bannerView.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onBannerClickListener(int position, View view) {
                ALog.d("" + position);
                if (bannerList.size() <= 0) return;
                BannerListBean slideBean = bannerList.get(position);
                jumpActivity(slideBean);
            }
        });
        /*********************initRecommend***********************************/
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_list_recommend.setLayoutManager(layoutManager);
        RecommendAdapter recommendAdapter = new RecommendAdapter(R.layout.item_recommended,getActivity());
        recommendAdapter.addData(redomList);
        rv_list_recommend.setAdapter(recommendAdapter);
        /*********************initServiceItem***********************************/
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_service.setLayoutManager(manager);
        ServiceItemAdapter serviceItemAdapter = new ServiceItemAdapter(R.layout.item_home_service,getActivity());
        serviceItemAdapter.addData(objList);
//        rv_service.setNestedScrollingEnabled(false);
        rv_service.setAdapter(serviceItemAdapter);
    }

    /**
     * @param slideBean
     */
    private void jumpActivity(BannerListBean slideBean) {
        //跳转到网页
    }

    private void requestData() {
        ApiHome.getServiceItem(new ServiceCallBack<WrapServiceItemBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapServiceItemBean> payload) {
                WrapServiceItemBean body = payload.body();
                BaseWrapServiceItemBean result = body.result;
                objList = result.objList;
                bannerList = result.bannerList;
                redomList = result.redomList;
//                ShowProgressDialog.showProgressOff();
                initData();
            }
        });
    }

    private void initAllServiceAdapter() {
        int spanCount = 4;
        int spacing = VwUtils.getSW(getActivity(), 0);//item间隙宽度
        int itemDecorationCount = mRecyclerView.getItemDecorationCount();
        if (itemDecorationCount == 0) {
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
            mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                @Override public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view,
                                                        final int position) {
                    ServiceTypeBean item = (ServiceTypeBean) adapter.getItem(position);
                    Intent intent = new Intent(getActivity(), ServiceListActivity.class);
                    intent.putExtra("service_name",item.serviceType);
                    intent.putExtra("item_id",item.serviceTypeId);
                    getActivity().startActivity(intent);
                }
            });
        }
        GridLayoutManager manager = new GridLayoutManager(getActivity(), spanCount);
        mRecyclerView.setLayoutManager(manager);
        listAdapter = new AllServiceAdapter(R.layout.item_all_service_list, getActivity());
        listAdapter.setEnableLoadMore(false);
        listAdapter.addData(result);//       TODO:
        mRecyclerView.setAdapter(listAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        bannerView = rootView.findViewById(R.id.banner);
        tv_recommended_more = (TextView) rootView.findViewById(R.id.tv_recommended_more);
        tv_recommended_more.setOnClickListener(this);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        rv_service = (RecyclerView) rootView.findViewById(R.id.rv_service);
        rv_list_recommend = (RecyclerView) rootView.findViewById(R.id.rv_list_recommend);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_location:
            case R.id.tv_location:
                startActivity(new Intent(getActivity(), GetAddressActivity.class));
                break;
            case R.id.iv_msg:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.tv_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.tv_recommended_more:

                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onLocationSelect(LocationBean location){
        double lat = location.getLat();
        double lng = location.getLng();
        String address_msg = location.getAddress_msg();
        String address_title = location.getAddress_title();
        ALog.e("lat="+lat+",lng="+lng+"\naddress_msg="+address_msg+"address_title="+address_title);
        tv_location.setText(address_title);
        //重新根据请求数据
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

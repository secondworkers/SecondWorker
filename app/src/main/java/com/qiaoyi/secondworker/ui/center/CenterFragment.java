package com.qiaoyi.secondworker.ui.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.MessageEvent;
import com.qiaoyi.secondworker.bean.UserBean;
import com.qiaoyi.secondworker.bean.WrapUserBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.Contacts;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.BaseFragment;
import com.qiaoyi.secondworker.ui.center.center.ApplyStatusActivity;
import com.qiaoyi.secondworker.ui.center.center.BecomeWorkerActivity;
import com.qiaoyi.secondworker.ui.center.center.FillInInvitationCodeActivity;
import com.qiaoyi.secondworker.ui.center.center.InvitationActivity;
import com.qiaoyi.secondworker.ui.center.center.ModifyUerInfoActivity;
import com.qiaoyi.secondworker.ui.center.center.MyCollectionActivity;
import com.qiaoyi.secondworker.ui.center.center.MyCommentActivity;
import com.qiaoyi.secondworker.ui.center.center.MyOpinionActivity;
import com.qiaoyi.secondworker.ui.center.wallet.MyIntegralActivity;
import com.qiaoyi.secondworker.ui.center.wallet.MyWalletActivity;
import com.qiaoyi.secondworker.ui.center.center.ShareBaseActivity;
import com.qiaoyi.secondworker.ui.center.center.SystemSettingsActivity;
import com.qiaoyi.secondworker.ui.center.order.MyOrderActivity;
import com.qiaoyi.secondworker.ui.center.address.MyLocationActivity;
import com.qiaoyi.secondworker.ui.center.center.MyRequirementActivity;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/19
 * 个人中心
 *
 * @author Spirit
 */

public class CenterFragment extends BaseFragment implements View.OnClickListener {
    public static final String BROADCAST_LOGOUT_ACTION = "action.LOGOUT";
    private View rootView;
    private ImageView iv_semicircle,iv_setting;
    private ImageView iv_my_head_photo;
    private TextView tv_username;
    private TextView tv_member_code;
    private ImageView iv_circle,iv_isVip;
    private RelativeLayout rl_top;
    private TextView tv_my_order;
    private TextView tv_see_all;
    private TextView tv_line;
    private TextView tv_not_pay_money;
    private TextView tv_not_service;
    private TextView tv_not_confirm;
    private TextView tv_not_comment;
    private TextView tv_refund,tv_useful_money,tv_useful_integral;
    private LinearLayout ll_rool,ll_useful_money,ll_useful_integral;
    private RelativeLayout rl_my_order;
    private ImageView iv_invitation_activity;
    private TextView tv_shopping_cart;
    private TextView tv_et_invitation_code;
    private TextView tv_my_requirement;
    private TextView tv_my_address;
    private LinearLayout ll_center_top;
    private TextView tv_my_comment;
    private TextView tv_shop_enter;
    private TextView tv_become_second_worker;
    private TextView tv_opinion;
    private LinearLayout ll_center_bottom;
    private UserBean bean;
    private int auditStatus;
    private Button button_logout;
    private ImageView iv_semicircle1;
    private List<String> strings;


    public CenterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(getActivity());

    }

    private void requestData() {
        ApiUserService.getUserInfo(AccountHandler.getUserId(), new ServiceCallBack<WrapUserBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapUserBean> payload) {
                bean = payload.body().result;
                auditStatus = bean.auditStatus;
                initData();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_centers, container, false);
        }
        EventBus.getDefault().register(this);
        initView(rootView);
        return rootView;
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
        iv_semicircle = (ImageView) rootView.findViewById(R.id.iv_semicircle);
        iv_setting = (ImageView) rootView.findViewById(R.id.iv_setting);
        iv_semicircle.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        iv_my_head_photo = (ImageView) rootView.findViewById(R.id.iv_my_head_photo);
        iv_my_head_photo.setOnClickListener(this);
        tv_username = (TextView) rootView.findViewById(R.id.tv_username);
        tv_username.setOnClickListener(this);
        iv_isVip = (ImageView) rootView.findViewById(R.id.iv_isVip);
        iv_isVip.setOnClickListener(this);
        tv_member_code = (TextView) rootView.findViewById(R.id.tv_member_code);
        tv_member_code.setOnClickListener(this);
        iv_circle = (ImageView) rootView.findViewById(R.id.iv_circle);
        iv_circle.setOnClickListener(this);
        rl_top = (RelativeLayout) rootView.findViewById(R.id.rl_top);
        rl_top.setOnClickListener(this);
        tv_my_order = (TextView) rootView.findViewById(R.id.tv_my_order);
        tv_my_order.setOnClickListener(this);
        tv_see_all = (TextView) rootView.findViewById(R.id.tv_see_all);
        tv_see_all.setOnClickListener(this);
        tv_line = (TextView) rootView.findViewById(R.id.tv_line);
        tv_line.setOnClickListener(this);
        tv_not_pay_money = (TextView) rootView.findViewById(R.id.tv_not_pay_money);
        tv_not_pay_money.setOnClickListener(this);
        tv_not_service = (TextView) rootView.findViewById(R.id.tv_not_service);
        tv_not_service.setOnClickListener(this);
        tv_not_confirm = (TextView) rootView.findViewById(R.id.tv_not_confirm);
        tv_not_confirm.setOnClickListener(this);
        tv_not_comment = (TextView) rootView.findViewById(R.id.tv_not_comment);
        tv_not_comment.setOnClickListener(this);
        tv_refund = (TextView) rootView.findViewById(R.id.tv_refund);
        tv_useful_money = (TextView) rootView.findViewById(R.id.tv_useful_money);
        tv_useful_integral = (TextView) rootView.findViewById(R.id.tv_useful_integral);
        tv_refund.setOnClickListener(this);
        ll_rool = (LinearLayout) rootView.findViewById(R.id.ll_rool);
        ll_useful_money = (LinearLayout) rootView.findViewById(R.id.ll_useful_money);
        ll_useful_integral = (LinearLayout) rootView.findViewById(R.id.ll_useful_integral);
        ll_useful_money.setOnClickListener(this);
        ll_useful_integral.setOnClickListener(this);
        rl_my_order = (RelativeLayout) rootView.findViewById(R.id.rl_my_order);
        rl_my_order.setOnClickListener(this);
        iv_invitation_activity = (ImageView) rootView.findViewById(R.id.iv_invitation_activity);
        iv_invitation_activity.setOnClickListener(this);
        tv_shopping_cart = (TextView) rootView.findViewById(R.id.tv_shopping_cart);
        tv_shopping_cart.setOnClickListener(this);
        tv_et_invitation_code = (TextView) rootView.findViewById(R.id.tv_et_invitation_code);
        tv_et_invitation_code.setOnClickListener(this);
        tv_my_requirement = (TextView) rootView.findViewById(R.id.tv_my_requirement);
        tv_my_requirement.setOnClickListener(this);
        tv_my_address = (TextView) rootView.findViewById(R.id.tv_my_address);
        tv_my_address.setOnClickListener(this);
        ll_center_top = (LinearLayout) rootView.findViewById(R.id.ll_center_top);
        ll_center_top.setOnClickListener(this);
        tv_my_comment = (TextView) rootView.findViewById(R.id.tv_my_comment);
        tv_my_comment.setOnClickListener(this);
        tv_shop_enter = (TextView) rootView.findViewById(R.id.tv_shop_enter);
        tv_shop_enter.setOnClickListener(this);
        tv_become_second_worker = (TextView) rootView.findViewById(R.id.tv_become_second_worker);
        tv_become_second_worker.setOnClickListener(this);
        tv_opinion = (TextView) rootView.findViewById(R.id.tv_opinion);
        tv_opinion.setOnClickListener(this);
        ll_center_bottom = (LinearLayout) rootView.findViewById(R.id.ll_center_bottom);
        button_logout = rootView.findViewById(R.id.button_logout);
        button_logout.setOnClickListener(this);
        ll_center_bottom.setOnClickListener(this);
        requestData();
    }
    private void logout() {
        AccountHandler.logout();
        Intent intent = new Intent();
        intent.setAction(BROADCAST_LOGOUT_ACTION);
        getActivity().sendBroadcast(intent);
    }
    private void initData() {
        tv_member_code.setText("复制邀请码："+AccountHandler.getmInvitCode());
        if (!TextUtils.isEmpty(bean.username))
             tv_username.setText(bean.username);
        Glide.with(getActivity()).load(bean.avatar).apply(GlideUtils.setCircleAvatar()).into(iv_my_head_photo);
        //Todo:会员级别
        RequestOptions requestOptions = new RequestOptions().placeholder(getActivity().getResources().getDrawable(R.mipmap.ic_normal_vip)).centerCrop();
        Glide.with(getActivity()).load(getActivity().getResources().getDrawable(R.mipmap.ic_supreme_vip)).apply(requestOptions).into(iv_isVip);

        tv_useful_money.setText(String.valueOf(bean.balance));
        tv_useful_integral.setText(String.valueOf(bean.rewardpoints));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void synUserInfo(MessageEvent messageEvent) {
        if ("modify_user_info".equals(messageEvent.getMessage())) {
            requestData();
        }
    }

    /**
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_member_code:
                ClipboardManager cm =(ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(AccountHandler.getmInvitCode());
                ToastUtils.showShort("已复制");
                break;
            case R.id.ll_useful_money://钱包
                startActivity(new Intent(getActivity(),MyWalletActivity.class));
                break;
            case R.id.ll_useful_integral:
                startActivity(new Intent(getActivity(),MyIntegralActivity.class));
                break;
            case R.id.iv_my_head_photo:
                Intent intent1 = new Intent(getActivity(), ModifyUerInfoActivity.class);
                intent1.putExtra("userBean",bean);
                startActivity(intent1);
                break;
            case R.id.button_logout:
                logout();
                break;
            case R.id.iv_setting:
                startActivity(new Intent(getActivity(), SystemSettingsActivity.class));
                break;
            case R.id.tv_see_all:
                toMyOrderActivity(Contacts.ALL_ORDERS);
                break;
            case R.id.tv_not_pay_money:
                toMyOrderActivity(Contacts.WAITING_PAY);
                break;
            case R.id.tv_not_service:
                toMyOrderActivity(Contacts.WAITING_SERVICE);
                break;
            case R.id.tv_not_confirm:
                toMyOrderActivity(Contacts.WAITING_CONFIRM);
                break;
            case R.id.tv_not_comment:
                toMyOrderActivity(Contacts.WAITING_COMMENT);
                break;
            case R.id.tv_refund://退款
                ToastUtils.showShort("退款请联系客服！");
                break;
            case R.id.tv_shopping_cart:

                break;
          /*  case R.id.tv_my_collection://填写邀请码
                startActivity(new Intent(getActivity(),MyCollectionActivity.class));
                break;*/
            case R.id.tv_et_invitation_code:
                startActivity(new Intent(getActivity(),FillInInvitationCodeActivity.class));
                break;
            case R.id.tv_my_requirement:
                startActivity(new Intent(getActivity(),MyRequirementActivity.class));
                break;
            case R.id.tv_my_address:
                startActivity(new Intent(getActivity(),MyLocationActivity.class));
                break;
            case R.id.tv_my_comment:
                startActivity(new Intent(getActivity(),MyCommentActivity.class));
                break;
            case R.id.tv_shop_enter:
                ToastUtils.showShort("敬请期待");
                break;
            case R.id.tv_become_second_worker:
                if (auditStatus == 3){
                    startActivity(new Intent(getActivity(),ApplyStatusActivity.class));
                }else {
                    Intent intent = new Intent(getActivity(), BecomeWorkerActivity.class);
                    intent.putExtra("status",auditStatus);
                    startActivity(intent);
                }
                break;
            case R.id.tv_opinion:
                startActivity(new Intent(getActivity(),MyOpinionActivity.class));
                break;
            case R.id.iv_invitation_activity:
                startActivity(new Intent(getActivity(),InvitationActivity.class));
                break;
        }
    }

    /**
     * all -- qb
     * 0：待付款 -- dfk
     * 1：等待商家接单 2：派单中 3：已接单 4.工人正在路上 //待服务 -- dfw
     * 5.工人正在服务 6.完成服务  //待确认 -- dqr
     * 7.取消订单 8.删除订单
     * 9.待评价 -- dpj
     */
    private void toMyOrderActivity(String status) {
        Intent intent = new Intent(getActivity(), MyOrderActivity.class);
        intent.putExtra("status",status);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }
}

package com.qiaoyi.secondworker.ui.center.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.RequirementBean;
import com.qiaoyi.secondworker.bean.WrapRequirementBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.center.MyRequirementActivity;
import com.qiaoyi.secondworker.utlis.GlideUtils;

/**
 * Created on 2019/5/5
 *
 * @author Spirit
 */

public class MyRequirementAdapter extends BaseQuickAdapter<RequirementBean,BaseViewHolder>{
    Activity activity;
    private TextView tv_req_status;

    public MyRequirementAdapter(int item_service_type, Activity activity) {
        super(item_service_type);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, RequirementBean item) {
        ImageView iv_avatar = helper.getView(R.id.iv_avatar);
        Glide.with(activity).load(Contact.TEST_IMG).apply(GlideUtils.setCircleAvatar()).into(iv_avatar);
        TextView tv_req_status = helper.getView(R.id.tv_req_status);
        TextView btn_cancel = helper.getView(R.id.btn_cancel);
        TextView btn_done = helper.getView(R.id.btn_done);
        TextView tv_post_type = helper.getView(R.id.tv_post_type);
        TextView tv_service_time = helper.getView(R.id.tv_service_time);
        TextView tv_post_message = helper.getView(R.id.tv_post_message);
        TextView tv_post_time = helper.getView(R.id.tv_post_time);
        tv_post_type.setText(Html.fromHtml("<font color='#666666'>"
                + "发布类型："
                + "</font><font color='#212121'> "
                + item.serviceType
                + "</font>"));
        tv_service_time.setText(Html.fromHtml("<font color='#666666'>"
                + "服务时间："
                + "</font><font color='#212121'> "
                + item.atime
                + "</font>"));
        tv_post_message.setText(Html.fromHtml("<font color='#666666'>"
                + "发布内容："
                + "</font><font color='#212121'> "
                + item.text
                + "</font>"));
        tv_post_time.setText(item.releaseTime);
        String status = item.status;
        if (status.equals("1")){
            tv_req_status.setText("发布中");
            btn_cancel.setText("取消发布");
            btn_done.setVisibility(View.VISIBLE);
        }else if (status.equals("2")){
            tv_req_status.setText("已取消");
            btn_cancel.setText("删除需求");
        }else if (status.equals("3")){
            tv_req_status.setText("已完成");
            btn_cancel.setText("删除需求");
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("2")||status.equals("3")){
                    btnCancelClick(item,helper.getPosition(),"4");
                }else if (status.equals("1")){
                    btnCancelClick(item,helper.getPosition(),"2");
                }

            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDoneClick(item,helper.getPosition());
            }
        });
    }

    private void btnDoneClick(RequirementBean item, int position) {
        ApiUserService.updateRequirementList(item.id, "3", new ServiceCallBack<WrapRequirementBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapRequirementBean> payload) {
                item.setStatus("3");
                notifyItemChanged(position);
            }
        });
    }

    private void btnCancelClick(RequirementBean item, int position,String status) {
        ApiUserService.updateRequirementList(item.id, status, new ServiceCallBack<WrapRequirementBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapRequirementBean> payload) {
                if (status.equals(2)){
                    item.setStatus(status);
                    notifyItemChanged(position);
                }else {
                    item.setStatus(status);
                    remove(position);
                }

            }
        });
    }
}

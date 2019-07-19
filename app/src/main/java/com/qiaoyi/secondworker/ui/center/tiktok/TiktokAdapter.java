package com.qiaoyi.secondworker.ui.center.tiktok;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.TaskDetailBean;
import com.qiaoyi.secondworker.utlis.GlideUtils;

/**
 * Created on 2019/7/7
 *
 * @author Spirit
 */

public class TiktokAdapter extends BaseQuickAdapter<TaskDetailBean,BaseViewHolder>{
    Activity activity;
    public TiktokAdapter(int item_trill_everyday_task, Activity activity) {
        super(item_trill_everyday_task);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskDetailBean item) {
        String userGrade = item.userGrade;
        TextView tv_member_grade = helper.getView(R.id.tv_member_grade);
        if (userGrade.equals("0")){
            tv_member_grade.setText("会员专属");
            tv_member_grade.setBackground(activity.getResources().getDrawable(R.drawable.shape_trill_member_background_blue));
        }else if (userGrade.equals("1")){
            tv_member_grade.setText("高级会员专属");
            tv_member_grade.setBackground(activity.getResources().getDrawable(R.drawable.shape_trill_member_background_blue));
        }else if (userGrade.equals("2")){
            tv_member_grade.setText("至尊会员专属");
            tv_member_grade.setBackground(activity.getResources().getDrawable(R.drawable.shape_trill_member_background_black));
        }
        helper.setText(R.id.tv_forward_task,item.taskTitle);
        helper.setText(R.id.tv_money,String.valueOf(item.money));
        helper.setText(R.id.tv_number,"每天仅"+item.count+"名额丨剩余"+item.syCount+"名额");
        ImageView iv_trill = helper.getView(R.id.iv_trill);
        Glide.with(activity).load(item.taskIcon).apply(GlideUtils.setRoundTransform(activity,1)).into(iv_trill);
        ImageView ic_task_status = helper.getView(R.id.iv_task_status);
        String auditStatus = item.auditStatus;
        if (auditStatus.equals("0")||auditStatus.equals("3")){
            ic_task_status.setImageDrawable(activity.getResources().getDrawable(R.mipmap.iv_task_received));
        }else if (auditStatus.equals("1")){
            ic_task_status.setImageDrawable(activity.getResources().getDrawable(R.mipmap.iv_task_done));
        }else if (auditStatus.equals("2")){
            ic_task_status.setImageDrawable(activity.getResources().getDrawable(R.mipmap.iv_task_fail));
        }else {
            ic_task_status.setVisibility(View.INVISIBLE);
        }

    }
}

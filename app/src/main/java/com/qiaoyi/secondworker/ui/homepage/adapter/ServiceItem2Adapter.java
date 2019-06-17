package com.qiaoyi.secondworker.ui.homepage.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceItemBean;
import com.qiaoyi.secondworker.bean.ServiceItemListBean;
import com.qiaoyi.secondworker.ui.homepage.activity.ServiceListActivity;
import com.qiaoyi.secondworker.utlis.GlideBorderCircleTransform;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

/**
 * Created by Spirit on 2019/5/21.
 */

public class ServiceItem2Adapter extends RecyclerView.Adapter<ServiceItem2Adapter.ViewHolder> {
    private List<ServiceItemBean> mList;
    Activity activity;
    private TextView tv_service_type;
    private TextView tv_service_type_more;
    private ImageView iv_service1;
    private TextView tv_service1;
    private TextView tv_price1;
    private ImageView iv_service2;
    private TextView tv_service2;
    private TextView tv_price2;
    private ImageView iv_service3;
    private TextView tv_service3;
    private TextView tv_price3;
    private LinearLayout ll_img;

    public ServiceItem2Adapter(List<ServiceItemBean> mList, Activity activity) {
        this.mList = mList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_service, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceItemBean bean = mList.get(position);
        List<ServiceItemListBean> itemList = bean.serviceItemList;
        int sw = VwUtils.getSW(activity);
        int img_sw = (sw - 80) / 3;
        RequestOptions override = new RequestOptions().override(img_sw)
                .transform(new GlideBorderCircleTransform(activity, 3))
                .fitCenter()
                .centerCrop();
        if (itemList!=null&& itemList.size()>0) {
            ll_img.setVisibility(View.VISIBLE);
            if (itemList.size() == 1) {
                tv_service1.setText(itemList.get(0).serviceItem);
                tv_price1.setText("预收" + itemList.get(0).price + itemList.get(0).unit);
                Glide.with(activity).load(itemList.get(0).image).apply(GlideUtils.setRoundTransform(activity, 3)).into(iv_service1);
            } else if (itemList.size() == 2) {
                tv_service1.setText(itemList.get(0).serviceItem);
                tv_price1.setText("预收" + itemList.get(0).price + itemList.get(0).unit);
                Glide.with(activity).
                        load(itemList.get(0).image).
                        apply(GlideUtils.setRoundTransform(activity, 3)).
                        into(iv_service1);

                tv_service2.setText(itemList.get(1).serviceItem);
                tv_price2.setText("预收" + itemList.get(1).price + itemList.get(1).unit);
                Glide.with(activity).
                        load(itemList.get(1).image).
                        apply(GlideUtils.setRoundTransform(activity, 3)).
                        into(iv_service2);

            } else if (itemList.size() == 3) {
                tv_service1.setText(itemList.get(0).serviceItem);
                tv_price1.setText("预收" + itemList.get(0).price + itemList.get(0).unit);
                Glide.with(activity).
                        load(itemList.get(0).image).
                        apply(override).
                        into(iv_service1);

                tv_service2.setText(itemList.get(1).serviceItem);
                tv_price2.setText("预收" + itemList.get(1).price + itemList.get(1).unit);
                Glide.with(activity).
                        load(itemList.get(1).image).
                        apply(override).
                        into(iv_service2);

                tv_service3.setText(itemList.get(2).serviceItem);
                tv_price3.setText("预收" + itemList.get(2).price + itemList.get(2).unit);
                Glide.with(activity).
                        load(itemList.get(2).image).
                        apply(override).
                        into(iv_service3);
            }
        }else {
            ll_img.setVisibility(View.GONE);
        }
        tv_service_type_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ServiceListActivity.class);
                intent.putExtra("item_id", bean.serviceTypeId);
                intent.putExtra("service_name", bean.serviceType);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            ll_img = itemView.findViewById(R.id.ll_img);
            tv_service_type = itemView.findViewById(R.id.tv_service_type);
            tv_service_type_more = itemView.findViewById(R.id.tv_service_type_more);
            iv_service1 = itemView.findViewById(R.id.iv_service1);
            tv_service1 = itemView.findViewById(R.id.tv_service1);
            tv_price1 = itemView.findViewById(R.id.tv_price1);
            iv_service2 = itemView.findViewById(R.id.iv_service2);
            tv_service2 = itemView.findViewById(R.id.tv_service2);
            tv_price2 = itemView.findViewById(R.id.tv_price2);
            iv_service3 = itemView.findViewById(R.id.iv_service3);
            tv_service3 = itemView.findViewById(R.id.tv_service3);
            tv_price3 = itemView.findViewById(R.id.tv_price3);
        }

    }
}

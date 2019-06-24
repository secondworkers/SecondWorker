package com.qiaoyi.secondworker.ui.homepage.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ServiceItemBean;
import com.qiaoyi.secondworker.bean.ServiceItemListBean;
import com.qiaoyi.secondworker.ui.homepage.activity.ServiceListActivity;
import com.qiaoyi.secondworker.utlis.GlideBorderCircleTransform;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.plug.bannerview.util.GlideRoundTransform;

/**
 * Created on 2019/5/11
 *
 * @author Spirit
 */

public class ServiceItemAdapter extends BaseQuickAdapter<ServiceItemBean,BaseViewHolder>{
    Activity activity;
    private ImageView iv_service1,iv_service3,iv_service2;

    public ServiceItemAdapter(int item_home_service, Activity activity) {
        super(item_home_service);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceItemBean item) {
        helper.setText(R.id.tv_service_type,item.serviceType);
        List<ServiceItemListBean> itemList = item.serviceItemList;
        iv_service1 = helper.getView(R.id.iv_service1);
        iv_service2 = helper.getView(R.id.iv_service2);
        iv_service3 = helper.getView(R.id.iv_service3);
        LinearLayout ll_img = helper.getView(R.id.ll_img);
        int sw = VwUtils.getSW(activity);
        int img_sw = (sw - 80) / 3;
        RequestOptions override = new RequestOptions().override(img_sw)
                .placeholder(R.mipmap.ic_placeholder)
                        .error(R.mipmap.ic_placeholder)
                        .transform(new GlideRoundTransform(activity, 3));
        if (itemList!=null&& itemList.size()>0) {
            ll_img.setVisibility(View.VISIBLE);
            if (itemList.size() == 1) {
                helper.setText(R.id.tv_service1, itemList.get(0).serviceItem);
                helper.setText(R.id.tv_price1, "预收" + itemList.get(0).price + itemList.get(0).unit);
                Glide.with(activity).load(itemList.get(0).goodsPhoto).apply(GlideUtils.setRoundTransform(activity, 3)).into(iv_service1);
            } else if (itemList.size() == 2) {
                helper.setText(R.id.tv_service1, itemList.get(0).serviceItem);
                helper.setText(R.id.tv_price1, "预收" + itemList.get(0).price + itemList.get(0).unit);
                Glide.with(activity).
                        load(itemList.get(0).goodsPhoto).
                        apply(GlideUtils.setRoundTransform(activity, 3)).
                        into(iv_service1);

                helper.setText(R.id.tv_service2, itemList.get(1).serviceItem);
                helper.setText(R.id.tv_price2, "预收" + itemList.get(1).price + itemList.get(1).unit);
                Glide.with(activity).
                        load(itemList.get(1).goodsPhoto).
                        apply(GlideUtils.setRoundTransform(activity, 3)).
                        into(iv_service2);

            } else if (itemList.size() >= 3) {
                helper.setText(R.id.tv_service1, itemList.get(0).serviceItem);
                helper.setText(R.id.tv_price1, "预收" + itemList.get(0).price + itemList.get(0).unit);
                Glide.with(activity).
                        load(itemList.get(0).goodsPhoto).
                        apply(override).
                        into(iv_service1);

                helper.setText(R.id.tv_service2, itemList.get(1).serviceItem);
                helper.setText(R.id.tv_price2, "预收" + itemList.get(1).price + itemList.get(1).unit);
                Glide.with(activity).
                        load(itemList.get(1).goodsPhoto).
                        apply(override).
                        into(iv_service2);

                helper.setText(R.id.tv_service3, itemList.get(2).serviceItem);
                helper.setText(R.id.tv_price3, "预收" + itemList.get(2).price + itemList.get(2).unit);
                Glide.with(activity).
                        load(itemList.get(2).goodsPhoto).
                        apply(override).
                        into(iv_service3);
            }
        }else {
            ll_img.setVisibility(View.GONE);
        }
         helper.getView(R.id.tv_service_type_more).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(activity, ServiceListActivity.class);
                 intent.putExtra("item_id", item.serviceTypeId);
                 intent.putExtra("service_name", item.serviceType);
                 activity.startActivity(intent);
             }
         });

    }
}

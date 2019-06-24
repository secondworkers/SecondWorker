package com.qiaoyi.secondworker.ui.homepage.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.SearchServiceBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.ui.homepage.activity.ServiceListActivity;
import com.qiaoyi.secondworker.utlis.GlideUtils;

/**
 * Created on 2019/5/11
 *
 * @author Spirit
 */

public class SearchAdapter extends BaseQuickAdapter<SearchServiceBean,BaseViewHolder>{
    Activity activity;
    public SearchAdapter(int item_search_service, Activity activity) {
        super(item_search_service);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchServiceBean item) {
        helper.setText(R.id.tv_service,item.goodsName);
        helper.setText(R.id.tv_service_detail,item.profile);
        helper.setText(R.id.tv_price,item.price+item.unit);
        helper.setText(R.id.tv_order_count,"已售"+item.counts);
        helper.setText(R.id.tv_score,item.score+"分");
        RatingBar ratingbar = helper.getView(R.id.ratingbar);
        ratingbar.setRating((float) item.score);
        ImageView iv_service_photo = helper.getView(R.id.iv_service_photo);
        Glide.with(activity)
                .load(item.goodsPhoto)
                .apply(GlideUtils.setRoundTransform(activity,5))
                .into(iv_service_photo);
    }
}

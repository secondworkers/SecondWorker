package com.qiaoyi.secondworker.ui.map.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.WorkerBean;
import com.qiaoyi.secondworker.ui.map.WorkerCenterActivity;
import com.qiaoyi.secondworker.utlis.GlideUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.isif.alibs.utils.ALog;

/**
 * Created by Spirit on 2019/4/25.
 */

public class WorkerGalleryAdapter extends PagerAdapter {
  Context context;
  private LinkedList<View> mViewCache = new LinkedList<>();
  private List<WorkerBean> pbMarkers = new ArrayList<>();
  private WorkerBean workerBean;

  public WorkerGalleryAdapter(Context context) {
    this.context = context;
  }

  @Override public int getCount() {
    return pbMarkers.size();
  }

  @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }

  @NonNull
  @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
    ViewHolder holder = null;
    View convertView = null;
    if (mViewCache.size() == 0) {
      convertView = View.inflate(context, R.layout.item_worker_gallery_layout, null);
      holder = new ViewHolder();

      holder.iv_user_avatar = (ImageView) convertView.findViewById(R.id.iv_user_avatar);
      holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
      holder.tv_rating = (TextView) convertView.findViewById(R.id.tv_rating);
      holder.tv_worker_description = (TextView) convertView.findViewById(R.id.tv_worker_description);
      holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
      holder.rl_goto_chat = convertView.findViewById(R.id.rl_goto_chat);
      holder.ratingbar = convertView.findViewById(R.id.ratingbar);
      holder.ll_case = convertView.findViewById(R.id.ll_case);

      convertView.setTag(holder);
    } else {
      convertView = mViewCache.removeFirst();
      holder = (ViewHolder) convertView.getTag();
    }
    workerBean = pbMarkers.get(position);

    container.addView(convertView);
    Glide.with(context).load(pbMarkers.get(position).icon).apply(GlideUtils.setCircleAvatar()).into(holder.iv_user_avatar);

    ALog.e("pos ===="+position);
    ALog.e("name ===="+pbMarkers.get(position).workerName);
    ALog.e("id ===="+pbMarkers.get(position).workerId);
    holder.tv_user_name.setText(pbMarkers.get(position).workerName);
    holder.tv_worker_description.setText(pbMarkers.get(position).profile);
    holder.ratingbar.setRating((float) pbMarkers.get(position).score);
    holder.tv_rating.setText(pbMarkers.get(position).score+"分");
    holder.ll_case.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //worker center
        Intent intent = new Intent(context, WorkerCenterActivity.class);
        intent.putExtra("worker_id",pbMarkers.get(position).workerId);
        context.startActivity(intent);
      }
    });
    holder.rl_goto_chat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //worker center
        Intent intent = new Intent(context, WorkerCenterActivity.class);
        intent.putExtra("worker_id",pbMarkers.get(position).workerId);
        context.startActivity(intent);
      }
    });
    return convertView;
  }
  public void setData(List<WorkerBean> pbMarkers){
    this.pbMarkers.clear();
    if (null!=pbMarkers){
      this.pbMarkers.addAll(pbMarkers);
      mViewCache.clear();
    }
    this.notifyDataSetChanged();
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
    mViewCache.add((View) object);
  }
  //View复用
  public final class ViewHolder {
    public ImageView iv_user_avatar;
    public TextView tv_user_name,tv_distance,tv_worker_description,tv_rating;
    public LinearLayout ll_case;
    public RelativeLayout rl_goto_chat;
    public RatingBar ratingbar;
  }
}

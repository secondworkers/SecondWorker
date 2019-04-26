package com.qiaoyi.secondworker.ui.ItemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Spirit on 2019-04-24.
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration {
  int left;
  int top;
  int right;
  int bottom;

  public MyItemDecoration(int left, int top, int right, int bottom) {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    outRect.set(left, top, right, bottom);
  }
}

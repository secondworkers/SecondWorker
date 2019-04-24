package com.qiaoyi.secondworker.utlis;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerDecorationBox extends RecyclerView.ItemDecoration {
  int left;
  int top;
  int right;
  int bottom;

  public RecyclerDecorationBox(int left, int top, int right, int bottom) {
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
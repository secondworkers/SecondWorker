package com.qiaoyi.secondworker.ui;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

/**
 * Created by ola on 2017/7/10.
 */

public class MyFragmentTabHost extends FragmentTabHost {
  private String mCurrentTag;
  private String mNoTabChangedTag;

  public MyFragmentTabHost(Context context) {
    super(context);
  }

  public MyFragmentTabHost(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onTabChanged(String tag) {
    if (tag.equals(mNoTabChangedTag)) {
      setCurrentTabByTag(mCurrentTag);
    } else {
      super.onTabChanged(tag);
      mCurrentTag = tag;
    }
  }

  public void setNoTabChangedTag(String tag) {
    this.mNoTabChangedTag = tag;
  }
}

package com.qiaoyi.secondworker.ui.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.ui.BaseFragment;

/**
 * Created on 2019/4/19
 *
 * @author Spirit
 */

public class MapFragment extends BaseFragment{
    private View rootView;
    private TextView tv_textview;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_center, container, false);
        }
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
        tv_textview = (TextView) rootView.findViewById(R.id.tv_textview);
        tv_textview.setText("地图页");
    }
}

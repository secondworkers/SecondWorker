package com.qiaoyi.secondworker.ui.center.center;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;


/**
 * Created on 2019/5/15
 *
 * @author Spirit
 */

public class ApplyStatusActivity extends BaseActivity{

    private int status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = getIntent().getIntExtra("status",-1);
        if (status == 0){
            setContentView(R.layout.activity_apply_become_secondworker_auditing);
        }else if (status == 1){
            setContentView(R.layout.activity_apply_become_secondworker_audit_success);
        }else if (status == 2){
            setContentView(R.layout.activity_apply_become_secondworker_audit_failure);
        }
    }
}

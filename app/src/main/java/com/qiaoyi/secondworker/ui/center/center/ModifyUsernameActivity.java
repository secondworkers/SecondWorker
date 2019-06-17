package com.qiaoyi.secondworker.ui.center.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.UmengUtils;

/**
 * Created by Spirit on 2019/5/22.
 */

public class ModifyUsernameActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_title_more;
    private RelativeLayout view_right;
    private EditText et_user_name;
    private String nickname;
    private ImageView iv_close;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_username);
        nickname = getIntent().getStringExtra("nickname");
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_title_more = (TextView) findViewById(R.id.tv_title_more);
        view_right = (RelativeLayout) findViewById(R.id.view_right);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        iv_close = findViewById(R.id.iv_close);
        tv_title_txt.setText("修改昵称");
        et_user_name.setText(nickname);
        tv_title_more.setTextColor(getResources().getColor(R.color.text_blue));
        tv_title_more.setText("提交");
        view_back.setOnClickListener(this);
        view_right.setOnClickListener(this);
        iv_close.setOnClickListener(this);
    }

    private void submit() {
        // validate
        String name = et_user_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        }else {
            Intent intent = new Intent();
            intent.putExtra("modify_nickname",name);
            setResult(RESULT_OK,intent);
            finish();
        }

        // TODO validate success, do something


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_back:
                finish();
                break;
            case R.id.view_right:
                submit();
                break;
            case R.id.iv_close:
                et_user_name.setText("");
                break;
        }
    }
    public void onResume() {
        super.onResume();
        UmengUtils.startStatistics4Activity(this,"帐号与安全");
    }
    public void onPause() {
        super.onPause();
        UmengUtils.endStatistics4Activity(this,"帐号与安全");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

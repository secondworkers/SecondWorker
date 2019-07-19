package com.qiaoyi.secondworker.ui.shake.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.MainActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.Contacts;
import com.qiaoyi.secondworker.ui.center.order.MyOrderActivity;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.dialog.ShareServiceDialog;

/**
 * Created on 2019/4/24
 *  发布成功
 * @author Spirit
 */

public class PostSuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private ImageView iv_title_back;
    private RelativeLayout view_back,view_right;
    private TextView tv_title_more;
    private View view_title_line;
    private ImageView imageView;
    private TextView tv_success;
    private TextView tv_goto_shake;
    private TextView tv_see_details;
    private String from;
    private String price;
    private String serviceitem;
    public static void startSuccessActivity(Activity activity,String price, String serviceitem, String from){
        Intent intent = new Intent(activity, PostSuccessActivity.class);
        intent.putExtra("price",price);
        intent.putExtra("serviceItem",serviceitem);
        intent.putExtra("from",from);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_success);
        Intent intent = getIntent();
        price = intent.getStringExtra("price");
        serviceitem = intent.getStringExtra("serviceitem");
        from = intent.getStringExtra("from");
        initView();
        initData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        iv_title_back = (ImageView) findViewById(R.id.iv_title_back);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        view_right = (RelativeLayout) findViewById(R.id.view_right);
        tv_title_more = (TextView) findViewById(R.id.tv_title_more);
        view_title_line = (View) findViewById(R.id.view_title_line);
        imageView = (ImageView) findViewById(R.id.imageView);
        tv_success = (TextView) findViewById(R.id.tv_success);
        tv_goto_shake = (TextView) findViewById(R.id.tv_goto_shake);
        tv_see_details = (TextView) findViewById(R.id.tv_see_details);
        tv_title_txt.setText("支付成功");
        tv_title_more.setText("分享");
        view_right.setOnClickListener(this);
        view_back.setOnClickListener(this);
        tv_see_details.setOnClickListener(this);
        tv_goto_shake.setOnClickListener(this);
    }
    private void initData() {
        if (from.equals("pay")){
            tv_goto_shake.setVisibility(View.GONE);
            view_right.setVisibility(View.GONE);
            tv_see_details.setVisibility(View.VISIBLE);
            tv_success.setText("订单支付成功");
        }else if (from.equals("shake")){
            tv_goto_shake.setVisibility(View.VISIBLE);
            view_right.setVisibility(View.VISIBLE);
            tv_see_details.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.tv_goto_shake:
                startActivity(new Intent(this, ShakeActivity.class));
                finish();
                break;
            case R.id.tv_see_details:
                Intent intent1 = new Intent(this, MyOrderActivity.class);
                intent1.putExtra("status", Contacts.ALL_ORDERS);
                startActivity(intent1);
                finish();
                break;
            case R.id.view_right:
                Intent intent = new Intent(this, ShareServiceDialog.class);
                intent.putExtra("price",price);
                intent.putExtra("serviceitem",serviceitem);
                intent.putExtra("download",Contact.DOWNLOAD);
                startActivity(intent);
                overridePendingTransition(R.anim.push_bottom_in, 0);
                break;
        }
    }
}

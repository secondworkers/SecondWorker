package com.qiaoyi.secondworker.ui.center.tiktok;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.TaskDetailBean;
import com.qiaoyi.secondworker.bean.WrapTaskBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/7/7
 *
 * @author Spirit
 */

public class TiktokTaskDetailActivity extends BaseActivity implements View.OnClickListener {

    private String task_id;
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private ImageView iv_trill;
    private TextView tv_forward_task;
    private TextView tv_member_grade;
    private TextView tv_money;
    private TextView tv_number;
    private ImageView iv_task_status;
    private TextView tv_task_get,tv_task_info;
    private String auditStatus;
    private String taskAuditId;
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_trill_task_detail);
        initView();
        initWebView();
        VwUtils.fixScreen(this);
        task_id = getIntent().getStringExtra("task_id");
        requestData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        iv_trill = (ImageView) findViewById(R.id.iv_trill);
        tv_forward_task = (TextView) findViewById(R.id.tv_forward_task);
        tv_member_grade = (TextView) findViewById(R.id.tv_member_grade);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_number = (TextView) findViewById(R.id.tv_number);
        iv_task_status = (ImageView) findViewById(R.id.iv_task_status);
        tv_task_get = (TextView) findViewById(R.id.tv_task_get);
        tv_task_info = (TextView) findViewById(R.id.tv_task_info);
        mWebView = findViewById(R.id.wv_item);
        tv_title_txt.setText("任务详情");
        view_back.setOnClickListener(this);
        tv_task_get.setOnClickListener(this);
    }

    private void requestData() {
        ApiUserService.queryTask(task_id, new ServiceCallBack<WrapTaskBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapTaskBean> payload) {
                List<TaskDetailBean> result = payload.body().result;
                TaskDetailBean item = result.get(0);
                taskAuditId = item.taskAuditId;
                tv_money.setText(String.valueOf(item.money));
                tv_number.setText("每天仅"+item.count+"名额丨剩余"+item.syCount+"名额");
                tv_forward_task.setText(item.taskTitle);
                tv_task_info.setText(item.taskInfo);
                String userGrade = item.userGrade;
                if (userGrade.equals("0")){
                    tv_member_grade.setText("会员专属");
                    tv_member_grade.setBackground(getResources().getDrawable(R.drawable.shape_trill_member_background_blue));
                }else if (userGrade.equals("1")){
                    tv_member_grade.setText("高级会员专属");
                    tv_member_grade.setBackground(getResources().getDrawable(R.drawable.shape_trill_member_background_blue));
                }else if (userGrade.equals("2")){
                    tv_member_grade.setText("至尊会员专属");
                    tv_member_grade.setBackground(getResources().getDrawable(R.drawable.shape_trill_member_background_black));
                }

                Glide.with(TiktokTaskDetailActivity.this).load(item.taskIcon).apply(GlideUtils.setRoundTransform(TiktokTaskDetailActivity.this,1)).into(iv_trill);
                auditStatus = item.auditStatus;
                if (auditStatus.equals("0")){
                    tv_task_get.setText("今日任务审核中");
                    tv_task_get.setBackgroundColor(getResources().getColor(R.color.tine_grey));
                }else if (auditStatus.equals("3")||auditStatus.equals("2")){
                    tv_task_get.setText("提交任务");
                    tv_task_get.setBackground(getResources().getDrawable(R.drawable.shape_gradient_little_no_coners_blue));
                }else if (auditStatus.equals("1")){
                    tv_task_get.setText("今日任务已完成，明天再来");
                    tv_task_get.setBackgroundColor(getResources().getColor(R.color.tine_grey));
                }else if (auditStatus.equals("4")){
                    tv_task_get.setText("领取任务");
                    tv_task_get.setBackground(getResources().getDrawable(R.drawable.shape_gradient_little_no_coners_blue));
                }
                    iv_task_status.setVisibility(View.INVISIBLE);
                    String content = item.taskDetail;
                    if (TextUtils.isEmpty(content)) {
                        content = "";
                    }
                    mWebView.loadData(getHtmlData(content), "text/html; charset=UTF-8", null);
            }
        });
    }
    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        mWebView.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override public void onPageFinished(WebView view, String url) {
                onH5PageFinished();
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override public void onReceivedTitle(WebView view, String title) {
            }

            @Override public void onProgressChanged(WebView view, int newProgress) {
                onH5ProgressChanged(newProgress);
            }
        });
    }

    private void onH5ProgressChanged(int newProgress) {
    }

    private void onH5PageFinished() {
    }
    private String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML.trim() + "</body>"
                + "</html>";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_task_get:
                if (auditStatus.equals("4")){
                    ApiUserService.getTask(task_id, new ServiceCallBack() {
                        @Override
                        public void failed(String code, String errorInfo, String source) {
                            ToastUtils.showShort(errorInfo);
                        }

                        @Override
                        public void success(RespBean resp, Response payload) {
                            requestData();
                            tv_task_get.setText("提交任务");
                        }
                    });
                }else if (auditStatus.equals("3")||auditStatus.equals("2")){
                    Intent intent = new Intent(this, SubmitTaskActivity.class);
                    intent.putExtra("taskAuditId",taskAuditId);
                    intent.putExtra("task_id",task_id);
                    startActivity(intent);
                }else if (auditStatus.equals("0")){
                    ToastUtils.showShort("今日任务审核中");
                    return;
                }else {
                    ToastUtils.showShort("今日任务已完成，明天再来");
                    return;
                }
                break;
        }
    }
}

package com.qiaoyi.secondworker.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.ui.shake.activity.PostSuccessActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import cn.isif.alibs.utils.ToastUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.item_history.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Contact.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d("sss", "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_tip);
			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
			builder.show();*/
                int code = resp.errCode;
                switch (code) {
                    case 0:
                        try{
//                            MobclickAgent.onEvent(this, Contacts.ACTIVITY_MYSELF_SERVICE);
                        }catch (Exception e){

                        }
                        startActivity(new Intent(this, PostSuccessActivity.class));
                        finish();
                        break;
                    case -1:
                        ToastUtils.showShort("支付失败");
                        finish();
                        break;
                    case -2:
                        ToastUtils.showShort("支付取消");
                        finish();
                        break;
                    default:
                        ToastUtils.showShort("支付失败");
                        setResult(RESULT_OK);
                        finish();
                        break;
                }
            }
        }
}
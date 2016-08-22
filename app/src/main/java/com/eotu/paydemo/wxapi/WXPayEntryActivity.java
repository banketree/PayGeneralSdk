package com.eotu.paydemo.wxapi;

import org.greenrobot.eventbus.EventBus;

import com.eotu.browser.PaySdk;
import com.eotu.browser.R;
import com.eotu.browser.ui.BaseActivity;
import com.sdk.paygeneral.PayEvent;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thinkcore.event.TEvent;
import com.thinkcore.utils.TReflecterUtils;
import com.thinkcore.utils.TStringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class WXPayEntryActivity extends BaseActivity implements
		IWXAPIEventHandler {
	private static final String TAG = WXPayEntryActivity.class.getSimpleName();
	private IWXAPI mWxApi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weixin_pay_result);
		PaySdk paySdk = new PaySdk();
		mWxApi = WXAPIFactory.createWXAPI(this, paySdk.getWeiXinAppId());
		mWxApi.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		mWxApi.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		int result = 0;
	}

	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		String extData = "";
		try {
			extData = (String) TReflecterUtils.getProperty(resp, "extData");
		} catch (Exception e) {
		}

		if (resp.errCode == 0) {
			EventBus.getDefault().post(
					new PayEvent(PayEvent.Event_Pay_Success, extData));
		} else {
			EventBus.getDefault().post(
					new PayEvent(PayEvent.Event_Pay_Fail, extData));
			makeText("支付失败");
		}

		finish();
	}
}
package com.eotu.paydemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.sdk.paygeneral.PayGeneral;
import com.thinkcore.utils.TStringUtils;

public class PaySdk extends PayGeneral {

	private String mAlipayPartner = "";
	private String mAliPaySeller = "";
	private String mAliPayRsaPrivate = "";
	private String mAliPayRsaPublic = "";
	private String mAliPayNotifyUrl = "";

	//
	private String mWeiXinAppId = "";
	private String mWeiXinMchId = "";
	private String mWeiXinApiKey = "";
	private String mWeiXinNotifyUrl = "";

	public static String[] Pay = new String[] { "支付宝", "微信支付" };

	@Override
	public String getAliPayPartner() {
		return "test";// 商户PID
	}

	@Override
	public String getAliPaySeller() {
		return "test";// 商户收款账号
	}

	@Override
	public String getAliPayRsaPrivate() {// 商户私钥，pkcs8格式
		return "test";
	}

	@Override
	public String getAliPayRsaPublic() {// 支付宝公钥
		return "test";
	}

	@Override
	public String getAliPayNotifyUrl() {
		return "test";
	}

	@Override
	public String getWeiXinAppId() {
		return mWeiXinAppId;
	}

	public void setWeiXinAppId(String appId) {
		mWeiXinAppId = appId;
	}

	@Override
	public String getWeiXinMchId() {
		return mWeiXinMchId;
	}

	public void setWeiXinMchId(String mchId) {
		mWeiXinMchId = mchId;
	}

	@Override
	public String getWeiXinApiKey() {
		return mWeiXinApiKey;
	}

	public void setWeiXinApiKey(String apiKey) {
		mWeiXinApiKey = apiKey;
	}

	@Override
	public String getWeiXinNotifyUrl() {
		return mWeiXinNotifyUrl;
	}

	public void setWeiXinNotifyUrl(String notifyUrl) {
		mWeiXinNotifyUrl = notifyUrl;
	}

	public void showPay(final Activity context, final String goodsName,
			final String goodsDetails, final String orderId, final String price) {
		new AlertDialog.Builder(context)
				.setTitle("选择支付平台")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(Pay, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								if (which == 0) {
									aliPay(context, goodsName, goodsDetails,
											orderId, price);
								} else {
									weixinPay(context, goodsName, goodsDetails,
											orderId, price);
								}
							}
						}).setNegativeButton("取消", null).show();

	}

	public void weixinPay(final Activity context, final String goodsName,
			final String goodsDetails, final String orderId, final String price) {
		try {
			int priceD = (int) (TStringUtils.toDouble(price) * 100);
			getWeiXinPay().pay(context, goodsName, goodsDetails, orderId,
					priceD + "");
		} catch (Exception e) {
		}
	}

	public void weixinPayByUnity(final Activity context, final String prepayId,
			final String packageValue, final String nonceStr,
			final String timeStamp, final String sign, final String extData) {
		try {
			getWeiXinPay().payByUnity(context, prepayId, packageValue,
					nonceStr, timeStamp, sign, extData);
		} catch (Exception e) {
		}
	}


	public void aliPay(final Activity context, final String goodsName,
			final String goodsDetails, final String orderId, final String price) {
		try {
			getAliPay().pay(context, goodsName, goodsDetails, orderId, price);
		} catch (Exception e) {
		}
	}
}

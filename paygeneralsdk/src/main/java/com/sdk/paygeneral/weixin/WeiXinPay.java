package com.sdk.paygeneral.weixin;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.sdk.paygeneral.IPay;
import com.sdk.paygeneral.R;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thinkcore.TApplication;
import com.thinkcore.dialog.TDialogManager;
import com.thinkcore.utils.TIpUtil;
import com.thinkcore.utils.TStringUtils;
import com.thinkcore.utils.TToastUtils;
import com.thinkcore.utils.task.TTask;

public class WeiXinPay extends WeiXinInfo implements IPay {
	private static final String TAG = WeiXinPay.class.getSimpleName();
	private static final String WeiXinApi = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	private IWXAPI mWXApi;
	private TTask mPayTask;

	public WeiXinPay() {
	}

	@Override
	public void pay(final Activity context, final String goodsName,
			final String goodsDetails, final String orderId, final String price)
			throws Exception {
		if (context == null || TStringUtils.isEmpty(getWeiXinAppId())
				|| TStringUtils.isEmpty(getWeiXinMchId())
				|| TStringUtils.isEmpty(getWeiXinApiKey())
				|| TStringUtils.isEmpty(goodsName)
				|| TStringUtils.isEmpty(orderId) || TStringUtils.isEmpty(price)
				|| TStringUtils.isEmpty(getWeiXinNotifyUrl())) {
			throw new Exception(TApplication.getResString(R.string.data_lose));
		}

		if (mPayTask != null && !mPayTask.isCancelled()) {
			throw new Exception("正在支付");
		}
		mWXApi = WXAPIFactory.createWXAPI(context, null);
		if (!mWXApi.registerApp(getWeiXinAppId())) {
			throw new Exception("操作失败");
		}
		// 生成订单
		mPayTask = new TTask() {
			private String result,errorString;

			@Override
			protected Object doInBackground(Object[] params) {
				try {
					String url = String.format(WeiXinApi);
					String entity = genProductArgs(goodsName, goodsDetails,
							orderId, price, getWeiXinNotifyUrl());
					byte[] buf = WeiXinUtil.httpPost(url, entity);
					String content = new String(buf);
					Map<String, String> xml = decodeXml(content);
					String prepayID = xml.get("prepay_id");
					if (TStringUtils.isEmpty(prepayID)) {
						throw new Exception();
					}

					result = prepayID;
				} catch (Exception e) {
					errorString = "数据异常";
				}

				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				TDialogManager.showProgressDialog(context, "数据", "加载数据…");
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();

				TDialogManager.hideProgressDialog(context);
				if (!TStringUtils.isEmpty(errorString)) {
					TToastUtils.makeText(errorString);
				}

				if (!TStringUtils.isEmpty(result)) {
					PayReq payReq = genPayReq(result);
					mWXApi.sendReq(payReq);
				}
			}
		};

		mPayTask.newExecute();
	}

	// 统一下单
	public void payByUnity(final Activity context, final String prepayId,
			final String packageValue, final String nonceStr,
			final String timeStamp, final String sign, final String extData)
			throws Exception {
		if (context == null || TStringUtils.isEmpty(getWeiXinAppId())
				|| TStringUtils.isEmpty(getWeiXinMchId())
				|| TStringUtils.isEmpty(prepayId)
				|| TStringUtils.isEmpty(packageValue)
				|| TStringUtils.isEmpty(nonceStr)
				|| TStringUtils.isEmpty(timeStamp)) {
			throw new Exception(TApplication.getResString(R.string.data_lose));
		}

		mWXApi = WXAPIFactory.createWXAPI(context, null);
		if (!mWXApi.registerApp(getWeiXinAppId())) {
			throw new Exception("操作失败");
		}
		// 统一下单
		PayReq payReq = genPayReq(getWeiXinAppId(), getWeiXinMchId(), prepayId,
				packageValue, nonceStr, timeStamp, sign);
		payReq.extData = extData;
		mWXApi.sendReq(payReq);
	}

	public Map<String, String> decodeXml(String content) throws Exception {
		Map<String, String> xml = new HashMap<String, String>();
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(content));
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {

			String nodeName = parser.getName();
			switch (event) {
			case XmlPullParser.START_DOCUMENT:

				break;
			case XmlPullParser.START_TAG:

				if ("xml".equals(nodeName) == false) {
					// 实例化student对象
					xml.put(nodeName, parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}

		return xml;
	}

	private String genNonceStr() {
		Random random = new Random();
		return WeiXinMD5Util.getMessageDigest(String.valueOf(
				random.nextInt(10000)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	//
	private String genProductArgs(String goodsName, String goodsDetails,
			String orderId, String price, String notifyUrl) throws Exception {
		StringBuffer xml = new StringBuffer();
		String nonceStr = genNonceStr();
		xml.append("</xml>");
		List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appid", getWeiXinAppId()));
		packageParams.add(new BasicNameValuePair("body", goodsName)); // 商品描述
		packageParams.add(new BasicNameValuePair("detail", goodsDetails)); // 商品描述
		packageParams.add(new BasicNameValuePair("mch_id", getWeiXinMchId())); // 商家id
		packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));// 随机字符串
		packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));// 回调地址
		packageParams.add(new BasicNameValuePair("out_trade_no", orderId));// 商户订单号
		packageParams.add(new BasicNameValuePair("spbill_create_ip", TIpUtil
				.getIp()));// 终端IP

		packageParams.add(new BasicNameValuePair("total_fee", price));// 总金额
		packageParams.add(new BasicNameValuePair("trade_type", "APP"));

		String sign = genPackageSign(packageParams);
		packageParams.add(new BasicNameValuePair("sign", sign));
		String xmlstring = toXml(packageParams);
		xmlstring = new String(xmlstring.getBytes("UTF-8"), "ISO-8859-1");
		return xmlstring;
	}

	private PayReq genPayReq(String prepayId) {
		PayReq payReq = new PayReq();
		payReq.appId = getWeiXinAppId();
		payReq.partnerId = getWeiXinMchId();
		payReq.prepayId = prepayId;
		payReq.packageValue = "Sign=WXPay";
		payReq.nonceStr = genNonceStr();
		payReq.timeStamp = String.valueOf(genTimeStamp());
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", payReq.appId));
		signParams.add(new BasicNameValuePair("noncestr", payReq.nonceStr));
		signParams.add(new BasicNameValuePair("package", payReq.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", payReq.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", payReq.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", payReq.timeStamp));
		payReq.sign = genAppSign(signParams);
		return payReq;
	}

	private PayReq genPayReq(String appId, String mchId, String prepayId,
			String packageValue, String nonceStr, String timeStamp, String sign) {
		PayReq payReq = new PayReq();
		payReq.appId = appId;
		payReq.partnerId = mchId;
		payReq.prepayId = prepayId;
		payReq.packageValue = packageValue;
		payReq.nonceStr = nonceStr;
		payReq.timeStamp = timeStamp;
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", payReq.appId));
		signParams.add(new BasicNameValuePair("noncestr", payReq.nonceStr));
		signParams.add(new BasicNameValuePair("package", payReq.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", payReq.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", payReq.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", payReq.timeStamp));
		payReq.sign = sign;
		return payReq;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(getWeiXinApiKey());
		String appSign = WeiXinMD5Util.getMessageDigest(
				sb.toString().getBytes()).toUpperCase();
		return appSign;
	}

	/**
	 * 生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(getWeiXinApiKey());

		String packageSign = WeiXinMD5Util.getMessageDigest(
				sb.toString().getBytes()).toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		return sb.toString();
	}
}

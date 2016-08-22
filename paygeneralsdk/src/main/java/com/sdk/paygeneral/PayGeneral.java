package com.sdk.paygeneral;

import com.sdk.paygeneral.alipay.AliPay;
import com.sdk.paygeneral.alipay.IAliPayInfo;
import com.sdk.paygeneral.weixin.IWeiXinInfo;
import com.sdk.paygeneral.weixin.WeiXinPay;
import com.thinkcore.utils.TStringUtils;

public class PayGeneral implements IAliPayInfo, IWeiXinInfo {

	public boolean hasPay(String name) {
		if (TStringUtils.isEmpty(name))
			return false;

		if (name.contains("微信支付") || name.contains("支付宝")) {
			return true;
		}

		return false;
	}

	public AliPay getAliPay() {
		AliPay aliPay = new AliPay();
		aliPay.setAliPayPartner(getAliPayPartner());
		aliPay.setAliPayRsaPrivate(getAliPayRsaPrivate());
		aliPay.setAliPaySeller(getAliPaySeller());
		aliPay.setAliPayNotifyUrl(getAliPayNotifyUrl());
		return aliPay;
	}

	public WeiXinPay getWeiXinPay() {
		WeiXinPay weiXinPay = new WeiXinPay();
		weiXinPay.setWeiXinAppId(getWeiXinAppId());
		weiXinPay.setWeiXinMchId(getWeiXinMchId());
		weiXinPay.setWeiXinApiKey(getWeiXinApiKey());
		weiXinPay.setWeiXinNotifyUrl(getWeiXinNotifyUrl());
		return weiXinPay;
	}

	// alipay
	@Override
	public String getAliPayPartner() {
		return "";
	}

	@Override
	public String getAliPaySeller() {
		return "";
	}

	@Override
	public String getAliPayRsaPrivate() {
		return "";
	}

	@Override
	public String getAliPayRsaPublic() {
		return "";
	}

	@Override
	public String getAliPayNotifyUrl() {
		return "";
	}

	// weixinpay
	@Override
	public String getWeiXinAppId() {
		return "";
	}

	@Override
	public String getWeiXinMchId() {
		return "";
	}

	@Override
	public String getWeiXinApiKey() {
		return "";
	}

	@Override
	public String getWeiXinNotifyUrl() {
		return "";
	}

}

package com.sdk.paygeneral.weixin;

class WeiXinInfo implements IWeiXinInfo {
	protected String mAppId = "";// appid wxefe8fbaacaf77743
	protected String mMchId = ""; // 商户号 1277512301
	protected String mApiKey = "";// API密钥，在商户平台设置
									// FC5E038D38A57032085441E7FE7010B0
	protected String mNotifyUrl = "";

	// protected String mPrePayId = "";

	// public String getPrePayId() {
	// return mPrePayId;
	// }
	//
	// public void setPrePayId(String prePayId) {
	// mPrePayId = prePayId;
	// }
	@Override
	public String getWeiXinAppId() {
		return mAppId;
	}

	public void setWeiXinAppId(String appId) {
		mAppId = appId;
	}

	@Override
	public String getWeiXinMchId() {
		return mMchId;
	}

	public void setWeiXinMchId(String mchId) {
		mMchId = mchId;
	}

	@Override
	public String getWeiXinApiKey() {
		return mApiKey;
	}

	public void setWeiXinApiKey(String apiKey) {
		mApiKey = apiKey;
	}

	@Override
	public String getWeiXinNotifyUrl() {
		return mNotifyUrl;
	}

	public void setWeiXinNotifyUrl(String notifyUrl) {
		mNotifyUrl = notifyUrl;
	}

}

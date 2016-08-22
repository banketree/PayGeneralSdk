package com.sdk.paygeneral;

import android.app.Activity;

public interface IPay {

	public void pay(final Activity context, String goodsName,
			String goodsDetails, String orderId, String price) throws Exception;

}

package com.sdk.paygeneral.alipay;

class AliPayInfo implements IAliPayInfo {
	protected String mPartner = "";// "2088612490264613";
	protected String mSeller = "";// "vflashbuy@hotmail.com";
	protected String mRsaPrivate = "";// "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK9N0wkgmaMAajntej5Z5ewGcp8EIdH2FxWiq7Mw+Yx+6jJllXVx7+YJD3tM3/9F7DOel5UV8kM/woNIoNqMXfqlAHdYzzFCw1DpWMPUtnKNp0NdTqd5C+6q6kOkGUuz9gaQcrQRau2VcxNGeMZ9Xpu2zI98tVUwO/4lHSC3hprdAgMBAAECgYAjyusM0QgtfNczjqZH9sj4IPTkPM6+9NUPyM/v3U8GfZ9JvO4ANcw6lpz/mtmdYGRywa3HddyqGGmZromRNdm2DbzdRXBltNKJ9NBIfDmyfHs+3cGJEi+mtnnKgfAxhwPXjOSczZixH35kcbH5Hrn+edb7w//kTlzRCsi9OeZcgQJBAN8Z6A6YWviwL+ITDoJiiH7+74pM4mh+3JhApkGGdt2rcEYetoxbyZlEX+3zuRmG9QvA07GDXjwxN0Wgqku47P0CQQDJJ5GZWU83TUsXkV45jILWvbDXklnCxGuWVfPddinQ0AnP4fjFaIxEle+W60aoGg2Po/2MpDGRUhd7abaiOLthAkEAs/dv7CYWbDmjxphcwey98sOvcz6l/GkphV/RPVY8mH/s54Nq+joom4w+XfKWw/LLxRldKvqvj5qb3XzHIqIf+QJBAI60kai3IWncXe+kY7J0KsZp4LGu1BtG+gS3icCNefD4Bl4CtFhoaUqnDEiHNaNAiGplLxW9zqSIFrypLLY9hUECQG6ruQSEdsD9l/wPZfpC0V0/TT0rthpNyf2EYhCWWOOKZWsl73GBHtiZngBbKH+Qc79RA30lnvsKb55vNjL7Nd0=";
	protected String mRsaPublic = "";// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	protected String mNotifyUrl = "";
	
	@Override
	public String getAliPayPartner() {
		return mPartner;
	}

	public void setAliPayPartner(String partner) {
		mPartner = partner;
	}

	@Override
	public String getAliPaySeller() {
		return mSeller;
	}

	public void setAliPaySeller(String seller) {
		mSeller = seller;
	}

	@Override
	public String getAliPayRsaPrivate() {
		return mRsaPrivate;
	}

	public void setAliPayRsaPrivate(String rsaPrivate) {
		mRsaPrivate = rsaPrivate;
	}

	@Override
	public String getAliPayRsaPublic() {
		return mRsaPublic;
	}

	public void setAliPayRsaPublic(String rsaPublic) {
		mRsaPublic = rsaPublic;
	}

	@Override
	public String getAliPayNotifyUrl() {
		return mNotifyUrl;
	}
	
	public void setAliPayNotifyUrl(String notifyUrl) {
		mNotifyUrl = notifyUrl;
	}
}

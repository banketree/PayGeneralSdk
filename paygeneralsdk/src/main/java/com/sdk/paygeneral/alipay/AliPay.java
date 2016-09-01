package com.sdk.paygeneral.alipay;

import java.net.URLEncoder;


import com.alipay.sdk.app.PayTask;
import com.sdk.paygeneral.IPay;
import com.sdk.paygeneral.PayEvent;
import com.sdk.paygeneral.R;
import com.thinkcore.TApplication;
import com.thinkcore.dialog.TDialogManager;
import com.thinkcore.utils.TStringUtils;
import com.thinkcore.utils.TToastUtils;
import com.thinkcore.utils.task.TTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public class AliPay extends AliPayInfo implements IPay {
    private TTask mPayTask;

    @Override
    public void pay(final Activity context, String goodsName,
                    String goodsDetails, final String orderId, String price)
            throws Exception {
        if (context == null || TStringUtils.isEmpty(getAliPayPartner())
                || TStringUtils.isEmpty(getAliPaySeller())
                || TStringUtils.isEmpty(getAliPayRsaPrivate())
                || TStringUtils.isEmpty(goodsName)
                || TStringUtils.isEmpty(orderId) || TStringUtils.isEmpty(price)
                || TStringUtils.isEmpty(getAliPayNotifyUrl())) {
            throw new Exception(TApplication.getResString(R.string.data_lose));
        }

        if (mPayTask != null && !mPayTask.isCancelled()) {
            throw new Exception("正在支付");
        }

        String orderInfo = getOrderInfo(orderId, goodsName, goodsDetails,
                price, getAliPayNotifyUrl()); // 订单信息
        String sign = sign(orderInfo);// 对订单做RSA 签名
        sign = URLEncoder.encode(sign, "UTF-8");// 仅需对sign 做URL编码
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        // 生成订单
        mPayTask = new TTask() {
            private String result, errorString;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                 TDialogManager.showProgressDialog(context, "数据",
                 "加载数据…");
            }

            @Override
            protected Boolean doInBackground(Object[] params) {
                try {
                    PayTask alipay = new PayTask(context);// 构造PayTask 对象
                    result = alipay.pay(payInfo);// 调用支付接口，获取支付结果
                } catch (Exception e) {
                    errorString = "数据异常";
                }
                return null;
            }

            @Override
            protected void onCancelled() {
                TDialogManager.hideProgressDialog(context);
                super.onCancelled();

                if (!TStringUtils.isEmpty(errorString)) {
                    TToastUtils.makeText(errorString);
                }

                if (!TStringUtils.isEmpty(result)) {
                    PayResult payResult = new PayResult(result);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        TToastUtils.makeText("支付成功");

                        EventBus.getDefault().post(
                                new PayEvent(PayEvent.Event_Pay_Success,
                                        orderId));
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            TToastUtils.makeText("支付结果确认中");
                        } else {// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            TToastUtils.makeText("支付失败");
                        }
                    }
                }

                mPayTask = null;
            }
        };

        mPayTask.newExecute();
    }

    // 查询终端设备是否存在支付宝认证账户
    public void check(final Activity context) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask payTask = new PayTask(context);// 构造PayTask 对象
                boolean isExist = payTask.checkAccountIfExist();// 调用查询接口，获取查询结果
                EventBus.getDefault().post(
                        new PayEvent(PayEvent.Event_Alipay_Check,
                                isExist ? "1" : "."));
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    // 获取SDK版本号
    public void getSDKVersion(final Activity context) {
        PayTask payTask = new PayTask(context);
        String version = payTask.getVersion();
        Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
    }

    // 创建订单信息
    public String getOrderInfo(String orderNo, String goodsName,
                               String goodsDetails, String price, String notify_url) {
        String orderInfo = "partner=" + "\"" + getAliPayPartner() + "\""; // 签约合作者身份ID
        orderInfo += "&seller_id=" + "\"" + getAliPaySeller() + "\"";// 签约卖家支付宝账号
        orderInfo += "&out_trade_no=" + "\"" + orderNo + "\""; // 商户网站唯一订单号
        orderInfo += "&subject=" + "\"" + goodsName + "\"";// 商品名称
        orderInfo += "&body=" + "\"" + goodsDetails + "\"";// 商品详情
        orderInfo += "&total_fee=" + "\"" + price + "\"";// 商品金额
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";// 服务器异步通知页面路径
        orderInfo += "&service=\"mobile.securitypay.pay\"";// 服务接口名称， 固定值
        orderInfo += "&payment_type=\"1\"";// 支付类型， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";// 参数编码， 固定值
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    // 对订单信息进行签名
    public String sign(String content) {
        return AlipayUtils.sign(content, getAliPayRsaPrivate());
    }

    // 获取签名方式
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
}

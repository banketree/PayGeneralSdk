package com.eotu.paydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    PaySdk paySdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.Button_alipay).setOnClickListener(this);
        findViewById(R.id.Button_weixinpay).setOnClickListener(this);
        paySdk = new PaySdk();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Button_alipay) {
            paySdk.aliPay(this, "test", "test", "test", "0.1");
        } else if (v.getId() == R.id.Button_weixinpay) {
            paySdk.weixinPay(this, "test", "test", "test", "1");
        }
    }
}

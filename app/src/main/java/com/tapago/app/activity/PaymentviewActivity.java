package com.tapago.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tapago.app.R;

public class PaymentviewActivity extends BaseActivity {
    int count = 1;
    CountDownTimer yourCountDownTimer;
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentview);
        mWebView = findViewById(R.id.web_view);

        // The target url to surf using web view
        String url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);

        // Enable java script on web view
        mWebView.getSettings().setJavaScriptEnabled(true);

        // The java script string to execute in web view after page loaded
        // First line put a value in input box
        // Second line submit the form
        final String js = "javascript:document.getElementById('search_form_input_homepage').value='android';" +
                "document.getElementById('search_button_homepage').click()";

        // Set a web view client for web view
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        count++;
                        if (count == 4) {
                            starttimer();
                        }
                    }
                });
            }
        });

    }

    private void starttimer() {
        yourCountDownTimer = new CountDownTimer(10000, 1000) {                     //geriye sayma
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (yourCountDownTimer != null)
            yourCountDownTimer.cancel();
    }
}

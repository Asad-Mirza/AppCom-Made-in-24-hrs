package com.example.admin.appcom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class About_us_activity extends AppCompatActivity {
    String link;
    private WebView webView;
    private ProgressBar progressBar;
    private TextView titleImTextView,URlTextView;
    private ImageView closeImageView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_activity);
        titleImTextView = findViewById(R.id.title);
        URlTextView  =findViewById(R.id.urlTextView);

        closeImageView = findViewById(R.id.closeImageView);

        link = "https://stackoverflow.com/users/9106312/asad-mirza?tab=profile";
        URlTextView.setText(link);




        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setIndeterminate(true);


        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        webView=findViewById(R.id.webview);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        webView.setWebViewClient(new HelloWebViewClient());
        webView.loadUrl(link);

    }
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            // TODO show you progress image

            progressBar.setVisibility(View.VISIBLE);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {    progressBar.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);

            URlTextView.setText(view.getTitle());

            // TODO hide your progress image
            super.onPageFinished(view, url);
        }

    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}


package com.getstream.sdk.chat.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.getstream.sdk.chat.R;
import com.getstream.sdk.chat.StreamChat;
import com.getstream.sdk.chat.model.ModelType;
import com.getstream.sdk.chat.utils.Utils;

/**
 * An Activity showing attachments such as websites, youtube and giphy.
 */
public class AttachmentActivity extends AppCompatActivity {

    private final String TAG = AttachmentActivity.class.getSimpleName();
    WebView webView;

    ImageView iv_image;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_activity_attachment);

        webView = findViewById(R.id.webView);
        iv_image = findViewById(R.id.iv_image);
        progressBar = findViewById(R.id.progressBar);

        configUIs();

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String url = intent.getStringExtra("url");
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(url)) {
            Toast.makeText(this, "Something error!", Toast.LENGTH_SHORT);
            return;
        }
        showAttachment(type, url);
    }


    private void configUIs() {
        iv_image.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        // WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new AppWebViewClients());
    }

    private void showAttachment(final String type, final String url) {
        if (type.equals(ModelType.attach_giphy))
            showGiphy(url);
        else
            loadUrlToWeb(url);
    }

    /**
     * Show web view with url
     *
     * @param url web url
     */
    public void loadUrlToWeb(String url) {
        iv_image.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(StreamChat.getInstance(this).getUploadStorage().signFileUrl(url));
    }


    /**
     * Play giphy with url
     *
     * @param url giphy url
     */
    public void showGiphy(String url) {
        if (url == null) {
            Utils.showMessage(this, "Error!");
            return;
        }
        iv_image.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);

        Glide.with(this)
                .load(StreamChat.getInstance(this).getUploadStorage().signGlideUrl(url))
                .placeholder(R.drawable.stream_placeholder)
                .into(iv_image);
    }

    private class AppWebViewClients extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(StreamChat.getInstance(AttachmentActivity.this).getUploadStorage().signFileUrl(url));
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, StreamChat.getInstance(AttachmentActivity.this).getUploadStorage().signFileUrl(url));
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
            if (error == null){
                StreamChat.logE(this.getClass(),"The load failed due to an unknown error.");
                return;
            }

            StreamChat.logE(this.getClass(), error.toString());
            Utils.showMessage(AttachmentActivity.this, error.toString());
        }
    }
}

package com.example.bandi.instagramapp.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.bandi.instagramapp.Constants;
import com.example.bandi.instagramapp.R;
import com.example.bandi.instagramapp.interfaces.AuthenticationListener;

public class AuthenticationDialog extends Dialog {
    private  AuthenticationListener listener;
    private Context context;
    private WebView webView;

    private final String url = Constants.BASE_URL
            +"oauth/authorize/?client_id="
            +Constants.INSTAGRAM_CLIENT_ID
            +"&redirect_uri="
            +Constants.REDIRECT_URI
            +"&response_typetoken"
            +"&display=touch&scope=public_content";

    public AuthenticationDialog(Context context, AuthenticationListener listener) {
        super(context);

        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);
        initializeWebView();
    }

    private void initializeWebView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){

            String access_token;
            boolean authComplete;
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //need to check here
                if(url.contains("#access_token") && !authComplete){
                    Uri uri =Uri.parse(url);
                    access_token=uri.getEncodedFragment();
                    //get the whole token after sign
                    access_token=access_token.substring(access_token.lastIndexOf("m")+1);
                    Log.e("access_token",access_token);
                    authComplete = true;
                    listener.onCodeRecived(access_token);
                    dismiss();


                }else if(url.contains("?error"))
                {
                    Log.e("access_token","getting error fetching access token");
                    Log.e("response",url);
                    dismiss();
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }
}

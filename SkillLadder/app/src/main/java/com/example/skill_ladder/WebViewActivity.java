package com.example.skill_ladder;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        WebView webView01 = findViewById(R.id.WebView01);
        webView01.getSettings().setJavaScriptEnabled(true);

        webView01.setWebViewClient(new WebViewClient());
        String url="https://www.w3schools.com/php/default.asp";
        webView01.loadUrl(url);
        ImageView imageView01 = findViewById(R.id.webBackIcon01);

        imageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView01.goBack();
            }
        });

        ImageView imageView02 = findViewById(R.id.webforwardIcon01);
        imageView02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView01.goForward();
            }
        });

        Button button01 = findViewById(R.id.webViewBtn01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView01.reload();
            }
        });


    }
}
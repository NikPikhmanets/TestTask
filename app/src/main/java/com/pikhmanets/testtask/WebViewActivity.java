package com.pikhmanets.testtask;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import static com.pikhmanets.testtask.MainActivity.INTENT_POST;
import static com.pikhmanets.testtask.MainActivity.INTENT_TITLE;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String title = getIntent().getStringExtra(INTENT_TITLE);
        setTitle(title);
        String text = getIntent().getStringExtra(INTENT_POST);
        WebView wView = findViewById(R.id.webview);
        wView.loadData(text, "text/html; charset=utf-8", "UTF-8");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

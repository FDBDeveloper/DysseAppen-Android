package com.johnhellbom.dysseappen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URLEncoder;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by John Hellbom on 2016-03-18.
 */
public class News extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = (Toolbar) findViewById(R.id.news_toolbar);
        toolbar.setTitle(getIntent().getStringExtra("title"));

        String header = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" /></head><body class=\"appbody\">";
        String footer = "</body></html>";
        String body = header + getIntent().getStringExtra("body") + footer;

        WebView web = (WebView) findViewById(R.id.newsWeb);
        web.loadDataWithBaseURL("http://dysseappen.se.preview.binero.se/appmain/", body, "text/html", "UTF-8", "");

        setSupportActionBar(toolbar);
    }

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    // endregion
}

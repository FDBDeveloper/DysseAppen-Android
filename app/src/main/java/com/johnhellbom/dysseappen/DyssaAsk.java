package com.johnhellbom.dysseappen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.net.URLEncoder;

import java.net.URLEncoder;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by John Hellbom on 2016-03-18.
 */
public class DyssaAsk extends AppCompatActivity {

    LinearLayout wrapper;
    OffsetButton sendButton;
    TextView notification;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyssa_ask);

        Toolbar toolbar = (Toolbar) findViewById(R.id.dyssa_ask_toolbar);
        setSupportActionBar(toolbar);

        wrapper = (LinearLayout) findViewById(R.id.dyssa_ask_wrapper);
        sendButton = (OffsetButton) findViewById(R.id.dyssa_ask_button);
        notification = (TextView) findViewById(R.id.dyssa_ask_notification);
        text = (EditText) findViewById(R.id.dyssa_ask_text);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(text.getText().toString().length() > 0)
                {
                    try {
                        String url = "http://dysseappen.se.preview.binero.se/ws/daws.asmx/Dyssa_Post?post=" + URLEncoder.encode(text.getText().toString(), "UTF-8");
                        new DyssaSendQuestionTask(DyssaAsk.this, url).execute();
                    } catch (Exception e) { Log.d("error", "URLEncode"); }
                }
            }
        });
    }

    public void callBackSendQuestion(Boolean result) {
        if(result) {
            wrapper.setVisibility(View.INVISIBLE);
            notification.setVisibility(View.VISIBLE);
        }
    }

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    // endregion
}

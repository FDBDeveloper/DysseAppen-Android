package com.johnhellbom.dysseappen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by John Hellbom on 2016-03-18.
 */
public class DyssaResult extends AppCompatActivity {

    String activeQuestion = "";
    Integer activeQuestionID = 0;

    ProgressBar progress;
    LinearLayout wrapper;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyssa_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.dyssa_result_toolbar);
        setSupportActionBar(toolbar);

        progress = (ProgressBar) findViewById(R.id.dyssa_result_progressbar);
        wrapper = (LinearLayout) findViewById(R.id.dyssa_result_wrapper);

        wrapper.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        activeQuestionID = intent.getIntExtra("activeQuestionID", 0);
        activeQuestion = intent.getStringExtra("activeQuestion");

        TextView title = (TextView) findViewById(R.id.dyssa_result_title);
        title.setText(activeQuestion);

        listView = (ListView) findViewById(R.id.dyssa_answers_listview);

        String url = "http://dysseappen.se.preview.binero.se/ws/daws.asmx/Dyssa_SelectReplies?questionID=" + activeQuestionID;
        new DyssaResultTask(DyssaResult.this, url).execute();
    }

    @SuppressWarnings("unchecked")
    public void callBackResult(Pair<Integer, Integer> result) {

        double allReplies = result.first + result.second;
        double yesReplies = result.first;
        double noReplies = result.second;

        double yes = yesReplies / allReplies;
        double no = noReplies / allReplies;

        double negYes = (1.0 - yes);
        double negNo = (1.0 - no);

        RelativeLayout yesBlock = (RelativeLayout) findViewById(R.id.dyssa_yeshare_block);
        RelativeLayout yesSpace = (RelativeLayout) findViewById(R.id.dyssa_yesshare_space);
        TextView yesLabel = (TextView) findViewById(R.id.dyssa_result_yesshare_text);

        RelativeLayout noBlock = (RelativeLayout) findViewById(R.id.dyssa_noshare_block);
        RelativeLayout noSpace = (RelativeLayout) findViewById(R.id.dyssa_noshare_space);
        TextView noLabel = (TextView) findViewById(R.id.dyssa_result_noshare_text);

        ((LinearLayout.LayoutParams)yesSpace.getLayoutParams()).weight = (float)yes;
        ((LinearLayout.LayoutParams)yesBlock.getLayoutParams()).weight = (float)negYes;
        yesSpace.requestLayout();
        yesBlock.requestLayout();

        ((LinearLayout.LayoutParams)noSpace.getLayoutParams()).weight = (float)no;
        ((LinearLayout.LayoutParams)noBlock.getLayoutParams()).weight = (float)negNo;
        noSpace.requestLayout();
        noBlock.requestLayout();

        int shareYes = (int) Math.round(yes * 100.0);
        int shareNo = 100 - shareYes;

        //yesLabel.setText(String.valueOf((int) Math.ceil(yes * 100.0)) + " %");
        //noLabel.setText(String.valueOf((int) Math.ceil(no * 100.0)) + " %");

        yesLabel.setText(String.valueOf(shareYes) + " %");
        noLabel.setText(String.valueOf(shareNo) + " %");

        if(yes < 0.25)
        {
            yesLabel.setVisibility(View.INVISIBLE);
        }

        if(no < 0.25)
        {
            noLabel.setVisibility(View.INVISIBLE);
        }

        String url = "http://dysseappen.se.preview.binero.se/ws/daws.asmx/Dyssa_SelectAnswers?questionID=" + activeQuestionID;
        new DyssaAnswerTask(DyssaResult.this, url).execute();
    }

    public void callBackAnswersData(ArrayList<DyssaAnswer> result) {
        DyssaAnswerAdapter episodeList = new DyssaAnswerAdapter(this, result);
        listView.setAdapter(episodeList);

        wrapper.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
    }

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    // endregion
}

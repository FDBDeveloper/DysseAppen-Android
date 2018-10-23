package com.johnhellbom.dysseappen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by John Hellbom on 2016-03-18.
 */
public class DyssaQuestion extends AppCompatActivity implements DyssaActiveQuestionCaller {

    ProgressBar progress;
    LinearLayout wrapper;

    String activeQuestion = "";
    int activeQuestionID = 0;
    ImageView buttonSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyssa_question);

        Toolbar toolbar = (Toolbar) findViewById(R.id.dyssa_question_toolbar);

        progress = (ProgressBar) findViewById(R.id.dyssa_question_progressbar);
        wrapper = (LinearLayout) findViewById(R.id.dyssa_question_wrapper);

        wrapper.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        Integer passedQuestionID = intent.getIntExtra("activeQuestionID", -1);
        if(passedQuestionID != -1) {

            toolbar.setTitle("Dyssa");

            activeQuestionID = passedQuestionID;
            activeQuestion = intent.getStringExtra("activeQuestion");

            TextView title = (TextView) findViewById(R.id.dyssa_question_title);
            title.setText(activeQuestion);

            if(DroidUtils.ReadString("dyssa_question_" + activeQuestionID) != null) {
                intent = new Intent(DyssaQuestion.this, DyssaResult.class);
                intent.putExtra("activeQuestionID", activeQuestionID);
                intent.putExtra("activeQuestion", activeQuestion);
                startActivity(intent);
            }

            wrapper.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);

        } else {
            String url = "http://dysseappen.se.preview.binero.se/ws/daws.asmx/Dyssa_SelectActiveQuestion";
            new DyssaQuestionTask(this, url).execute();
        }

        setSupportActionBar(toolbar);

        buttonSwitch = (ImageView) findViewById(R.id.dyssa_question_switch);
        buttonSwitch.setTag(true);
        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int offResId = getResources().getIdentifier("offswitch", "drawable", DysseAppenApplication.getAppContext().getPackageName());
                int onResId = getResources().getIdentifier("onswitch", "drawable", DysseAppenApplication.getAppContext().getPackageName());

                boolean val = Boolean.parseBoolean(v.getTag().toString());
                buttonSwitch.setTag(!val);

                ImageView iv = (ImageView) v;

                if (val) {
                    iv.setImageResource(offResId);
                } else {
                    iv.setImageResource(onResId);
                }
            }
        });

        OffsetButton sendButton = (OffsetButton) findViewById(R.id.dyssa_question_answer_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean activeReply = Boolean.parseBoolean(buttonSwitch.getTag().toString());

                DroidUtils.SaveString(DyssaQuestion.this, "dyssa_question_" + activeQuestionID, String.valueOf(activeReply));

                try {
                    String activeAge = "";
                    String activeGen = "";
                    String activeLoc = URLEncoder.encode("Var bor du?", "UTF-8");

                    if (DroidUtils.ReadString("age_displayval") != null) {
                        activeAge = URLEncoder.encode(DroidUtils.ReadString("age_displayval"), "UTF-8");
                    }

                    if (DroidUtils.ReadString("gen_displayval") != null) {
                        activeGen = URLEncoder.encode(DroidUtils.ReadString("gen_displayval"), "UTF-8");
                    }

                    String url = "http://dysseappen.se.preview.binero.se/ws/daws.asmx/Dyssa_Reply?questionID=" + String.valueOf(activeQuestionID) + "&reply=" + String.valueOf(activeReply) + "&a=" + activeAge + "&g=" + activeGen + "&l=" + activeLoc;
                    new DyssaSendAnswerTask(DyssaQuestion.this, url).execute();
                } catch(Exception e) { Log.d("Error", "URLEncode"); }
            }
        });
    }

    public void callBackActiveQuestion(Pair<Integer, String> result) {
        activeQuestionID = result.first;
        activeQuestion = result.second;
        TextView title = (TextView) findViewById(R.id.dyssa_question_title);
        title.setText(result.second);

        DroidUtils.SaveInteger("active_question", activeQuestionID);

        if(DroidUtils.ReadString("dyssa_question_" + activeQuestionID) != null) {
            Intent intent = new Intent(DyssaQuestion.this, DyssaResult.class);
            intent.putExtra("activeQuestionID", activeQuestionID);
            intent.putExtra("activeQuestion", activeQuestion);
            startActivity(intent);
        }

        wrapper.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
    }

    public void callBackSendAnswer(Boolean result) {
        if(result) {
            Intent intent = new Intent(DyssaQuestion.this, DyssaResult.class);
            intent.putExtra("activeQuestionID", activeQuestionID);
            intent.putExtra("activeQuestion", activeQuestion);
            startActivity(intent);
        }
    }

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    // endregion
}

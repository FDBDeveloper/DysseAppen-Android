package com.johnhellbom.dysseappen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by John Hellbom on 2016-03-18.
 */
public class DyssaAllQuestions extends AppCompatActivity {

    ArrayList<Pair<Integer, String>> questions = new ArrayList<>();

    public ListView listView;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyssa_allquestions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.dyssa_allquestions_toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.dyssa_allquestions_listview);
        progressBar = (ProgressBar) findViewById(R.id.dyssa_allquestions_progressbar);

        loadQuestions();
    }

    @SuppressWarnings("unchecked")
    private void loadQuestions(){
        String url = "http://dysseappen.se.preview.binero.se/ws/daws.asmx/Dyssa_SelectAllQuestions";
        new DyssaAllQuestionsTask(this, url).execute();
    }

    // region @ DyssaAllQuestionsTask
    public void callBackData(ArrayList<Pair<Integer, String>> result) {

        if(result != null) {
            for(Pair<Integer, String> question : result) {
                questions.add(question);
            }
        }
        DysseAllQuestionsAdapter questionsList = new DysseAllQuestionsAdapter(this, result);
        listView.setAdapter(questionsList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pair<Integer, String> selectedQuestion = questions.get(position);

                if(DroidUtils.ReadString("dyssa_question_" + selectedQuestion.first) != null) {
                    Intent intent = new Intent(DyssaAllQuestions.this, DyssaResult.class);
                    intent.putExtra("activeQuestionID", selectedQuestion.first);
                    intent.putExtra("activeQuestion", selectedQuestion.second);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DyssaAllQuestions.this, DyssaQuestion.class);
                    intent.putExtra("activeQuestionID", selectedQuestion.first);
                    intent.putExtra("activeQuestion", selectedQuestion.second);
                    startActivity(intent);
                }
            }
        });

        progressBar.setVisibility(View.INVISIBLE);
    }
    // endregion

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //endregion
}

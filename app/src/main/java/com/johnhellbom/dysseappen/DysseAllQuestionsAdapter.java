package com.johnhellbom.dysseappen;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by John Hellbom on 2016-03-06.
 */
public class DysseAllQuestionsAdapter extends ArrayAdapter<String> {

    private ArrayList<Pair<Integer, String>> questions;
    private Activity context;

    @SuppressWarnings("unchecked")
    public DysseAllQuestionsAdapter(Activity context, ArrayList list) {
        super(context, R.layout.listitem_dyssaquestion, list);
        this.context = context;
        this.questions = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = inflater.inflate(R.layout.listitem_dyssaquestion, null, true);

        TextView title = (TextView) listItem.findViewById(R.id.dyssa_question_title);
        ImageView answerIcon = (ImageView) listItem.findViewById(R.id.dyssa_question_answericon);

        Pair<Integer, String> question = questions.get(position);

        listItem.setTag(question.first);
        title.setText(question.second);

        if(DroidUtils.ReadString("dyssa_question_" + question.first) != null) {
            answerIcon.setVisibility(View.VISIBLE);
        }

        return listItem;
    }

}

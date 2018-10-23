package com.johnhellbom.dysseappen;

import android.app.Activity;
import android.util.Log;
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
public class DyssaAnswerAdapter extends ArrayAdapter<String> {

    private ArrayList<DyssaAnswer> answers;
    private Activity context;

    @SuppressWarnings("unchecked")
    public DyssaAnswerAdapter(Activity context, ArrayList list) {
        super(context, R.layout.listitem_dyssa, list);
        this.context = context;
        this.answers = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = inflater.inflate(R.layout.listitem_dyssa, null, true);

        TextView who = (TextView) listItem.findViewById(R.id.dyssa_answer_who);
        TextView text = (TextView) listItem.findViewById(R.id.dyssa_answer_text);
        ImageView profileImage = (ImageView) listItem.findViewById(R.id.dyssa_answer_profileimage);

        DyssaAnswer answer = answers.get(position);

        who.setText(answer.who);
        text.setText("\"" + answer.text + "\"");

        String profileImageUrl = answer.profileImage;
        new DownloadImageTask(profileImage).execute(profileImageUrl);
        profileImage.setScaleType(ImageView.ScaleType.FIT_XY);

        return listItem;
    }

}

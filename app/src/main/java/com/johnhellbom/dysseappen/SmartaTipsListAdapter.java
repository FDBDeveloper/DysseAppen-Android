package com.johnhellbom.dysseappen;

import android.app.Activity;
import android.media.Image;
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
 * Created by John Hellbom on 2016-03-13.
 */
public class SmartaTipsListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> tips;
    private Activity context;
    private SmartaTipsEntryData data;

    @SuppressWarnings("unchecked")
    public SmartaTipsListAdapter(Activity context, ArrayList list, SmartaTipsEntryData data) {
        super(context, R.layout.listitem_smartatips, list);
        this.context = context;
        this.tips = list;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = inflater.inflate(R.layout.listitem_smartatips, null, true);

        if(tips.size() > 0) {
            TextView tipsText = (TextView) listItem.findViewById(R.id.tips_text);
            tipsText.setText(tips.get(position));

            ImageView checkBox = (ImageView) listItem.findViewById(R.id.tips_checkbox);

            if(DroidUtils.ReadBool(context, "tips_" + data.id + "_" + position)) {
                checkBox.setImageResource(R.drawable.checked);
            } else if(!DroidUtils.ReadBool(context, "tips_" + data.id + "_" + position)) {
                checkBox.setImageResource(R.drawable.unchecked);
            }

            TextView tipsHeader = (TextView) listItem.findViewById(R.id.tips_text_header);
            if(data.id == 8 && position == 0) {
                tipsHeader.setText("Läsförståelse");
                tipsHeader.setVisibility(View.VISIBLE);
            } else if(data.id == 8 && position == 5) {
                tipsHeader.setText("Hörförståelse");
                tipsHeader.setVisibility(View.VISIBLE);
            }

            listItem.invalidate();
        }

        return listItem;
    }

}

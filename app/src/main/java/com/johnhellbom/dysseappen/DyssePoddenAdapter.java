package com.johnhellbom.dysseappen;

import android.app.Activity;
import android.content.Context;
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
public class DyssePoddenAdapter extends ArrayAdapter<String> {

    private ArrayList<DyssePoddenEpisode> episodes;
    private Activity context;

    @SuppressWarnings("unchecked")
    public DyssePoddenAdapter(Activity context, ArrayList list) {
        super(context, R.layout.listitem_dyssepodden, list);
        this.context = context;
        this.episodes = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = inflater.inflate(R.layout.listitem_dyssepodden, null, true);

        TextView title = (TextView) listItem.findViewById(R.id.podTitle);
        TextView desc = (TextView) listItem.findViewById(R.id.podDesc);
        TextView date = (TextView) listItem.findViewById(R.id.podDate);

        ImageView playedIcon = (ImageView) listItem.findViewById(R.id.playedIcon);

        DyssePoddenEpisode episode = episodes.get(position);

        title.setText(episode.title);
        desc.setText(episode.description);

        String formattedDate = "";

        try {
            Date d = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH).parse(episode.date.replace(" +0000", ""));
            formattedDate = new SimpleDateFormat("MMMM yyyy").format(d);
       } catch(Exception e) { Log.d("Exception", "Ex: " + e.getMessage()); }

        date.setText(DroidUtils.upperCaseFirst(formattedDate));

        if(DroidUtils.ReadString(episode.url) != null) {
            playedIcon.setVisibility(View.VISIBLE);
        }

        return listItem;
    }

}

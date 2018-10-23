package com.johnhellbom.dysseappen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashSet;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by John Hellbom on 2016-03-05.
 */
public class DyssePodden extends AppCompatActivity implements Animation.AnimationListener, DyssePoddenCaller {

    public ListView listView;
    public ProgressBar progressBar;
    public ArrayList<DyssePoddenEpisode> episodes = new ArrayList<>();
    public DyssePoddenEpisode selectedEpisode;

    public String newsCallerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyssepodden);

        Toolbar toolbar = (Toolbar) findViewById(R.id.dyssepodden_toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.dyssepodden_progressBar);

        newsCallerData = getIntent().getStringExtra("newsCallerData");

        loadEpisodes();
    }

    @SuppressWarnings("unchecked")
    private void loadEpisodes(){
        String url = "http://www.fdb.nu/feed/podcast/";
        new DyssePoddenTask(this, url).execute();
    }

    // region @ DyssePoddenTask
    public void callBackEpisodeData(ArrayList<DyssePoddenEpisode> result) {

        HashSet<String> vals = new HashSet<>();
        if(result != null) {
            for(DyssePoddenEpisode episode : result) {
                episodes.add(episode);
                vals.add(episode.guid);
            }
        }

        DroidUtils.SaveStrings("episodes", vals);

        DyssePoddenAdapter episodeList = new DyssePoddenAdapter(this, episodes);
        listView.setAdapter(episodeList);

        final Animation animBounce;
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        animBounce.setAnimationListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedEpisode = episodes.get(position);
                ImageView playedIcon = (ImageView) view.findViewById(R.id.playedIcon);
                playedIcon.setVisibility(View.VISIBLE);
                ImageView playIcon = (ImageView) view.findViewById(R.id.playIcon);
                playIcon.startAnimation(animBounce);
            }
        });

        if(newsCallerData != null)
        {
            DyssePoddenEpisode directPlayEpisode = null;

            assert result != null;
            for(DyssePoddenEpisode episode : result) {
                if(episode.guid.equals(newsCallerData))
                {
                    directPlayEpisode = episode;
                    break;
                }
            }

            if(directPlayEpisode != null)
            {
                newsCallerData = null;
                Intent playbackIntent = new Intent(DyssePodden.this, Playback.class);
                playbackIntent.putExtra("url", directPlayEpisode.url);
                playbackIntent.putExtra("previewImage", directPlayEpisode.previewImage);
                startActivity(playbackIntent);
            }
        }

        progressBar.setVisibility(View.INVISIBLE);
    }
    // endregion

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //endregion

    //region @ AnimationListener

    @Override
    public void onAnimationEnd(Animation animation) {
        if(selectedEpisode != null)
        {
            Intent playbackIntent = new Intent(DyssePodden.this, Playback.class);
            playbackIntent.putExtra("url", selectedEpisode.url);
            playbackIntent.putExtra("previewImage", selectedEpisode.previewImage);
            startActivity(playbackIntent);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    // endregion

}

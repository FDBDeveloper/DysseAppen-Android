package com.johnhellbom.dysseappen;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.devbrackets.android.exomedia.EMVideoView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by John Hellbom on 2016-03-08.
 */
public class SmartaTipsPlayback extends AppCompatActivity implements MediaPlayer.OnPreparedListener, Animation.AnimationListener, ShareActionProvider.OnShareTargetSelectedListener  {

    ShareActionProvider shareActionProvider = null;
    Intent shareIntent = new Intent(Intent.ACTION_SEND);

    Menu shareMenu;

    SmartaTipsEntryData data;
    EMVideoView playbackView;
    ListView listView;

    Animation animBounce;
    Animation shareAnimBounce;

    Boolean wasPlaying = false;

    LinearLayout.LayoutParams defaultVideoViewParams;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartatips_playback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tips_playback_toolbar);
        toolbar.setBackgroundColor(Color.rgb(34, 34, 34));

        playbackView = (EMVideoView)findViewById(R.id.tips_playback_view);
        playbackView.setShutterColor(Color.TRANSPARENT);
        playbackView.setOnPreparedListener(this);

        listView = (ListView) findViewById(R.id.tips_list);

        if(getIntent().getExtras().getParcelable("data") != null)
        {
            data = getIntent().getExtras().getParcelable("data");
        }

        if(data != null)
        {
            toolbar.setTitle(data.header);

            LinearLayout bg = (LinearLayout)findViewById(R.id.tips_background);
            int resId = getResources().getIdentifier("tipsgradient_" + data.id, "drawable", DysseAppenApplication.getAppContext().getPackageName());
            bg.setBackgroundResource(resId);

            playbackView.setVideoURI(Uri.parse(data.videoUrl));

            animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fastbounce);
            animBounce.setAnimationListener(this);

            shareAnimBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fastbounce);
            shareAnimBounce.setAnimationListener(this);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageView checkbox = (ImageView) view.findViewById(R.id.tips_checkbox);
                    if (!DroidUtils.ReadBool(SmartaTipsPlayback.this, "tips_" + data.id + "_" + position)) {
                        data.tipsStatus.set(position, true);
                        DroidUtils.SaveBool(SmartaTipsPlayback.this, "tips_" + data.id + "_" + position, true);
                        checkbox.setBackgroundResource(R.drawable.checked);
                    } else {
                        data.tipsStatus.set(position, false);
                        DroidUtils.SaveBool(SmartaTipsPlayback.this, "tips_" + data.id + "_" + position, false);
                        checkbox.setBackgroundResource(R.drawable.unchecked);
                    }
                    checkbox.startAnimation(animBounce);
                    setShareText();
                }
            });

            loadTips();

            TextView shareText = (TextView) findViewById(R.id.tips_share_text);
            shareText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(shareAnimBounce);
                }
            });
        }

        shareIntent.setType("text/plain");

        setSupportActionBar(toolbar);
    }

    private void loadTips() {
        SmartaTipsListAdapter tipsList = new SmartaTipsListAdapter(this, data.tips, data);
        listView.setAdapter(tipsList);
    }

    private void setShareText()
    {
        if(data != null) {
            StringBuilder tipsText = new StringBuilder("Min lista - " + data.header + "\n-----\n\n");
            StringBuilder bodyText = new StringBuilder();

            for (int i = 0; i < data.tips.size(); i++) {
                Boolean active = (Boolean) data.tipsStatus.get(i);
                if (active) {

                    if (data.id == 8 && i < 5 && !bodyText.toString().contains("Läsförståelse:")) {
                        bodyText.append("Läsförståelse:\n");
                    }

                    if (data.id == 8 && i >= 5 && !bodyText.toString().contains("Hörförståelse:")) {
                        if (bodyText.toString().contains("Läsförståelse:")) {
                            bodyText.append("\n");
                        }
                        bodyText.append("Hörförståelse:\n");
                    }

                    bodyText.append("• ").append(data.tips.get(i)).append("\n");
                }
            }

            if (bodyText.length() == 0) {
                for (int i = 0; i < data.tips.size(); i++) {

                    if (data.id == 8 && i < 5 && !bodyText.toString().contains("Läsförståelse:")) {
                        bodyText.append("Läsförståelse:\n");
                    }

                    if (data.id == 8 && i >= 5 && !bodyText.toString().contains("Hörförståelse:")) {
                        if (bodyText.toString().contains("Läsförståelse:")) {
                            bodyText.append("\n");
                        }
                        bodyText.append("Hörförståelse:\n");
                    }

                    bodyText.append("• ").append(data.tips.get(i)).append("\n");
                }
            }

            tipsText.append(bodyText.toString().trim());
            tipsText.append("\n\n-----\nSmarta tips i DysseAppen");

            shareIntent.putExtra(Intent.EXTRA_TEXT, tipsText.toString());
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tips_playback_toolbar);
        LinearLayout listWrapper = (LinearLayout) findViewById(R.id.tips_listwrapper);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            defaultVideoViewParams = (LinearLayout.LayoutParams) playbackView.getLayoutParams();

            toolbar.setVisibility(View.GONE);
            listWrapper.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            playbackView.setLayoutParams(params);
            playbackView.layout(10, 10, 10, 10);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

            toolbar.setVisibility(View.VISIBLE);
            listWrapper.setVisibility(View.VISIBLE);

            playbackView.setLayoutParams(defaultVideoViewParams);
            playbackView.layout(10, 10, 10, 10);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(playbackView.isPlaying()) {
            wasPlaying = true;
            playbackView.pause();
        } else {
            wasPlaying = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        playbackView.requestLayout();
        if(wasPlaying)
        {
            playbackView.start();
        }
    }

    // region @ ShareTargetListener
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        shareMenu = menu;
        getMenuInflater().inflate(R.menu.sharemenu, menu);
        MenuItem item = menu.findItem(R.id.share);
        shareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
        shareActionProvider.setOnShareTargetSelectedListener(this);
        setShareText();
        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        this.startActivity(intent);
        return(true);
    }

    // endregion

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //endregion

    // region @ MediaPlayer.OnPreparedListener

    @Override
    public void onPrepared(MediaPlayer mp) {
        playbackView.start();
        //DroidUtils.SaveString(this, url, "played");
    }

    // endregion

    //region @ AnimationListener

    @Override
    public void onAnimationEnd(Animation animation) {

        if(animation == animBounce) {
            listView.invalidateViews();
        } /*else if(animation == shareAnimBounce)
        {
            if(shareMenu != null) {
                shareMenu.performIdentifierAction(R.id.share, 0);
            }
        }*/

        /*if(selectedEpisode != null)
        {
            Intent playbackIntent = new Intent(DyssePodden.this, Playback.class);
            playbackIntent.putExtra("url", selectedEpisode.url);
            playbackIntent.putExtra("previewImage", selectedEpisode.previewImage);
            startActivity(playbackIntent);
        }*/
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    // endregion
}

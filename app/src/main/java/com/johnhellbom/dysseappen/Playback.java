package com.johnhellbom.dysseappen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.devbrackets.android.exomedia.EMVideoView;

/**
 * Created by John Hellbom on 2016-03-08.
 */
public class Playback extends AppCompatActivity implements MediaPlayer.OnPreparedListener  {

    EMVideoView playbackView;
    ImageView previewImageView;

    String url;
    String previewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.playback_toolbar);
        setSupportActionBar(toolbar);

        Intent playbackIntent = getIntent();
        url = playbackIntent.getStringExtra("url");
        previewImage = playbackIntent.getStringExtra("previewImage");

        previewImageView = (ImageView) findViewById(R.id.previewImageView);
        new DownloadImageTask(previewImageView).execute(previewImage);
        previewImageView.setClickable(false);

        playbackView = (EMVideoView)findViewById(R.id.playback_view);
        playbackView.setOnPreparedListener(this);
        playbackView.setVideoURI(Uri.parse(url));
    }

    // region @ MediaPlayer.OnPreparedListener

    @Override
    public void onPrepared(MediaPlayer mp) {
        playbackView.setBackgroundColor(Color.TRANSPARENT);
        playbackView.start();
        DroidUtils.SaveString(this, url, "played");
    }

    // endregion
}

package com.johnhellbom.dysseappen;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by John Hellbom on 2016-02-28.
 */
public class SmartaTipsEntry extends Fragment implements Animation.AnimationListener {

    public SmartaTipsEntryData data;

    public static Fragment newInstance(SmartaTipsEntryData data)
    {
        SmartaTipsEntry f = new SmartaTipsEntry();
        Bundle bdl = new Bundle();
        bdl.putParcelable("data", data);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Object obj = getArguments().getParcelable("data");
        data = (SmartaTipsEntryData)obj;

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.entry_smartatips, container, false);

        LinearLayout wrapper = (LinearLayout)rootView.findViewById(R.id.tips_wrapper);
        int resId = getResources().getIdentifier("tipsgradient_" + data.id, "drawable", DysseAppenApplication.getAppContext().getPackageName());
        wrapper.setBackgroundResource(resId);

        ImageView thumb = (ImageView)rootView.findViewById(R.id.tips_thumb);
        resId = getResources().getIdentifier("tips_" + data.id, "drawable", DysseAppenApplication.getAppContext().getPackageName());
        thumb.setBackgroundResource(resId);

        TextView header = (TextView)rootView.findViewById(R.id.tips_title);
        header.setText(data.header);

        final Animation animBounce;
        animBounce = AnimationUtils.loadAnimation(DysseAppenApplication.getAppContext(), R.anim.bounce);
        animBounce.setAnimationListener(this);

        rootView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImageView playIcon = (ImageView)v.findViewById(R.id.tips_play);
                playIcon.startAnimation(animBounce);
            }
        });

        return rootView;
    }

    //region @ AnimationListener

    @Override
    public void onAnimationEnd(Animation animation) {
        if(data != null)
        {
            Intent intent = new Intent(getContext(), SmartaTipsPlayback.class);
            intent.putExtra("data", data);
            startActivity(intent);
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
package com.johnhellbom.dysseappen;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.matthewtamlin.sliding_intro_screen_library.SelectionIndicator;
import com.onesignal.OneSignal;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends FragmentActivity implements AppCompatCallback {

    private AppCompatDelegate delegate;

    private ViewPager entrypageCarousel;
    private PagerAdapter entrypageCarouselAdapter;

    private ProgressBar mainProgress;
    private SelectionIndicator carouselPagination;

    private XmlPullParserFactory xmlFactoryObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        delegate.setSupportActionBar(toolbar);

        entrypageCarousel = (ViewPager) findViewById(R.id.entrypageCarousel);
        carouselPagination = (SelectionIndicator) findViewById(R.id.carouselPagination);
        mainProgress = (ProgressBar) findViewById(R.id.main_progressBar);

        ImageView fdbLogo = (ImageView) findViewById(R.id.fdblogo);
        fdbLogo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.fdb.nu"));
                startActivity(browserIntent);
            }
        });

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainLayout);
        ViewTreeObserver vto = entrypageCarousel.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainLayout);
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = entrypageCarousel.getMeasuredHeight();
                carouselPagination.getLayoutParams().height = height * 2;
                mainProgress.getLayoutParams().height = height;
            }
        });

        mainProgress.setVisibility(View.VISIBLE);

        loadCarouselEntries();
        setupButtonIntents();

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new OneSignalNotificationHandler())
                .init();
    }

    // region @ OnesignalNotificationHandler
    private class OneSignalNotificationHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            try {
                if (additionalData != null) {
                    if(additionalData.has("action"))
                    {
                        Intent intent;

                        switch (additionalData.getString("action"))
                        {
                            case "dyssepodden":
                                intent = new Intent(DysseAppenApplication.getAppContext(), DyssePodden.class);
                                startActivity(intent);
                                break;

                            case "dyssa":
                                intent = new Intent(DysseAppenApplication.getAppContext(), Dyssa.class);
                                startActivity(intent);
                                break;

                            default:
                                break;
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
    // endregion

    // region @ Buttons

    private void setupButtonIntents() {
        OffsetButton button = (OffsetButton) findViewById(R.id.menubutton1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DyssePodden.class));
            }
        });

        button = (OffsetButton) findViewById(R.id.menubutton2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SmartaTips.class));
            }
        });

        button = (OffsetButton) findViewById(R.id.menubutton3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Dyssa.class));
            }
        });

        button = (OffsetButton) findViewById(R.id.menubutton4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Verktyg.class));
            }
        });
    }

    // endregion

    // region @ Carousel

    @SuppressWarnings("unchecked")
    private void loadCarouselEntries(){
        String url = "http://dysseappen.se.preview.binero.se/ws/daws.asmx/StartCarousel_SelectLatestEntries";
        new CarouselEntriesTask(this, url).execute();
    }

    public void callBackCarouselData(ArrayList<CarouselEntryData> result) {

        List<Fragment> entrypageCarouselFragments = new ArrayList<>();

        if(result != null) {
            for(CarouselEntryData data : result) {
                entrypageCarouselFragments.add(CarouselEntry.newInstance(data));
            }
        }

        entrypageCarouselAdapter = new CarouselAdapter(getSupportFragmentManager(), entrypageCarouselFragments);
        entrypageCarousel.setAdapter(entrypageCarouselAdapter);

        carouselPagination.setNumberOfItems(entrypageCarouselFragments.size());
        //carouselPagination.setActiveItem(0, false);

        entrypageCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                SelectionIndicator carouselPagination = (SelectionIndicator) findViewById(R.id.carouselPagination);
                carouselPagination.setActiveItem(position, true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mainProgress.setVisibility(View.INVISIBLE);

    }

    // endregion

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    // endregion

    // region @ AppCompatCallback
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
    }

    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
    }
    // endregion
}

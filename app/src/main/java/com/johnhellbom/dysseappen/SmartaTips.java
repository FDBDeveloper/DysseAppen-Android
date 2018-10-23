package com.johnhellbom.dysseappen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.matthewtamlin.sliding_intro_screen_library.SelectionIndicator;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by John Hellbom on 2016-03-06.
 */
public class SmartaTips extends AppCompatActivity {

    public  ViewPager tipsCarousel;
    public PagerAdapter tipsCarouselAdapter;
    private final String videoBaseUrl = "http://www.fdb.nu/dysseappen/video/android/";

    public String newsCallerData;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartatips);

        Toolbar toolbar = (Toolbar) findViewById(R.id.smartatips_toolbar);
        setSupportActionBar(toolbar);

        newsCallerData = getIntent().getStringExtra("newsCallerData");

        final List<Fragment> tipsFragments = new ArrayList<>();

        int num = 0;
        SmartaTipsEntryData entry = new SmartaTipsEntryData(1, "Läsa", videoBaseUrl + "lasa.mp4");
        entry.tips.add("Lyssna på text");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Talsyntes");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Du behöver inte läsa högt för klassen");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Tankekarta");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Rubriker och sammanfattning hjälper dig innan du ska läsa en text");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Äga din bok");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(2, "Berätta för kompisar", videoBaseUrl + "beratta-for-kompisar.mp4");
        entry.tips.add("Jag vill berätta för klassen om vad dyslexi är och att jag har det");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Jag vill att min lärare ska berätta för klassen om vad dyslexi är och att jag har det");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(3, "Moderna språk", videoBaseUrl + "moderna-sprak.mp4");
        entry.tips.add("Boken inläst");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Spela in dig själv och lyssna på mobilen");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Titta på film och klipp");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Kortlek med bild och ord");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Äga din bok");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Muntliga förhör");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(4, "Matte", videoBaseUrl + "matte.mp4");
        entry.tips.add("Få talen upplästa");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Multiplikationsruta");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Komihågruta/lathund");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Dela upp och rita lästalet");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        entry = new SmartaTipsEntryData(5, "Prata inför klassen", videoBaseUrl + "prata-infor-klassen.mp4");
        entry.tips.add("Prata med lärare om hur du vill redovisa");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Powerpoint som stöd");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Du behöver ej läsa högt");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(6, "Skriva", videoBaseUrl + "skriva.mp4");
        entry.tips.add("Utgå från en tankekarta");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("StavaRex");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Ordprediktion");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Rätta i din text med hjälp av en talsyntes");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("App som skriver åt dig");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(7, "Engelska", videoBaseUrl + "engelska.mp4");
        entry.tips.add("Engelskabok inläst");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Spela in dig själv och lyssna på mobilen");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Titta på engelsk film och klipp");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Kortlek med bild och ord");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Äga din bok");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Muntliga förhör");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("SpellRight");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(8, "Nationella proven 1", videoBaseUrl + "nationella-proven-1.mp4");
        entry.tips.add("Texten uppdelad i mindre delar");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Förstorat typsnitt");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Frågor upplästa");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Skriva på dator, svara muntligt");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Texten uppläst");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Själva kunna styra lyssnandet med paus och play");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Frågorna upplästa");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Svara muntligt");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Skriva på dator");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Sitta i eget rum eller mindre grupp");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(9, "Nationella proven 2", videoBaseUrl + "nationella-proven-2.mp4");
        entry.tips.add("Förlängd skrivtid");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Uppdelad på flera tillfällen");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Provet uppläst");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Svara muntligt");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Skriva på dator");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Talsyntes");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Rättstavningsprogram");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Förstorad text");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Sitta själv eller i mindre grupp");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(10, "Betyg", videoBaseUrl + "betyg.mp4");
        entry.tips.add("Använd dig av pys paragrafen");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Vad är syftet med läxan? Vad ska jag lära mig?");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Hur ska jag lära mig det här på bästa sätt?");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("När ska jag göra läxan?");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Var ska jag göra läxan?");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Prata med familjen");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Markera rubriker och sammanfatta text");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Läxor ska vara som en repetition");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(11, "Läxor", videoBaseUrl + "laxor.mp4");
        entry.tips.add("Vad är syftet med läxan? Vad ska jag lära mig?");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Hur ska jag lära mig det här på bästa sätt?");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("När ska jag göra läxan?");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Var ska jag göra läxan?");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Prata med familjen");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Markera rubriker och sammanfatta text");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Läxor ska vara som en repetition");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(12, "Prov 1", videoBaseUrl + "prov-1.mp4");
        entry.tips.add("Förbered dig i god tid innan och använd dig av orden Vad, Hur, När och Var");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Fundera kring vilken hjälp du vill ha och berätta för läraren");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Ej avdrag för stavfel");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Du behöver inte rätta någon annans prov");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        num = 0;
        entry = new SmartaTipsEntryData(13, "Prov 2", videoBaseUrl + "prov-2.mp4");
        entry.tips.add("Muntligt");
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Skriva på datorn");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Lyssna på provet");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Dela upp på fler tillfällen");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Komihågruta/lathund");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        entry.tips.add("Eget rum eller sitta avskärmat, alternativt sitta med hörlurar och musik");
        num++;
        entry.tipsStatus.add(DroidUtils.ReadBool(SmartaTips.this, "tips_" + entry.id + "_" + num));
        tipsFragments.add(SmartaTipsEntry.newInstance(entry));

        tipsCarousel = (ViewPager) findViewById(R.id.smartatips_carousel);

        SelectionIndicator tipsPagination = (SelectionIndicator) findViewById(R.id.smartatips_pagination);
        tipsPagination.setNumberOfItems(tipsFragments.size());
        tipsPagination.setActiveItem(0, false);

        tipsCarouselAdapter = new InfinitePagerAdapter(new SmartaTipsAdapter(getSupportFragmentManager(), tipsFragments));
        tipsCarousel.setAdapter(tipsCarouselAdapter);

        tipsCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SelectionIndicator tipsPagination = (SelectionIndicator) findViewById(R.id.smartatips_pagination);
                tipsPagination.setActiveItem(tipsCarousel.getCurrentItem(), true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        OffsetButton startButton = (OffsetButton) findViewById(R.id.tips_startbutton);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SmartaTipsEntry entry = (SmartaTipsEntry)tipsFragments.get(tipsCarousel.getCurrentItem());
                Intent intent = new Intent(SmartaTips.this, SmartaTipsPlayback.class);
                intent.putExtra("data", entry.data);
                startActivity(intent);
            }
        });

        if(newsCallerData != null)
        {
            try {
                Integer directIndex = Integer.parseInt(newsCallerData);
                if (directIndex >= 0 && directIndex < tipsFragments.size()) {
                    newsCallerData = null;
                    tipsCarousel.setCurrentItem(directIndex);
                }
            } catch(Exception e) { Log.d("Error", "could not parse integer"); }
        }
    }

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    // endregion


}
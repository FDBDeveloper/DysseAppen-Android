package com.johnhellbom.dysseappen;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.onesignal.OneSignal;

import org.json.JSONObject;

public class DysseAppenApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        DysseAppenApplication.context = getApplicationContext();

        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/CenturyGothic.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );*/



    }



    public static Context getAppContext() {
        return DysseAppenApplication.context;
    }

}

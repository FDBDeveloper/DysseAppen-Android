package com.johnhellbom.dysseappen;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by johnh on 2016-03-20.
 */
public class BackgroundService extends Service {

    private static final String TAG = "BackgroundService";
    public static final String BACKGROUND_SERVICE_BATTERY_CONTROL = new String("battery_service");

    IBinder mBinder;
    boolean mAllowRebind;

    PeriodicTaskReceiver mPeriodicTaskReceiver = new PeriodicTaskReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = DysseAppenApplication.getAppContext();
        DysseAppenApplication myApplication = (DysseAppenApplication) context.getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        IntentFilter batteryStatusIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = context.registerReceiver(null, batteryStatusIntentFilter);

        if (batteryStatusIntent != null) {
            int level = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPercentage = level / (float) scale;
            float lowBatteryPercentageLevel = 0.14f;

            try {
                int lowBatteryLevel = Resources.getSystem().getInteger(Resources.getSystem().getIdentifier("config_lowBatteryWarningLevel", "integer", "android"));
                lowBatteryPercentageLevel = lowBatteryLevel / (float) scale;
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Missing low battery threshold resource");
            }

            sharedPreferences.edit().putBoolean(BACKGROUND_SERVICE_BATTERY_CONTROL, batteryPercentage >= lowBatteryPercentageLevel).apply();
        } else {
            sharedPreferences.edit().putBoolean(BACKGROUND_SERVICE_BATTERY_CONTROL, true).apply();
        }

        mPeriodicTaskReceiver.restartPeriodicTaskHeartBeat(context, myApplication);
        return 0;
    }
}

package com.johnhellbom.dysseappen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by johnh on 2016-03-20.
 */
public class BootAndUpdateReceiver extends BroadcastReceiver {

    private static final String TAG = "BootAndUpdateReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.MY_PACKAGE_REPLACED"))
        {
            Intent startServiceIntent = new Intent(context, BackgroundService.class);
            context.startService(startServiceIntent);
        }
    }
}
package com.johnhellbom.dysseappen;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnh on 2016-03-20.
 */
public class PeriodicTaskReceiver extends BroadcastReceiver implements DyssePoddenCaller, DyssaActiveQuestionCaller {

    private static final String TAG = "PeriodicTaskReceiver";
    private static final String INTENT_ACTION = "com.johnhellbom.dysseappen.PERIODIC_TASK_HEART_BEAT";
    public static final String BACKGROUND_SERVICE_BATTERY_CONTROL = "battery_service";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            DysseAppenApplication myApplication = (DysseAppenApplication) context.getApplicationContext();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

            if (intent.getAction().equals("android.intent.action.BATTERY_LOW")) {
                sharedPreferences.edit().putBoolean(BACKGROUND_SERVICE_BATTERY_CONTROL, false).apply();
                stopPeriodicTaskHeartBeat(context);
            } else if (intent.getAction().equals("android.intent.action.BATTERY_OKAY")) {
                sharedPreferences.edit().putBoolean(BACKGROUND_SERVICE_BATTERY_CONTROL, true).apply();
                restartPeriodicTaskHeartBeat(context, myApplication);
            } else if (intent.getAction().equals(INTENT_ACTION)) {
                doPeriodicTask(context, myApplication);
            }
        }
    }

    private void doPeriodicTask(Context context, DysseAppenApplication myApplication) {
        //loadEpisodes();
        //loadActiveQuestion();
    }

    @SuppressWarnings("unchecked")
    private void loadEpisodes() {
        String url = "http://www.fdb.nu/feed/podcast/";
        new DyssePoddenTask(this, url).execute();
    }

    private void loadActiveQuestion() {
        String url = "http://dysseappen.se.preview.binero.se/ws/daws.asmx/Dyssa_SelectActiveQuestion";
        new DyssaQuestionTask(this, url).execute();
    }

    public void callBackEpisodeData(ArrayList<DyssePoddenEpisode> result) {

        if(result != null) {

            DyssePoddenEpisode latestEpisode = null;
            HashSet<String> vals = new HashSet<>();

            for(DyssePoddenEpisode episode : result) {
                if(!DroidUtils.ContainsString("episodes", episode.guid)) {
                    if(latestEpisode == null) {
                        latestEpisode = episode;
                    }
                }
                vals.add(episode.guid);
            }

            DroidUtils.SaveStrings("episodes", vals);

            if(latestEpisode != null) {
                showPodcastNotification(DysseAppenApplication.getAppContext(), latestEpisode);
            } else {
                loadActiveQuestion();
            }

        }
    }

    public void callBackActiveQuestion(Pair<Integer, String> result) {
        Integer activeQuestionID = result.first;
        String activeQuestion = result.second;

        Integer storedQuestionID = DroidUtils.ReadInteger("active_question");
        Boolean showNotification = false;
        if(!storedQuestionID.equals(activeQuestionID)) {
            showNotification = true;
        }

        DroidUtils.SaveInteger("active_question", activeQuestionID);

        if(showNotification) {
            showQuestionNotification(DysseAppenApplication.getAppContext(), activeQuestion);
        }
    }

    void showPodcastNotification(Context context, DyssePoddenEpisode episode)
    {
        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_logo)
                .setContentTitle("DyssePodden - Nytt avsnitt")
                .setContentText(episode.title)
                .setLights(0x0000FF, 5000, 100)
                .setAutoCancel(true);

        Intent intent = new Intent(context, DyssePodden.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

        mBuilder.setContentIntent(pi);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    void showQuestionNotification(Context context, String question)
    {
        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_logo)
                .setContentTitle("Dyssa - Ny fråga")
                .setContentText(question)
                .setLights(0x0000FF, 5000, 100)
                .setAutoCancel(true);

        Intent intent = new Intent(context, DyssaQuestion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

        mBuilder.setContentIntent(pi);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    public void restartPeriodicTaskHeartBeat(Context context, DysseAppenApplication myApplication) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        boolean isBatteryOk = sharedPreferences.getBoolean(BACKGROUND_SERVICE_BATTERY_CONTROL, true);
        Intent alarmIntent = new Intent(context, PeriodicTaskReceiver.class);
        boolean isAlarmUp = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null;

        if (isBatteryOk && !isAlarmUp) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmIntent.setAction(INTENT_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HOUR, pendingIntent);
            //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1*60*1000, pendingIntent);
        }
    }

    public void stopPeriodicTaskHeartBeat(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, PeriodicTaskReceiver.class);
        alarmIntent.setAction(INTENT_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);
    }
}

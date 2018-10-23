package com.johnhellbom.dysseappen;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by John Hellbom on 2016-03-05.
 */
public class DyssaSendQuestionTask extends AsyncTask<String, Void, Boolean> {

    private DyssaAsk activity;
    private String url;

    public DyssaSendQuestionTask(DyssaAsk activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected final Boolean doInBackground(String... params) {
        try {

            URL url = new URL(this.url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream stream = connection.getInputStream();
            stream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AsyncTask", "exception");
            return false;
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(Boolean result) {
        activity.callBackSendQuestion(result);
    }

}

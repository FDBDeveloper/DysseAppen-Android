package com.johnhellbom.dysseappen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by John Hellbom on 2016-03-05.
 */
public class DyssaQuestionTask extends AsyncTask<Pair, Void, Pair> {

    private DyssaActiveQuestionCaller activity;
    private String url;
    private XmlPullParserFactory xmlFactoryObject;

    public DyssaQuestionTask(DyssaActiveQuestionCaller activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected final Pair doInBackground(Pair... params) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream stream = connection.getInputStream();
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);

            Pair result = parseXML(parser);
            stream.close();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AsyncTask", "exception");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Pair parseXML(XmlPullParser parser) {

        Integer activeQuestionID = 0;
        String activeQuestion = null;

        int event;
        String name;

        try {
            event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {

                name = parser.getName();

                if(name != null) {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            switch (name)
                            {
                                case "id":
                                    activeQuestionID = Integer.parseInt(parser.nextText());
                                    break;

                                case "question":
                                    activeQuestion = parser.nextText();
                                    break;
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            break;
                    }
                }
                event = parser.next();
            }

            return new Pair(activeQuestionID, activeQuestion);

        } catch (Exception e) {
            Log.d("error", "xml");
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(Pair result) {
        activity.callBackActiveQuestion(result);
    }

}

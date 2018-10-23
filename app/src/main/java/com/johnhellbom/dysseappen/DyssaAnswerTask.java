package com.johnhellbom.dysseappen;

/**
 * Created by John Hellbom on 2016-02-29.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DyssaAnswerTask extends AsyncTask<ArrayList<DyssaAnswer>, Void, ArrayList> {

    private DyssaResult activity;
    private String url;
    private XmlPullParserFactory xmlFactoryObject;

    public DyssaAnswerTask(DyssaResult activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @SafeVarargs
    @Override
    protected final ArrayList doInBackground(ArrayList<DyssaAnswer>... params) {
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

            ArrayList<DyssaAnswer> result = parseXML(parser);
            stream.close();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AsyncTask", "exception");
            return null;
        }
    }

    public ArrayList<DyssaAnswer> parseXML(XmlPullParser parser) {

        ArrayList<DyssaAnswer> answers = new ArrayList<>();
        DyssaAnswer data = null;

        int event;
        String name;

        try {
            event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                name = parser.getName();

                if(name != null) {
                    switch (event) {

                        case XmlPullParser.START_TAG:
                            if(name.equals("item"))
                            {
                                data = new DyssaAnswer();
                            }
                            else {
                                if(data != null)
                                {
                                    switch (name)
                                    {
                                        case "answer":
                                            data.text = parser.nextText();
                                            break;

                                        case "author":
                                            data.who = parser.nextText();
                                            break;

                                        case "image":
                                            data.profileImage = parser.nextText();
                                            break;

                                        default:
                                            break;
                                    }
                                }
                            }

                            break;

                        case XmlPullParser.END_TAG:
                            if(data != null && name.equals("item")) {
                                answers.add(data);
                            }
                            break;
                    }
                }

                event = parser.next();
            }

            return answers;

        } catch (Exception e) {
            Log.d("error", "xml");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(ArrayList result) {
        activity.callBackAnswersData(result);
    }
}
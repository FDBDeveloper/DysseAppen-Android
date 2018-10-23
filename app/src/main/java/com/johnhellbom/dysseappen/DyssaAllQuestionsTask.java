package com.johnhellbom.dysseappen;

/**
 * Created by John Hellbom on 2016-02-29.
 */

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DyssaAllQuestionsTask extends AsyncTask<ArrayList<Pair<Integer, String>>, Void, ArrayList> {

    private DyssaAllQuestions activity;
    private String url;
    private XmlPullParserFactory xmlFactoryObject;

    public DyssaAllQuestionsTask(DyssaAllQuestions activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @SafeVarargs
    @Override
    protected final ArrayList doInBackground(ArrayList<Pair<Integer, String>>... params) {
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

            ArrayList<Pair<Integer, String>> result = parseXML(parser);
            stream.close();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AsyncTask", "exception");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Pair<Integer, String>> parseXML(XmlPullParser parser) {

        ArrayList<Pair<Integer, String>> entries = new ArrayList<>();
        ArrayList data = new ArrayList();

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
                                data = new ArrayList();
                            }
                            else {
                                switch (name)
                                {
                                    case "id":
                                        data.add(parser.nextText());
                                        break;

                                    case "question":
                                        data.add(parser.nextText());
                                        break;

                                    default:
                                        break;
                                }
                            }

                            break;

                        case XmlPullParser.END_TAG:
                            if(name.equals("item")) {
                                Integer id = Integer.valueOf(data.get(0).toString());
                                String question = data.get(1).toString();
                                entries.add(new Pair(id, question));
                            }
                            break;
                    }
                }

                event = parser.next();
            }

            return entries;

        } catch (Exception e) {
            Log.d("errpr", "xml");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(ArrayList result) {
        activity.callBackData(result);
    }
}
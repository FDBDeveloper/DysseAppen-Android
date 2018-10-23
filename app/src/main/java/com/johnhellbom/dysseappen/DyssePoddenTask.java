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

public class DyssePoddenTask extends AsyncTask<ArrayList<DyssePoddenEpisode>, Void, ArrayList> {

    private DyssePoddenCaller activity;
    private String url;
    private XmlPullParserFactory xmlFactoryObject;

    public DyssePoddenTask(DyssePoddenCaller activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @SafeVarargs
    @Override
    protected final ArrayList doInBackground(ArrayList<DyssePoddenEpisode>... params) {
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

            ArrayList<DyssePoddenEpisode> result = parseXML(parser);
            stream.close();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AsyncTask", "exception");
            return null;
        }
    }

    public ArrayList<DyssePoddenEpisode> parseXML(XmlPullParser parser) {

        ArrayList<DyssePoddenEpisode> entries = new ArrayList<>();
        DyssePoddenEpisode data = null;

        int event;
        String name;

        String previewImage = null;

        try {
            event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                name = parser.getName();

                if(name != null) {
                    switch (event) {

                        case XmlPullParser.START_TAG:
                            if(name.equals("itunes:image"))
                            {
                                previewImage = parser.getAttributeValue(null, "href");
                            }
                            else if(name.equals("item"))
                            {
                                data = new DyssePoddenEpisode();
                                data.previewImage = previewImage.replace("http:/www", "http://www");
                            }
                            else {
                                if(data != null)
                                {
                                    switch (name)
                                    {
                                        case "title":
                                            data.title = parser.nextText();
                                            break;

                                        case "pubDate":
                                            data.date = parser.nextText();
                                            break;

                                        case "guid":
                                            data.guid = parser.nextText();
                                            break;

                                        case "description":
                                            data.description = parser.nextText();
                                            break;

                                        case "enclosure":
                                            data.url = parser.getAttributeValue(null, "url");
                                            break;

                                        default:
                                            break;
                                    }
                                }
                            }

                            break;

                        case XmlPullParser.END_TAG:
                            if(data != null && name.equals("item")) {
                                entries.add(data);
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
        activity.callBackEpisodeData(result);
    }
}
package com.johnhellbom.dysseappen;

/**
 * Created by John Hellbom on 2016-02-29.
 */
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;

public class CarouselEntriesTask extends AsyncTask<ArrayList<CarouselEntryData>, Void, ArrayList> {

    private Float textScaleFactor = 1.0f;

    private MainActivity activity;
    private String url;
    private XmlPullParserFactory xmlFactoryObject;

    public CarouselEntriesTask(MainActivity activity, String url) {
        this.activity = activity;
        this.url = url;

        TypedValue multi = new TypedValue();
        activity.getResources().getValue(R.dimen.entrypage_textmultiplier, multi, true);
        textScaleFactor = multi.getFloat();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @SafeVarargs
    @Override
    protected final ArrayList doInBackground(ArrayList<CarouselEntryData>... params) {
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

            ArrayList<CarouselEntryData> result = parseXML(parser);
            stream.close();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AsyncTask", "exception");
            return null;
        }
    }

    public ArrayList<CarouselEntryData> parseXML(XmlPullParser parser) {

        ArrayList<CarouselEntryData> entries = new ArrayList<>();
        CarouselEntryData data = null;

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
                                data = new CarouselEntryData();
                            }
                            else {
                                if(data != null)
                                {
                                    switch (name)
                                    {
                                        case "category":
                                            data.categoryColor = parser.getAttributeValue(null, "color");
                                            data.categorySize = Float.parseFloat(parser.getAttributeValue(null, "size")) * textScaleFactor;
                                            data.category = parser.nextText();
                                            break;

                                        case "title":
                                            data.titleColor = parser.getAttributeValue(null, "color");
                                            data.titleSize = Float.parseFloat(parser.getAttributeValue(null, "size")) * textScaleFactor;
                                            data.title = parser.nextText();
                                            break;

                                        case "description":
                                            data.descriptionColor = parser.getAttributeValue(null, "color");
                                            data.descriptionSize = Float.parseFloat(parser.getAttributeValue(null, "size")) * textScaleFactor;
                                            data.description = parser.nextText();
                                            break;

                                        case "body":
                                            data.body = parser.nextText();
                                            break;

                                        case "image":
                                            data.image = parser.nextText();
                                            break;

                                        case "action":
                                            data.action = parser.nextText();
                                            break;

                                        case "data":
                                            data.data = parser.nextText();
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
            Log.d("error", "xml");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(ArrayList result) {
        activity.callBackCarouselData(result);
    }
}
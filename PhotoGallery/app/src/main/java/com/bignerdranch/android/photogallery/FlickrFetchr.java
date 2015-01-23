package com.bignerdranch.android.photogallery;

import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by administrator on 1/22/15.
 * Class to fetch on URL (used by downloading "photo list xml" and "thumbnail image" )
 */
public class FlickrFetchr {
    public static final String TAG = "FlickrFetchr";

    // Flickr URL
    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String API_KEY = "19d8508ccb78a8ba9d3fc6dc2b870b53";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String PARAM_EXTRAS = "extras";
    private static final String EXTRA_SMALL_URL = "url_s";

    // XML parsing
    private static final String XML_PHOTO = "photo";


    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    //  https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=xxx.
    public ArrayList<GalleryItem> fetchItems() {
        ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();

        try {
            String url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GET_RECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                    .build().toString();
            String xmlString = getUrl(url);
            Log.i(TAG, "Received xml: " + xmlString);

            //parse
            XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));

            parseItems(items, parser);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (XmlPullParserException xppe) {
            //e.printStackTrace();
            Log.e(TAG,"Failed to parse Items", xppe);
        }
        return items;
    }

    // parse photo in XML
    // like: <photo id="15720882324" owner="130526913@N02" secret="d02f665b6b" server="7562" farm="8" title="Band" ispublic="1" isfriend="0" isfamily="0" url_s="https://farm8.staticflickr.com/7562/15720882324_d02f665b6b_m.jpg" height_s="240" width_s="134" />
    void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser)
            throws XmlPullParserException, IOException {
        int eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG &&
                    XML_PHOTO.equals(parser.getName())) {
                String id = parser.getAttributeValue(null, "id");
                String caption = parser.getAttributeValue(null, "title");
                String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);
                GalleryItem item = new GalleryItem();
                item.setId(id);
                item.setCaption(caption);
                item.setUrl(smallUrl);
                items.add(item);
            }
            eventType = parser.next();
        }
    }

}

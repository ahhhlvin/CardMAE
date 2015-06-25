package madelyntav.c4q.nyc.googlecards;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by alvin2 on 6/24/15.
 */
public class FlickrGetter {

    public static final String TAG = "FlickrGetter";

    public static final String FLICKR_JSON_API = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";

    // TODO : Step 1 - complete this method,return the entire json string.
    public String getJsonString() throws IOException {

        URL url = new URL(FLICKR_JSON_API);
// when parsin a Json file this is the procedure use.
        // 0. Create the string that hold the Json
        // 1. Need to create a URL
        // 2. Create the Http(s) that will take the URL
        // 3. Create InputStream: make a connection of the data
        // 4. Create BufferReader: takes a bunch of data and process it
        // 4. Create StringBuilder:
        HttpsURLConnection connection = null;
        InputStream stream = null;

        try {
            connection = (HttpsURLConnection) url.openConnection();
            stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            return builder.toString();
        }
        finally {

            if (connection != null) {
                connection.disconnect();
            }
            if (stream != null) {
                stream.close();
            }

        }
    }

    // TODO : Step 2 - by using Step 1's result, get 20 images' url addresses and save into ArrayList in String.
    public List<String> getBitmapList() throws JSONException, IOException {

        List<String> imageUrlList = new ArrayList<String>();

        String jsonString = getJsonString();

        JSONObject object = new JSONObject(jsonString);
        JSONArray items = object.getJSONArray("items");

        for (int i = 0; i < 4; i++) {

            JSONObject item = (JSONObject) items.get(i);
            JSONObject media = item.getJSONObject("media");
            String imageurl = media.getString("m");
            imageUrlList.add(imageurl);

        }

        return imageUrlList;
    }
}

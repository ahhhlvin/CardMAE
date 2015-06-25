package madelyntav.c4q.nyc.googlecards;


import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private ImageButton mBtnFind, nameButton;
    private GoogleMap mMap;
    private EditText etPlace;
    private MapFragment mapFragment;
    private ArrayList<String> list;
    private ListView listView;
    private EditText listEnter, enterAddress;
    private EditText searchBar, nameText;
    private Button homeButton, workButton;
    private String homeAddress = "", workAddress = "";
    private LinearLayout addressLayout;
    private Button saveAddress;
    private SwipeRefreshLayout swipeLayout;
    private TextView nameView;
    GridView mGridView;
    ImageAdapter adapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        saveAddress = (Button) findViewById(R.id.homeWorkButton);
        enterAddress = (EditText) findViewById(R.id.enterAddress);
        addressLayout = (LinearLayout) findViewById(R.id.addressLayout);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        new AsyncLoading().execute();

                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initializeViews();
                new AsyncLoading().execute();

            }
        });


//        mGridView = (GridView) findViewById(R.id.gridView);
//        mGridView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        nameText = (EditText) findViewById(R.id.nameText);
        nameView = (TextView) findViewById(R.id.nameView);
        nameButton = (ImageButton) findViewById(R.id.nameButton);
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameText.setVisibility(View.GONE);
                nameButton.setVisibility(View.GONE);
                nameView.setVisibility(View.VISIBLE);
                nameView.setText("Hello, " + nameText.getText().toString() + "!");
            }
        });


        listEnter = (EditText) findViewById(R.id.enterList);
        ImageButton listButton = (ImageButton) findViewById(R.id.listButton);


        searchBar = (EditText) findViewById(R.id.searchText);
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchClick(view);
            }
        });




        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);


        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.add(listEnter.getText().toString());
                listEnter.setText("");
                listView.refreshDrawableState();
            }
        });


        // Getting reference to the find button
        mBtnFind = (ImageButton) findViewById(R.id.btn_show);

        // Getting reference to the SupportMapFragment
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // TODO: Getting reference to the Google Map

        mMap = mapFragment.getMap();




        // Getting reference to EditText
        etPlace = (EditText) findViewById(R.id.et_place);

        // Setting click event listener for the find button
        mBtnFind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Getting the place entered
                String location = etPlace.getText().toString();
                findLocation(location);
            }
        });


                saveAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addressLayout.setVisibility(View.GONE);
                    }
                });

                homeButton = (Button) findViewById(R.id.homeButton);
                homeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (homeAddress.equals("")) {
                            addressLayout.setVisibility(View.VISIBLE);
                            saveAddress.setText("SAVE HOME");
                            homeAddress = enterAddress.getText().toString();
                        } else {
                            findLocation(homeAddress);
                        }
                    }
                });
                workButton = (Button) findViewById(R.id.workButton);
                workButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (workAddress.equals("")) {
                            addressLayout.setVisibility(View.VISIBLE);
                            saveAddress.setText("SAVE WORK");
                            workAddress = enterAddress.getText().toString();
                        } else {
                            findLocation(workAddress);
                        }
                    }
                });



    }






    // GOOGLE SEARCH BAR CARD
    public void onSearchClick(View v) {
        try {
            Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
            String searchTerm = searchBar.getText().toString();
            searchIntent.putExtra(SearchManager.QUERY, searchTerm);
            startActivity(searchIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Search Error, please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        }catch(Exception e){
            Log.d("Exception with url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;







    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);



        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();
                return false;
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true));
                Toast.makeText(getApplicationContext(), "Marker has been added", Toast.LENGTH_SHORT).show();
            }

        });

    }

    /** A class, to download Places from Geocoding webservice */
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){

            // Instantiating ParserTask which parses the json data from Geocoding webservice
            // in a non-ui thread
            ParserTask parserTask = new ParserTask();

            // Start parsing the places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }

    /** A class to parse the Geocoding Places in non-ui thread */
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            GeocodeJSONParser parser = new GeocodeJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a an ArrayList */
                places = parser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            // Clears all the existing markers
            mMap.clear();

            for(int i=0;i<list.size();i++){

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("formatted_address");

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker
                markerOptions.title(name);

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                // Locate the first location
                if(i==0)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }


    public void findLocation(String location) {
        if (location == null || location.equals("")) {
            Toast.makeText(getBaseContext(), "No Place is entered", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://maps.googleapis.com/maps/api/geocode/json?";

        try {
            // encoding special characters like space in the user input place
            location = URLEncoder.encode(location, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String address = "address=" + location;

        String sensor = "sensor=false";

        // url , from where the geocoding data is fetched
        url = url + address + "&" + sensor;

        // Instantiating DownloadTask to get places from Google Geocoding service
        // in a non-ui thread
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading the geocoding places
        downloadTask.execute(url);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ConnectionChangeReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive( Context context, Intent intent )
        {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(     ConnectivityManager.TYPE_MOBILE );
            if ( activeNetInfo != null )
            {
                Toast.makeText( context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
            }
            if( mobNetInfo != null )
            {
                Toast.makeText( context, "Mobile Network Type : " + mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
            }
        }
    }

    private void initializeViews() {
        mGridView = (GridView) findViewById(R.id.gridView);
    }


    private class AsyncLoading extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            // TODO : Step 3 - by using FlickrGetter.java, get latest 20 images' Urls from Flickr and return the result.


            try {
                return new FlickrGetter().getBitmapList();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> imageList) {
            // TODO : Step 5 - Now we have ImageAdapter and the data(list), post the picture!

            adapter = new ImageAdapter(MainActivity.this, imageList);
            mGridView.setAdapter(adapter);

        }
    }




}



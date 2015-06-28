package madelyntav.c4q.nyc.googlecards;


import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
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

    private ImageButton mBtnFind, nameButton, largeNameButton, listButton, addCardButton;
    private GoogleMap mMap;
    private EditText etPlace;
    private MapFragment mapFragment;
    private ArrayList<String> list;
    private EditText listEnter, enterAddress;
    private EditText searchBar, nameText;
    private Button homeButton, workButton;
    private String homeAddress = "", workAddress = "", name = "";
    private LinearLayout addressLayout, nameLayout, enterNameLayout;
    private Button saveAddress;
    private SwipeRefreshLayout swipeLayout;
    private TextView nameView;
    private ListView listView;
    GridView mGridView;
    ArrayAdapter<String> listAdapter;
    ImageAdapter adapter;
    protected static CardView flickrCard;
    protected static CardView weatherCard;
    protected static CardView stocksCard;
    protected static CardView mapCard;
    protected static CardView calendarCard;
    protected static CardView todoCard;
    protected static boolean flickrChecked = true;
    protected static boolean weatherChecked = true;
    protected static boolean stocksChecked = true;
    protected static boolean mapChecked = true;
    protected static boolean calendarChecked = true;
    protected static boolean todoChecked = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        flickrCard = (CardView) findViewById(R.id.flickrCard);
//        weatherCard = (CardView) findViewById(R.id.weatherCard);
//        stocksCard = (CardView) findViewById(R.id.stocksCard);
        mapCard = (CardView) findViewById(R.id.mapCard);
//        calendarCard = (CardView) findViewById(R.id.calendarCard);
        todoCard = (CardView) findViewById(R.id.todoCard);

        if (flickrChecked) {
            flickrCard.setVisibility(View.VISIBLE);
        } else {
            flickrCard.setVisibility(View.GONE);
        }
//        if (weatherChecked) {
//            weatherCard.setVisibility(View.VISIBLE);
//        } else {
//            weatherCard.setVisibility(View.GONE);
//        }
//        if (stocksChecked) {
//            stocksCard.setVisibility(View.VISIBLE);
//        } else {
//            stocksCard.setVisibility(View.GONE);
//        }
        if (mapChecked) {
            mapCard.setVisibility(View.VISIBLE);
        } else {
            mapCard.setVisibility(View.GONE);
        }
//        if (calendarChecked) {
//            calendarCard.setVisibility(View.VISIBLE);
//        } else {
//            calendarCard.setVisibility(View.GONE);
//        }
        if (todoChecked) {
            todoCard.setVisibility(View.VISIBLE);
        } else {
            todoCard.setVisibility(View.GONE);
        }


        saveAddress = (Button) findViewById(R.id.homeWorkButton);
        enterAddress = (EditText) findViewById(R.id.enterAddress);
        addressLayout = (LinearLayout) findViewById(R.id.addressLayout);
        nameLayout = (LinearLayout) findViewById(R.id.nameLayout);
        enterNameLayout = (LinearLayout) findViewById(R.id.enterNameLayout);


        // WRITING INFORMATION TO FILE
//        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
//        name = prefs.getString("name", null);
//        homeAddress = prefs.getString("home", "newHome");
//        workAddress = prefs.getString("work", "newWork");
//
//        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
//        editor.putString("name", nameText.getText().toString());
//        editor.putString("home", homeAddress);
//        editor.putString("work", workAddress);
        // TODO: IS THIS CORRECT OR CAN I SAVE ENTIRE ARRAYLIST??
//        for (int i = 0; i < list.size(); i++) {
//            editor.putString("listItem" + i, list.get(i));
//        }
//        editor.commit();
        // TODO: add the list items !!


        // FOR REFRESH ON SWIPE DOWN
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        // TODO: PUT CODE IN HERE THAT WILL BE REFRESHED WITH SWIPE REFRESH LAYOUT
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

        // HANDLES THE METHODS THAT WILL REFRESH UPON SWIPE
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initializeViews();
                new AsyncLoading().execute();

            }
        });


//

        // NAME CARD
        nameText = (EditText) findViewById(R.id.nameText);
        nameView = (TextView) findViewById(R.id.nameView);
        if (!name.equals("")) {
            nameLayout.setVisibility(View.VISIBLE);
            nameView.setText(name);
            enterNameLayout.setVisibility(View.GONE);
        }

        nameButton = (ImageButton) findViewById(R.id.nameButton);
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameText.getText().toString();
                enterNameLayout.setVisibility(View.GONE);
                nameLayout.setVisibility(View.VISIBLE);
                nameView.setText("Hello, " + name + "!");
            }
        });

        largeNameButton = (ImageButton) findViewById(R.id.largeNameButton);
        largeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameLayout.setVisibility(View.GONE);
                enterNameLayout.setVisibility(View.VISIBLE);
            }
        });





        // GOOOGLE SEARCH BAR
        searchBar = (EditText) findViewById(R.id.searchText);
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchClick(view);
            }
        });




        // TO-DO LIST
        listEnter = (EditText) findViewById(R.id.enterList);
        listButton = (ImageButton) findViewById(R.id.listButton);


        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, list);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                listButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!listEnter.equals("")) {
                            list.add(listEnter.getText().toString());
                            listView.setAdapter(listAdapter);
                        }
                            listEnter.setText("");

                    }
                });
            }
        });

        //Makes to-do list scrollable from main scrollview
        listView.setOnTouchListener(new ListView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (list.size() > 3) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                }
                return true;

            }

        });


        // MAP CARD
        mBtnFind = (ImageButton) findViewById(R.id.btn_show);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // TODO: Getting reference to the Google Map
        mMap = mapFragment.getMap();

        etPlace = (EditText) findViewById(R.id.et_place);
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
                if (saveAddress.getText().toString().equals("save home")) {
                    if (!homeAddress.equals("")) {
                        Toast.makeText(getApplicationContext(), "Home address is already set as: " + homeAddress + ", continuing will overwrite.", Toast.LENGTH_LONG).show();
                    }

                    homeAddress = enterAddress.getText().toString();
                    Toast.makeText(getApplicationContext(), "Home address saved", Toast.LENGTH_SHORT).show();

                }

                if (saveAddress.getText().toString().equals("save work")) {
                    if (!workAddress.equals("")) {
                        Toast.makeText(getApplicationContext(), "Work address is already set as: " + workAddress + ", continuing will overwrite.", Toast.LENGTH_LONG).show();
                    }

                    workAddress = enterAddress.getText().toString();
                    Toast.makeText(getApplicationContext(), "Work address saved", Toast.LENGTH_SHORT).show();

                }

                addressLayout.setVisibility(View.GONE);
            }
        });

        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeAddress.equals("")) {
                    addressLayout.setVisibility(View.VISIBLE);
                    saveAddress.setText("save home");
                    enterAddress.setHint("Enter Home Address");
                    enterAddress.setText("");

                } else {
                    findLocation(homeAddress);
                }

            }
        });

        homeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                addressLayout.setVisibility(View.VISIBLE);
                enterAddress.setText("");
                saveAddress.setText("save home");
                if (homeAddress.equals("")) {
                    enterAddress.setHint("Enter Home Address");
                } else {
                    enterAddress.setHint(homeAddress);
                }
                return false;
            }
        });

        workButton = (Button) findViewById(R.id.workButton);
        workButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (workAddress.equals("")) {
                    addressLayout.setVisibility(View.VISIBLE);
                    saveAddress.setText("save work");
                    enterAddress.setHint("Enter Work Address");
                    enterAddress.setText("");
                } else {
                    findLocation(workAddress);
                }

            }
        });

        workButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                addressLayout.setVisibility(View.VISIBLE);
                enterAddress.setText("");
                saveAddress.setText("save work");
                if (homeAddress.equals("")) {
                    enterAddress.setHint("Enter Work Address");
                } else {
                    enterAddress.setHint(workAddress);
                }
                return false;
            }
        });


//         CHECKABLE CARD LIST
        addCardButton = (ImageButton) findViewById(R.id.addCardButton);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectCardIntent = new Intent(getApplicationContext(), SelectCardActivity.class);
                startActivity(selectCardIntent);
            }
        });




    }


    // GOOGLE SEARCH BAR CODE
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


    // GOOGLE MAP CODE
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
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
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Exception with url", e.toString());
        } finally {
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


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng));
                Toast.makeText(getApplicationContext(), "Marker has been added", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * A class, to download Places from Geocoding webservice
     */
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {

            // Instantiating ParserTask which parses the json data from Geocoding webservice
            // in a non-ui thread
            ParserTask parserTask = new ParserTask();

            // Start parsing the places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Geocoding Places in non-ui thread
     */
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            GeocodeJSONParser parser = new GeocodeJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a an ArrayList */
                places = parser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers
            mMap.clear();

            for (int i = 0; i < list.size(); i++) {

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
                if (i == 0)
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


    // FLICKR CARD CODE
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



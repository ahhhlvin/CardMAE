package madelyntav.c4q.nyc.googlecards;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class MapFragment extends Fragment {
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
    View view;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);


        // MAP CARD
        mBtnFind = (ImageButton) view.findViewById(R.id.btn_show);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(getActivity());


        // TODO: Getting reference to the Google Map
        //  mMap = mapFragment.getMap();

        etPlace = (EditText) view.findViewById(R.id.et_place);
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
                        Toast.makeText(getActivity(), "Home address is already set as: " + homeAddress + ", continuing will overwrite.", Toast.LENGTH_LONG).show();
                    }

                    homeAddress = enterAddress.getText().toString();
                    Toast.makeText(getActivity(), "Home address saved", Toast.LENGTH_SHORT).show();

                }

                if (saveAddress.getText().toString().equals("save work")) {
                    if (!workAddress.equals("")) {
                        Toast.makeText(getActivity(), "Work address is already set as: " + workAddress + ", continuing will overwrite.", Toast.LENGTH_LONG).show();
                    }

                    workAddress = enterAddress.getText().toString();
                    Toast.makeText(getActivity(), "Work address saved", Toast.LENGTH_SHORT).show();

                }

                addressLayout.setVisibility(View.GONE);
            }
        });

        homeButton = (Button) view.findViewById(R.id.homeButton);
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

        workButton = (Button) view.findViewById(R.id.workButton);
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

        return view;
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
                Toast.makeText(getActivity(), "Marker has been added", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "No Place is entered", Toast.LENGTH_SHORT).show();
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
}
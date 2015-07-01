package madelyntav.c4q.nyc.googlecards;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;



public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private ImageButton mBtnFind, nameButton, largeNameButton, listButton, addCardButton;
    SharedPreferences toDoListSharedPreferences;
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
    public ImageAdapter adapter;
    protected static CardView flickrCard;
    protected static CardView weatherCard;
    protected static CardView mapCard;
    protected static CardView calendarCard;
    protected static CardView todoCard;
    protected static boolean flickrChecked = true;
    protected static boolean weatherChecked = true;
    protected static boolean stocksChecked = true;
    protected static boolean mapChecked = true;
    protected static boolean calendarChecked = true;
    protected static boolean todoChecked = true;
    com.google.api.services.calendar.Calendar mService;
    public GoogleAccountCredential credential;
    TextView mStatusText;
    public List<String> eventStrings;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    CardFragment mCardFragment;
    AllEventsFragment mAllEventsFragment;
    AddEventToCal mAddEventToCal;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final String PREF_ACCOUNT_NAME = "accountName";
    public static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};
    public PagesAdapter mPagesAdapter;
    public static final String TAG = MainActivity.class.getSimpleName();
    private WeatherInf mCurrentWeather;


    @Bind(R.id.bt_website)
    Button mButton;
    @Bind(R.id.temperatureLabel)
    TextView mTemperatureLabel;
    @Bind(R.id.humidityValue)
    TextView mHumidityValue;
    @Bind(R.id.precipValue)
    TextView mPrecipValue;
    @Bind(R.id.summaryLabel)
    TextView mSummaryLabel;
    @Bind(R.id.windValue)
    TextView mWindValue;
    @Bind(R.id.icon_image_view)
    ImageView mIconImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ButterKnife.bind(this);

//        mCalendar= new Calendar();

        String apiKey = "4bdd5c74892f3ce180e1c291279ce444";
        double latitude = 40.748817;
        double longitude = -73.985428;
        String forcastURL = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forcastURL).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUseraboutError();
                        }
                    } catch (IOException e) {
                    } catch (JSONException e) {
                    }
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://m.weather.com/weather/tenday/USGA0028");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        new ShareNote();

        nameText = (EditText) findViewById(R.id.nameText);
        nameView = (TextView) findViewById(R.id.nameView);

        SharedPreferences sharedPreferences = getSharedPreferences("Name", MODE_PRIVATE);
        String storedName = sharedPreferences.getString("UserName", null);
        homeAddress = sharedPreferences.getString("homeAddress", "");
        workAddress = sharedPreferences.getString("workAddress", "");
        if (name.equals("")) {
            nameView.setText("Hello, " + storedName + "!");
        } else {
            nameLayout.setVisibility(View.VISIBLE);
            nameView.setText(name);
            enterNameLayout.setVisibility(View.GONE);
        }

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                FileInputStream input = null; // Open input stream
                try {
                    input = openFileInput("ToDo.txt");

                    DataInputStream din = new DataInputStream(input);
                    int sz = din.readInt(); // Read line count
                    for (int i = 0; i < sz; i++) { // Read lines
                        String line = din.readUTF();
                        if (line.equals("") || line.equals(null)) {
                        } else {
                            list.add(line);
                        }
                    }
                    din.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });


        flickrCard = (CardView) findViewById(R.id.flickrCard);
        weatherCard = (CardView) findViewById(R.id.weatherCard);
        mapCard = (CardView) findViewById(R.id.mapCard);
        calendarCard = (CardView) findViewById(R.id.calendarCard);
        todoCard = (CardView) findViewById(R.id.todoCard);

        if (flickrChecked) {
            flickrCard.setVisibility(View.VISIBLE);
        } else {
            flickrCard.setVisibility(View.GONE);
        }
        if (weatherChecked) {
            weatherCard.setVisibility(View.VISIBLE);
        } else {
            weatherCard.setVisibility(View.GONE);
        }

        if (mapChecked) {
            mapCard.setVisibility(View.VISIBLE);
        } else {
            mapCard.setVisibility(View.GONE);
        }
        if (calendarChecked) {
            calendarCard.setVisibility(View.VISIBLE);
        } else {
            calendarCard.setVisibility(View.GONE);
        }
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

        // FOR REFRESH ON SWIPE DOWN
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        // TODO: PUT CODE IN HERE THAT WILL BE REFRESHED WITH SWIPE REFRESH LAYOUT
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //new ParserTask.AsyncLoading().execute();

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
                //new ParserTask.AsyncLoading().execute();

            }
        });


//

        // NAME CARD
        nameText = (EditText) findViewById(R.id.nameText);
        nameView = (TextView) findViewById(R.id.nameView);

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
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);


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

        //Swipe to dismiss for ToDoList
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            listAdapter.remove(listAdapter.getItem(position));
                            //TODO remove space leftover after removing

                        }
                        listAdapter.notifyDataSetChanged();


                    }
                });
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());

//        //Makes to-do list scrollable from main scrollview
//        listView.setOnTouchListener(new ListView.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (list.size() > 3) {
//                    int action = event.getAction();
//                    switch (action) {
//                        case MotionEvent.ACTION_DOWN:
//                            // Disallow ScrollView to intercept touch events.
//                            v.getParent().requestDisallowInterceptTouchEvent(true);
//                            break;
//
//                        case MotionEvent.ACTION_UP:
//                            // Allow ScrollView to intercept touch events.
//                            v.getParent().requestDisallowInterceptTouchEvent(false);
//                            break;
//                    }
//
//                    // Handle ListView touch events.
//                    v.onTouchEvent(event);
//                }
//                return true;
//
//            }
//
//        });

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mCardFragment = (CardFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, "mCardFragment");
            mAllEventsFragment = (AllEventsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mTomorrowFragment");

            mAddEventToCal = (AddEventToCal) getSupportFragmentManager().getFragment(savedInstanceState, "mAddEventToCal");
        }
        android.app.FragmentManager fragmentManager = getFragmentManager();

        mPagesAdapter = new PagesAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mPagesAdapter);

        mStatusText = (TextView) findViewById(R.id.mStatusText);

        // Initialize credentials and service object.
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
        }
    }
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == RESULT_OK) {
                    refreshResults();
                } else {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        refreshResults();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    mStatusText.setText("Account unspecified.");
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    refreshResults();
                } else {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        getSupportFragmentManager().putFragment(outState, "mCardFragment", mCardFragment);
//        getSupportFragmentManager().putFragment(outState, "mTomorrowFragment", mAllEventsFragment);
////        getSupportFragmentManager().putFragment(outState,"mAddEventToCal", mAddEventToCal);
////        outState.putStringArrayList("eventStrings",);
    }

    /**
     * Attempt to get a set of data from the Google Calendar API to display. If the
     * email address isn't known yet, then call chooseAccount() method so the
     * user can pick an account.
     */
    private void refreshResults() {
        if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                new ApiAsyncTask(this).execute();
            } else {
                //mStatusText.setText("No network connection available.");
            }
        }
    }

    /**
     * Clear any existing Google Calendar API data from the TextView and update
     * the header message; called from background threads and async tasks
     * that need to update the UI (in the UI thread).
     */
    public void clearResultsText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusText.setText("Retrieving data…");

            }
        });
    }

    /**
     * Fill the data TextView with the given List of Strings; called from
     * background threads and async tasks that need to update the UI (in the
     * UI thread).
     *
     * @param dataStrings a List of Strings to populate the main TextView with.
     */
    public void updateResultsText(final List<String> dataStrings) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataStrings == null) {mStatusText.setText("Error retrieving data!");
                } else if (dataStrings.size() == 0) {
                    mStatusText.setText("No data found.");
                } else {
                    mStatusText.setText("");
                }
            }
        });
    }

    /**
     * Show a status message in the list header TextView; called from background
     * threads and async tasks that need to update the UI (in the UI thread).
     *
     * @param message a String to display in the UI header TextView.
     */
    public void updateStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusText.setText(message);
            }
        });
    }

    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {
        startActivityForResult(
                credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,
                        MainActivity.this,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
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

    private void initializeViews() {
        mGridView = (GridView) findViewById(R.id.gridView);
    }

    private void alertUseraboutError() {
        HandlerError dialofFragment = new HandlerError();
        dialofFragment.show(getFragmentManager(), "error_dialog");
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrentWeather.getTemperature() + "");
        mHumidityValue.setText(mCurrentWeather.getHumidity() + "%");
        mPrecipValue.setText(mCurrentWeather.getPrecipChance() + "%");
        mSummaryLabel.setText(mCurrentWeather.getSummary());
        mWindValue.setText(mCurrentWeather.getWind() + "mph");


        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private WeatherInf getCurrentDetails(String jsonData) throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");

        WeatherInf currentWeather = new WeatherInf();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setWind(currently.getDouble("windSpeed"));
        currentWeather.setState(timeZone);

        return currentWeather;

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

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
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


    @Override
    protected void onPause() {
        super.onPause();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
                    FileOutputStream output = openFileOutput("ToDo.txt", MODE_WORLD_READABLE);
                    DataOutputStream dout = new DataOutputStream(output);
                    dout.writeInt(list.size()); // Save line count
                    for (String line : list) // Save lines
                        dout.writeUTF(line);
                    dout.flush(); // Flush stream ...
                    dout.close(); // ... and close.
                } catch (IOException exc) {
                    exc.printStackTrace();
                }

            }
        });

        SharedPreferences namePref = getSharedPreferences("Name", MODE_PRIVATE);
        SharedPreferences.Editor editor = namePref.edit();
        editor.putString("UserName", nameText.getText().toString());
        editor.putString("homeAddress", homeAddress);
        editor.putString("workAddress", workAddress);
        editor.apply();

    }

    public class ApiAsyncTask extends AsyncTask<Void, Void, ArrayList<String>>  {
        public MainActivity mActivity;
        private OnTaskCompleted listener;
        private Context contxt;
        private Activity activity;
        private ArrayList<String> eventStrings;
        private ArrayList<String> todayEventstrings;

        /**
         * Constructor.
         *
         * @param activity MainActivity that spawned this task.
         */
        ApiAsyncTask(MainActivity activity) {
            this.mActivity = activity;
        }
        public ApiAsyncTask(OnTaskCompleted listener) {
            this.listener = listener;
        }
        public ApiAsyncTask(Context context, OnTaskCompleted listener) {
            // API = apiURL;
            this.contxt = context;
            this.listener = listener;
        }


        /**
         * Background task to call Google Calendar API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> results = new ArrayList<String>();
            try {
                results.addAll(getDataFromApi());
            } catch (IOException e) {
                // log an error
            }
            return results;
        }

        public void onTaskCompleted(Boolean success) {
            mPagesAdapter.notifyDataSetChanged();
        }


        @Override
        protected  void onPostExecute(final ArrayList<String> eventStrings) {
            if (eventStrings.size() > 0) {
                mActivity.clearResultsText();

            } else {

            }
            mAllEventsFragment.updateEventData(eventStrings);

            if(todayEventstrings.size() > 0) {
                mCardFragment.updateEventData(todayEventstrings);
            }
            else{

                mStatusText.setText("No Events Found");
            }

            mStatusText.setText("");

        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         *
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private ArrayList<String> getDataFromApi() throws IOException {

            DateTime now = new DateTime(System.currentTimeMillis());
            DateTime tomorrow = new DateTime(System.currentTimeMillis() + 86400000);

            eventStrings = new ArrayList<String>();
            todayEventstrings= new ArrayList<>();

            Events events = mActivity.mService.events().list("primary")
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            final List<Event> items = events.getItems();


            try {
                FileOutputStream output = openFileOutput("lines.txt", MODE_WORLD_READABLE);
                DataOutputStream dout = new DataOutputStream(output);
                dout.writeInt(items.size()); // Save line count
                for (Event line : items) // Save lines
                    dout.writeUTF(String.valueOf(line));
                dout.flush(); // Flush stream ...
                dout.close(); // ... and close.
            } catch (IOException exc) {
                exc.printStackTrace();
            }

            if(items.size()==0){

                FileInputStream input = null; // Open input stream
                try {
                    input = openFileInput("lines.txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                DataInputStream din = new DataInputStream(input);
                int sz = 0; // Read line count
                try {
                    sz = din.readInt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < sz; i++) { // Read lines
                    String line = null;
                    try {
                        line = din.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    eventStrings.add(line);
                }
                try {
                    din.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }





            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();

                DateTime end = event.getEnd().getDateTime();

                String location = event.getLocation();
                if (location == null) {
                    location = "";
                }
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                    if (end == null) {
                        end = event.getEnd().getDate();
                    }
                }
                eventStrings.add(
                        String.format(event.getSummary() + " /" + location + " /" + start + " /" + end));
            }
            //Get all of the events of the primary calendar for the next 24 hours
            Events events1=mActivity.mService.events().list("primary")
                    .setTimeMin(now)
                    .setTimeMax(tomorrow)
                    .setSingleEvents(true)
                    .execute();


            List<Event> items1 = events1.getItems();
            for (Event event : items1) {
                DateTime start1 = event.getStart().getDateTime();

                DateTime end1 = event.getEnd().getDateTime();

                String location = event.getLocation();
                if (location == null) {
                    location = "";
                }
                if (start1 == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start1 = event.getStart().getDate();
                    if (end1 == null) {
                        end1 = event.getEnd().getDate();
                    }
                }
                todayEventstrings.add(event.getSummary()+"/"+location+"/"+start1+"/"+end1);
            }

            return eventStrings;
        }

    }

    public class PagesAdapter extends FragmentPagerAdapter {
        public PagesAdapter(android.support.v4.app.FragmentManager fragmentManager) {
            super(fragmentManager);
            mCardFragment = new CardFragment();
            mAllEventsFragment = new AllEventsFragment();
            mAddEventToCal = new AddEventToCal();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if (position == 0) {
                return mCardFragment;
            } else if (position == 1) {
                return mAllEventsFragment;
            } else if (position == 2) {
                return mAddEventToCal;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position ==0){
                return "Today's Events";
            }
            else if(position==1){
                return "All Upcoming Events";
            }
            else if(position==2){
                return "Create New Event";
            }
            return null;
        }
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

        public final class AsyncLoading extends AsyncTask<Void, Void, List<String>> {

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
}










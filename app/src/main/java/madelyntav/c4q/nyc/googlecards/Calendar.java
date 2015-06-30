package madelyntav.c4q.nyc.googlecards;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Calendar extends ActionBarActivity implements OnTaskCompleted {

    /**
     * A Google Calendar API service object used to access the API.
     * Note: Do not confuse this class with API library's model classes, which
     * represent specific data structures.
     */
    com.google.api.services.calendar.Calendar mService;

    public GoogleAccountCredential credential;
    TextView mStatusText;
    TextView mResultsText;
    public List<String> eventStrings;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    CardFragment mCardFragment;
    AllEventsFragment mAllEventsFragment;
    AddEventToCal mAddEventToCal;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};
    private PagesAdapter mPagesAdapter;

    /**
     * Create the main activity.
     *
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mCardFragment = (CardFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, "mCardFragment");
            mAllEventsFragment = (AllEventsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mTomorrowFragment");

            mAddEventToCal = (AddEventToCal) getSupportFragmentManager().getFragment(savedInstanceState, "mAddEventToCal");
        }

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

    /**
     * Called whenever this activity is pushed to the foreground, such as after
     * a call to onCreate().
     */
    //if googlePlay is on... refresh activity and show events, otherwise app has to be restarted
    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        android.os.Handler handler = new android.os.Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                try {
                    FileOutputStream output = openFileOutput("lines.txt", MODE_WORLD_READABLE);
                    DataOutputStream dout = new DataOutputStream(output);
                    dout.writeInt(eventStrings.size()); // Save line count
                    for (String line : eventStrings) // Save lines
                        dout.writeUTF(line);
                    dout.flush(); // Flush stream ...
                    dout.close(); // ... and close.
                } catch (IOException exc) {
                    exc.printStackTrace();
                }


            }
        });
    }


    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
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
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mCardFragment", mCardFragment);
        getSupportFragmentManager().putFragment(outState, "mTomorrowFragment", mAllEventsFragment);
//        getSupportFragmentManager().putFragment(outState,"mAddEventToCal", mAddEventToCal);
//        outState.putStringArrayList("eventStrings",);
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
                        Calendar.this,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }



    @Override
    public void onTaskCompleted(Boolean success) {
        mPagesAdapter.notifyDataSetChanged();
    }


    public class ApiAsyncTask extends AsyncTask<Void, Void, ArrayList<String>>  {
        public Calendar mActivity;
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
        ApiAsyncTask(Calendar activity) {
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


        @Override
        protected  void onPostExecute(final ArrayList<String> eventStrings) {
            if (eventStrings.size() > 0) {
                mActivity.clearResultsText();
                mAllEventsFragment.updateEventData(eventStrings);

            } else {

                mStatusText.setText("No Events Found");
            }

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

                List<Event> items = events.getItems();
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
}

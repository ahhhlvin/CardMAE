//package madelyntav.c4q.nyc.googlecards;
//
//import android.os.AsyncTask;
//import android.provider.*;
//import android.provider.Settings;
//
//import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
//import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
//import com.google.api.client.util.DateTime;
//
//import com.google.api.services.calendar.model.*;
//
//import java.io.IOException;
//import java.sql.Time;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.concurrent.CancellationException;
//
///**
// * An asynchronous task that handles the Google Calendar API call.
// * Placing the API calls in their own task ensures the UI stays responsive.
// */
//public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
//    private Calendar mActivity;
//    public List<String> eventStrings;
//
//    /**
//     * Constructor.
//     * @param activity MainActivity that spawned this task.
//     */
//    ApiAsyncTask(Calendar activity) {
//        this.mActivity = activity;
//    }
//
//    /**
//     * Background task to call Google Calendar API.
//     * @param params no parameters needed for this task.
//     */
//    @Override
//    protected Void doInBackground(Void... params) {
//        try {
//            mActivity.clearResultsText();
//            mActivity.updateResultsText(getDataFromApi());
//
//        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
//            mActivity.showGooglePlayServicesAvailabilityErrorDialog(
//                    availabilityException.getConnectionStatusCode());
//
//        } catch (UserRecoverableAuthIOException userRecoverableException) {
//            mActivity.startActivityForResult(
//                    userRecoverableException.getIntent(),
//                    Calendar.REQUEST_AUTHORIZATION);
//
//        } catch (IOException e) {
//            mActivity.updateStatus("The following error occurred: " +
//                    e.getMessage());
//        }
//        return null;
//    }
//    /**
//     * Fetch a list of the next 10 events from the primary calendar.
//     * @return List of Strings describing returned events.
//     * @throws IOException
//     */
//    private List<String> getDataFromApi() throws IOException {
//        // List the next 10 events from the primary calendar.
////        org.joda.time.DateTime now = new org.joda.time.DateTime(System.currentTimeMillis());
////        org.joda.time.DateTime tom= now.plusDays(1).withTimeAtStartOfDay();
//
//        DateTime now= new DateTime(System.currentTimeMillis());
//        DateTime tomorrow= new DateTime(System.currentTimeMillis()+86400000);
//
//
//        eventStrings = new ArrayList<String>();
//        Events events = mActivity.mService.events().list("primary")
//                .setMaxResults(10)
//
//                .setTimeMin(now)
//                .setTimeMax(tomorrow)
//                .setOrderBy("startTime")
//                .setSingleEvents(true)
//                .execute();
//        List<Event> items = events.getItems();
//
//        for (Event event : items) {
//            DateTime start = event.getStart().getDateTime();
//            DateTime end= event.getEnd().getDateTime();
//            if (start == null) {
//                // All-day events don't have start times, so just use
//                // the start date.
//                start = event.getStart().getDate();
//                if(end==null){
//                    end=event.getEnd().getDate();
//                }
//            }
//            eventStrings.add(
//                    String.format("%s (%s) (%s)", event.getSummary(), start, end));
//        }
//        return eventStrings;
//    }
//
//
//
//}
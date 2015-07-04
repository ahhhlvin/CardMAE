package madelyntav.c4q.nyc.googlecards;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllEventsFragment extends android.support.v4.app.Fragment {

    View rowView;
    TextView location;
    private ListView mEventListView;
    private List<String> mEvents = new ArrayList<String>();
    private EventAdapter mEventAdapter;

    public AllEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRestoreInstanceState(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            // Inflate the layout for this fragment
            final View fragmentView= inflater.inflate(R.layout.fragment_tomorrow, container, false);
            rowView=getActivity().getLayoutInflater().inflate(R.layout.row, null);

            mEventListView=(ListView) fragmentView.findViewById(R.id.myListView);
            mEventAdapter= new EventAdapter(getActivity(),R.id.timeStart, mEvents);
            location=(TextView) rowView.findViewById(R.id.location);
            mEventListView.setAdapter(mEventAdapter);


        mEventListView.setOnTouchListener(new ListView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

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

                return true;

            }

        });


            return fragmentView;
        }

    //Here you can restore saved data in onSaveInstanceState Bundle
    private void onRestoreInstanceState(Bundle savedInstanceState){
        if(savedInstanceState!=null){
            //String SomeText = savedInstanceState.getString("title");
            mEvents=savedInstanceState.getStringArrayList("Event Array");
        }
    }

    public void updateEventData(List<String> eventDataList) {
        mEventAdapter.clear();
        mEventAdapter.addAll(eventDataList);
        mEvents.addAll(eventDataList);
        mEventAdapter.notifyDataSetChanged();
    }


    private class EventAdapter extends ArrayAdapter {
        public EventAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = getActivity().getLayoutInflater().inflate(R.layout.row, null);
            String [] div=mEvents.get(position).split("/");

            //String[] div = event1.split("/");
            String eventTitle = div[0].toString();
            String eventLocation = div[1].toString();
            String timeBegin = div[2].toString();
            String dateBegin;
            String timeBegin1;
            String concatTimeBegin="";
            String dateBeginFix="";
            if(timeBegin.length()>11){
                String[] divDate=timeBegin.split("T");
                dateBegin=divDate[0].toString();
                dateBeginFix+=Character.toString(dateBegin.charAt(5))+ Character.toString(dateBegin.charAt(6))+Character.toString(dateBegin.charAt(7))+Character.toString(dateBegin.charAt(8))+Character.toString(dateBegin.charAt(9))+Character.toString(dateBegin.charAt(4))+Character.toString(dateBegin.charAt(0))+Character.toString(dateBegin.charAt(1))+Character.toString(dateBegin.charAt(2))+Character.toString(dateBegin.charAt(3));
                timeBegin1=divDate[1].toString();
                for(int i=0;i<5;i++){
                    concatTimeBegin+=timeBegin1.charAt(i);
                }
            } else{ concatTimeBegin="";
                dateBeginFix+=Character.toString(timeBegin.charAt(5))+ Character.toString(timeBegin.charAt(6))+Character.toString(timeBegin.charAt(7))+Character.toString(timeBegin.charAt(8))+Character.toString(timeBegin.charAt(9))+Character.toString(timeBegin.charAt(4))+Character.toString(timeBegin.charAt(0))+Character.toString(timeBegin.charAt(1))+Character.toString(timeBegin.charAt(2))+Character.toString(timeBegin.charAt(3));
            }

            String eventTimeEnd = div[3].toString();

            String dateEnd;
            String timeEventEnd;
            String concatEndTime = "";
            String dateEndFix="";
            if(eventTimeEnd.length()>11){
                String[] endDivDate=eventTimeEnd.split("T");
                dateEnd=endDivDate[0].toString();
                dateEnd.toString();
                dateEndFix+= Character.toString(dateEnd.charAt(5))+Character.toString(dateEnd.charAt(6))+Character.toString(dateEnd.charAt(7))+Character.toString(dateEnd.charAt(8))+Character.toString(dateEnd.charAt(9))+Character.toString(dateEnd.charAt(4))+Character.toString(dateEnd.charAt(0))+Character.toString(dateEnd.charAt(1))+Character.toString(dateEnd.charAt(2))+Character.toString(dateEnd.charAt(3));
                timeEventEnd=endDivDate[1].toString();
                for(int i=0;i<5;i++){
                    concatEndTime+=timeEventEnd.charAt(i);
                }
            } else{
                dateEndFix+= " - " +Character.toString(eventTimeEnd.charAt(5))+Character.toString(eventTimeEnd.charAt(6))+Character.toString(eventTimeEnd.charAt(7))+Character.toString(eventTimeEnd.charAt(8))+Character.toString(eventTimeEnd.charAt(9))+Character.toString(eventTimeEnd.charAt(4))+Character.toString(eventTimeEnd.charAt(0))+Character.toString(eventTimeEnd.charAt(1))+Character.toString(eventTimeEnd.charAt(2))+Character.toString(eventTimeEnd.charAt(3));
                timeEventEnd="";
            }

            if(dateEndFix.toString().equals(dateBeginFix.toString())){
                dateEndFix="";

            }

            TextView date= (TextView) rowView.findViewById(R.id.date);
            date.setText(dateBeginFix);
            TextView endDate=(TextView) rowView.findViewById(R.id.endDate);
            endDate.setText(dateEndFix);
            TextView title = (TextView) rowView.findViewById(R.id.title);
            title.setText(eventTitle);
            TextView location = (TextView) rowView.findViewById(R.id.location);
            location.setText(eventLocation);
            TextView timeStart = (TextView) rowView.findViewById(R.id.timeStart);
            timeStart.setText(concatTimeBegin);
            TextView timeEnd=(TextView) rowView.findViewById(R.id.timeEnd);
            timeEnd.setText(concatEndTime);

            TextView to=(TextView) rowView.findViewById(R.id.to);
            if(timeEventEnd!=""&& concatTimeBegin!=""){
                to.setText(" until ");}
            else{ to.setText("");}

            return rowView;

        }
    }
}
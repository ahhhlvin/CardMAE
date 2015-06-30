package madelyntav.c4q.nyc.googlecards;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends android.support.v4.app.Fragment {

    private View mCardFragmentView;
    private ListView mEventListView;
    private List<String> mEvents = new ArrayList<String>();
    private EventAdapter mEventAdapter;

    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState!=null){
//           mEvents=new ArrayList<>();
//            mEvents=savedInstanceState.getStringArrayList("Event Array");
//        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            mEvents=new ArrayList<>();
            mEvents=savedInstanceState.getStringArrayList("Event Array");
        }
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        mCardFragmentView = inflater.inflate(R.layout.fragment_card, container, false);
        mEventListView = (ListView) mCardFragmentView.findViewById(R.id.myListView);
        mEventAdapter = new EventAdapter(getActivity(), R.id.timeStart, mEvents);
        mEventListView.setAdapter(mEventAdapter);
        return mCardFragmentView;
    }
    //Here you can restore saved data in onSaveInstanceState Bundle
//    private void onRestoreInstanceState(Bundle savedInstanceState){
//        if(savedInstanceState!=null){
//            mEvents=savedInstanceState.getStringArrayList("Event Array");
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("Event Array" , (ArrayList<String>) mEvents);
    }

    public void updateEventData(List<String> eventDataList) {
        mEventAdapter.clear();
        //mEventAdapter.addAll(eventDataList);
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
            Handler handler= new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(mEvents==null){
                        FileInputStream input = null; // Open input stream
                        try {
                            input = getActivity().openFileInput("lines.txt");
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
                        for (int i=0;i<sz;i++) { // Read lines
                            String line = null;
                            try {
                                line = din.readUTF();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mEvents.add(line);
                        }
                        try {
                            din.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });


            String [] div=mEvents.get(position).split("/");

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
                dateEndFix+= " - "+Character.toString(eventTimeEnd.charAt(5))+Character.toString(eventTimeEnd.charAt(6))+Character.toString(eventTimeEnd.charAt(7))+Character.toString(eventTimeEnd.charAt(8))+Character.toString(eventTimeEnd.charAt(9))+Character.toString(eventTimeEnd.charAt(4))+Character.toString(eventTimeEnd.charAt(0))+Character.toString(eventTimeEnd.charAt(1))+Character.toString(eventTimeEnd.charAt(2))+Character.toString(eventTimeEnd.charAt(3));
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

            Handler handler2= new Handler();
            handler2.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
                        FileOutputStream output = getActivity().openFileOutput("lines.txt", Context.MODE_WORLD_READABLE);
                        DataOutputStream dout = new DataOutputStream(output);
                        dout.writeInt(mEvents.size()); // Save line count
                        for(String line : mEvents) // Save lines
                            dout.writeUTF(line);
                        dout.flush(); // Flush stream ...
                        dout.close(); // ... and close.
                    }
                    catch (IOException exc) { exc.printStackTrace(); }

                }
            });


                return rowView;

        }
    }
}
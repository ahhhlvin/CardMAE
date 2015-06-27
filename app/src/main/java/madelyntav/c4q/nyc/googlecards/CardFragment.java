package madelyntav.c4q.nyc.googlecards;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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
        setRetainInstance(true);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        // Inflate the layout for this fragment
        mCardFragmentView = inflater.inflate(R.layout.fragment_card, container, false);
        mEventListView = (ListView) mCardFragmentView.findViewById(R.id.myListView);
        mEventAdapter = new EventAdapter(getActivity(), R.id.times, mEvents);
        mEventListView.setAdapter(mEventAdapter);
        return mCardFragmentView;
    }

    public void updateEventData(List<String> eventDataList) {
        mEventAdapter.clear();
        mEventAdapter.addAll(eventDataList);
        mEventAdapter.notifyDataSetChanged();
    }

    private class EventAdapter extends ArrayAdapter {
        public EventAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            String event = (String) getItem(position);
            View rowView = getActivity().getLayoutInflater().inflate(R.layout.row, null);
            TextView title = (TextView) rowView.findViewById(R.id.title);
            title.setText(event);
            TextView location = (TextView) rowView.findViewById(R.id.location);
            location.setText("LOCATION B****!!!!");
            TextView times = (TextView) rowView.findViewById(R.id.times);
            times.setText("TIME B****!!!!");
            return rowView;
        }
    }
}
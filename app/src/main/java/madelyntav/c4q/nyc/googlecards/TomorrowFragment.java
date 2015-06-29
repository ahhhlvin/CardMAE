package madelyntav.c4q.nyc.googlecards;


import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TomorrowFragment extends android.support.v4.app.Fragment {

    private ListView mListView;
    View rowView;
    private List<String> mNewsList;
    String Title;
    String Location;
    String Times;
    TextView location;
    Calendar calendar;
    public ArrayList<String> listContent;
    private Cursor mCursor = null;
    private static final String[] COLS = new String[]
            { CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART};
    Button viewFull;
    ArrayList<String> transfer;
    public TomorrowFragment() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            // Inflate the layout for this fragment
            final View fragmentView= inflater.inflate(R.layout.fragment_tomorrow, container, false);
            rowView=getActivity().getLayoutInflater().inflate(R.layout.row, null);
            //viewFull=(Button) rowView.findViewById(R.id.button);
            location=(TextView) rowView.findViewById(R.id.location);
            TextView tv = (TextView) rowView.findViewById(R.id.title);


            mNewsList=new ArrayList<>();
            mNewsList.add("Beach");
            mNewsList.add("Work");
            mNewsList.add("C4Q");
            mListView=(ListView) fragmentView.findViewById(R.id.myListView);
            mListView.setAdapter(new MyAdapter());



            return fragmentView;
        }





        private class MyAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                return mNewsList.size();
            }

            @Override
            public Object getItem(int position) {
                return mNewsList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }



            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                rowView=getActivity().getLayoutInflater().inflate(R.layout.row,null);
                TextView title=(TextView)rowView.findViewById(R.id.title);
                TextView location=(TextView) rowView.findViewById(R.id.location);
                TextView times=(TextView) rowView.findViewById(R.id.timeStart);

                title.setText(mNewsList.get(position));
                //location.setText(transfer.get(position));


                return rowView;
            }
        }

    }

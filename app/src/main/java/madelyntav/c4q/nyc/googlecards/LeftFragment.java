package madelyntav.c4q.nyc.googlecards;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.*;

/**
 * Created by alvin2 on 7/21/15.
 */
public class LeftFragment extends Fragment {

    View rootView;
    Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // this is the default screen that opens when the app is opened up
        rootView = inflater.inflate(R.layout.fragment_left, container, false);
        String[] listItems = {"search", "to-do list", "weather", "flickr", "maps", "calendar"};
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listItems));
        listView.setOnItemClickListener(new DrawerItemClickListener());


        return rootView;
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    private void selectItem(int position) {
        // update the main content by replacing fragments

        if (position == 0) {
            fragment = new searchFragment();
        } else if (position == 1) {
            fragment = new toDoFragment();
        } else if (position == 2) {
            fragment = new weatherFragment();
        } else if (position == 3) {
            fragment = new flickrFragment();
        } else if (position == 4) {
            fragment = new MapFragment();
        } else if (position == 5) {
            fragment = new calendarFragment();
        }

        // Create fragment manager to begin interacting with the fragments and the container
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
}

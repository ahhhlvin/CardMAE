package madelyntav.c4q.nyc.googlecards;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventToCal extends android.support.v4.app.Fragment {


    public AddEventToCal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView= inflater.inflate(R.layout.fragment_add_event_to_cal, container, false);



        return thisView;
    }


}

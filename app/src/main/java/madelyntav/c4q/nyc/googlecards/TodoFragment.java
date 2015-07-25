package madelyntav.c4q.nyc.googlecards;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {

    ImageButton listButton;
    private ArrayList<String> list;
    private ListView listView;
    ArrayAdapter<String> listAdapter;
    private EditText listEnter;


    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_todo, container, false);

        // TO-DO LIST
        listEnter = (EditText) view.findViewById(R.id.enterList);
        listButton = (ImageButton) view.findViewById(R.id.listButton);


        listView = (ListView) view.findViewById(R.id.listView);
        list = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_checked, list);

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

        //Makes to-do list scrollable from main scrollview
        listView.setOnTouchListener(new ListView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (list.size() > 3) {
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
                }
                return true;

            }

        });
        return view;
    }


}

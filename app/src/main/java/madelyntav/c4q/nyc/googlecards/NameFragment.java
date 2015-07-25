package madelyntav.c4q.nyc.googlecards;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NameFragment extends Fragment {

    private ImageButton mBtnFind, nameButton, largeNameButton, listButton, addCardButton;
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
    ImageAdapter adapter;
    View view;

    public NameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_name, container, false);

        // NAME CARD
        nameText = (EditText) view.findViewById(R.id.nameText);
        nameView = (TextView) view.findViewById(R.id.nameView);
        if (!name.equals("")) {
            nameLayout.setVisibility(View.VISIBLE);
            nameView.setText(name);
            enterNameLayout.setVisibility(View.GONE);
        }

        nameButton = (ImageButton) view.findViewById(R.id.nameButton);
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameText.getText().toString();
                enterNameLayout.setVisibility(View.GONE);
                nameLayout.setVisibility(View.VISIBLE);
                nameView.setText("Hello, " + name + "!");
            }
        });

        largeNameButton = (ImageButton) view.findViewById(R.id.largeNameButton);
        largeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameLayout.setVisibility(View.GONE);
                enterNameLayout.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }


}

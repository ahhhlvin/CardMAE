package madelyntav.c4q.nyc.googlecards;


import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBarFragment extends Fragment {

    private EditText searchBar, nameText;
    View view;

    public SearchBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_search_bar, container, false);


        // GOOOGLE SEARCH BAR
        searchBar = (EditText) view.findViewById(R.id.searchText);
        ImageButton searchButton = (ImageButton) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchClick(view);
            }
        });


        return view;
    }


    // GOOGLE SEARCH BAR CODE
    public void onSearchClick(View v) {
        try {
            Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
            String searchTerm = searchBar.getText().toString();
            searchIntent.putExtra(SearchManager.QUERY, searchTerm);
            startActivity(searchIntent);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Search Error, please try again", Toast.LENGTH_SHORT).show();
        }
    }


}

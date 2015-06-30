package madelyntav.c4q.nyc.googlecards;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventToCal extends android.support.v4.app.Fragment {

    Button submit;
    EditText newTitle;
    EditText newLocation;
    EditText description;
    String getTitle;
    String getLocation;
    String getDescription;

    public AddEventToCal() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView= inflater.inflate(R.layout.fragment_add_event_to_cal, container, false);

        Button submit=(Button)thisView.findViewById(R.id.submit);
        newTitle=(EditText) thisView.findViewById(R.id.newTitle);
        newLocation=(EditText) thisView.findViewById(R.id.newLocation);
        description=(EditText) thisView.findViewById(R.id.newDescription);




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(newTitle.getText()!=null){
                    getTitle=newTitle.getText().toString();
                } else {
                    getTitle="";
                }




                if (newLocation.getText()!=null){
                    getLocation=newLocation.getText().toString();
                }
                else {
                    getLocation = "";
                }


                if(description.getText()!=null){
                    getDescription=description.getText().toString();
                } else{
                    getDescription="";
                }

                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, getTitle);
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION,getLocation);
                calIntent.putExtra(CalendarContract.Events.DESCRIPTION,getDescription);
                startActivity(calIntent);
            }
        });

        return thisView;
    }
}

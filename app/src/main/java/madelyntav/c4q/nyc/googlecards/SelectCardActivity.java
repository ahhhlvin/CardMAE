package madelyntav.c4q.nyc.googlecards;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SelectCardActivity extends ActionBarActivity {

    ArrayList<String> selectedItems = new ArrayList<String>();
    Button updateButton;
    ListView checkableList;
    String selectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card);


        checkableList = (ListView) findViewById(R.id.checkableList);





            updateButton = (Button) findViewById(R.id.updateButton);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                    for (int i = 0; i < selectedItems.size(); i++) {
//                        String item = selectedItems.get(i);
//
//                        if (item.equals("Flickr")) {
//                            MainActivity.flickrChecked = true;
//                        } else if (item.equals("Weather")) {
//                            MainActivity.weatherChecked = true;
//                        } else if (item.equals("Map")) {
//                            MainActivity.mapChecked = true;
//                        } else if (item.equals("Calendar")) {
//                            MainActivity.calendarChecked = true;
//                        } else if (item.equals("To-do List")) {
//                            MainActivity.todoChecked = true;
//                        }
//                    }
//
//
//                    Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(toMain);
//                }
//            });
//
//        }
//
//
//    public void onResume() {
//        super.onResume();
//
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                if (MainActivity.flickrChecked) {
//                    checkableList.performItemClick(
//                            checkableList.getChildAt(0),
//                            0,
//                            checkableList.getAdapter().getItemId(0));
//                    MainActivity.flickrChecked = false;
//                } else {
//                }
//
//
//                if (MainActivity.weatherChecked) {
//                    checkableList.performItemClick(
//                            checkableList.getChildAt(1),
//                            1,
//                            checkableList.getAdapter().getItemId(1));
//                    MainActivity.weatherChecked = false;
//                } else {
//                }
//
//
//                if (MainActivity.mapChecked) {
//                    checkableList.performItemClick(
//                            checkableList.getChildAt(2),
//                            2,
//                            checkableList.getAdapter().getItemId(2));
//                    MainActivity.mapChecked = false;
//                } else {
//                }
//
//
//                if (MainActivity.calendarChecked) {
//                    checkableList.performItemClick(
//                            checkableList.getChildAt(3),
//                            3,
//                            checkableList.getAdapter().getItemId(3));
//                } else {
//                    checkableList.performItemClick(
//                            checkableList.getChildAt(3),
//                            3,
//                            checkableList.getAdapter().getItemId(3));
//                }
//
//                    if (MainActivity.todoChecked) {
//                        checkableList.performItemClick(
//                                checkableList.getChildAt(4),
//                                4,
//                                checkableList.getAdapter().getItemId(4));
//                        MainActivity.todoChecked = false;
//                    } else {
//                    }

                }

        });

        }

        // SELECT CARD CODE

    public void onStart() {


        super.onStart();
        //create an instance of ListView
        //set multiple selection mode
        checkableList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        String[] items ={"Flickr", "Weather", "Map", "Calendar", "To-do List"};
        //supply data itmes to ListView
        ArrayAdapter<String> aa=new ArrayAdapter<String>(getApplicationContext(),R.layout.checkable_list_layout,R.id.txt_title,items);
        checkableList.setAdapter(aa);
        //set OnItemClickListener
        checkableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem))
                    selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                else {
                selectedItems.add(selectedItem);}



            }

        });
    }

    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish()
    }




}


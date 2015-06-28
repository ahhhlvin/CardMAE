package madelyntav.c4q.nyc.googlecards;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SelectCardActivity extends ActionBarActivity {

    ArrayList<String> selectedItems = new ArrayList<String>();
    Button updateButton;
    private ListView checkableList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card);


            if (MainActivity.flickrChecked) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        checkableList.performItemClick(
                                checkableList.getChildAt(0),
                                0,
                                checkableList.getAdapter().getItemId(0));
                    }
                });
                MainActivity.flickrChecked = false;
            } else {
            }
//        if (MainActivity.weatherChecked) {

//        } else {

//        }
//        if (MainActivity.stocksChecked) {

//        } else {

//        }
            if (MainActivity.mapChecked) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        checkableList.performItemClick(
                                checkableList.getChildAt(3),
                                3,
                                checkableList.getAdapter().getItemId(3));
                    }
                });
                MainActivity.mapChecked = false;
            } else {
            }
//        if (MainActivity.calendarChecked) {

//        } else {

//        }
            if (MainActivity.todoChecked) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        checkableList.performItemClick(
                                checkableList.getChildAt(5),
                                5,
                                checkableList.getAdapter().getItemId(5));
                    }
                });
                MainActivity.todoChecked = false;
            } else {
            }


        updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                for (int i = 0; i < selectedItems.size(); i++) {
                    String item = selectedItems.get(i);


                    if (item.equals("Flickr")) {
                        MainActivity.flickrChecked = true;
                    } else if (item.equals("Weather")) {
                        MainActivity.weatherChecked = true;
                    } else if (item.equals("Stocks")) {
                        MainActivity.stocksChecked = true;
                    } else if (item.equals("Map")) {
                        MainActivity.mapChecked = true;
                    } else if (item.equals("Calendar")) {
                        MainActivity.calendarChecked = true;
                    } else if (item.equals("To-do List")) {
                        MainActivity.todoChecked = true;
                    }
                }

                Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(toMain);
            }
        });

    }

    // SELECT CARD CODE
    public void onStart(){


        super.onStart();
        //create an instance of ListView
        checkableList = (ListView) findViewById(R.id.checkableList);
        //set multiple selection mode
        checkableList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        String[] items ={"Flickr", "Weather", "Stocks", "Map", "Calendar", "To-do List"};
        //supply data itmes to ListView
        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,R.layout.checkable_list_layout,R.id.txt_title,items);
        checkableList.setAdapter(aa);
        //set OnItemClickListener
        checkableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem))
                    selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                else
                    selectedItems.add(selectedItem); //add selected item to the list of selected items


            }

        });
    }

    public void showSelectedItems(View view){
        String selItems="";
        for(String item: selectedItems){
            if(selItems=="")
                selItems=item;
            else
                selItems+="/"+item;
        }
        Toast.makeText(this, selItems, Toast.LENGTH_LONG).show();
    }


}
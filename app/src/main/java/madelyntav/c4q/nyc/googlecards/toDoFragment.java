package madelyntav.c4q.nyc.googlecards;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by alvin2 on 7/23/15.
 */
public class toDoFragment extends android.app.Fragment {

    EditText listEnter;
    ImageButton listButton;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter listAdapter;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_todo, container, false);


        listEnter = (EditText) view.findViewById(R.id.enterList);
        listButton = (ImageButton) view.findViewById(R.id.listButton);


        listView = (ListView) view.findViewById(R.id.listView);
        list = new ArrayList<>();
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                FileInputStream input = null; // Open input stream
                try {
                    input = view.getContext().openFileInput("ToDo.txt");

                    DataInputStream din = new DataInputStream(input);
                    int sz = din.readInt(); // Read line count
                    for (int i = 0; i < sz; i++) { // Read lines
                        String line = din.readUTF();
                        if (line.equals("") || line.equals(null)) {
                        } else {
                            list.add(line);
                        }
                    }
                    din.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                listAdapter.notifyDataSetChanged();

            }

        });

        listAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_checked, list);

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!listEnter.getText().toString().equals("")) {
                    list.add(listEnter.getText().toString());
                    listAdapter.notifyDataSetChanged();
                    listView.setAdapter(listAdapter);

                } else {
                }

                listEnter.setText("");

            }
        });


        listView.setChoiceMode(listView.CHOICE_MODE_NONE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.removeViewInLayout(view);
                list.remove(position);
                listAdapter.notifyDataSetChanged();
            }
        });
        return view;

    }

}

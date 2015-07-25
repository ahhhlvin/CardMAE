package madelyntav.c4q.nyc.googlecards;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends FragmentActivity {

    protected static CardView flickrCard;
    protected static CardView weatherCard;
    protected static CardView mapCard;
    protected static CardView calendarCard;
    protected static CardView todoCard;
    protected static boolean flickrChecked = true;
    protected static boolean weatherChecked = true;
    protected static boolean stocksChecked = true;
    protected static boolean mapChecked = true;
    protected static boolean calendarChecked = true;
    protected static boolean todoChecked = true;
    TextView calendarTitle;
    LinearLayout nameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        fragmentTransaction.add(R.id.first_frag, mainActivityFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();


//        flickrCard = (CardView) findViewById(R.id.flickrCard);
//        weatherCard = (CardView) findViewById(R.id.weatherCard);
//        mapCard = (CardView) findViewById(R.id.mapCard);
//        calendarCard = (CardView) findViewById(R.id.calendarCard);
//        todoCard = (CardView) findViewById(R.id.todoCard);
//        calendarTitle = (TextView) findViewById(R.id.calendarTitle);
//        nameLayout = (LinearLayout) findViewById(R.id.nameLayout);
//
//        if (flickrChecked) {
//            flickrCard.setVisibility(View.VISIBLE);
//        } else {
//            flickrCard.setVisibility(View.GONE);
//        }
//        if (weatherChecked) {
//            weatherCard.setVisibility(View.VISIBLE);
//        } else {
//            weatherCard.setVisibility(View.GONE);
//        }
//
//        if (mapChecked) {
//            mapCard.setVisibility(View.VISIBLE);
//        } else {
//            mapCard.setVisibility(View.GONE);
//        }
//        if (calendarChecked) {
//            calendarCard.setVisibility(View.VISIBLE);
//            calendarTitle.setVisibility(View.VISIBLE);
//        } else {
//            calendarCard.setVisibility(View.GONE);
//            calendarTitle.setVisibility(View.GONE);
//
//        }
//        if (todoChecked) {
//            todoCard.setVisibility(View.VISIBLE);
//        } else {
//            todoCard.setVisibility(View.GONE);
//        }
//    }


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }
}
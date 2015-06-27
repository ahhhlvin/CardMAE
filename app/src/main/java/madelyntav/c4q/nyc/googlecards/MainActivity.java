package madelyntav.c4q.nyc.googlecards;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_quickstart);

        Intent i= new Intent(this, Calendar.class);
        startActivity(i);
    }
}
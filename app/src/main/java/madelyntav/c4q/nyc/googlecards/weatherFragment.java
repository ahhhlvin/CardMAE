package madelyntav.c4q.nyc.googlecards;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by alvin2 on 7/23/15.
 */
public class weatherFragment extends android.app.Fragment {

    View view;
    private WeatherInf mCurrentWeather;

    Button mButton;
    TextView mTemperatureLabel;
    TextView mHumidityValue;
    TextView mPrecipValue;
    TextView mSummaryLabel;
    TextView mWindValue;
    ImageView mIconImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_weather, container, false);

        mTemperatureLabel = (TextView) view.findViewById(R.id.temperatureLabel);
        mHumidityValue = (TextView) view.findViewById(R.id.humidityValue);
        mPrecipValue = (TextView) view.findViewById(R.id.precipValue);
        mSummaryLabel = (TextView) view.findViewById(R.id.summaryLabel);
        mWindValue = (TextView) view.findViewById(R.id.windValue);
        mIconImageView = (ImageView) view.findViewById(R.id.icon_image_view);

        String apiKey = "4bdd5c74892f3ce180e1c291279ce444";
        double latitude = 40.748817;
        double longitude = -73.985428;
        String forcastURL = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forcastURL).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {

                    try {
                        String jsonData = response.body().string();
                        Log.v("blah", jsonData);
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUseraboutError();
                        }
                    } catch (IOException e) {
                    }
                    catch (JSONException e) {
                    }
                }
            });
        }
        else {
            Toast.makeText(view.getContext(), getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
        }

        mButton = (Button) view.findViewById(R.id.bt_website);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://m.weather.com/weather/tenday/USGA0028");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        new ShareNote();



        return view;
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrentWeather.getTemperature() + "");
        mHumidityValue.setText(mCurrentWeather.getHumidity() + "%");
        mPrecipValue.setText(mCurrentWeather.getPrecipChance() + "%");
        mSummaryLabel.setText(mCurrentWeather.getSummary());
        mWindValue.setText(mCurrentWeather.getWind() + "mph");


        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private WeatherInf getCurrentDetails(String jsonData) throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");

        WeatherInf currentWeather = new WeatherInf();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setWind(currently.getDouble("windSpeed"));
        currentWeather.setState(timeZone);

        return currentWeather;

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUseraboutError() {
        HandlerError dialofFragment = new HandlerError();
        dialofFragment.show(getFragmentManager(), "error_dialog");
    }
}

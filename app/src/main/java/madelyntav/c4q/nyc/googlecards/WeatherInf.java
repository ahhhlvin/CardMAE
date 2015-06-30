package madelyntav.c4q.nyc.googlecards;

/**
 * Created by elvisboves on 6/29/15.
 */
public class WeatherInf {

    private String mSummary;
    private String mIcon;
    private String mState;
    private double mTemperature;
    private double mPrecipChance;
    private double mHumidity;
    private double mWind;

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getIcon() {
        return mIcon;
    }

    public int getIconId() {

        int iconId = R.mipmap.cloudy_final;

        if (mIcon.equals("clear-day")) {
            iconId = R.mipmap.clear_day;
        }else if (mIcon.equals("clear-night")) {
            iconId = R.mipmap.clear_night;
        }else if (mIcon.equals("rain")) {
            iconId = R.mipmap.rain;
        }else if (mIcon.equals("snow")) {
            iconId = R.mipmap.snow;
        }else if (mIcon.equals("sleet")) {
            iconId = R.mipmap.sleet;
        }else if (mIcon.equals("wind")) {
            iconId = R.mipmap.wind;
        }else if (mIcon.equals("fog")) {
            iconId = R.mipmap.fog;
        }else if (mIcon.equals("cloudy")) {
            iconId = R.mipmap.cloudy_final;
        }else if (mIcon.equals("partly-cloudy-day")) {
            iconId = R.mipmap.partly_cloudy;
        }else if (mIcon.equals("partly-cloudy-night")) {
            iconId = R.mipmap.cloudy_night;
        }
        return iconId;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public int getPrecipChance() {

        double precipPercentage = mPrecipChance * 100;
        return (int) Math.round(precipPercentage);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public int getHumidity() {

        double humidityValue = mHumidity * 100;
        return (int) Math.round(humidityValue);
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getWind() {
        double windValue = mWind * 100;
        return (int) Math.round(windValue);
    }

    public void setWind(double wind) {
        mWind = wind;
    }
}

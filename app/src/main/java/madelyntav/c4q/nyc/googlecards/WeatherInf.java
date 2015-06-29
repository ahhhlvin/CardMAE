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

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public double getTemperature() {
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

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public double getWind() {
        return mWind;
    }

    public void setWind(double wind) {
        mWind = wind;
    }
}

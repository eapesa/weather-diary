package android_project.voyager.com.weatherdiary.interfaces;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android_project.voyager.com.weatherdiary.helpers.Utilities;
import android_project.voyager.com.weatherdiary.models.Weather;
import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/15/15.
 */
public class WeatherApi {
    private Context mContext;

    private String contentTypeLabel = "Content-Type";
    private String contentTypeValue = "application/json";
    private String httpMethod = "GET";

    public interface WeatherApiListener {
        void onStartOfQuery();
        void onUpdateViews(Weather weather);
    }

    public WeatherApi (Context context) {
        this.mContext = context;
    }

    public void getWeatherForecast(WeatherApiListener listener, double lat, double lon) {
        GetWeatherForecastAsyncTask getWeather = new GetWeatherForecastAsyncTask
                (listener, lat, lon);
        getWeather.execute();
    }

    private Weather getData(InputStream inputStream) throws IOException, JSONException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Weather weather = new Weather();
        String line;
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        if (null != inputStream) {
            inputStream.close();
        }

        JSONObject json = new JSONObject(result);

        String city = json.getString("name");
        String country = json.getJSONObject("sys").getString("country");
        String namePlace = city + ", " + country;
        weather.nameOfPlace = namePlace;

        double mainTempKelvin = json.getJSONObject("main").getDouble("temp");
        weather.celsiusTemp = String.valueOf( Utilities.kelvinToCelsius(mainTempKelvin) )
                + Constants.CELSIUS_UNIT ;
        weather.fahrenheitTemp = String.valueOf( Utilities.kelvinToFahrenheit(mainTempKelvin) )
                + Constants.FAHRENHEIT_UNIT;

        String cloudiness = json.getJSONArray("weather").getJSONObject(0).getString("description");
        weather.cloudiness = cloudiness;

        double wind = json.getJSONObject("wind").getDouble("speed");
        weather.windSpeed = String.valueOf(wind) + "m/s";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = sdf.format(new Date());
        weather.forecastTime = "Forecast since " + currentDateTime;

        weather.iconCode = json.getJSONArray("weather").getJSONObject(0).getString("icon");

        return weather;
    }

    private class GetWeatherForecastAsyncTask extends AsyncTask<Void, Void, Weather> {
        private WeatherApiListener mListener;
        private double mLatitude;
        private double mLongitude;

        public GetWeatherForecastAsyncTask (WeatherApiListener listener, double lat, double lon) {
            this.mListener = listener;
            this.mLatitude = lat;
            this.mLongitude = lon;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mListener.onStartOfQuery();
        }

        @Override
        protected Weather doInBackground(Void... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection;
            int statusCode;

            try {
                URL url = new URL(Constants.OPEN_WEATHER_API + "?lat=" + mLatitude
                        + "&lon=" + mLongitude);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty(contentTypeLabel, contentTypeValue);
                urlConnection.setRequestMethod(httpMethod);
                statusCode = urlConnection.getResponseCode();
                inputStream = urlConnection.getInputStream();

                if (statusCode == 200) {
                    return getData(inputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            mListener.onUpdateViews(weather);
        }
    }

}

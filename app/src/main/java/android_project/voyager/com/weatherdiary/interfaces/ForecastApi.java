package android_project.voyager.com.weatherdiary.interfaces;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android_project.voyager.com.weatherdiary.helpers.Utilities;
import android_project.voyager.com.weatherdiary.models.Weather;
import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/18/15.
 */
public class ForecastApi {
    private Context mContext;

    private String contentTypeLabel = "Content-Type";
    private String contentTypeValue = "application/json";
    private String httpMethod = "GET";

    public interface ForecastApiListener {
        void onStartOfQuery();
        void onProcessResult(ArrayList<Weather> weathers);
    }

    public ForecastApi (Context context) {
        this.mContext = context;
    }

    public void getForecast(ForecastApiListener listener, double lat, double lon) {
        GetWeatherForecastAsyncTask getWeather = new GetWeatherForecastAsyncTask
                (listener, lat, lon);
        getWeather.execute();
    }

    private ArrayList<Weather> getData(InputStream inputStream) throws IOException, JSONException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        if (null != inputStream) {
            inputStream.close();
        }

        JSONObject json = new JSONObject(result);
        JSONArray forecastList = json.getJSONArray("list");

        String city = json.getJSONObject("city").getString("name");
        String country = json.getJSONObject("city").getString("country");
        String namePlace = city + ", " + country;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = sdf.format(new Date());

        ArrayList<Weather> weatherArray = new ArrayList<>();
        for (int i = 0; i < forecastList.length(); i++) {
            Weather weather = new Weather();
            weather.nameOfPlace = namePlace;
            weather.forecastTime = "Forecast since " + currentDateTime;

            JSONObject forecast = forecastList.getJSONObject(i);

            Calendar dt = Calendar.getInstance();
            dt.setTimeInMillis(forecast.getLong("dt") * 1000);

            weather.day = String.valueOf(dt.get(Calendar.DAY_OF_MONTH));
            weather.month = Constants.MONTHS[dt.get(Calendar.MONTH)];
            weather.minTemp = forecast.getJSONObject("temp").getString("min");
            weather.maxTemp = forecast.getJSONObject("temp").getString("max");
            weather.windSpeed = "Wind Speed: " + forecast.getString("speed") + "m/s";
            weather.forecastDescription = forecast.getJSONArray("weather")
                    .getJSONObject(0).getString("description");
            weather.cloudiness = "Cloudiness percentage: " + forecast.getString("clouds") + "%";

            weatherArray.add(weather);
        }

        return weatherArray;
    }

    private class GetWeatherForecastAsyncTask extends AsyncTask<Void, Void, ArrayList<Weather>> {
        private ForecastApiListener mListener;
        private double mLatitude;
        private double mLongitude;

        public GetWeatherForecastAsyncTask (ForecastApiListener listener, double lat, double lon) {
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
        protected ArrayList<Weather> doInBackground(Void... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection;
            int statusCode;

            try {
                URL url = new URL(Constants.OPEN_WEATHER_DAILY_API + "&lat=" + mLatitude
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
        protected void onPostExecute(ArrayList<Weather> weathers) {
            mListener.onProcessResult(weathers);
        }
    }
}

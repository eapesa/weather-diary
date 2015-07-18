package android_project.voyager.com.weatherdiary.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import android_project.voyager.com.weatherdiary.data.WeatherDiaryTables.MarkedPlacesWeatherData;
import android_project.voyager.com.weatherdiary.helpers.DatabaseHelper;
import android_project.voyager.com.weatherdiary.models.Weather;
import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/16/15.
 */
public class MarkedPlacesWeatherDAO {
    private static final String TAG = MarkedPlacesWeatherDAO.class.getSimpleName();
    private Context mContext;
    private DatabaseHelper mDbHelper;

    public MarkedPlacesWeatherDAO(Context context) {
        this.mContext = context;
        mDbHelper = DatabaseHelper.getInstance(mContext);
    }

    public boolean checkMarkerIfExists(String markerId) {
        String[] columns = { MarkedPlacesWeatherData.MARKER_ID };
        String[] selectionArgs = { markerId };

        Cursor cursor = mDbHelper.query(columns, selectionArgs);
        if ( cursor.getCount() < 1 ) {
            return false;
        } else {
            return true;
        }
    }

    public Weather getSpecificWeatherData(String markerId) {
        String[] columns = { MarkedPlacesWeatherData.PLACE_NAME,
                MarkedPlacesWeatherData.MONTH,
                MarkedPlacesWeatherData.DAY,
                MarkedPlacesWeatherData.MIN_TEMP,
                MarkedPlacesWeatherData.MAX_TEMP,
                MarkedPlacesWeatherData.WIND_SPEED,
                MarkedPlacesWeatherData.CLOUDINESS,
                MarkedPlacesWeatherData.FORECAST_DESCRIPTION,
                MarkedPlacesWeatherData.FORECAST_TIME };
        String[] selectionArgs = { markerId };

        Cursor cursor = mDbHelper.query(columns, selectionArgs);
        Weather weather = new Weather();
        if (cursor.getCount() < 1) {
            weather = null;
        } else {
            cursor.moveToFirst();
            weather.nameOfPlace = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesWeatherData.PLACE_NAME));
            weather.month = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesWeatherData.MONTH));
            weather.day = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesWeatherData.DAY));
            weather.minTemp = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesWeatherData.MIN_TEMP));
            weather.maxTemp = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesWeatherData.MAX_TEMP));
            weather.windSpeed = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesWeatherData.WIND_SPEED));
            weather.cloudiness = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesWeatherData.CLOUDINESS));
            weather.forecastDescription = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesWeatherData.FORECAST_DESCRIPTION));
            weather.forecastTime = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesWeatherData.FORECAST_TIME));
        }

        return weather;
    }

    public ArrayList<Weather> getDailyForecast(String markerId) {
        int dayCount = Constants.FORECAST_DAY_COUNT;

        ArrayList<Weather> weathers = new ArrayList<>();
        for (int i = 0; i < dayCount; i++) {
            Weather weather = getSpecificWeatherData(markerId + "-" + i);
            weathers.add(weather);
        }

        return weathers;
    }

    public void storeWeatherData(String markerId, Weather weather) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MarkedPlacesWeatherData.MARKER_ID, markerId);
        contentValues.put(MarkedPlacesWeatherData.PLACE_NAME, weather.nameOfPlace);
        contentValues.put(MarkedPlacesWeatherData.MONTH, weather.month);
        contentValues.put(MarkedPlacesWeatherData.DAY, weather.day);
        contentValues.put(MarkedPlacesWeatherData.MIN_TEMP, weather.minTemp);
        contentValues.put(MarkedPlacesWeatherData.MAX_TEMP, weather.maxTemp);
        contentValues.put(MarkedPlacesWeatherData.WIND_SPEED, weather.windSpeed);
        contentValues.put(MarkedPlacesWeatherData.CLOUDINESS, weather.cloudiness);
        contentValues.put(MarkedPlacesWeatherData.FORECAST_DESCRIPTION, weather.forecastDescription);
        contentValues.put(MarkedPlacesWeatherData.FORECAST_TIME, weather.forecastTime);

        mDbHelper.insert(MarkedPlacesWeatherData.TABLE_NAME, contentValues);
    }

}
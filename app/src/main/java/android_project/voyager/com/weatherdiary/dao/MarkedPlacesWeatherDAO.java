package android_project.voyager.com.weatherdiary.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import android_project.voyager.com.weatherdiary.data.WeatherDiaryTables.MarkedPlacesData;
import android_project.voyager.com.weatherdiary.data.WeatherDiaryTables.MarkedPlacesWeatherData;
import android_project.voyager.com.weatherdiary.helpers.DatabaseHelper;
import android_project.voyager.com.weatherdiary.models.MarkedPlace;
import android_project.voyager.com.weatherdiary.models.Weather;
import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/16/15.
 */
public class MarkedPlacesWeatherDAO {
    private Context mContext;
    private DatabaseHelper mDbHelper;

    public MarkedPlacesWeatherDAO(Context context) {
        this.mContext = context;
        mDbHelper = DatabaseHelper.getInstance(mContext);
    }

    public boolean checkMarkerIfExists(String markerId) {
        String[] columns = { MarkedPlacesWeatherData.MARKER_ID };
        String[] selectionArgs = { markerId };

        Cursor cursor = mDbHelper.query(MarkedPlacesWeatherData.TABLE_NAME, columns,
                MarkedPlacesWeatherData.MARKER_ID + "=?", selectionArgs);
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
                MarkedPlacesWeatherData.FORECAST_TIME,
                MarkedPlacesWeatherData.ICON_CODE };
        String[] selectionArgs = { markerId };

        Cursor cursor = mDbHelper.query(MarkedPlacesWeatherData.TABLE_NAME, columns,
                MarkedPlacesWeatherData.MARKER_ID + "=?", selectionArgs);
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
            weather.iconCode = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesWeatherData.ICON_CODE));
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
        contentValues.put(MarkedPlacesWeatherData.ICON_CODE, weather.iconCode);

        mDbHelper.insert(MarkedPlacesWeatherData.TABLE_NAME, contentValues);
    }

    public void deleteWeatherData(String markerId) {
        for (int i = 0; i < Constants.FORECAST_DAY_COUNT; i++) {
            String[] whereArgs = { markerId + "-" + i };
            mDbHelper.delete(MarkedPlacesWeatherData.TABLE_NAME, MarkedPlacesWeatherData.MARKER_ID
                    + "=?", whereArgs);
        }
    }

    public void updateWeatherData(String markerId, Weather weather) {
        String where = MarkedPlacesWeatherData.MARKER_ID + "=?";
        String[] whereArgs = { markerId };
        ContentValues contentValues = new ContentValues();
        contentValues.put(MarkedPlacesWeatherData.MONTH, weather.month);
        contentValues.put(MarkedPlacesWeatherData.DAY, weather.day);
        contentValues.put(MarkedPlacesWeatherData.MIN_TEMP, weather.minTemp);
        contentValues.put(MarkedPlacesWeatherData.MAX_TEMP, weather.maxTemp);
        contentValues.put(MarkedPlacesWeatherData.WIND_SPEED, weather.windSpeed);
        contentValues.put(MarkedPlacesWeatherData.CLOUDINESS, weather.cloudiness);
        contentValues.put(MarkedPlacesWeatherData.FORECAST_DESCRIPTION, weather.forecastDescription);
        contentValues.put(MarkedPlacesWeatherData.FORECAST_TIME, weather.forecastTime);
        contentValues.put(MarkedPlacesWeatherData.ICON_CODE, weather.iconCode);
        mDbHelper.update(MarkedPlacesWeatherData.TABLE_NAME, where, whereArgs, contentValues);
    }

    public boolean checkMarkedPlaceIfExists(String markerId) {
        String[] columns = { MarkedPlacesData.MARKER_ID };
        String[] selectionArgs = { markerId };

        Cursor cursor = mDbHelper.query(MarkedPlacesData.TABLE_NAME, columns,
                MarkedPlacesData.MARKER_ID + "=?", selectionArgs);
        if ( cursor.getCount() < 1 ) {
            return false;
        } else {
            return true;
        }
    }

    public MarkedPlace getSpecificMarkedPlace(String markerId) {
        String[] columns = { MarkedPlacesData.MARKER_ID,
                MarkedPlacesData.PLACE_NAME,
                MarkedPlacesData.LATITUDE,
                MarkedPlacesData.LONGITUDE,
                MarkedPlacesData.FORECAST_TIME };
        String[] selectionArgs = { markerId };

        Cursor cursor = mDbHelper.query(MarkedPlacesData.TABLE_NAME, columns,
                MarkedPlacesData.MARKER_ID + "=?", selectionArgs);
        MarkedPlace markedPlace = new MarkedPlace();
        if (cursor.getCount() < 1) {
            markedPlace = null;
        } else {
            cursor.moveToFirst();
            markedPlace.markerId = markerId;
            markedPlace.nameOfPlace = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesData.PLACE_NAME));
            markedPlace.forecastTime = cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesData.FORECAST_TIME));

            Double latitude = Double.parseDouble(cursor.getString(cursor
                            .getColumnIndex(MarkedPlacesData.LATITUDE)));
            Double longitude = Double.parseDouble(cursor.getString(cursor
                    .getColumnIndex(MarkedPlacesData.LONGITUDE)));
            markedPlace.mapCoordinates = new LatLng(latitude, longitude);
        }

        return markedPlace;
    }

    public ArrayList<MarkedPlace> getAllMarkedPlaces() {
        ArrayList<MarkedPlace> markedPlaces = new ArrayList<>();

        String[] columns = { MarkedPlacesData.MARKER_ID,
                MarkedPlacesData.PLACE_NAME,
                MarkedPlacesData.LATITUDE,
                MarkedPlacesData.LONGITUDE,
                MarkedPlacesData.FORECAST_TIME };

        Cursor cursor = mDbHelper.query(MarkedPlacesData.TABLE_NAME, columns, null, null);
        int markerId = cursor.getColumnIndex(MarkedPlacesData.MARKER_ID);
        int placeName = cursor.getColumnIndex(MarkedPlacesData.PLACE_NAME);
        int forecastTime = cursor.getColumnIndex(MarkedPlacesData.FORECAST_TIME);
        int latitude = cursor.getColumnIndex(MarkedPlacesData.LATITUDE);
        int longitude = cursor.getColumnIndex(MarkedPlacesData.LONGITUDE);

        while(cursor.moveToNext()) {
            MarkedPlace markedPlace = new MarkedPlace();
            markedPlace.markerId = cursor.getString(markerId);
            markedPlace.nameOfPlace = cursor.getString(placeName);
            markedPlace.forecastTime = cursor.getString(forecastTime);

            double latDouble = cursor.getDouble(latitude);
            double longDouble = cursor.getDouble(longitude);
            markedPlace.mapCoordinates = new LatLng(latDouble, longDouble);

            markedPlaces.add(markedPlace);
        }

        return markedPlaces;
    }

    public void storeMarkedPlace(MarkedPlace markedPlace) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MarkedPlacesData.MARKER_ID, markedPlace.markerId);
        contentValues.put(MarkedPlacesData.PLACE_NAME, markedPlace.nameOfPlace);
        contentValues.put(MarkedPlacesData.FORECAST_TIME, markedPlace.forecastTime);

        String latitude = String.valueOf(markedPlace.mapCoordinates.latitude);
        String longitude = String.valueOf(markedPlace.mapCoordinates.longitude);
        contentValues.put(MarkedPlacesData.LATITUDE, latitude);
        contentValues.put(MarkedPlacesData.LONGITUDE, longitude);

        mDbHelper.insert(MarkedPlacesData.TABLE_NAME, contentValues);
    }

    public void deleteMarkedPlace(String markerId) {
        String[] whereArgs = { markerId };
        mDbHelper.delete(MarkedPlacesData.TABLE_NAME, MarkedPlacesData.MARKER_ID
                + "=?", whereArgs);
    }

    public void updateMarkedPlace(String markerId, MarkedPlace markedPlace) {
        String where = MarkedPlacesData.MARKER_ID + "=?";
        String[] whereArgs = { markerId };
        ContentValues contentValues = new ContentValues();
        contentValues.put(MarkedPlacesData.FORECAST_TIME, markedPlace.forecastTime);
        mDbHelper.update(MarkedPlacesData.TABLE_NAME, where, whereArgs, contentValues);
    }
}

package android_project.voyager.com.weatherdiary.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android_project.voyager.com.weatherdiary.data.WeatherDiaryTables.MarkedPlacesData;
import android_project.voyager.com.weatherdiary.data.WeatherDiaryTables.MarkedPlacesWeatherData;
import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/16/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;
    private SQLiteDatabase db;

    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MarkedPlacesWeatherData.CREATE_TABLE_QUERY);
        db.execSQL(MarkedPlacesData.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MarkedPlacesWeatherData.DROP_TABLE_QUERY);
        db.execSQL(MarkedPlacesData.DROP_TABLE_QUERY);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
        return db.query(table, columns, selection, selectionArgs, null, null, null);
    }

    public void insert(String table, ContentValues contentValues) {
        db.insert(table, null, contentValues);
    }

    public void delete(String table, String where, String[] whereArgs) {
        db.delete(table, where, whereArgs);
    }

    public void update(String table, String where, String[] whereArgs, ContentValues contentValues) {
        db.update(table, contentValues, where, whereArgs);
    }
}

package android_project.voyager.com.weatherdiary.utils;

/**
 * Created by eapesa on 7/13/15.
 */
public class Constants {
    public static final String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5/weather";
    public static final int FORECAST_DAY_COUNT = 7;
    public static final String OPEN_WEATHER_DAILY_API = "http://api.openweathermap.org/data/2.5/forecast/daily?cnt="
            + String.valueOf(FORECAST_DAY_COUNT);

    public static final String CELSIUS_UNIT = "°C";
    public static final String FAHRENHEIT_UNIT = "°F";

    public static final String DEFAULT_LAT_VALUE = "14.583879";
    public static final String DEFAULT_LONG_VALUE = "121.056928";
    public static final String DEFAULT_PLACENAME_VALUE = "Megamall, PH";
    public static final int DEFAULT_ZOOM_VALUE = 10;
    public static final String DEFAULT_WEATHER_VALUES = "N/A";

    public static final String SHARED_PREFS_TAG = "weather_diary_shared_prefs";
    public static final String DB_NAME = "weather_diary_sql";
    public static final int DB_VERSION = 1;
    public static final String ARGS_LATITUDE = "current_latitude";
    public static final String ARGS_LONGITUDE = "current_longitude";
    public static final String ARGS_PLACENAME = "name_of_place";
    public static final String ARGS_CELSIUS = "celsius";
    public static final String ARGS_FAHRENHEIT = "fahrenheit";
    public static final String ARGS_CLOUDINESS = "cloudiness";
    public static final String ARGS_WINDSPEED = "windspeed";
    public static final String ARGS_FORECAST_TIME = "time_of_forecast";
    public static final String ARGS_ICON = "time_of_forecast";
    public static final String ARGS_MARKER = "map_marker";

    public static final String[] MONTHS = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December"};
}

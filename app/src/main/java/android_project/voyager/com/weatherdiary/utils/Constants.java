package android_project.voyager.com.weatherdiary.utils;

/**
 * Created by eapesa on 7/13/15.
 */
public class Constants {
    public static final String WEATHER_API = "http://api.openweathermap.org/data/2.5/weather";

    public static final String CELSIUS_UNIT = "°C";
    public static final String FAHRENHEIT_UNIT = "°F";

    public static final String CURRENT_LAT_LABEL = "current_lat";
    public static final String CURRENT_LONG_LABEL = "current_long";
    public static final String CURRENT_PLACENAME_LABEL = "place_name";

    public static final String DEFAULT_LAT_VALUE = "14.583879";
    public static final String DEFAULT_LONG_VALUE = "121.056928";
    public static final String DEFAULT_PLACENAME_VALUE = "Megamall, PH";
    public static final int DEFAULT_ZOOM_VALUE = 12;

    public static final String ALERT_FORECAST_TITLE = "Get Forecast";
    public static final String ALERT_FORECAST_MESSAGE = "Get forecast of the place?";
    public static final String ALERT_FORECAST_AFFIRMATIVE_BUTTON_LABEL = "Ok";
    public static final String ALERT_FORECAST_NEGATIVE_BUTTON_LABEL = "Cancel";

    public static final String DEFAULT_WEATHER_VALUES = "N/A";

    public static final String SHARED_PREFS_TAG = "weather_diary_shared_prefs";
    public static final String ARGS_LATITUDE = "current_latitude";
    public static final String ARGS_LONGITUDE = "current_longitude";
    public static final String ARGS_PLACENAME = "name_of_place";
    public static final String ARGS_CELSIUS = "celsius";
    public static final String ARGS_FAHRENHEIT = "fahrenheit";
    public static final String ARGS_CLOUDINESS = "cloudiness";
    public static final String ARGS_WINDSPEED = "windspeed";
    public static final String ARGS_FORECAST_TIME = "time_of_forecast";
}

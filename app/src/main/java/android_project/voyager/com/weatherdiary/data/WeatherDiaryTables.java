package android_project.voyager.com.weatherdiary.data;

/**
 * Created by eapesa on 7/16/15.
 */
public class WeatherDiaryTables {
    public class MarkedPlacesData {
        public static final String TABLE_NAME = "marked_places";
        public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String MARKER_ID = "marker_id";
        public static final String PLACE_NAME = "name_of_place";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String FORECAST_TIME = "forecast_time";

        public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME
                + "("
                + MARKER_ID + " TEXT, "
                + PLACE_NAME + " TEXT, "
                + LATITUDE + " TEXT, "
                + LONGITUDE + " TEXT, "
                + FORECAST_TIME + " TEXT "
                + ")";
    }

    public class MarkedPlacesWeatherData {
        public static final String TABLE_NAME = "marked_places_weather_data";
        public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String MARKER_ID = "marker_id";
        public static final String PLACE_NAME = "name_of_place";
        public static final String MONTH = "month";
        public static final String DAY = "day";
        public static final String MIN_TEMP = "min_temp";
        public static final String MAX_TEMP = "max_temp";
        public static final String WIND_SPEED = "wind_speed";
        public static final String CLOUDINESS = "cloudiness";
        public static final String FORECAST_DESCRIPTION = "forecast_description";
        public static final String FORECAST_TIME = "forecast_time";

        public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME
                + "("
                + MARKER_ID + " TEXT, "
                + PLACE_NAME + " TEXT, "
                + MONTH + " TEXT, "
                + DAY + " TEXT, "
                + MIN_TEMP + " TEXT, "
                + MAX_TEMP + " TEXT, "
                + WIND_SPEED + " TEXT, "
                + CLOUDINESS + " TEXT, "
                + FORECAST_DESCRIPTION + " TEXT, "
                + FORECAST_TIME + " TEXT "
                + ")";
    }
}

package android_project.voyager.com.weatherdiary.fragments;

//import android.app.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.activities.MainActivity;
import android_project.voyager.com.weatherdiary.interfaces.WeatherApi;
import android_project.voyager.com.weatherdiary.models.Weather;
import android_project.voyager.com.weatherdiary.utils.Constants;
import android_project.voyager.com.weatherdiary.utils.Toasts;

import static android_project.voyager.com.weatherdiary.interfaces.WeatherApi.WeatherApiListener;

/**
 * Created by eapesa on 7/13/15.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, LocationListener,
        WeatherApiListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private Button mButtonUpdate;
    private TextView mTextViewPlaceName;
    private TextView mTextViewCelsius;
    private TextView mTextViewFahrenheit;
    private TextView mTextViewCloudiness;
    private TextView mTextViewWind;
    private TextView mTextViewTime;

    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mSharedPrefsEditor;
    private String placeName;
    private String celsius;
    private String fahrenheit;
    private String cloudiness;
    private String windSpeed;
    private String forecastTime;

    private Context mContext;
    private Criteria mCriteria;
    private String mProvider;
    private ProgressDialog mProgressDialogUpdate;
    private LocationManager mLocationManager;

    private WeatherApi weatherApi;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weatherdiary_home_fragment, container, false);

        initializeHelpers();
        loadTexts();
        initializeViews(rootView);

        return rootView;
    }

    /*
     * Initializers
     */
    private void loadTexts () {
        placeName = mSharedPrefs.getString
                (Constants.ARGS_PLACENAME, Constants.DEFAULT_WEATHER_VALUES);
        celsius = mSharedPrefs.getString
                (Constants.ARGS_CELSIUS, Constants.DEFAULT_WEATHER_VALUES);
        fahrenheit = mSharedPrefs.getString
                (Constants.ARGS_FAHRENHEIT, Constants.DEFAULT_WEATHER_VALUES);
        cloudiness = mSharedPrefs.getString
                (Constants.ARGS_CLOUDINESS, Constants.DEFAULT_WEATHER_VALUES);
        windSpeed = mSharedPrefs.getString
                (Constants.ARGS_WINDSPEED, Constants.DEFAULT_WEATHER_VALUES);
        forecastTime = mSharedPrefs.getString
                (Constants.ARGS_FORECAST_TIME, "Forecast since " + Constants.DEFAULT_WEATHER_VALUES);
    }

    private void initializeViews(View view) {
        mTextViewPlaceName = (TextView) view.findViewById
                (R.id.weatherdiary_home_placename_textview);
        mTextViewCelsius = (TextView) view.findViewById
                (R.id.weatherdiary_home_celsius_textview);
        mTextViewFahrenheit = (TextView) view.findViewById
                (R.id.weatherdiary_home_fahrenheit_textview);
        mTextViewCloudiness = (TextView) view.findViewById
                (R.id.weatherdiary_home_cloudiness_textview);
        mTextViewWind = (TextView) view.findViewById
                (R.id.weatherdiary_home_wind_textview);
        mTextViewTime = (TextView) view.findViewById
                (R.id.weatherdiary_home_forecasttime_textview);

        mTextViewPlaceName.setText(placeName);
        mTextViewCelsius.setText(celsius);
        mTextViewFahrenheit.setText(fahrenheit);
        mTextViewCloudiness.setText(cloudiness);
        mTextViewWind.setText(windSpeed);
        mTextViewTime.setText(forecastTime);

        mButtonUpdate = (Button) view.findViewById
                (R.id.weatherdiary_home_updateforecast_button);
        mButtonUpdate.setOnClickListener(this);

        mProgressDialogUpdate = new ProgressDialog(getActivity());
        mProgressDialogUpdate.setMessage(getString
                (R.string.weatherdiary_progdialog_forecast_update_text));
    }

    private void initializeHelpers() {
        weatherApi = new WeatherApi(mContext);
        mSharedPrefs = getActivity().getSharedPreferences
                (Constants.SHARED_PREFS_TAG, Context.MODE_PRIVATE);
        mSharedPrefsEditor = mSharedPrefs.edit();
        mCriteria = new Criteria();
        mLocationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        mProvider = mLocationManager.getBestProvider(mCriteria, false);

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocationManager.requestSingleUpdate(mCriteria, this, Looper.getMainLooper());
        } else {
            Toast.makeText(getActivity(), Toasts.NO_GPS, Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Button Click Listener Methods
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weatherdiary_home_updateforecast_button:
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Location currentLoc = mLocationManager.getLastKnownLocation(mProvider);

                    mSharedPrefsEditor.putString(Constants.ARGS_LATITUDE,
                            String.valueOf(currentLoc.getLatitude()));
                    mSharedPrefsEditor.putString(Constants.ARGS_LONGITUDE,
                            String.valueOf(currentLoc.getLongitude()));
                    mSharedPrefsEditor.commit();

                    String lat = mSharedPrefs.getString(Constants.ARGS_LATITUDE,
                            Constants.DEFAULT_LAT_VALUE);
                    String lon = mSharedPrefs.getString(Constants.ARGS_LONGITUDE,
                            Constants.DEFAULT_LAT_VALUE);

                    weatherApi.getWeatherForecast(HomeFragment.this, currentLoc.getLatitude(),
                            currentLoc.getLongitude());
                } else {
                    Toast.makeText(getActivity(), Toasts.NO_GPS, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /*
     * Location Listener Methods
     */
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /*
     * Weather API Listener Methods
     */
    @Override
    public void onStartOfQuery() {
        mProgressDialogUpdate.show();
    }

    @Override
    public void onUpdateViews(Weather weather) {
        mProgressDialogUpdate.dismiss();

        mSharedPrefsEditor.putString(Constants.ARGS_PLACENAME, weather.nameOfPlace);
        mSharedPrefsEditor.putString(Constants.ARGS_CELSIUS, weather.celsiusTemp);
        mSharedPrefsEditor.putString(Constants.ARGS_FAHRENHEIT, weather.fahrenheitTemp);
        mSharedPrefsEditor.putString(Constants.ARGS_CLOUDINESS, weather.cloudiness);
        mSharedPrefsEditor.putString(Constants.ARGS_WINDSPEED, weather.windSpeed);
        mSharedPrefsEditor.putString(Constants.ARGS_FORECAST_TIME, weather.forecastTime);
        mSharedPrefsEditor.commit();

        mTextViewPlaceName.setText(weather.nameOfPlace);
        mTextViewCelsius.setText(weather.celsiusTemp);
        mTextViewFahrenheit.setText(weather.fahrenheitTemp);
        mTextViewCloudiness.setText(weather.cloudiness);
        mTextViewWind.setText(weather.windSpeed);
        mTextViewTime.setText(weather.forecastTime);
    }
}

package android_project.voyager.com.weatherdiary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import java.util.Set;

import android_project.voyager.com.weatherdiary.fragments.ForecastDiaryFragment;
import android_project.voyager.com.weatherdiary.fragments.GetForecastFragment;
import android_project.voyager.com.weatherdiary.fragments.HomeFragment;
import android_project.voyager.com.weatherdiary.interfaces.WeatherApi;
import android_project.voyager.com.weatherdiary.models.WeatherForecast;
import android_project.voyager.com.weatherdiary.utils.Constants;
import android_project.voyager.com.weatherdiary.utils.Labels;
import android_project.voyager.com.weatherdiary.utils.Toasts;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        LocationListener, WeatherApi.WeatherApiListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ProgressDialog mForecastUpdate;
    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mSharedPrefsEditor;
    private WeatherApi mWeatherApi;
    private Criteria mCriteria;
    private LocationManager mLocationManager;
    private Location mCurrentLocation;
    private WeatherForecast mWeatherForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeHelpers();
        initializeForecast();
        initializeViews();
    }

    /*
     * Initializers
     */
    private void initializeHelpers() {
        mForecastUpdate = new ProgressDialog(this);
        mForecastUpdate.setMessage(Labels.HOME_UPDATE_FORECAST);

        mSharedPrefs = getSharedPreferences(Constants.SHARED_PREFS_TAG, Context.MODE_PRIVATE);
        mSharedPrefsEditor = mSharedPrefs.edit();
        mWeatherApi = new WeatherApi(this);
    }

    private void initializeForecast() {
        mCriteria = new Criteria();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ( mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ){
            mLocationManager.requestSingleUpdate(mCriteria, this, Looper.getMainLooper());
            getWeatherForecast();
        } else {
            Toast.makeText(this, Toasts.NO_GPS, Toast.LENGTH_SHORT).show();
        }
    }

    private void getWeatherForecast() {
        // Add checker here that if something is stored, load it
        // Also get the current time for displaying that the forecast obtained was since...
        String provider = mLocationManager.getBestProvider(mCriteria, false);
        mCurrentLocation = mLocationManager.getLastKnownLocation(provider);
        mWeatherApi.getWeatherForecast(this, mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude());
    }

    private void initializeViews() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    /*
     * Navigation Drawer Methods
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, GetForecastFragment.newInstance())
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ForecastDiaryFragment.newInstance())
                        .commit();
                break;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance())
                        .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
     * Weather API Listener
     */
    @Override
    public void onStartOfQuery() {
        mForecastUpdate.show();
    }

    @Override
    public void onUpdateViews(WeatherForecast weather) {
        mForecastUpdate.hide();
        mWeatherForecast = weather;
        mWeatherForecast.mapCoordinates = mCurrentLocation;

        mSharedPrefsEditor.putString(Constants.ARGS_LATITUDE,
                String.valueOf(mCurrentLocation.getLatitude()));
        mSharedPrefsEditor.commit();
        mSharedPrefsEditor.putString(Constants.ARGS_LONGITUDE,
                String.valueOf(mCurrentLocation.getLongitude()));
        mSharedPrefsEditor.putString(Constants.ARGS_PLACENAME, weather.nameOfPlace);
        mSharedPrefsEditor.putString(Constants.ARGS_CELSIUS, weather.celsiusTemp);
        mSharedPrefsEditor.putString(Constants.ARGS_FAHRENHEIT, weather.fahrenheitTemp);
        mSharedPrefsEditor.putString(Constants.ARGS_CLOUDINESS, weather.cloudiness);
        mSharedPrefsEditor.putString(Constants.ARGS_WINDSPEED, weather.windSpeed);
        mSharedPrefsEditor.putString(Constants.ARGS_FORECAST_TIME, weather.forecastTime);
        mSharedPrefsEditor.commit();
    }
}

package android_project.voyager.com.weatherdiary.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.activities.NewForecastActivity;
import android_project.voyager.com.weatherdiary.interfaces.WeatherApi;
import android_project.voyager.com.weatherdiary.models.CurrentWeather;
import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/13/15.
 */
public class MarkPlacesFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener,
        WeatherApi.WeatherApiListener {

    private static SupportMapFragment mGoogleMapFragment;
    private GoogleMap mGoogleMap;

    SharedPreferences mSharedPrefs;
    SharedPreferences.Editor mSharedPrefsEditor;
    private ProgressDialog mProgressDialogUpdate;
    private WeatherApi mWeatherApi;

    private double mLatitude;
    private double mLongitude;

    private String contentTypeLabel = "Content-Type";
    private String contentTypeValue = "application/json";
    private String httpMethod = "GET";

    public static MarkPlacesFragment newInstance() {
        MarkPlacesFragment fragment = new MarkPlacesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weatherdiary_markplaces_fragment, container, false);

        initializeHelpers();
        initializeViews();
        initializeMap();

        return rootView;
    }

    /*
     * Initializers
     */
    private void initializeHelpers() {
        mSharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        mLatitude = Double.parseDouble(mSharedPrefs.getString(Constants.ARGS_LATITUDE,
                Constants.DEFAULT_LAT_VALUE));
        mLongitude = Double.parseDouble( mSharedPrefs.getString(Constants.ARGS_LONGITUDE,
                Constants.DEFAULT_LONG_VALUE) );
        mWeatherApi = new WeatherApi(getActivity());
    }

    private void initializeViews() {
        mProgressDialogUpdate = new ProgressDialog(this.getActivity());
        mProgressDialogUpdate.setMessage(getString
                (R.string.weatherdiary_progdialog_forecast_update_text));
    }

    private void initializeMap() {
        FragmentManager fragmentManager = getChildFragmentManager();
        mGoogleMapFragment = (SupportMapFragment) fragmentManager
                .findFragmentById(R.id.weatherdiary_markplaces_map_fragment);
        mGoogleMapFragment.getMapAsync(this);

        if (mGoogleMapFragment == null) {
            mGoogleMapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.weatherdiary_markplaces_map_fragment, mGoogleMapFragment)
                    .commit();
        }
    }

    /*
     * Custom Methods
     */
    private Bundle putInBundle(CurrentWeather weather) {
        Bundle bundle = new Bundle();

        bundle.putString(Constants.ARGS_PLACENAME, weather.nameOfPlace);
        bundle.putString(Constants.ARGS_CELSIUS, weather.celsiusTemp);
        bundle.putString(Constants.ARGS_FAHRENHEIT, weather.fahrenheitTemp);
        bundle.putString(Constants.ARGS_WINDSPEED, weather.windSpeed);
        bundle.putString(Constants.ARGS_CLOUDINESS, weather.cloudiness);
        bundle.putString(Constants.ARGS_FORECAST_TIME, weather.forecastTime);

        return bundle;
    }

    private void getForecastDialog(final MarkerOptions mapMarker, final LatLng point) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = dialogBuilder
                .setTitle(Constants.ALERT_FORECAST_TITLE)
                .setMessage(Constants.ALERT_FORECAST_MESSAGE)
                .setPositiveButton(Constants.ALERT_FORECAST_AFFIRMATIVE_BUTTON_LABEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        getWeatherForecast(point.latitude, point.longitude);
                        mWeatherApi.getWeatherForecast(MarkPlacesFragment.this,
                                point.latitude, point.longitude);
                        mGoogleMap.addMarker(mapMarker);
                    }
                })
                .setNeutralButton(Constants.ALERT_FORECAST_NEGATIVE_BUTTON_LABEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }

//    private void getWeatherForecast(final double latitude, final double longitude) {
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                mProgressDialogUpdate.show();
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                InputStream inputStream = null;
//                HttpURLConnection urlConnection;
//                int statusCode;
//
//                try {
//                    URL url = new URL(Constants.WEATHER_API_CURRENT + "?lat=" + latitude
//                            + "&lon=" + longitude);
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestProperty(contentTypeLabel, contentTypeValue);
//                    urlConnection.setRequestMethod(httpMethod);
//                    statusCode = urlConnection.getResponseCode();
//                    inputStream = urlConnection.getInputStream();
//
//                    if (statusCode == 200) {
//                        return readInputStream(inputStream);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(String response) {
//                mProgressDialogUpdate.hide();
//                Log.d("@@@ CHOSEN PLACE", "RESPONSE: " + response);
////                mSharedPrefsEditor.putString("open_api_response", response);
//                Intent intent = new Intent(getActivity().getApplicationContext(), NewForecastActivity.class);
//                intent.putExtra("open_api_response", response);
//                startActivity(intent);
//
//            }
//        }.execute();
//    }
//
//    private String readInputStream(InputStream inputStream) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//        String line;
//        String result = "";
//
//        while ((line = bufferedReader.readLine()) != null) {
//            result += line;
//        }
//
//        if (null != inputStream) {
//            inputStream.close();
//        }
//
//        return result;
//    }

    /*
     * Google Map Listener Methods
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        LatLng currentLatLng = new LatLng(mLatitude, mLongitude);
        String placeLabel = mSharedPrefs.getString(Constants.CURRENT_PLACENAME_LABEL,
                Constants.DEFAULT_PLACENAME_VALUE);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                Constants.DEFAULT_ZOOM_VALUE));
        MarkerOptions mapMarker = new MarkerOptions().position(currentLatLng).title(placeLabel);
        Marker currentPlaceMarker = googleMap.addMarker(mapMarker);

        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions mapMarker = new MarkerOptions().position(
                new LatLng(latLng.latitude, latLng.longitude)).title("New Place");
        getForecastDialog(mapMarker, latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /*
     * Weather API Listener Methods
     */
    @Override
    public void onStartOfQuery() {
        mProgressDialogUpdate.show();
    }

    @Override
    public void onUpdateViews(CurrentWeather weather) {
        mProgressDialogUpdate.hide();
        Intent intent = new Intent(getActivity().getApplicationContext(),
                NewForecastActivity.class);
        intent.putExtras(putInBundle(weather));
        startActivity(intent);
    }
}

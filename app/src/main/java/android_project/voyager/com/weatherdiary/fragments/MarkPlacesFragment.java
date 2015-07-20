package android_project.voyager.com.weatherdiary.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.activities.MarkedPlaceForecastActivity;
import android_project.voyager.com.weatherdiary.dao.MarkedPlacesWeatherDAO;
import android_project.voyager.com.weatherdiary.interfaces.ForecastApi;
import android_project.voyager.com.weatherdiary.interfaces.WeatherApi;
import android_project.voyager.com.weatherdiary.models.MarkedPlace;
import android_project.voyager.com.weatherdiary.models.Weather;
import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/13/15.
 */
public class MarkPlacesFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener,
        ForecastApi.ForecastApiListener {

    private static SupportMapFragment mGoogleMapFragment;
    private GoogleMap mGoogleMap;

    SharedPreferences mSharedPrefs;
    SharedPreferences.Editor mSharedPrefsEditor;
    private ProgressDialog mProgressDialogUpdate;
    private ForecastApi mForecastApi;
    private MarkedPlacesWeatherDAO mWeatherDAO;

    private double mLatitude;
    private double mLongitude;
    private Marker mCurrentMarker;

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
        mForecastApi = new ForecastApi(getActivity());
        mWeatherDAO = new MarkedPlacesWeatherDAO(getActivity());
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

    private void reloadMarkers(GoogleMap googleMap) {
        ArrayList<MarkedPlace> markedPlaces = mWeatherDAO.getAllMarkedPlaces();
        for (int i = 0; i < markedPlaces.size(); i++) {
            MarkedPlace marked = markedPlaces.get(i);
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position(marked.mapCoordinates)
                    .title(marked.nameOfPlace);
            googleMap.addMarker(markerOptions);
        }
    }

    /*
     * Custom Methods
     */
    private void getForecastDialog(final MarkerOptions mapMarker, final LatLng point) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = dialogBuilder
                .setTitle(getString(R.string.weatherdiary_markplaces_alert_getforecast_title))
                .setMessage(getString(R.string.weatherdiary_markplaces_alert_getforecast_message))
                .setPositiveButton(getString(R.string.weatherdiary_markplaces_alert_getforecast_affbutton_label),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mForecastApi.getForecast(MarkPlacesFragment.this, point.latitude,
                                point.longitude);
                        mCurrentMarker = mGoogleMap.addMarker(mapMarker);
                    }
                })
                .setNeutralButton(getString(R.string.weatherdiary_markplaces_alert_getforecast_neutralbutton_label),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }

    private void manageMarkerDialog(final Marker marker) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = dialogBuilder
                .setTitle(getString(R.string.weatherdiary_markplaces_alert_managemarker_title))
                .setMessage(getString(R.string.weatherdiary_markplaces_alert_managemarker_message))
                .setPositiveButton(getString(R.string.weatherdiary_markplaces_alert_managemarker_affbutton_label),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mForecastApi.getForecast(MarkPlacesFragment.this,
                                        marker.getPosition().latitude,
                                        marker.getPosition().longitude);
                                mCurrentMarker = marker;
                            }
                        })
                .setNeutralButton(getString(R.string.weatherdiary_markplaces_alert_managemarker_neutralbutton_label),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mWeatherDAO.deleteWeatherData(marker.getId());
                                mWeatherDAO.deleteMarkedPlace(marker.getId());
                                marker.remove();
                            }
                        })
                .setNegativeButton(getString(R.string.weatherdiary_markplaces_alert_managemarker_negbutton_label),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
        dialog.show();
    }

    /*
     * Google Map Listener Methods
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        reloadMarkers(googleMap);

        LatLng currentLatLng = new LatLng(mLatitude, mLongitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                Constants.DEFAULT_ZOOM_VALUE));
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions mapMarker = new MarkerOptions().position(
                new LatLng(latLng.latitude, latLng.longitude));
        getForecastDialog(mapMarker, latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        manageMarkerDialog(marker);
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
    public void onProcessResult(ArrayList<Weather> weathers, LatLng latLng) {
        mProgressDialogUpdate.hide();

        String nameOfPlace = weathers.get(0).nameOfPlace;
        String forecastTime = weathers.get(0).forecastTime;

        for (int i = 0; i < weathers.size(); i++) {
            Weather weather = weathers.get(i);
            String markerId = mCurrentMarker.getId() + "-" + i;
            if (mWeatherDAO.checkMarkerIfExists(markerId)) {
                mWeatherDAO.updateWeatherData(markerId, weather);
            } else {
                mCurrentMarker.setTitle(nameOfPlace);
                mWeatherDAO.storeWeatherData(markerId, weather);
            }
        }

        MarkedPlace markedPlace = new MarkedPlace();
        markedPlace.markerId = mCurrentMarker.getId();
        markedPlace.mapCoordinates = latLng;
        markedPlace.nameOfPlace = nameOfPlace;
        markedPlace.forecastTime = forecastTime;

        if (mWeatherDAO.checkMarkedPlaceIfExists(mCurrentMarker.getId())) {
            mWeatherDAO.updateMarkedPlace(mCurrentMarker.getId(), markedPlace);
        } else {
            mWeatherDAO.storeMarkedPlace(markedPlace);
        }

        Intent intent = new Intent(getActivity().getApplicationContext(),
                MarkedPlaceForecastActivity.class);
        intent.putExtra(Constants.ARGS_MARKER, mCurrentMarker.getId());
        intent.putExtra(Constants.ARGS_PLACENAME, nameOfPlace);
        intent.putExtra(Constants.ARGS_FORECAST_TIME, forecastTime);
        startActivity(intent);
    }
}

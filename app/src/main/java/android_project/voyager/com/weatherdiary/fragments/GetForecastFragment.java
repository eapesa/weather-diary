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
import android_project.voyager.com.weatherdiary.NewForecastActivity;
import android_project.voyager.com.weatherdiary.utils.Constants;
import android_project.voyager.com.weatherdiary.utils.Labels;

/**
 * Created by eapesa on 7/13/15.
 */
public class GetForecastFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private static SupportMapFragment mGoogleMapFragment;
    SharedPreferences mSharedPrefs;
    SharedPreferences.Editor mSharedPrefsEditor;
    GoogleMap mGoogleMap;
    ProgressDialog mProgressDialogUpdate;

    private LatLng clickedLatLng;
    private double latitude;
    private double longitude;

    private String contentTypeLabel = "Content-Type";
    private String contentTypeValue = "application/json";
    private String httpMethod = "GET";

    public static GetForecastFragment newInstance() {
        GetForecastFragment fragment = new GetForecastFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weatherdiary_getforecast_fragment, container, false);

        mSharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        latitude = Double.parseDouble(mSharedPrefs.getString(Constants.CURRENT_LAT_LABEL,
                Constants.DEFAULT_LAT_VALUE));
        longitude = Double.parseDouble( mSharedPrefs.getString(Constants.CURRENT_LONG_LABEL,
                Constants.DEFAULT_LONG_VALUE) );

        mProgressDialogUpdate = new ProgressDialog(this.getActivity());
        mProgressDialogUpdate.setMessage(Labels.HOME_UPDATE_FORECAST);

        FragmentManager fragmentManager = getChildFragmentManager();
        mGoogleMapFragment = (SupportMapFragment) fragmentManager
                .findFragmentById(R.id.weatherdiary_getforecast_map_fragment);
        mGoogleMapFragment.getMapAsync(this);

        if (mGoogleMapFragment == null) {
            mGoogleMapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.weatherdiary_getforecast_map_fragment, mGoogleMapFragment)
                    .commit();
        }

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        LatLng currentLatLng = new LatLng(latitude, longitude);
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
        Log.d("@@@ CLICKED PLACE", "LAT: " + latLng.latitude + " LONG: " + latLng.longitude);

        MarkerOptions mapMarker = new MarkerOptions().position(
                new LatLng(latLng.latitude, latLng.longitude)).title("New Place");
        getForecastDialog(mapMarker, latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /*
     * Miscellaneous
     */
    private void getForecastDialog(final MarkerOptions mapMarker, final LatLng point) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = dialogBuilder
                .setTitle(Constants.ALERT_FORECAST_TITLE)
                .setMessage(Constants.ALERT_FORECAST_MESSAGE)
                .setPositiveButton(Constants.ALERT_FORECAST_AFFIRMATIVE_BUTTON_LABEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getWeatherForecast(point.latitude, point.longitude);
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

    private void getWeatherForecast(final double latitude, final double longitude) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialogUpdate.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                InputStream inputStream = null;
                HttpURLConnection urlConnection;
                int statusCode;

                try {
                    URL url = new URL(Constants.WEATHER_API + "?lat=" + latitude
                            + "&lon=" + longitude);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty(contentTypeLabel, contentTypeValue);
                    urlConnection.setRequestMethod(httpMethod);
                    statusCode = urlConnection.getResponseCode();
                    inputStream = urlConnection.getInputStream();

                    if (statusCode == 200) {
                        return readInputStream(inputStream);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String response) {
                mProgressDialogUpdate.hide();
                Log.d("@@@ CHOSEN PLACE", "RESPONSE: " + response);
//                mSharedPrefsEditor.putString("open_api_response", response);
                Intent intent = new Intent(getActivity().getApplicationContext(), NewForecastActivity.class);
                intent.putExtra("open_api_response", response);
                startActivity(intent);

            }
        }.execute();
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }
}
